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

package io.rockscript.service;

import io.rockscript.DeployScriptCommand;
import io.rockscript.EndActivityCommand;
import io.rockscript.ScriptService;
import io.rockscript.StartScriptExecutionCommand;
import io.rockscript.engine.ContinuationReference;
import io.rockscript.engine.EngineScriptExecution;

import java.util.List;

public class ScriptServiceImpl implements ScriptService {

  protected Configuration configuration;

  public ScriptServiceImpl(Configuration configuration) {
    this.configuration = configuration;
  }

  public DeployScriptCommand newDeployScriptCommand() {
    return new DeployScriptCommand(configuration);
  }

  public StartScriptExecutionCommand newStartScriptExecutionCommand() {
    return new StartScriptExecutionCommand(configuration);
  }

  @Override
  public EndActivityCommand newEndActivityCommand() {
    return new EndActivityCommand(configuration);
  }

  public Configuration getConfiguration() {
    return configuration;
  }

  @Override
  public List<EngineScriptExecution> recoverCrashedScriptExecutions() {
    return configuration
      .getEventStore()
      .recoverCrashedScriptExecutions();
  }
}
