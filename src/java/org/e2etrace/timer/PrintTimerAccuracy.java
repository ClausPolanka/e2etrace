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
 * Utility class to determine the e2etrace timer configuration and accuracy.
 * <p>
 *
 * The source code of this utility class is based on the article "My kingdom for
 * a good timer!" by Vladimir Roubtsov on JavaWorld (see link below).
 *
 * @author Gunther Popp
 * @see <a
 *      href="http://www.javaworld.com/javaworld/javaqa/2003-01/01-qa-0110-timing.html">Article
 *      "My kingdom for a good timer!"</a>
 *
 */
public class PrintTimerAccuracy {

  /**
   * Main method.
   *
   * @param args command line args
   */
  public static void main(String[] args) {
    ITimerFactory tf = new DefaultTimerFactory();
    ITimer timer = tf.newInstance();
    long time, time_prev;

    System.out.println("e2etrace - PrintTimerAccuracy\n");
    System.out.println("Used timer type: " + timer.getClass().getName() + "\n");

    // JIT/hotspot warmup:
    timer.start();
    for (int r = 0; r < 3000; ++r) {
      timer.measure();
    }

    timer.start();

    for (int i = 0; i < 5; ++i) {
      time = timer.measure();
      time_prev = time;
      while (time == time_prev)
        time = timer.measure();

      System.out.println("accuracy = " + (time - time_prev) + " ms");
    }
  }
}
