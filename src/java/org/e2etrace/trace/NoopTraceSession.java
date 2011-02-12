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
 * NOOP implementation of a trace session (does nothing).
 * <p>
 * 
 * This trace session is internally used if tracing is switched off in the
 * e2etrace configuration.
 * 
 * @author Gunther Popp
 * 
 */
public class NoopTraceSession implements ITraceSession {

  // Faked trace config that always returns false for isTraceEnabled() and
  // isTraceEnabledForId
  private static final ITraceConfig _FAKED_TRACE_CONFIG = new ITraceConfig() {

    /** {@inheritDoc} */
    public boolean isTraceEnabled() {
      return false;
    }

    /** {@inheritDoc} */
    public boolean isTraceEnabledForId(ITraceStepId id) {
      return false;
    }
  };

  /** {@inheritDoc} */
  public void enterStep(ITraceStepId id) {
    // This method is intentionally left blank

  }

  /** {@inheritDoc} */
  public void leaveStep(ITraceStepId id) {
    // This method is intentionally left blank

  }

  /** {@inheritDoc} */
  public ITraceStep getRootStep() {
    // This method is intentionally left blank
    return null;
  }

  /** {@inheritDoc} */
  public long getDuration() {
    // This method is intentionally left blank
    return 0;
  }

  /** {@inheritDoc} */
  public void setConfig(ITraceConfig tc) {
    // This method is intentionally left blank

  }

  /** {@inheritDoc} */
  public ITraceConfig getConfig() {
    return _FAKED_TRACE_CONFIG;

  }

  /** {@inheritDoc} */
  public ITraceStep getCurrentStep() {
    // This method is intentionally left blank
    return null;
  }

  /** {@inheritDoc} */
  public void enterStep(Class clazz, String method) {
    // This method is intentionally left blank

  }

  /** {@inheritDoc} */
  public void enterStep(String id) {
    // This method is intentionally left blank

  }

  /** {@inheritDoc} */
  public void leaveStep(Class clazz, String method) {
    // This method is intentionally left blank
  }

  /** {@inheritDoc} */
  public void leaveStep(String id) {
    // This method is intentionally left blank

  }

}
