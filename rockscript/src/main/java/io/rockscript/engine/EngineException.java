/*
 * Copyright (c) 2017, RockScript.io. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.rockscript.engine;


import io.rockscript.engine.impl.Execution;

/** Base class for all RockScript exceptions. */
public class EngineException extends RuntimeException {

  Execution execution;

  public EngineException(String message) {
    super(message, null);
  }

  public EngineException(Throwable cause) {
    super(cause);
  }

  public EngineException(String message, Throwable cause) {
    super(message, cause);
  }

  public EngineException(String message, Execution execution) {
    this(message, execution, null);
  }

  public EngineException(String message, Execution execution, Throwable cause) {
    super(message, cause);
    this.execution = execution;
  }

  public static <T> T throwIfNull(T value) {
    return throwIfNull(value, null);
  }

  public static <T> T throwIfNull(T value, String message, Object... messageArguments) {
    if (value==null) {
      if (message==null) {
        message = "null value is invalid here";
      } else if (messageArguments!=null) {
        message = String.format(message, (Object[])messageArguments);
      }
      throw new EngineException(message);
    }
    return value;
  }

  public Execution getExecution() {
    return execution;
  }
}
