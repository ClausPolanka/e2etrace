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
 * Trace step id for single methods.
 * <p>
 *
 * This trace step provides a standard way to trace method executions. It
 * generates a uniform string representation using the class and method names.
 * <p>
 *
 * @author Gunther Popp
 *
 */
public class MethodTraceStepId extends AbstractTraceStepId {

  private static final long serialVersionUID = 1L;

  /**
   * Constructor using a <code>Class</code> instance.
   * <p>
   *
   * @param clazz Class instance
   * @param method method name
   */
  public MethodTraceStepId(Class clazz, String method) {
    this(clazz.getName(), method);
  }

  /**
   * Constructor using a classname.
   * <p>
   *
   * @param className class name
   * @param method method name
   */
  public MethodTraceStepId(String className, String method) {
    super(className + "#" + method);
  }

}
