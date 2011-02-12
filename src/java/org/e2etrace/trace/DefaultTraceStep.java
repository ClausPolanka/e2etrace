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

import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.e2etrace.timer.ITimer;
import org.e2etrace.timer.ITimerFactory;

/**
 * Default <code>ITraceStep</code> implementation.
 * <p>
 * 
 * Trace steps may be serialized to remote clients. Please note that after
 * serializiation <code>enter()</code> and <code>leave()</code> should be called
 * anymore (the methods will handle this case gracefully and print a warning
 * message).
 * <p>
 * 
 * To enable logging of the trace-calls just set the log level for this class to
 * <code>DEBUG</code>. For example, in a log4j configuration file the following
 * entry activates log output for every call to <code>enter()</code> and
 * <code>leave()</code>:
 * <p>
 * 
 * <pre>
 * log4j.category.e2etrace.trace.DefaultTraceStep = DEBUG
 * </pre>
 * 
 * @author Gunther Popp
 * 
 */
public class DefaultTraceStep extends AbstractTraceStep {

  private static final long serialVersionUID = 1L;
  private static final Log log = LogFactory.getLog(DefaultTraceStep.class);

  private long duration = 0L;
  private boolean active = false;
  private transient ITimer timer;
  private transient ITimerFactory timerFactory;

  /**
   * Constructor. New instances can only be created by factories
   * {@link ITraceStepFactory}.
   * 
   * @param id Id of the new trace step
   * @param timerFactory factory for the timer instance that is used to
   *          calculate the duration between <code>enter</code> and
   *          <code>leave</code>
   */
  DefaultTraceStep(ITraceStepId id, ITimerFactory timerFactory) {
    super(id);

    if (timerFactory == null) {
      throw new IllegalArgumentException("timerFactory must not be null");
    }

    this.timerFactory = timerFactory;
  }

  /** {@inheritDoc} */
  public void enter() {
    if (isActive()) {
      // enter must only be called once for a trace step
      log.warn("enter() has been called before for trace step '"
          + this.getId().asString() + "'");

      return;
    }

    if (timerFactory == null) {
      // enter cannot be called after Serialization
      log.warn("enter() cannot be invoked after Serialization of the trace step");

      return;
    }

    if (log.isDebugEnabled()) {
      log.debug(">>> Entering " + this.getId().asString());
    }

    this.timer = this.timerFactory.newInstance();
    this.timer.start();
    this.active = true;

  }

  /** {@inheritDoc} */
  public void leave() {
    if (timerFactory == null) {
      // leave cannot be called after Serialization
      log.warn("leave() cannot be invoked after Serialization of the trace step");

      return;
    }

    // Only leave a trace step once
    if (!isActive()) {
      // leave must only be called once for a trace step and only after a call
      // to enter()
      log
          .warn("leave() has been called before OR leave() has been called without prior enter() for trace step '"
              + this.getId().asString() + "'");

      return;
    }

    // Forward the call to all children
    leaveAllChildren();

    // Measure the duration of this trace step
    this.duration = this.timer.measure();
    this.active = false;

    if (log.isDebugEnabled()) {
      log.debug("<<< Leaving " + this.getId().asString() + " (" + this.duration + "ms)");
    }

  }

  /** {@inheritDoc} */
  public boolean isActive() {
    return this.active;
  }

  /** {@inheritDoc} */
  public long getDuration() {
    return this.duration;
  }

  /**
   * Returns the isolated duration of this TraceStep.
   * <p>
   * 
   * The duration is the time elapsed between the calls to <code>enter</code>
   * and <code>leave</code> minus the durations of all children.
   * <p>
   * 
   * Please note that the returned value only reflects the real execution time,
   * if all children of this trace step really have been executed. That is, if
   * trace steps are added manually, <code>getIsolatedDuration()</code> will
   * return meaningless values. An example for this scenario are traces that are
   * collected from parallel execution threads and manually added to the trace
   * step of the method that spawned the threads.
   * <p>
   * 
   * @return duration of this TraceStep in ms (-1: no valid duration exists for
   *         this trace step)
   */
  public long getIsolatedDuration() {
    long duration;

    duration = this.getDuration();

    // Subtract durations of all direct children
    if (this.children != null) {
      for (Iterator iter = this.children.iterator(); iter.hasNext();) {
        ITraceStep element = (ITraceStep) iter.next();

        duration -= element.getDuration();
      }
    }

    // In certain situation duration may be invalid (e.g. if additional trace
    // steps have been added manually. In this case the method returns a defined
    // error value.
    if (duration < 0) {
      duration = -1;
    }
    return duration;
  }

  /**
   * Override toString.
   * 
   * @return the Objects String representation.
   */
  public String toString() {
    StringBuffer buffer = new StringBuffer();

    buffer.append("[DefaultTraceStep:");
    buffer.append(" id: ");
    buffer.append(this.getId());
    buffer.append(" duration: ");
    buffer.append(duration);
    buffer.append(" parent: ");
    buffer.append(this.getParent());
    buffer.append("]");

    return buffer.toString();
  }

}
