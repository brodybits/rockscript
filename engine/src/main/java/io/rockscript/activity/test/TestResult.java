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
package io.rockscript.activity.test;

import io.rockscript.EngineException;
import io.rockscript.engine.ErrorMessage;
import io.rockscript.engine.Event;
import io.rockscript.engine.Location;
import io.rockscript.util.Exceptions;

import java.util.ArrayList;
import java.util.List;

public class TestResult {

  List<Event> events = new ArrayList<>();
  ErrorMessage error;

  public void setError(ErrorMessage error) {
    this.error = error;
  }

  public void addEvent(Event event) {
    events.add(event);
  }

  public ErrorMessage getError() {
    return error;
  }

  public List<Event> getEvents() {
    return events;
  }
}
