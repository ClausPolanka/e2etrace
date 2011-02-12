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

import org.e2etrace.config.DefaultTraceConfig;
import org.e2etrace.formatter.ITraceFormatter;
import org.e2etrace.formatter.PlainTextTraceFormatter;
import org.e2etrace.trace.DefaultTraceSession;
import org.e2etrace.trace.DefaultTraceSessionManager;
import org.e2etrace.trace.DefaultTraceStepFactory;
import org.e2etrace.trace.ITraceSession;
import org.e2etrace.trace.SimpleTraceStepId;

import junit.framework.TestCase;

/**
 * JUnit testcase for {@link org.e2etrace.trace.DefaultTraceSessionManager}
 *
 * <em>Caution:</em> The test erases any trace sessions of the session
 * manager!
 *
 * @author Gunther Popp
 *
 */
public class DefaultTraceSessionManagerTest extends TestCase {

  private static final DefaultTraceSessionManager tsm = DefaultTraceSessionManager
      .getInstance();

  public static void main(String[] args) {
    junit.textui.TestRunner.run(DefaultTraceSessionManagerTest.class);
  }

  /**
   * Test getCurrentSession
   */
  public void testGetCurrentSession() {
    ITraceSession ts = new DefaultTraceSession("testGetCurrentSession",
        new DefaultTraceStepFactory());

    tsm.setCurrentSession(ts);

    // The following test intentionally compares the trace session references
    assertTrue("getCurrentSession liefert falsche Referenz zurück", tsm
        .getCurrentSession() == ts);
  }

  /**
   * Test disabling of trace sessions
   */
  public void testDisableSession() {
    MockTraceConfig tc;
    DefaultTraceSession ts;

    tc = new MockTraceConfig();
    tc.setTraceEnabledForId(new SimpleTraceStepId("$testDisableSession"), false);

    ts = new DefaultTraceSession("testDisableSession", new DefaultTraceStepFactory(
        new MockTimerFactory(new long[] { 60, 20 })));

    tsm.setConfig(tc);
    tsm.setCurrentSession(ts);

    tsm.getCurrentSession().enterStep("1");
    tsm.getCurrentSession().enterStep("2");
    tsm.getCurrentSession().enterStep("3");
    tsm.getCurrentSession().leaveStep("3");
    tsm.getCurrentSession().leaveStep("2");
    tsm.getCurrentSession().leaveStep("1");

    printTraceTree(tsm.getCurrentSession());

    // Verify, that no steps have been added to the disabled session
    assertEquals(0, tsm.getCurrentSession().getDuration());
    assertNull(tsm.getCurrentSession().getCurrentStep());
    assertNull(tsm.getCurrentSession().getRootStep());

  }

  /**
   * Test calling enterStep/leaveStep without defining a current trace session.
   */
  public void testCallsWithoutSession() {

    tsm.getCurrentSession().enterStep("1");
    tsm.getCurrentSession().enterStep("2");
    tsm.getCurrentSession().enterStep("3");
    tsm.getCurrentSession().leaveStep("3");
    tsm.getCurrentSession().leaveStep("2");
    tsm.getCurrentSession().leaveStep("1");

    printTraceTree(tsm.getCurrentSession());

    assertEquals(0, tsm.getCurrentSession().getDuration());
    assertNull(tsm.getCurrentSession().getCurrentStep());
    assertNull(tsm.getCurrentSession().getRootStep());
  }

  /**
   * Test method setCurrentSession() usind a NoopTraceSession
   */
  public void testSetCurrentSessionWithNoopTraceSession() {   
    ITraceSession ts = new NoopTraceSession();

    tsm.setCurrentSession(ts);

    // The following test intentionally compares the trace session references
    assertTrue("getCurrentSession liefert falsche Referenz zurück", tsm
        .getCurrentSession() instanceof NoopTraceSession );

    
  }

  /**
   * Helper method: Print a tree of trace steps to stdout.
   * <p>
   *
   * @param ts
   */
  private void printTraceTree(ITraceSession ts) {
    ITraceFormatter tf = new PlainTextTraceFormatter();
    StringWriter testOutput = new StringWriter();

    try {
      tf.format(ts, testOutput);
    } catch (IOException e) {
      e.printStackTrace();
    }

    System.out.println(testOutput.toString());

  }

  /** {@inheritDoc} */
  protected void tearDown() throws Exception {
    super.tearDown();

    // Clean up
    tsm.setConfig(new DefaultTraceConfig());
    tsm.releaseCurrentSession();

  }

}
