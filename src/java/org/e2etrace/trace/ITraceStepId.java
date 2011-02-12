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

/**
 * Unique ID for a trace step within a trace session.
 * <p>
 *
 * An example for a suitable trace step id is the fully qualified class name
 * plus the method name.
 * <p>
 *
 * @author Gunther Popp
 *
 */
public interface ITraceStepId {

  /**
   * Returns a String representation of the trace step id.
   * <p>
   *
   * In contrast to <code>toString</code> the String representation should be suitable
   * to be used in log and configuration files. Additionally it must uniquely identify an
   * id.<p>
   *
   * @return String representation of the trace step id
   */
  String asString();
}
