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

import org.e2etrace.timer.DefaultTimerFactory;
import org.e2etrace.timer.ITimerFactory;

/**
 * Default Factory class for trace steps.
 *
 * @author Gunther Popp
 *
 */
public class DefaultTraceStepFactory implements ITraceStepFactory {

  private ITimerFactory timerFactory;

  /**
   * Default constructor.
   * <p>
   *
   * A factory created with this constructor uses
   * <code>DefaultTimerFactor</code> as timer factories.
   *
   */
  public DefaultTraceStepFactory() {
    this.timerFactory = new DefaultTimerFactory();

  }

  /**
   * Constructor: Use a custom timer factory.
   * <p>
   *
   * @param timerFactory custom timer factory
   */
  public DefaultTraceStepFactory(ITimerFactory timerFactory) {
    this.timerFactory = timerFactory;
  }

  /** {@inheritDoc} */
  public ITraceStep newInstance(ITraceStepId id) {
    return new DefaultTraceStep(id, this.timerFactory);
  }

}
