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

import java.io.IOException;
import java.io.StringWriter;

import org.e2etrace.formatter.ITraceFormatter;
import org.e2etrace.formatter.PlainTextTraceFormatter;
import org.e2etrace.trace.DefaultTraceSession;
import org.e2etrace.trace.DefaultTraceStepFactory;
import org.e2etrace.trace.ITraceStep;
import org.e2etrace.trace.MethodTraceStepId;
import org.e2etrace.trace.SimpleTraceStepId;

import junit.framework.TestCase;

/**
 * JUnit testcase for {@link org.e2etrace.trace.DefaultTraceStep}
 *
 * The test uses {@link org.e2etrace.trace.MockTimer} instead of real timers.
 *
 * @author Gunther Popp
 *
 */
public class DefaultTraceSessionTest extends TestCase {

  public static void main(String[] args) {
    junit.textui.TestRunner.run(DefaultTraceSessionTest.class);
  }

  /**
   * OK: Measure duration between enterStep and leaveStep
   *
   * @throws InterruptedException
   * @throws IOException
   */
  public void testMeasureDuration() throws InterruptedException, IOException {
    DefaultTraceSession ts;
    ITraceStep step;

    ts = new DefaultTraceSession("testMeasureDuration", new DefaultTraceStepFactory(
        new MockTimerFactory(new long[] { 40, 20 })));

    ts.enterStep("1");
    ts.enterStep("2");
    ts.leaveStep("2");
    ts.leaveStep("1");

    // Verify that the duration of the Trace Session matches
    // the simulated
    // execution times
    assertEquals(40, ts.getDuration());

    // The Trace Session serves only as anchor and doesnt measure any
    // execution time. Hence, the accumulated duration of sts and
    // it´s root step must be identical
    step = ts.getRootStep();

    assertEquals(ts.getDuration(), step.getDuration());

    // Verify the isolated duration of the Trace Step "1"
    step = step.getChildren()[0];
    assertEquals(20, step.getIsolatedDuration());

  }

  /**
   * OK: Omit leaveStep on all children
   */
  public void testOmitLeaveStep() {
    DefaultTraceSession ts;
    ITraceStep step;

    ts = new DefaultTraceSession("testOmitLeaveStep", new DefaultTraceStepFactory(
        new MockTimerFactory(new long[] { 110, 70, 20 })));

    ts.enterStep("1");
    ts.enterStep("2");
    ts.enterStep("3");

    // Leave only Step 1 (it is expected, that all children of 1 are left
    // automatically)
    ts.leaveStep("1");

    assertEquals(110, ts.getDuration());

    step = ts.getRootStep().getChildren()[0];
    assertEquals(40, step.getIsolatedDuration());

    step = step.getChildren()[0];
    assertEquals(50, step.getIsolatedDuration());

    step = step.getChildren()[0];
    assertEquals(20, step.getIsolatedDuration());
  }

  /**
   * Tests the session using a trace configuration with disabled tracing.
   * <p>
   */
  public void testSwitchOffTracing() {
    DefaultTraceSession ts;
    MockTraceConfig tc;

    tc = new MockTraceConfig();
    tc.setTraceEnabled(false);

    ts = new DefaultTraceSession("testUsingConfig", new DefaultTraceStepFactory(
        new MockTimerFactory(new long[] { 40, 20 })));
    ts.setConfig(tc);

    ts.enterStep("1");
    ts.enterStep("2");
    ts.leaveStep("2");
    ts.leaveStep("1");

    // No trace data has been collected by the sessiono
    assertEquals(0, ts.getDuration());

    // Verify, that no children have been added to the session
    assertEquals(0, ts.getRootStep().getChildren().length);

  }

  /**
   * Tests the session using a trace configuration with disabled tracing for
   * some trace ids.
   * <p>
   */
  public void testSwitchOffIds() {
    DefaultTraceSession ts;
    ITraceStep step;
    MockTraceConfig tc;

    // Switch off "upper level" trace step
    // -----------------------------------------
    tc = new MockTraceConfig();
    tc.setTraceEnabledForId(new SimpleTraceStepId("1"), false);

    ts = new DefaultTraceSession("testUsingConfig", new DefaultTraceStepFactory(
        new MockTimerFactory(new long[] { 60, 20 })));
    ts.setConfig(tc);

    ts.enterStep("1");
    ts.enterStep("2");
    ts.enterStep("3");
    ts.leaveStep("3");
    ts.leaveStep("2");
    ts.leaveStep("1");

    // printTraceTree(ts);

    // Verify, that only step 2 has been added to the session (this step
    // will receive the first
    // execution time specified for the MockTimer).
    assertEquals(1, ts.getRootStep().getChildren().length);
    assertEquals(60, ts.getDuration());

    step = ts.getRootStep().getChildren()[0];
    assertEquals("2", step.getId().asString());
    assertEquals(40, step.getIsolatedDuration());
    assertEquals(ts.getRootStep(), step.getParent());

    // Switch off "lower level" trace step
    // -----------------------------------------
    tc = new MockTraceConfig();
    tc.setTraceEnabledForId(new SimpleTraceStepId("2"), false);

    ts = new DefaultTraceSession("testUsingConfig", new DefaultTraceStepFactory(
        new MockTimerFactory(new long[] { 60, 20 })));
    ts.setConfig(tc);

    ts.enterStep("1");
    ts.enterStep("2");
    ts.enterStep("3");
    ts.leaveStep("3");
    ts.leaveStep("2");
    ts.leaveStep("1");

    // printTraceTree(ts);

    // Verify, that only step 1 has been added to the session (this step
    // will receive the first
    // execution time specified for the MockTimer).
    assertEquals(1, ts.getRootStep().getChildren().length);
    assertEquals(60, ts.getDuration());

    step = ts.getRootStep().getChildren()[0];
    assertEquals("1", step.getId().asString());
    assertEquals(40, step.getIsolatedDuration());
    assertEquals(ts.getRootStep(), step.getParent());
    assertEquals(1, step.getChildren().length);

  }

  /**
   * Test-case: Check getDuration() after children are added manually to a trace
   * step.
   * <p>
   *
   * This didn´t work initially for the multithreaded test-app, where each of
   * the parallel threads is added manually to the trace session of the client
   * application.
   * <p>
   */
  public void testMeasureAfterAddChildren() {
    DefaultTraceSession ts1, ts2;

    // OK: Add children to root step ------------------------------

    ts1 = new DefaultTraceSession("testMeasureAfterAddChildren-1",
        new DefaultTraceStepFactory(new MockTimerFactory(new long[] { 40, 20 })));

    ts1.enterStep("1");
    ts1.enterStep("2");
    ts1.leaveStep("2");
    ts1.leaveStep("1");

    ts2 = new DefaultTraceSession("testMeasureAfterAddChildren-2",
        new DefaultTraceStepFactory(new MockTimerFactory(new long[] { 40, 20 })));

    ts2.enterStep("1");
    ts2.enterStep("2");
    ts2.leaveStep("2");
    ts2.leaveStep("1");

    // Add trace of session 2 to session 1
    ts1.getRootStep().addChild(ts2.getRootStep());

    // Verify that the duration of the Trace Session matches
    // the accumulated duration of BOTH sessions
    assertEquals(80, ts1.getDuration());

    // OK: Add children to "normal" step ------------------------------

    ts2 = new DefaultTraceSession("testMeasureAfterAddChildren-2",
        new DefaultTraceStepFactory(new MockTimerFactory(new long[] { 40, 20 })));

    ts2.enterStep("1");
    ts2.enterStep("2");
    ts2.leaveStep("2");
    ts2.leaveStep("1");

    ts1 = new DefaultTraceSession("testMeasureAfterAddChildren-1",
        new DefaultTraceStepFactory(new MockTimerFactory(new long[] { 40, 20 })));

    ts1.enterStep("1");
    ts1.enterStep("2");
    ts1.leaveStep("2");
    ts1.getCurrentStep().addChild(ts2.getRootStep());
    ts1.leaveStep("1");

    // printTraceTree(ts1);

    // In this case the duration of session 2 is not added to session 1
    assertEquals(40, ts1.getDuration());

    // For trace step 1 no valid isolated duration can be determined, since
    // session 2 has been
    // manually added
    assertEquals(-1, ts1.getRootStep().getChildren()[0].getIsolatedDuration());

  }

  /**
   * Test case for multiple calls to the same sub-method from within one trace
   * step.
   * <p>
   *
   */
  public void testMultipleMethodCalls() {
    DefaultTraceSession ts;

    ts = new DefaultTraceSession("testMultipleMethodCalls", new DefaultTraceStepFactory(
        new MockTimerFactory(new long[] { 100, 20, 20, 20, 20, 20 })));

    ts.enterStep(new MethodTraceStepId(this.getClass(), "method1"));

    for (int i = 0; i < 5; i++) {
      ts.enterStep(new MethodTraceStepId(this.getClass(), "method2"));
      ts.leaveStep(new MethodTraceStepId(this.getClass(), "method2"));
    }

    ts.leaveStep(new MethodTraceStepId(this.getClass(), "method1"));

    // printTraceTree(ts);

    // Verify that the duration of the Trace Session matches
    // the simulated
    // execution times
    assertEquals(100, ts.getDuration());
    assertEquals(5, ts.getRootStep().getChildren()[0].getChildren().length);
  }

  /**
   * Test case for recursive calls to the same sub-method from within one trace
   * step.
   * <p>
   *
   */
  public void testRecursiveMethodCalls() {
    DefaultTraceSession ts;
    ITraceStep step;

    ts = new DefaultTraceSession("testMultipleMethodCalls", new DefaultTraceStepFactory(
        new MockTimerFactory(new long[] { 100, 20, 20, 20, 20, 20 })));

    ts.enterStep(new MethodTraceStepId(this.getClass(), "method1"));

    for (int i = 0; i < 5; i++) {
      ts.enterStep(new MethodTraceStepId(this.getClass(), "method2"));
    }
    for (int i = 0; i < 5; i++) {
      ts.leaveStep(new MethodTraceStepId(this.getClass(), "method2"));
    }

    ts.leaveStep(new MethodTraceStepId(this.getClass(), "method1"));

    // printTraceTree(ts);

    // Verify that the duration of the Trace Session matches
    // the simulated
    // execution times
    assertEquals(100, ts.getDuration());

    step = ts.getRootStep().getChildren()[0];
    for (int i = 0; i < 5; i++) {
      step = step.getChildren()[0];
      assertEquals(20, step.getDuration());

    }

  }

  /**
   * Helper method: Print a tree of trace steps to stdout.
   * <p>
   *
   * @param ts
   */
  private void printTraceTree(DefaultTraceSession ts) {
    ITraceFormatter tf = new PlainTextTraceFormatter();
    StringWriter testOutput = new StringWriter();

    try {
      tf.format(ts, testOutput);
    } catch (IOException e) {
      e.printStackTrace();
    }

    System.out.println(testOutput.toString());

  }
}
