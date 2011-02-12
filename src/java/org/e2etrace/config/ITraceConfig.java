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
 * Represents configuration settings of e2etrace.
 * <p>
 *
 * @author Gunther Popp
 *
 */
public interface ITraceConfig {

  /**
   * Is tracing enabled at all?
   * <p>
   *
   * If this method returns <code>false</code> e2etrace does not collect any
   * trace information. All potential time consuming methods of e2etrace will
   * immediately return.
   *
   * @return true / false
   */
  boolean isTraceEnabled();

  /**
   * Is tracing enabled for the given trace step id?
   * <p>
   *
   * If this method returns <code>false</code> for the given id, e2etrace does
   * not collect any trace information for this specific trace id. Basically,
   * this reduces the amount of collected data but not the general overhead of
   * the trace mechanismn itself.
   *
   * Note: If tracing is disabled for a trace session id, no trace
   * data will be collected for the complete session.<p>
   * <p>
   *
   * @param id the trace id
   * @return true / false
   */
  boolean isTraceEnabledForId(ITraceStepId id);
}
