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
import org.e2etrace.trace.SimpleTraceStepId;

import junit.framework.TestCase;

/**
 * JUnit testcase for {@link org.e2etrace.trace.DefaultTraceStep}
 *
 * All tests use {@link org.e2etrace.trace.MockTimer} to fake the measurement of
 * execution times.
 * <p>
 *
 * @author Gunther Popp
 *
 */
public class DefaultTraceStepTest extends TestCase {

  public static void main(String[] args) {
    junit.textui.TestRunner.run(DefaultTraceStepTest.class);
  }

  /**
   * Tests constructor/getId.
   */
  public void testGetId() {
    DefaultTraceStepFactory factory;
    factory = new DefaultTraceStepFactory();
    ITraceStep ts = factory.newInstance(new SimpleTraceStepId("1"));

    assertEquals(new SimpleTraceStepId("1"), ts.getId());
  }

  /**
   * Tests enter/leave
   *
   * @throws InterruptedException
   */
  public void testEnterLeave() throws InterruptedException {
    DefaultTraceStepFactory factory;
    ITraceStep ts;
    long duration;
    
    factory = new DefaultTraceStepFactory(new MockTimerFactory(new long[] { 10 }));
    ts = factory.newInstance(new SimpleTraceStepId("1"));

    // OK: Test time measuring
    ts.enter();
    ts.leave();

    duration = ts.getIsolatedDuration();

    assertEquals(10, duration);

    // OK: Repeated calls to enter() should be handled gracefully
    ts = factory.newInstance(new SimpleTraceStepId("1"));
    ts.enter();
    ts.enter();

    // OK: Calls to leave() without enter() should be handled gracefully
    ts = factory.newInstance(new SimpleTraceStepId("2"));
    ts.leave();

  }

  /**
   * Tests addChild/getChildren/getAccumulatedDuration/getParent
   */
  public void testAddChildEtc() {
    DefaultTraceStepFactory factory;
    ITraceStep ts;
    ITraceStep tsChild1;
    ITraceStep tsChild1_1;
    ITraceStep tsChild2;
    ITraceStep[] children;

    factory = new DefaultTraceStepFactory(new MockTimerFactory(
        new long[] { 35, 20, 10, 5 }));

    ts = factory.newInstance(new SimpleTraceStepId("root"));
    tsChild1 = factory.newInstance(new SimpleTraceStepId("1"));
    tsChild1_1 = factory.newInstance(new SimpleTraceStepId("1_1"));
    tsChild2 = factory.newInstance(new SimpleTraceStepId("2"));

    // OK: Check for empty list of children
    assertEquals(0, ts.getChildren().length);

    // OK: Add valid children
    tsChild1.addChild(tsChild1_1);

    ts.addChild(tsChild1);
    ts.addChild(tsChild2);

    children = ts.getChildren();

    assertEquals(2, children.length);
    assertEquals(new SimpleTraceStepId("1"), children[0].getId());
    assertEquals(new SimpleTraceStepId("2"), children[1].getId());

    assertEquals(new SimpleTraceStepId("1_1"), (children[0].getChildren())[0].getId());

    // OK: Add invalid child
    ts.addChild(null);

    // OK: Get accumulated duration
    // Measure execution times
    ts.enter();
    tsChild1.enter();
    tsChild1_1.enter();
    tsChild2.enter();
    tsChild2.leave();
    tsChild1_1.leave();
    tsChild1.leave();
    ts.leave();

    assertEquals(35, ts.getDuration());

    // OK: Get isolated durations
    assertEquals(35 - 20 - 5, ts.getIsolatedDuration());
    assertEquals(20 - 10, tsChild1.getIsolatedDuration());
    assertEquals(10, tsChild1_1.getIsolatedDuration());
    assertEquals(5, tsChild2.getIsolatedDuration());

    // OK: Try to add an existing child again
    ts.addChild(tsChild2);
    assertEquals(35, ts.getDuration());

    // OK: Verify parents
    assertEquals(new SimpleTraceStepId("root"), children[0].getParent().getId());
    assertEquals(new SimpleTraceStepId("root"), children[1].getParent().getId());

    assertEquals(new SimpleTraceStepId("1"), (children[0].getChildren())[0].getParent()
        .getId());
  }

  /**
   * Tests isActive
   */
  public void testIsActive() {
    DefaultTraceStepFactory factory;
    factory = new DefaultTraceStepFactory();
    ITraceStep ts = factory.newInstance(new SimpleTraceStepId("1"));

    assertFalse(ts.isActive());
    ts.enter();
    assertTrue(ts.isActive());
    ts.leave();
    assertFalse(ts.isActive());

  }

  /**
   * Tests equals
   */
  public void testEquals() {
    DefaultTraceStepFactory factory;

    factory = new DefaultTraceStepFactory(new MockTimerFactory(new long[] { 1, 2, 3 }));
    ITraceStep ts1 = factory.newInstance(new SimpleTraceStepId("1"));
    ITraceStep ts2 = factory.newInstance(new SimpleTraceStepId("2"));
    ITraceStep ts3 = factory.newInstance(new SimpleTraceStepId("1"));

    // Assign the execution times of the MockTimerFactory to the three steps
    ts1.enter();
    ts1.leave();
    ts2.enter();
    ts2.leave();
    ts3.enter();
    ts3.leave();

    // equals is supposed to compare _only_ the ids of trace steps. Hence,
    // ts1 and ts3 are equals, regardless of the different execution times
    assertTrue(ts1.equals(ts3));
    assertFalse(ts1.equals(ts2));

  }
}
