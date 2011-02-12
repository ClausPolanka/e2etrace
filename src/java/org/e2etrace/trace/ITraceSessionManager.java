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

import org.e2etrace.config.ITraceConfig;

/**
 * Manages the trace sessions of the current application.
 * <p>
 *
 * An application should only maintain a single trace session manager. However,
 * the session manager may be able to administer multiple trace sessions, e.g.
 * one for every thread of the application. For details, please refer to the
 * concrete session manager implementations.
 * <p>
 *
 * @author Gunther Popp
 *
 */
public interface ITraceSessionManager {

  /**
   * Returns the current (active) trace session.
   * <p>
   *
   * If no current trace session has been assigned yet using <code>setCurrentSession</code> or
   * if <code>releaseCurrentSession</code> has been called, a {@link NoopTraceSession} will
   * be returned. This allows, for example, the execution of unit-tests for classes that
   * contain <code>enterStep/leaveStep</code> without intializing a trace session first.
   *
   * @return trace session
   */
  public ITraceSession getCurrentSession();

  /**
   * Sets the current (active) trace session.
   * <p>
   *
   * @param session new current trace session
   */
  public void setCurrentSession(ITraceSession session);

  /**
   * Release current session.<p>
   *
   * This call releases the reference to the current trace session. It should be called in
   * when all trace data for the current trace session has been collected and the session
   * is no longer needed.<p>
   */
  public void releaseCurrentSession();

  /**
   * Assign a new trace configuration to the session manager.
   * <p>
   *
   * The configuration will be used by the manager itself and all trace sessions
   * under its control.
   * <p>
   *
   * @param config trace configuration
   */
  public void setConfig(ITraceConfig config);

  /**
   * Returns the current trace configuration.
   *
   * @return trace configuration
   */
  public ITraceConfig getConfig();

}
