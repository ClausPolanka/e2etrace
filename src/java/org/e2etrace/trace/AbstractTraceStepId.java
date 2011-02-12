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

import java.io.Serializable;

/**
 * Base class for trace step ids.
 * <p>
 *
 * This class ensures that all trace step id use the same <code>equals</code>
 * and <code>hashCode</code> implementation based on the string representation
 * of the trace step id.
 * <p>
 *
 * @author Gunther Popp
 *
 */
public class AbstractTraceStepId implements ITraceStepId, Serializable {

  private static final long serialVersionUID = 1L;
  private String id;

  /**
   * Constructor.
   * <p>
   *
   * @param idAsString string representation of the trace step id.
   *          <p>
   */
  protected AbstractTraceStepId(String idAsString) {
    this.id = idAsString;
  }

  /** {@inheritDoc} */
  public int hashCode() {
    final int PRIME = 31;
    int result = 1;
    result = PRIME * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  /** {@inheritDoc} */
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    final AbstractTraceStepId other = (AbstractTraceStepId) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

  /** {@inheritDoc} */
  public String asString() {
    return this.id;
  }

}
