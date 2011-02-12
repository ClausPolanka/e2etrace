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
 * Default implementation of a trace session manager.
 * <p>
 *
 * This simple trace session manager maintains a single trace session. Hence, it
 * is only suitable for monitoring sequential service execution in an
 * application. This approach does <em>not</em> work in application server
 * environments!
 * <p>
 *
 * This class implements the singleton pattern.
 * <p>
 *
 * @author Gunther Popp
 *
 */
public class DefaultTraceSessionManager extends AbstractTraceSessionManager {

  private static final DefaultTraceSessionManager instance = new DefaultTraceSessionManager();

  private ITraceSession session;

  /**
   * Returns the singleton instance of the session manager.
   * <p>
   *
   * @return trace session manager
   */
  public static DefaultTraceSessionManager getInstance() {
    return instance;
  }

  /**
   * Default constructor.
   * <p>
   *
   */
  private DefaultTraceSessionManager() {
    super();
  }

  /** {@inheritDoc} */
  protected ITraceSession requestCurrentSession() {
    return this.session;
  }

  /** {@inheritDoc} */
  protected void assignCurrentSession(ITraceSession session) {
    this.session = session;
  }

  /** {@inheritDoc} */
  public void releaseCurrentSession() {
    this.session = null;
  }

}
