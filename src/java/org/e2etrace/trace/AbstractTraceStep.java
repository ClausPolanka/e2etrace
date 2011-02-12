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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Abstract base class for trace steps.
 * <p>
 *
 * All custom trace step implementation must inherit from this class.
 * <p>
 *
 * @author Gunther Popp
 *
 */
public abstract class AbstractTraceStep implements ITraceStep, Serializable {

  private static final long serialVersionUID = 1L;
  private ITraceStepId id;
  protected List children;
  private ITraceStep parent;

  /**
   * Constructor.
   *
   * @param id Id of the new trace step
   */
  public AbstractTraceStep(ITraceStepId id) {
    this.id = id;
  }

  /** {@inheritDoc} */
  public ITraceStepId getId() {
    return this.id;
  }

  /** {@inheritDoc} */
  public void addChild(ITraceStep child) {
    if (child == null) {
      return;
    }

    // A "late" intialization is used for the children list to reduce the
    // overhead
    // for TraceSteps.
    if (this.children == null) {
      this.children = new ArrayList();
    }

    this.children.add(child);

    child.setParent(this);

  }

  /** {@inheritDoc} */
  public ITraceStep[] getChildren() {
    ITraceStep[] steps = new ITraceStep[0];

    if (this.children != null) {
      steps = new ITraceStep[this.children.size()];

      for (int i = 0; i < steps.length; i++) {
        steps[i] = (ITraceStep) this.children.get(i);
      }

    }

    return steps;
  }

  /** {@inheritDoc} */
  public ITraceStep getParent() {
    return this.parent;
  }

  /** {@inheritDoc} */
  public void setParent(ITraceStep parent) {
    this.parent = parent;
  }

  /** {@inheritDoc} */
  public int hashCode() {
    int hashCode = 1;

    hashCode = 31 * hashCode + (id == null ? 0 : id.hashCode());

    return hashCode;
  }

  /** {@inheritDoc} */
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }
    if (o.getClass() != getClass()) {
      return false;
    }
    AbstractTraceStep castedObj = (AbstractTraceStep) o;

    return ((this.id == null ? castedObj.id == null : this.id.equals(castedObj.id)));
  }

  /** {@inheritDoc} */
  public abstract void enter() throws IllegalStateException;

  /** {@inheritDoc} */
  public abstract long getDuration();

  /** {@inheritDoc} */
  public abstract long getIsolatedDuration();

  /** {@inheritDoc} */
  public abstract boolean isActive();

  /** {@inheritDoc} */
  public abstract void leave() throws IllegalStateException;

  /**
   * Invokes <code>leave()</code> on all children of this trace step.
   * <p>
   */
  protected void leaveAllChildren() {

    if (this.children != null) {
      for (Iterator iter = this.children.iterator(); iter.hasNext();) {
        ITraceStep element = (ITraceStep) iter.next();

        if (element.isActive()) {
          element.leave();
        }
      }
    }
  }

}
