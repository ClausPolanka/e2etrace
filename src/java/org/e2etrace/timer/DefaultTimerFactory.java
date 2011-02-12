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

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Default Factory class for Timer instances.
 * <p>
 *
 * The factory returns a new Timer instance. In the default configuration, the
 * new Timer is of the type {@link org.e2etrace.timer.DefaultTimer}. Please refer
 * to the documentation of this class to learn about the restrictions of the
 * underlying implementation.
 * <p>
 *
 * However, if e2etrace has been compiled and is executed using JDK 5.0 (or
 * higher), the factory will return instances of the type
 * {@link org.e2etrace.timer.ExactTimer}. This Timer implementation uses the
 * advanced timing features of the new JDK releases and avoids all problems
 * described in the documentation of <code>DefaultTimer</code>.
 * <p>
 *
 * Please note the different constructors of the factory. You can force the
 * factory to create specific types of Timers by using the appropriate
 * constructor. If you use the default constructor, the above described logic
 * applies.
 * <p>
 *
 * You can use the class {@link org.e2etrace.timer.PrintTimerAccuracy} to determine
 * which Timer type will be used by e2etrace on your system and which accuracy
 * the Timers are able to deliver.
 * <p>
 *
 *
 * @author Gunther Popp
 *
 */
public class DefaultTimerFactory implements ITimerFactory {

  private static final Log log = LogFactory.getLog(DefaultTimerFactory.class);
  private static final boolean supportsExactMeasuring = supportsNanoTime();

  private Class timerType;

  /**
   * Default constructor.
   * <p>
   */
  public DefaultTimerFactory() {
    if (supportsExactMeasuring) {
      setTimerType(ExactTimer.class);

    } else {
      setTimerType(DefaultTimer.class);

    }

  }

  /**
   * Constructor using a pre-defined Timer type.
   * <p>
   *
   * @param timerType type of timers the factory will return
   */
  public DefaultTimerFactory(Class timerType) {
    setTimerType(timerType);
  }

  /** {@inheritDoc} */
  public ITimer newInstance() {
    ITimer timer = null;

    try {
      timer = (ITimer) timerType.newInstance();
    } catch (Exception e) {
      log.error("Error creating a new Timer of type " + timerType.getName()
          + ". Will create a DefaultTimer instead.", e);
      timer = new DefaultTimer();
    }

    return timer;

  }

  /**
   * Changes the type of Timer returned by the Factory.
   *
   * @param timerType New Timer type
   */
  public void setTimerType(Class timerType) {
    // log.info("TimerFactory uses now Timers of type " +
    // timerType.getName());

    this.timerType = timerType;
  }

  /**
   * Returns the type of Timers currently used by the Factory.
   * <p>
   *
   * @return Timer type
   */
  public Class getTimerType() {
    return timerType;
  }

  /**
   * Helper-method: Checks, if the current VM supports the System.nanoTime()
   * Method.
   *
   * @return true: VM supports the exact time measuring
   */
  private static boolean supportsNanoTime() {
    boolean ret = false;
    Method[] methods = System.class.getDeclaredMethods();

    for (int i = 0; i < methods.length; i++) {
      Method method = methods[i];

      if (method.getName().equals("nanoTime")) {
        ret = true;
        break;
      }
    }

    if (ret) {
      log.info("This VM supports exact time measuring using System.nanoTime()");
    } else {
      log
          .warn("This VM only supports only System.currentTimeMillis(). This may lead to inaccurate results.");
    }

    return ret;
  }

}
