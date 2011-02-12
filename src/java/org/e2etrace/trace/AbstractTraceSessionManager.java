package org.e2etrace.trace;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.e2etrace.config.DefaultTraceConfig;
import org.e2etrace.config.ITraceConfig;
import org.e2etrace.config.PropertiesTraceConfig;


/**
 * Base class for all trace session manager implementations.
 * <p>
 *
 * The base class automatically loads a trace configuration if a property <code>
 * e2etrace.configuration</code>
 * has been defined. This property must contain the name of a valid e2etrace
 * configuration file. For example, if
 * <code>-De2etrace.configuration=e2etrace.properties</code> is passed on the
 * command line of the application, the respective file will be looked up in the
 * classpath and loaded into an instance of {@link PropertiesTraceConfig}. The
 * base class applies the following rules to determine the correct configuration
 * class for a file name:
 * <p>
 * <ul>
 * <li>file name ends with <code>.properties</code>: Load file into
 * {@link PropertiesTraceConfig}.</li>
 * </ul>
 * <p>
 *
 * If <code>e2etrace.configuration</code> has <em>not</em> been defined, a
 * default trace configuration (see {@link org.e2etrace.config.DefaultTraceConfig}
 * will be used for the trace session manager.
 * <p>
 *
 * If tracing has been disabled in the trace config,
 * <code>getCurrentSession</code> will always return a NOOP trace session (see
 * {@link org.e2etrace.trace.NoopTraceSession}.
 * <p>
 * <p>
 *
 * @author Gunther Popp
 */
public abstract class AbstractTraceSessionManager implements ITraceSessionManager {

  private static final Log log = LogFactory.getLog(AbstractTraceSessionManager.class);
  private static final String CONFIG_PROPERTY = "e2etrace.configuration";

  private ITraceConfig config;
  private ITraceSession noopSession;

  /**
   * Constructor.
   *
   */
  protected AbstractTraceSessionManager() {
    this.config = initConfiguration();
    this.noopSession = new NoopTraceSession();
  }

  /**
   * Helper method: Initialize the e2etrace configuration.
   * <p>
   *
   * @return e2etrace configuration
   */
  private ITraceConfig initConfiguration() {
    ITraceConfig initConfig = null;
    String configFile;

    configFile = System.getProperty(CONFIG_PROPERTY);

    if (configFile != null) {
      try {
        if (configFile.endsWith(".properties")) {
          PropertiesTraceConfig propConfig = new PropertiesTraceConfig();

          propConfig.loadConfigFile("/" + configFile);

          initConfig = propConfig;
        }
      } catch (Exception e) {
        log
            .error(
                "Error while initializing e2etrace configuration! Will use default configuration instead.",e);
      }
    }

    // If no valid configuration has been loaded at this point use the default
    // configuration
    if (initConfig == null) {
      initConfig = new DefaultTraceConfig();
    }

    return initConfig;

  }

  /**
   * CALL-BACK: Request the current trace session from sub-classes.
   * <p>
   *
   * This call-back is triggered for every invokation of
   * <code>getCurrentSession</code>.
   * <p>
   *
   * @return current (active) trace session
   */
  protected abstract ITraceSession requestCurrentSession();

  /**
   * CALL-BACK: Forward a newly assigned current trace session to sub-classes.
   * <p>
   *
   * This call-back is triggered for every invokation of
   * <code>setCurrentSession</code>.
   * <p>
   *
   * @param session new current (active) trace session
   */
  protected abstract void assignCurrentSession(ITraceSession session);

  /** {@inheritDoc} */
  public final void setConfig(ITraceConfig config) {
    this.config = config;

  }

  /** {@inheritDoc} */
  public final ITraceConfig getConfig() {
    return this.config;
  }

  /** {@inheritDoc} */
  public ITraceSession getCurrentSession() {
    ITraceSession currentSession;

    // If tracing is enabled, retrieve the current trace session
    if (getConfig().isTraceEnabled()) {
      currentSession = requestCurrentSession();

      if( currentSession == null ) {
        currentSession = this.noopSession;
      }
    } else {
      currentSession = this.noopSession;
    }

    return currentSession;
  }

  /** {@inheritDoc} */
  public void setCurrentSession(ITraceSession session) {
    // Check if tracing has been disabled for this session
    if (session.getRootStep() == null || !(getConfig().isTraceEnabledForId(session.getRootStep().getId()))) {
      assignCurrentSession(this.noopSession);
    } else {
      session.setConfig(this.getConfig());
      assignCurrentSession(session);
    }

  }

}
