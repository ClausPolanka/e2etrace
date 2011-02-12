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

import java.util.HashMap;
import java.util.Map;

import org.e2etrace.config.ITraceConfig;
import org.e2etrace.trace.ITraceStepId;


/**
 * Mock-Object for trace configurations.
 *
 * All trace parameters can be defined at runtime using the setters of the mock.
 * <p>
 *
 * @author Gunther Popp
 */
public class MockTraceConfig implements ITraceConfig {

  boolean traceEnabled = true;
  Map traceEnabledForId;

  /**
   * Default constructor.
   * <p>
   *
   */
  public MockTraceConfig() {
    this.traceEnabledForId = new HashMap();
  }

  /**
   * Modifies the configuration setting <code>isTraceEnabled</code>.
   * <p>
   *
   * @param enabled new setting
   */
  public void setTraceEnabled(boolean enabled) {
    this.traceEnabled = enabled;
  }

  /** {@inheritDoc} */
  public boolean isTraceEnabled() {
    return this.traceEnabled;
  }

  /**
   * Modifies the configuration setting <code>isTraceEnabledForId</code>.
   * <p>
   *
   * @param id trace step id
   * @param enabled tracing enabled for this step
   */
  public void setTraceEnabledForId(ITraceStepId id, boolean enabled) {
    this.traceEnabledForId.put(id, new Boolean(enabled));
  }

  /** {@inheritDoc} */
  public boolean isTraceEnabledForId(ITraceStepId id) {
    boolean ret = true;
    Boolean enabled;

    enabled = (Boolean) this.traceEnabledForId.get(id);

    if (enabled != null) {
      ret = enabled.booleanValue();
    }

    return ret;
  }

}
