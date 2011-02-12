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

/**
 * Mock-Object for Timers.
 *
 * The timer returns the duration that is passed in the constructor.
 *
 * @author Gunther Popp
 */
public class MockTimer implements ITimer {

  private long duration;

  /**
   * Constructor.
   *
   * @param testDurations list of predefined durations
   */
  public MockTimer(long duration) {
    this.duration = duration;
  }

  /** {@inheritDoc} */
  public void start() {

  }

  /** {@inheritDoc} */
  public long measure() {
    return this.duration;
  }

}
