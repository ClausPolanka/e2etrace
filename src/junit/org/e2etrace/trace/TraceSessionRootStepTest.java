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

import org.e2etrace.trace.DefaultTraceStepFactory;
import org.e2etrace.trace.ITraceStep;
import org.e2etrace.trace.ITraceStepFactory;
import org.e2etrace.trace.SimpleTraceStepId;
import org.e2etrace.trace.TraceSessionRootStep;
import org.e2etrace.trace.TraceSessionRootStepId;

import junit.framework.TestCase;

/**
 * JUnit testcase for {@link org.e2etrace.trace.TraceSessionRootStep}
 *
 * @author Gunther Popp
 *
 */
public class TraceSessionRootStepTest extends TestCase {

  public static void main(String[] args) {
    junit.textui.TestRunner.run(TraceSessionRootStepTest.class);
  }

  /**
   * Tests <code>getAccumulatedDuration</code> and <code>getDuration</code>.
   *
   */
  public void testDurations() {
    TraceSessionRootStep root;
    ITraceStep step1, step2;
    ITraceStepFactory factory;

    factory = new DefaultTraceStepFactory(new MockTimerFactory(new long[] { 10, 20 }));
    step1 = factory.newInstance(new SimpleTraceStepId("1"));
    step2 = factory.newInstance(new SimpleTraceStepId("2"));

    step1.enter();
    step1.leave();

    step2.enter();
    step2.leave();

    root = new TraceSessionRootStep(new TraceSessionRootStepId("root"));

    root.addChild(step1);
    root.addChild(step2);

    // Test, if the accumulated duration of the root step matches the summed up
    // durations of the childs
    assertEquals(30, root.getDuration());

    // Test, if the duration of the root step is always 0
    assertEquals(0, root.getIsolatedDuration());
  }

}
