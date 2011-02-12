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
import org.e2etrace.formatter.PlainTextTraceFormatter;
import org.e2etrace.trace.ITraceSession;

import junit.framework.TestCase;

/**
 * JUnit testcase for {@link org.e2etrace.formatter.PlainTextTraceFormatter}.<p>
 *
 * Note: This test case is only intended for manual debugging.<p>
 *
 * @author Gunther Popp
 *
 */
public class PlainTextTraceFormatterDebug extends TestCase {

  public static void main(String[] args) {
    junit.textui.TestRunner.run(PlainTextTraceFormatterDebug.class);
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
    formatter = new PlainTextTraceFormatter();
    testOutput = new StringWriter();

    formatter.format(session, testOutput);

    System.out.println(testOutput.toString());

  }

}
