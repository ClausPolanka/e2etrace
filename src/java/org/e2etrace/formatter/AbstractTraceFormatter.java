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
import org.e2etrace.trace.ITraceStep;


/**
 * Base class for trace formatters.
 * <p>
 *
 * This class provides the logic for traversing a tree of trace steps. For each
 * step the call-back <code>formatSingleStep</code> is invoked and the result
 * written to the output stream. Additionally, <code>formatHeader</code> and
 * <code>formatFooter</code> are invoked to generate formatter header and
 * footer information.
 * <p>
 *
 * @author Gunther Popp
 *
 */
public abstract class AbstractTraceFormatter implements ITraceFormatter {

  /**
   * Default constructor.
   * <p>
   *
   */
  protected AbstractTraceFormatter() {

  }

  /**
   * CALL-BACK: Generate formatted output for a single trace step.
   * <p>
   *
   * @param step trace step
   * @param level current call level (starting with 0)
   * @return formatted output
   */
  protected abstract String formatSingleStep(ITraceStep step, int level);

  /** {@inheritDoc} */
  public void format(ITraceSession session, Writer toWriter) throws IOException {
    writeHeader(session, toWriter);
    if(session.getRootStep() != null ) {
      writeSteps(session.getRootStep(), toWriter, 0);
    }
    writeFooter(session, toWriter);

  }

  /**
   * Returns the platform dependent newline character(s).<p>
   *
   * @return newline character(s)
   */
  protected String getNewLine() {
    return System.getProperty("line.separator");
  }

  /**
   * CALL-BACK: Write the footer of the trace output.
   * <p>
   *
   * @param session trace session
   * @param toWriter output writer
   * @throws IOException A problem occured sending the output to toWriter
   */
  protected abstract void writeFooter(ITraceSession session, Writer toWriter)
      throws IOException;

  /**
   * CALL-BACK: Write the header of the trace output.
   * <p>
   *
   * @param session trace session
   * @param toWriter output writer
   * @throws IOException A problem occured sending the output to toWriter
   */
  protected abstract void writeHeader(ITraceSession session, Writer toWriter)
      throws IOException;

  /**
   * Writes output of a trace step using a given call level and forwards the
   * call to all child steps.
   * <p>
   *
   * @param step current step
   * @param toWriter toWriter Writer to send the output to
   * @param level current call level (starting with 0)
   * @throws IOException A problem occured sending the output to toWriter
   */
  protected void writeSteps(ITraceStep step, Writer toWriter, int level)
      throws IOException {
    ITraceStep childSteps[];

    if (step != null) {
      toWriter.write(formatSingleStep(step, level));

      childSteps = step.getChildren();
      level = level + 1;
      for (int i = 0; i < childSteps.length; i++) {
        writeSteps(childSteps[i], toWriter, level);
      }

    }
  }
}
