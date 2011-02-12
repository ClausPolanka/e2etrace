package org.e2etrace.formatter;

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

import org.e2etrace.trace.DefaultTraceSession;
import org.e2etrace.trace.DefaultTraceStepFactory;
import org.e2etrace.trace.ITraceSession;
import org.e2etrace.trace.MockTimerFactory;


/**
 * Generates a tree of trace steps for testing purposes.
 * <p>
 *
 * @author Gunther Popp
 *
 */
public class GenerateTestSession {

  /**
   * Generate the tree of test steps.
   * <p>
   *
   * @return root step of the tree
   */
  public static ITraceSession generate() {
    DefaultTraceSession sts;

    sts = new DefaultTraceSession("TestSession", new DefaultTraceStepFactory(
        new MockTimerFactory(new long[] { 110, 70, 80, 80, 20, 10, 5 })));

    sts.enterStep("Root_1");
    sts.enterStep("Child_1_1");
    sts.leaveStep("Child_1_1");
    sts.leaveStep("Root_1");

    sts.enterStep("Root_2");
    sts.enterStep("Child_2_1");
    sts.enterStep("Child_2_2");
    sts.leaveStep("Child_2_2");
    sts.enterStep("Child_2_3");
    sts.leaveStep("Child_2_3");
    sts.leaveStep("Child_2_1");
    sts.leaveStep("Root_2");

    sts.enterStep("Root_3");
    sts.leaveStep("Root_3");

    return sts;

  }

}
