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
 * Every monitored step in a service call is represented by a trace step.
 * <p>
 *
 * A trace step at minimum consists of a unique id, that identifies the
 * monitored step within the trace session and the duration (in ms) of the
 * executed step. An example for such an id is the fully qualified class name
 * plus the name of the executed method.
 * <p>
 *
 * Additionally, most trace steps have a parent and one to several children.
 * <p>
 *
 * @author Gunther Popp
 *
 */
public interface ITraceStep {

  /**
   * Return the id of the trace step.
   * <p>
   *
   * @return trace step id
   */
  ITraceStepId getId();

  /**
   * Enter the trace step and start time measuring.<p>
   *
   * This method should only be called once for a trace step. All subsequent calls
   * will be ignored.
   */
  void enter();

  /**
   * Leave the trace step and calculate the elapsed time since
   * <code>enter</code> has been called.
   *
   * The elapsed time is returned by <code>getDuration()</code>.
   * <p>
   *
   * Leaving a step implies that all children are left, too.
   * <p>
   *
   * This method should only be called once for a trace step. All subsequent calls
   * will be ignored.
   */
  void leave();

  /**
   * Checks, if the current trace step is active.
   * <p>
   *
   * A step is active between the calls to <code>enter</code> and
   * <code>leave</code>.
   *
   * @return true: The trace step is active.
   */
  boolean isActive();

  /**
   * Returns the isolated duration of this TraceStep.
   * <p>
   *
   * The duration is the time elapsed between the calls to <code>enter</code>
   * and <code>leave</code> minus the durations of all children.
   * <p>
   *
   * @return duration of this TraceStep in ms (-1: no valid duration exists for
   *         this trace step)
   */
  long getIsolatedDuration();

  /**
   * Returns the duration of this TraceStep and all children.
   * <p>
   *
   * This value reflects the overall execution time of a trace step between the
   * calls to <code>enter</code> and <code>leave</code> inclusive the
   * durations of all children.
   * <p>
   *
   * @return accumulated duration in ms
   */
  long getDuration();

  /**
   * Adds a new child to this trace step.
   * <p>
   *
   * The implementation must make sure that the child receives a references to this
   * trace step. This reference must be returned by <code>getParent()</code>.<p>
   *
   * @param child trace step to add as child
   */
  void addChild(ITraceStep child);

  /**
   * Returns the list of children for this trace steps.
   *
   * @return Array containing the children
   */
  ITraceStep[] getChildren();

  /**
   * Returns the parent of a TraceStep, if any exists.
   * <p>
   *
   * @return Parent trace step
   */
  ITraceStep getParent();

  /**
   * Set the parent of a TraceStep.
   * <p>
   *
   * This method is invoked when a new child is added using <code>addChild()</code>. It should
   * never be called manually.<p>
   * <p>
   *
   * @param parent new parent of the child.
   */
   void setParent(ITraceStep parent);

}
