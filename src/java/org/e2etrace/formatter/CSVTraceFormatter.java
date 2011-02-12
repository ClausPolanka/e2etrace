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
import java.util.Stack;

import org.e2etrace.trace.ITraceSession;
import org.e2etrace.trace.ITraceStep;
import org.e2etrace.trace.TraceSessionRootStep;
import org.e2etrace.trace.TraceSessionRootStepId;


/**
 * CSV trace formatter.
 * <p>
 *
 * This formatter generates a CSV stream from the data of a trace tree to
 * stdout. The format of the CSV stream is as follows:
 * <p>
 *
 * <pre>
 *  trace id, trace path, threadname, duration, isolated duration
 * </pre>
 *
 * The column <code>trace path</code> contains chained ids that represent the trace path.
 * The IDs will be separated by dots. Example: The Id
 * <code>$Session.step1.step2</code> will be generated for a trace step with
 * id <code>step2</code> that is a child of <code>step1</code> and is
 * executed in the context of trace session <code>$Session</code>.
 * <p>
 *
 * @author Gunther Popp
 *
 */
public class CSVTraceFormatter extends AbstractTraceFormatter {

  private static final String CSV_DELIM = ",";
  private static final String PATH_DELIM = "|";

  /**
   * Default constructor.
   * <p>
   *
   */
  public CSVTraceFormatter() {

  }

  /** {@inheritDoc} */
  protected String formatSingleStep(ITraceStep step, int level) {
    StringBuffer output = new StringBuffer();
    StringBuffer path = new StringBuffer();
    ITraceStep ts = step;
    String threadName = null;
    TraceSessionRootStepId rootId;
    Stack stepHierarchy = new Stack();

    // Build the hierarchical id and retrieve the thread name
    do {
      stepHierarchy.push(ts.getId().asString());

      if( ts instanceof TraceSessionRootStep && threadName == null) {
        rootId = (TraceSessionRootStepId) ts.getId();
        threadName = rootId.getThreadName();
      }

      ts = ts.getParent();

    } while( ts != null);

    while( !stepHierarchy.empty() ){
      path.append((String) stepHierarchy.pop());
      if(!stepHierarchy.empty()) {
        path.append(PATH_DELIM);
      }
    }

    // Write the CSV line
    output.append(step.getId().asString());
    output.append(CSV_DELIM);
    output.append(path.toString());
    output.append(CSV_DELIM);
    output.append(threadName);
    output.append(CSV_DELIM);
    output.append(step.getDuration());
    output.append(CSV_DELIM);
    output.append(step.getIsolatedDuration());
    output.append(getNewLine());

    return output.toString();
  }

  /** {@inheritDoc} */
  protected void writeFooter(ITraceSession session, Writer toWriter) {
    // This method intentionally does nothing

  }

  /** {@inheritDoc}
   * @throws IOException */
  protected void writeHeader(ITraceSession session, Writer toWriter) throws IOException {
    toWriter.write("id,path,threadname,duration,isolated_duration");
    toWriter.write(getNewLine());
  }
}
