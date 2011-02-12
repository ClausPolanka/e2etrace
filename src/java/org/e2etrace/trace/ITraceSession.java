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
 * A trace session manages all trace steps for a single service call.
 * <p>
 *
 * The id of a trace session is the id of the root step (see
 * <code>getRootStep</code>). This id can be used to deactivate tracing on a
 * per-session basis (see {@link ITraceConfig}).
 * <p>
 *
 * @author Gunther Popp
 *
 */
public interface ITraceSession {

  /**
   * Enters a new trace step with the supplied id.
   * <p>
   *
   * In reality, a trace step covers for example the execution of a single
   * method. In this case, <code>enterStep</code> must be called when the
   * method starts and <code>leaveStep</code> when the method ends.
   * <p>
   *
   * To get exact results, each call of <code>enterStep</code> should be
   * matched by a corresponding call of <code>leaveStep</code>. A trace
   * session can handle missing <code>leaveStep</code> calls. In this case,
   * <code>leaveStep</code> will be automatically invoked when the parent step
   * ends (if any). If no parent exists, no duration will be calculated.
   * <p>
   *
   * @param id ID of the trace step
   */
  void enterStep(ITraceStepId id);

  /**
   * Leaves the trace step with the given id.
   * <p>
   *
   * @param id ID of the trace step
   */
  void leaveStep(ITraceStepId id);

  /**
   * Wrapper for {@link ITraceSession#enterStep(ITraceStepId)} using a
   * {@link MethodTraceStepId} as TraceStep-IDs.
   * <p>
   *
   * @param clazz Class instance to which the method belongs
   * @param method Name of the executed method
   */
  void enterStep(Class clazz, String method);

  /**
   * Wrapper for {@link ITraceSession#leaveStep(ITraceStepId)} using a
   * {@link MethodTraceStepId} as TraceStep-IDs.
   * <p>
   *
   * @param clazz Class instance to which the method belongs
   * @param method Name of the executed method
   */
  void leaveStep(Class clazz, String method);

  /**
   * Wrapper for {@link ITraceSession#enterStep(ITraceStepId)} using a String as
   * TraceStep-IDs.
   * <p>
   *
   * @param id String used as id
   */
  void enterStep(String id);

  /**
   * Wrapper for {@link ITraceSession#leaveStep(ITraceStepId)} using a String as
   * TraceStep-IDs.
   * <p>
   *
   * @param id String used as id
   */
  void leaveStep(String id);

  /**
   * Returns the the root step.
   * <p>
   *
   * The root step is the step that covers the complete service call (i.e. that
   * has no parent step). The id of the root step is used as id for the complete
   * trace session.
   * <p>
   *
   * @return root step (null: no trace data has been collected for this session)
   */
  ITraceStep getRootStep();

  /**
   * Returns the active trace step.
   * <p>
   *
   * The active trace step is determined by the last call to
   * <code>enterStep</code>. If <code>enterStep</code> has not been called
   * yet, the method returns the root step of the trace session (see
   * <code>getRootStep</code>).
   * <p>
   *
   * @return active trace step (null: no trace data is collected for this
   *         session)
   */
  ITraceStep getCurrentStep();

  /**
   * Returns the execution time of the complete trace session.
   * <p>
   *
   * Normally for each service call a separate trace session exist. So the
   * returned duration is the overall execution time of the service call.
   * <p>
   *
   * @return execution time of the trace session
   */
  long getDuration();

  /**
   * Assigns a new trace configuratio to the trace session.
   * <p>
   *
   * @param tc trace configuration
   */
  void setConfig(ITraceConfig tc);

  /**
   * Returns the trace configuration of the session.
   * <p>
   *
   * @return trace configuration
   */
  ITraceConfig getConfig();

}