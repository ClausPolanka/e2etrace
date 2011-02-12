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

import org.e2etrace.timer.ITimer;
import org.e2etrace.timer.ITimerFactory;

/**
 * Factory for Mock-Timers.
 * <p>
 *
 * The factory takes a list of predefined durations as parameter in the
 * constructor. Each call to <code>newInstance</code> selects the next entry
 * in the list and creates a {@link org.e2etrace.trace.MockTimer} that returns the
 * resp. duration. When the last entry is reached, <code>newInstance</code>
 * starts again with the first entry.
 * <p>
 *
 * @author Gunther Popp
 */
public class MockTimerFactory implements ITimerFactory {

  private long[] testDurations;
  private int index = -1;

  /**
   * Constructor
   *
   * @param testDurations forwarded to {@link MockTimer}
   */
  public MockTimerFactory(long[] testDurations) {
    this.testDurations = testDurations;
  }

  /** {@inheritDoc} */
  public ITimer newInstance() {
    index++;

    if (index >= testDurations.length) {
      index = 0;
    }

    return new MockTimer(this.testDurations[index]);
  }

}
