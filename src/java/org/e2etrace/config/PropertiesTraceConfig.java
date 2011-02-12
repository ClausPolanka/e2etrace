package org.e2etrace.config;

/*
 * Copyright 2006 Gunther Popp
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.e2etrace.trace.ITraceStepId;


/**
 * Properties file implementation of the e2etrace configuration.
 * <p>
 * <p>
 *
 * The property file must be structured as follows:
 * <p>
 *
 * <pre>
 *            global.enabletrace=true/false
 *            global.reloadinterval=&lt;n&gt;
 *            id.&lt;trace step id&gt;=true/false
 * </pre>
 *
 * If a <code>reloadinterval</code> is defined in the configuration file, the
 * property file will be automatically reloaded every n seconds. If no reload
 * interval or an interval of 0 seconds is defined, the configuration will only
 * be loaded once.
 * <p>
 *
 * The trace step ids must match the string representation of the id (see
 * {@link ITraceStepId#asString()}). The default value for trace ids is
 * <code>true</code>. That is, if no ids are defined in the configuration
 * file , tracing will be enabled for all trace steps.
 * <p>
 *
 * @author Gunther Popp
 *
 */
public class PropertiesTraceConfig implements ITraceConfig {

  private static final Log log = LogFactory.getLog(PropertiesTraceConfig.class);

  private static final String KEY_ENABLETRACE = "global.enabletrace";
  private static final String KEY_RELOADINTERVAL = "global.reloadinterval";
  private static final String KEY_IDPREFIX = "id.";

  private String fileName;
  private boolean enabled;
  private boolean configIsDirty;
  private HashSet disabledForId;
  private long reloadInterval;
  private long lastReload;

  /**
   * Default constructor.
   * <p>
   *
   */
  public PropertiesTraceConfig() {

  }

  /** {@inheritDoc} */
  public boolean isTraceEnabled() {
    reloadifIntervalExpired();

    return this.enabled;
  }

  /** {@inheritDoc} */
  public boolean isTraceEnabledForId(ITraceStepId id) {
    boolean ret = true;

    reloadifIntervalExpired();

    if (this.disabledForId.contains(id.asString())) {
      ret = false;
    }

    return ret;
  }

  /**
   * Loads the e2etrace configuration from the specified properties file.
   * <p>
   *
   * The following search order is used:
   * <p>
   * <ul>
   * <li>Load the file using the classloader of
   * <code>PropertiesTraceConfig</code>. If e2etrace is deployed as part of
   * an Java EE application, this loader should find all files that are part of
   * the ear/war file.</li>
   * <li>If the file has not been found, try again using the thread context
   * class loader.</li>
   * <li>If this fails again, load the file directly from the file system.
   * </li>
   * </ul>
   * loaded as resource via the thread context class loader. Hence, the
   * specified file must be accessible in the classpath of the application.
   * <p>
   *
   * @param fileName path and name of the configuration file
   * @throws IOException Error loading the specified file
   */
  public void loadConfigFile(String fileName) throws IOException {
    this.fileName = fileName;
    this.configIsDirty = true;

    reloadConfiguration();

  }

  /**
   * Reloads the configuration file if the reload interval has expired.
   * <p>
   *
   */
  private void reloadifIntervalExpired() {
    if (this.reloadInterval > 0) {
      if (((System.currentTimeMillis() / 1000) - this.lastReload) >= this.reloadInterval) {
        try {
          // The following flag is used to prevent multiple reloads from
          // parallel threads
          this.configIsDirty = true;

          reloadConfiguration();

        } catch (IOException e) {
          log
              .error(
                  "Cannot reload configuration file. Will use the already loaded values instead.",
                  e);
        }
      }
    }
  }

  /**
   * Reloads the configuration file.
   * <p>
   *
   * @throws IOException Error loading the configuration file
   */
  private synchronized void reloadConfiguration() throws IOException {
    // If a parallel thread has reloaded the config just before us, simply
    // return at this point
    if (!this.configIsDirty) {
      return;
    }

    Properties loadedConfig;
    loadedConfig = locateAndLoadProperties();

    this.enabled = getPropertyAsBoolean(loadedConfig,
        PropertiesTraceConfig.KEY_ENABLETRACE, true);
    this.reloadInterval = getPropertyAsLong(loadedConfig,
        PropertiesTraceConfig.KEY_RELOADINTERVAL, 0);

    // Load the on/off settings per trace id
    Enumeration e = loadedConfig.keys();
    String key;
    String id;
    boolean idEnabled;

    this.disabledForId = new HashSet();

    while (e.hasMoreElements()) {
      key = (String) e.nextElement();

      if (key.startsWith(KEY_IDPREFIX)) {
        id = key.substring(key.indexOf(".") + 1);
        idEnabled = getPropertyAsBoolean(loadedConfig, key, true);

        if (!idEnabled) {
          this.disabledForId.add(id);
        }
      }
    }

    // Memorize the reload time
    this.lastReload = System.currentTimeMillis() / 1000;

    // Config has been refreshed
    this.configIsDirty = false;

    if (log.isInfoEnabled()) {
      log.info("Loaded configuration file " + this.fileName);
      log.info("Tracing is now " + (this.enabled ? "enabled" : "disabled"));
      log.info("Reload interval is now " + this.reloadInterval + " seconds");
      log.info("Tracing has been disabled for " + this.disabledForId.size() + " ids");
    }

  }

  /**
   * Searches the config file as described in <code>loadConfiguration</code>
   * and loads the properties.
   * <p>
   *
   * @return loaded properties
   * @throws IOException File not found or error while loading its content
   */
  private Properties locateAndLoadProperties() throws IOException {
    InputStream is = null;
    Properties loadedConfig = new Properties();

    try {
      // 1. Load configuration using the current class loader
      is = PropertiesTraceConfig.class.getResourceAsStream(this.fileName);

      if (is == null) {

        // 2. Load configuration using the context class loader
        ClassLoader ctxLoader = Thread.currentThread().getContextClassLoader();
        is = ctxLoader.getResourceAsStream(this.fileName);

        if (is == null) {
          // 3. Load configuration from the file system
          is = new FileInputStream(this.fileName);
        }
      }

      if (is != null) {
        loadedConfig.load(is);
      } else {
        throw new IOException("Cannot find or open file " + this.fileName);
      }
    } finally {
      if (is != null) {
        is.close();
      }
    }

    return loadedConfig;
  }

  /**
   * Returns the config value for a given property key as boolean.
   * <p>
   *
   * @param loadedConfig loaded configuration file
   * @param key key of the required config value
   * @param def default value, if the key is not existent in the configuration
   *
   * @return config value as boolean
   */
  private boolean getPropertyAsBoolean(Properties loadedConfig, String key, boolean def) {
    boolean ret = def;
    String retValue;

    retValue = loadedConfig.getProperty(key);

    if (retValue != null) {
      ret = new Boolean(retValue).booleanValue();

    }

    return ret;

  }

  /**
   * Returns the config value for a given property key as long.
   * <p>
   *
   * @param loadedConfig loaded configuration file
   * @param key key of the required config value
   * @param def default value, if the key is not existent in the configuration
   *
   * @return config value as long
   */
  private long getPropertyAsLong(Properties loadedConfig, String key, long def) {
    long ret = def;
    String retValue;

    retValue = loadedConfig.getProperty(key);

    if (retValue != null) {
      ret = new Long(retValue).longValue();

    }

    return ret;

  }

}
