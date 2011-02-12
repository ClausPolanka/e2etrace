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

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.e2etrace.formatter.AbstractTraceFormatter;
import org.e2etrace.formatter.CSVTraceFormatter;
import org.e2etrace.trace.ITraceSession;

import junit.framework.TestCase;

/**
 * JUnit testcase for {@link org.e2etrace.formatter.CSVTraceFormatter}.
 * <p>
 * 
 * @author Gunther Popp
 * 
 */
public class CSVTraceFormatterTest extends TestCase {

  private static String EXPECTED = "id,path,threadname,duration,isolated_duration"
      + System.getProperty("line.separator") 
      + "$TestSession,$TestSession,main,195,0"
      + System.getProperty("line.separator") 
      + "Root_1,$TestSession|Root_1,main,110,40"
      + System.getProperty("line.separator")
      + "Child_1_1,$TestSession|Root_1|Child_1_1,main,70,70"
      + System.getProperty("line.separator") 
      + "Root_2,$TestSession|Root_2,main,80,0"
      + System.getProperty("line.separator")
      + "Child_2_1,$TestSession|Root_2|Child_2_1,main,80,50"
      + System.getProperty("line.separator")
      + "Child_2_2,$TestSession|Root_2|Child_2_1|Child_2_2,main,20,20"
      + System.getProperty("line.separator")
      + "Child_2_3,$TestSession|Root_2|Child_2_1|Child_2_3,main,10,10"
      + System.getProperty("line.separator") 
      + "Root_3,$TestSession|Root_3,main,5,5"
      + System.getProperty("line.separator");

  public static void main(String[] args) {
    junit.textui.TestRunner.run(CSVTraceFormatterTest.class);
  }

  /**
   * Test case for <code>format</code>
   * 
   * @throws IOException Error while generating test output
   * 
   */
  public void testFormat() throws IOException {
    ITraceSession session;
    Writer testOutput;
    AbstractTraceFormatter formatter;

    session = GenerateTestSession.generate();
    formatter = new CSVTraceFormatter();
    testOutput = new StringWriter();

    formatter.format(session, testOutput);

    assertEquals("CSV output not equal", EXPECTED, testOutput.toString());

  }

}
