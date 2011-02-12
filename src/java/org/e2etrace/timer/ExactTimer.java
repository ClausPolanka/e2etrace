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
 * Timer Implementation with high accuracy regardless of the operating system.
 * <p>
 *
 * New JDKs provide an API for measuring time intervals that works accurate on
 * all operating systems. To be exact: It guarantees the accuracy the operating
 * system is able to deliver. In most to all cases this should be about 1-2ms.
 * <p>
 *
 * @author Gunther Popp
 */
public class ExactTimer implements ITimer {

  private long duration = -1;
  private long start = -1;

  /**
   * Constructor. New instances can only be created by
   * {@link DefaultTimerFactory}
   */
  ExactTimer() {

  }

  /** {@inheritDoc} */
  public void start() {
    start = System.nanoTime();

  }

  /** {@inheritDoc} */
  public long measure() {
    if (start > 0) {
      duration = ((System.nanoTime()) - start) / 1000000;
    }

    return duration;

  }

}
