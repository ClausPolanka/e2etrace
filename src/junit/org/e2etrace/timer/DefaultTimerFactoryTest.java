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

import org.e2etrace.timer.DefaultTimer;
import org.e2etrace.timer.DefaultTimerFactory;
import org.e2etrace.timer.ExactTimer;

import junit.framework.TestCase;

/**
 * JUnit testcase for {@link org.e2etrace.timer.DefaultTimerFactory}
 *
 * @author Gunther Popp
 *
 */
public class DefaultTimerFactoryTest extends TestCase {

  public static void main(String[] args) {
    junit.textui.TestRunner.run(DefaultTimerFactoryTest.class);
  }

  /**
   * Tests the methods newInstance/setTimerType/getTimerType and the different
   * constructors
   */
  public void testNewInstance() {
    DefaultTimerFactory tf = new DefaultTimerFactory();

    // OK: Default constructor
    // Note: The default constructor returns different Timer classes depending
    // on the JDK version.
    float specVersion = new Float(System.getProperty("java.specification.version"))
        .floatValue();

    if (specVersion < 1.5) {
      assertEquals(DefaultTimer.class, tf.getTimerType());
      assertEquals(DefaultTimer.class, tf.newInstance().getClass());
    } else {
      assertEquals(ExactTimer.class, tf.getTimerType());
      assertEquals(ExactTimer.class, tf.newInstance().getClass());
    }

    // OK: Override constructor
    tf = new DefaultTimerFactory(ExactTimer.class);
    assertEquals(ExactTimer.class, tf.getTimerType());
    assertEquals(ExactTimer.class, tf.newInstance().getClass());

    // OK: Default constructor, set type explicitly
    tf = new DefaultTimerFactory();
    tf.setTimerType(ExactTimer.class);
    assertEquals(ExactTimer.class, tf.getTimerType());
    assertEquals(ExactTimer.class, tf.newInstance().getClass());

    // OK: Override constructor, set type explicitly
    tf = new DefaultTimerFactory(ExactTimer.class);
    tf.setTimerType(DefaultTimer.class);
    assertEquals(DefaultTimer.class, tf.getTimerType());
    assertEquals(DefaultTimer.class, tf.newInstance().getClass());
  }

}
