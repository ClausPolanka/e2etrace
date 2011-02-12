package org.e2etrace.timer;

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
 * Default <code>ITimer</code> implementation.
 * <p>
 *
 * This implementation uses {@link System#currentTimeMillis()} to measure the
 * duration between the <code>start()</code> and <code>measure()</code>
 * calls on the timer. The advantage of this approach is that it works with all
 * JDK releases. The disadvantage is that the accuracy of
 * <code>currentTimeMillis</code> depends on the underlying operating systems.
 * On Windows, for example, <code>currentTimeMillis</code> will typically
 * provide an accuracy of about 10ms, which is too coarse for measuring short
 * intervals. However, on Linux the accuracy is 1ms, which is exactly what we
 * want. For details on this issue, please refer to the article "My kingdom for
 * a good timer!" by Vladimir Roubtsov on JavaWorld (see link below).
 * <p>
 *
 * @author Gunther Popp
 * @see <a
 *      href="http://www.javaworld.com/javaworld/javaqa/2003-01/01-qa-0110-timing.html">Article
 *      "My kingdom for a good timer!"</a>
 *
 */
public class DefaultTimer implements ITimer {

  private long duration = -1;
  private long start = -1;

  /**
   * Constructor. New instances can only be created by factories
   * {@link ITimerFactory}
   */
  DefaultTimer() {

  }

  /** {@inheritDoc} */
  public void start() {
    start = System.currentTimeMillis();

  }

  /** {@inheritDoc} */
  public long measure() {
    if (start > 0) {
      duration = System.currentTimeMillis() - start;
    }

    return duration;

  }

}
