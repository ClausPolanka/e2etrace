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

import org.e2etrace.trace.SimpleTraceStepId;
import org.e2etrace.trace.TraceSessionRootStepId;

import junit.framework.TestCase;

/**
 * Test case for session root ids.<p>
 *
 * @author Gunther Popp
 */
public class TraceSessionRootStepIdTest extends TestCase {

  /**
   * Tests equals.<p>
   *
   * Two trace step ids are equal if the string representation of the id value
   * is equal. That is, even two instances of different classes are equal, if
   * only the instance.asString() values are equal. This test is important since
   * the generates equals methods by default fail, if the classes of two instances
   * are different.<p>
   */
  public void testEqualsObject() {
    TraceSessionRootStepId id1 = new TraceSessionRootStepId("session");
    SimpleTraceStepId id2 = new SimpleTraceStepId("$session");

    assertEquals(id1, id2);

  }

}
