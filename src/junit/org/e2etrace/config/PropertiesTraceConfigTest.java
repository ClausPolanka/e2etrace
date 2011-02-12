package org.e2etrace.config;

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

import org.e2etrace.config.PropertiesTraceConfig;
import org.e2etrace.trace.SimpleTraceStepId;


import junit.framework.TestCase;

/**
 * JUnit testcase for {@link org.e2etrace.config.PropertiesTraceConfig}
 *
 * @author Gunther Popp
 *
 */
public class PropertiesTraceConfigTest extends TestCase {

  private static String TESTDATA_FILE = "e2etrace/config/PropertiesTraceConfigTest.properties";

  public static void main(String[] args) {
    junit.textui.TestRunner.run(PropertiesTraceConfigTest.class);
  }

  /**
   * Test case for <code>loadConfigFile</code>
   *
   * @throws IOException Unexpected error while loading the test data
   */
  public void testLoadConfigFile() throws IOException {
    PropertiesTraceConfig tc = new PropertiesTraceConfig();

    // OK - Test
    tc.loadConfigFile(TESTDATA_FILE);

    // NOK-Test
    boolean ok = false;
    try {
      tc.loadConfigFile("NotExistent");
    } catch (IOException ioe) {
      ok = true;
    }

    if (!ok) {
      fail("Expected an IOException!");
    }

  }

  /**
   * Test case for <code>isTraceEnabled</code> and
   * <code>isTraceEnabledForId</code>
   *
   * @throws IOException Unexpected error while loading the test data
   */
  public void testIsTraceEnabledEtc() throws IOException {
    PropertiesTraceConfig tc = new PropertiesTraceConfig();

    tc.loadConfigFile(TESTDATA_FILE);

    assertTrue("enabletrace should be true", tc.isTraceEnabled());
    assertTrue("Trace step id1 should be true", tc
        .isTraceEnabledForId(new SimpleTraceStepId("id1")));
    assertFalse("Trace step id2 should be false", tc
        .isTraceEnabledForId(new SimpleTraceStepId("id2")));
    assertTrue("Undefined Trace step xxx should be true", tc
        .isTraceEnabledForId(new SimpleTraceStepId("xxx")));
  }

}
