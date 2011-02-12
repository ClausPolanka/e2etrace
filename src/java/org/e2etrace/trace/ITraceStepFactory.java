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
 * Factory for {@link org.e2etrace.trace.ITraceStep} instances.
 * <p>
 *
 * @author Gunther Popp
 */
public interface ITraceStepFactory {

  /**
   * Returns a new trace step instance using the given id.
   *
   * @param id if of the new trace step
   *
   * @return Trace step instance
   */
  ITraceStep newInstance(ITraceStepId id);
}
