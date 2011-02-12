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

import org.e2etrace.trace.ITraceStepId;

/**
 * Default implementation of the e2etrace configuration.
 * <p>
 *
 * This implementation returns the following default values for all
 * configuration settings:
 * <p>
 * <ul>
 * <li>isTraceEnabled: true</li>
 * <li>isTraceEnabledForId: true, regardless of the supplied id</li>
 * </ul>
 * The default implementation is used by e2etrace if no explicit configuration
 * is set by the user (see
 * {@link org.e2etrace.trace.ITraceSessionManager#setConfig(ITraceConfig)}).
 * <p>
 *
 * @author Gunther Popp
 *
 */
public class DefaultTraceConfig implements ITraceConfig {

  /**
   * Default constructor.
   * <p>
   *
   */
  public DefaultTraceConfig() {

  }

  /** {@inheritDoc} */
  public boolean isTraceEnabled() {
    return true;
  }

  /** {@inheritDoc} */
  public boolean isTraceEnabledForId(ITraceStepId id) {
    return true;
  }

}
