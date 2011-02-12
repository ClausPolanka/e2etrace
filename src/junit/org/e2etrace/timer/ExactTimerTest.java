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

import org.e2etrace.timer.DefaultTimerFactory;
import org.e2etrace.timer.ExactTimer;
import org.e2etrace.timer.ITimer;

import junit.framework.TestCase;

/**
 * JUnit testcase for {@link org.e2etrace.timer.ExactTimer}
 *
 * @author Gunther Popp
 *
 */
public class ExactTimerTest extends TestCase {

  public static void main(String[] args) {
    junit.textui.TestRunner.run(ExactTimerTest.class);
  }

  /**
   * Tests the methods start/stop/getDuration.
   *
   * @throws InterruptedException
   */
  public void testGetDuration() throws InterruptedException {
    DefaultTimerFactory tf = new DefaultTimerFactory(ExactTimer.class);
    ITimer timer;
    long duration;

    // OK: Get Duration
    // --------------------------------------------------------------
    timer = tf.newInstance();

    assertEquals(timer.getClass(), ExactTimer.class);

    timer.start();
    Thread.sleep(100);
    duration = timer.measure();

    assertTrue("Expected duration: between 90ms and 110ms, measured duration: "
        + duration + "ms", duration >= 90 && duration <= 110);

    // NOK: Invalid calls to start/measure
    // ----------------------------------
    timer = tf.newInstance();
    assertEquals(-1, timer.measure());

  }

}
