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
import java.io.Writer;

import org.e2etrace.trace.ITraceSession;


/**
 * Formatters traverse a tree of trace steps and generate formatted output.
 * <p>
 *
 * @author Gunther Popp
 */
public interface ITraceFormatter {

  /**
   * Generate a formatted output for the supplied trace session.
   * <p>
   *
   * @param session trace session
   * @param toWriter Writer to send the output to
   * @throws IOException A problem occured sending the output to toWriter
   */
  void format(ITraceSession session, Writer toWriter) throws IOException;

}
