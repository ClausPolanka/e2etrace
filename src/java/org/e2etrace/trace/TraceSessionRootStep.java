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

/**
 * Root trace step for {@link org.e2etrace.trace.ITraceSession}.
 * <p>
 *
 * Each trace session contains one implicit trace step that serves as root of
 * the tree of "real" trace steps. The duration of this trace step is by
 * definition the accumulated duration of all children. In additon, the isolated
 * duration of this step is by definion 0ms.
 * <p>
 *
 * In practice, an instance of the root step represents a complete service
 * call. The children of the root step are the single execution steps of the
 * service.
 * <p>
 *
 * A root step can savely be added as child to any other trace step (e.g. if a
 * client invokes a remote service, the root step of the service can be added
 * to any trace step of the client). However, if a client spawns multiple services
 * asynchronously and waits until all of them return, the isolated duration of
 * the client step will return a meaningless value (since the overall processing
 * time of the parallel services exceeds the execution time of the client step).<p>
 *
 * This class is only instantiated by implementations of
 * {@link org.e2etrace.trace.ITraceSession}
 *
 * @author Gunther Popp
 *
 */
public class TraceSessionRootStep extends AbstractTraceStep {

  private static final long serialVersionUID = 1L;

  /**
   * Constructor.
   *
   * @param id ID of the root step
   */
  public TraceSessionRootStep(TraceSessionRootStepId id) {
    super(id);
  }

  /**
   * Returns the overall duration of this TraceStep and all children.
   * <p>
   *
   * @return duration in ms
   */
  public long getDuration() {
    long duration;
    ITraceStep[] children;

    // Sum up the durations of all direct children of the root
    // step
    children = this.getChildren();
    duration = 0;
    for (int i = 0; i < children.length; i++) {
      duration += children[i].getDuration();
    }

    return duration;
  }

  /**
   * The isolated duration of the root step is by definition 0ms.
   *
   * @return duration of this TraceStep in ms (-1:<code>leave</code> has not
   *         been called yet)
   */
  public long getIsolatedDuration() {
    return 0;
  }

  /** {@inheritDoc} */
  public void enter() throws IllegalStateException {
    // This method is intentionally left blank

  }

  /**
   * Checks, if the current trace step is active.
   * <p>
   *
   * The session root step is always active by definition.
   * <p>
   *
   * @return true
   */
  public boolean isActive() {
    return true;
  }

  /** {@inheritDoc} */
  public void leave() throws IllegalStateException {
    // Forward the call to all children
    leaveAllChildren();

  }

}
