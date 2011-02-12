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
 * Trace step id for session root steps.
 * <p>
 *
 * This id is only used by trace session root steps (see
 * {@link TraceSessionRootStep}). Root ids use a dollar sign ($) as prefix for
 * the step id. Additonally they memorize the name of the thread that executes
 * the trace session.
 * <p>
 */
public class TraceSessionRootStepId extends AbstractTraceStepId {

  private static final long serialVersionUID = 1L;

  private String threadName;

  /**
   * Constructor.
   *
   * @param id ID of the trace session. The constructor adds automatically a
   *          dollar sign ($) as prefix.
   */
  public TraceSessionRootStepId(String id) {
    super("$" + id);
    this.threadName = Thread.currentThread().getName();
  }

  /**
   * Returns the name of the thread that executes the trace session.
   * <p>
   *
   * @return the threadName
   */
  public String getThreadName() {
    return this.threadName;
  }

}
