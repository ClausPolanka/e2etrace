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

import org.e2etrace.config.DefaultTraceConfig;
import org.e2etrace.config.ITraceConfig;

/**
 * Default-Implementation of a trace session.
 * <p>
 *
 * The session uses a default trace configuration (see
 * {@link org.e2etrace.config.DefaultTraceConfig}). This can be changed by
 * <code>setConfig</code>. As soon as a trace session manager that extends
 * <code>AbstractTraceSessionManager</code> is in control of the session, the
 * trace configuration assigned to the session manager will automatically be
 * forwarded to the trace session.
 * <p>
 *
 * @author Gunther Popp
 */
public class DefaultTraceSession implements ITraceSession {

  private ITraceStep root;
  private ITraceStep current;
  private ITraceStepFactory stepFactory;
  private ITraceConfig tc;

  /**
   * Default constructor.
   * <p>
   *
   * @param sessionId id of the session. This id will be used for the root step
   *          of the session. It will be prefixed by a dollar sign ($) (see
   *          {@link TraceSessionRootStep} and {@link TraceSessionRootStepId})
   * @param stepFactory Factory used for the concrete trace steps in the context
   *          of this session
   */
  public DefaultTraceSession(String sessionId, ITraceStepFactory stepFactory) {
    this.root = new TraceSessionRootStep(new TraceSessionRootStepId(sessionId));
    this.current = this.root;
    this.stepFactory = stepFactory;
    this.tc = new DefaultTraceConfig();
  }

  /** {@inheritDoc} */
  public void enterStep(Class clazz, String method) {
    if (this.tc.isTraceEnabled()) {
      this.enterStep(new MethodTraceStepId(clazz, method));
    }
  }

  /** {@inheritDoc} */
  public void leaveStep(Class clazz, String method) {
    if (this.tc.isTraceEnabled()) {
      this.leaveStep(new MethodTraceStepId(clazz, method));
    }
  }

  /** {@inheritDoc} */
  public void enterStep(String id) {
    if (this.tc.isTraceEnabled()) {
      this.enterStep(new SimpleTraceStepId(id));
    }
  }

  /** {@inheritDoc} */
  public void leaveStep(String id) {
    if (this.tc.isTraceEnabled()) {
      this.leaveStep(new SimpleTraceStepId(id));
    }
  }

  /** {@inheritDoc} */
  public void enterStep(ITraceStepId id) {
    if (this.tc.isTraceEnabledForId(id)) {
      ITraceStep step;

      step = this.stepFactory.newInstance(id);

      // Add the new step as child to the current step
      this.current.addChild(step);

      // Now, the new trace step is the current step
      this.current = step;

      // Start time measuring for the new trace step
      step.enter();
    }

  }

  /** {@inheritDoc} */
  public void leaveStep(ITraceStepId id) {
    if (this.tc.isTraceEnabledForId(id)) {
      ITraceStep step = this.current;

      // If the supplied id does not match the current trace step we
      // probably missed a call to leaveStep()
      if (!(this.current.getId().equals(id))) {
        // Search for a matching trace step in all parents of the
        // current trace step until we hit the root-step
        step = current.getParent();
        while (step != null && step != root) {
          if (step.getId().equals(id)) {
            break;
          }

          step = step.getParent();

        }

      }

      if (step != null) {
        // End time measuring
        step.leave();
      }

      if (step != root && step != null) {
        // Return to the parent step
        this.current = step.getParent();
      }

    }

  }

  /** {@inheritDoc} */
  public ITraceStep getRootStep() {
    return this.root;
  }

  /** {@inheritDoc} */
  public long getDuration() {
    long duration = 0L;
    ITraceStep[] rootChildren = this.root.getChildren();

    // The root step itself always has a duration of 0, so the following
    // call returns the accumulated duration of all childs of root.
    for (int i = 0; i < rootChildren.length; i++) {
      duration += rootChildren[i].getDuration();
    }

    return duration;

  }

  /** {@inheritDoc} */
  public void setConfig(ITraceConfig tc) {
    this.tc = tc;

  }

  /** {@inheritDoc} */
  public ITraceConfig getConfig() {
    return this.tc;
  }

  /** {@inheritDoc} */
  public ITraceStep getCurrentStep() {
    return this.current;
  }
}
