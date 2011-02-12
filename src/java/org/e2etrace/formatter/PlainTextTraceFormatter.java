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

import java.io.Writer;

import org.e2etrace.trace.ITraceSession;
import org.e2etrace.trace.ITraceStep;
import org.e2etrace.trace.TraceSessionRootStep;
import org.e2etrace.trace.TraceSessionRootStepId;


/**
 * Plain text trace formatter.
 * <p>
 *
 * This formatter generates plain text and can be used to print trace trees to
 * stdout.
 * <p>
 *
 * @author Gunther Popp
 *
 */
public class PlainTextTraceFormatter extends AbstractTraceFormatter {

  /**
   * Default constructor.
   * <p>
   *
   */
  public PlainTextTraceFormatter() {

  }

  /** {@inheritDoc} */
  protected String formatSingleStep(ITraceStep step, int level) {
    StringBuffer output = new StringBuffer();
    TraceSessionRootStepId rootId;

    for (int i = 0; i < level; i++) {
      output.append("  ");
    }

    if (step instanceof TraceSessionRootStep) {
      rootId = (TraceSessionRootStepId) step.getId();

      output.append(">> ");
      output.append(rootId.asString());
      output.append(" [" + rootId.getThreadName() + "]");
    } else {
      output.append(step.getId().asString());

    }
    output.append(" (");
    output.append("Total: ");
    output.append(step.getDuration());
    output.append("ms, ");

    if (step.getIsolatedDuration() >= 0) {
      output.append("Step: ");
      output.append(step.getIsolatedDuration());
      output.append("ms");
    } else {
      output.append("n/a");
    }

    output.append(")");
    output.append(getNewLine());

    return output.toString();
  }

  /** {@inheritDoc} */
  protected void writeFooter(ITraceSession session, Writer toWriter) {
    // This method intentionally does nothing

  }

  /** {@inheritDoc} */
  protected void writeHeader(ITraceSession session, Writer toWriter) {
    // This method intentionally does nothing

  }

}
