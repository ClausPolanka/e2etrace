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

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Thread-Aware implementation of a trace session manager.
 * <p>
 *
 * This session manager is targeted for a multi-threaded environment (e.g.
 * application servers). It maintains a {@link org.e2etrace.trace.ITraceSession} for
 * every running thread.
 * <p>
 *
 * <em>Note:</em> The implementation uses a synchronized
 * <code>WeakHashMap</code> to cache the sessions (using the threads as keys).
 * Hence, as soon as a thread terminates, the GC will remove the respective
 * cache entry automatically. This design works for multi-threaded applications
 * and for application servers using thread pools. An alternative design would
 * have been to use <code>ThreadLocal</code> instead. However, there are
 * discussions going on that <code>ThreadLocal</code> may cause memory leaks
 * on application servers if applications are hot-deployed (just google for
 * threadlocal+leak). Personally, I would prefer <code>ThreadLocal</code>
 * instead of the custom cache that is currently implemented, because the
 * <code>ThreadLocal</code> approache doesn´t require a synchronized map that
 * stores the trace sessions. I´ll monitor the memory-leak discussion and decide
 * later, if a re-design is appropriate.
 * <p>
 *
 * This class implements the singleton pattern.
 * <p>
 *
 * @author Gunther Popp
 *
 */
public class ThreadedTraceSessionManager extends AbstractTraceSessionManager {

  private static final ThreadedTraceSessionManager instance = new ThreadedTraceSessionManager();

  private Map sessionCache;

  /**
   * Returns the singleton instance of the session manager.
   * <p>
   *
   * @return trace session manager
   */
  public static ThreadedTraceSessionManager getInstance() {
    return instance;
  }

  /**
   * Default constructor.
   * <p>
   *
   */
  private ThreadedTraceSessionManager() {
    super();

    this.sessionCache = Collections.synchronizedMap(new WeakHashMap());
  }

  /** {@inheritDoc} */
  protected ITraceSession requestCurrentSession() {
    return (ITraceSession) this.sessionCache.get(Thread.currentThread());
  }

  /** {@inheritDoc} */
  protected void assignCurrentSession(ITraceSession session) {
    this.sessionCache.put(Thread.currentThread(), session);
  }

  /** {@inheritDoc} */
  public void releaseCurrentSession() {
    this.sessionCache.remove(Thread.currentThread());
  }

}
