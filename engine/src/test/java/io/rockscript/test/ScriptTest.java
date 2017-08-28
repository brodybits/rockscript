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
package io.rockscript.test;

import io.rockscript.*;
import io.rockscript.engine.ContinuationReference;
import io.rockscript.service.Configuration;
import io.rockscript.service.ScriptServiceImpl;
import org.junit.Before;

import java.util.HashMap;
import java.util.Map;

public class ScriptTest {

  private static Map<Class<? extends ScriptServiceProvider>,ScriptService> scriptServiceCache = new HashMap<>();

  protected ScriptService scriptService;

  @Before
  public void setUp() {
    scriptService = initializeScriptService();
  }

  /** Override this method if you want your ScriptService to be
   * created for each test.
   *
   * Overwrite {@link #getScriptServiceProvider()} if you want to
   * customize and cache a ScriptService in your tests. */
  protected ScriptService initializeScriptService() {
    ScriptServiceProvider scriptServiceProvider = getScriptServiceProvider();
    Class<? extends ScriptServiceProvider> providerClass = scriptServiceProvider.getClass();
    ScriptService scriptService = scriptServiceCache.get(providerClass);
    if (scriptService==null) {
      scriptService = scriptServiceProvider.createScriptService();
      scriptServiceCache.put(providerClass, scriptService);
    }
    return scriptService;
  }

  protected interface ScriptServiceProvider {
    ScriptService createScriptService();
  }

  /** Override this method to customize and cache a ScriptService in your tests.
   *
   * Overwrite {@link #initializeScriptService()} if you want your ScriptService
   * to be created for each test. */
  protected ScriptServiceProvider getScriptServiceProvider() {
    return new ScriptServiceProvider() {
      @Override
      public ScriptService createScriptService() {
        return new TestConfiguration().build();
      }
    };
  }

  public Script deployScript(String scriptText) {
    return scriptService
      .newDeployScriptCommand()
      .scriptText(scriptText)
      .execute();
  }

  public ScriptExecution startScriptExecution(Script script) {
    return startScriptExecution(script.getId(), null);
  }

  public ScriptExecution startScriptExecution(Script script, Object input) {
    return startScriptExecution(script.getId(), input);
  }

  public ScriptExecution startScriptExecution(String scriptId) {
    return startScriptExecution(scriptId, null);
  }

  public ScriptExecution startScriptExecution(String scriptId, Object input) {
    StartScriptExecutionResponse response = scriptService.newStartScriptExecutionCommand()
        .scriptId(scriptId)
        .input(input)
        .execute();
    return response.getScriptExecution();
  }

  public ScriptExecution endActivity(ContinuationReference continuationReference) {
    return endActivity(continuationReference, null);
  }

  public ScriptExecution endActivity(ContinuationReference continuationReference, Object result) {
    return scriptService.newEndActivityCommand()
      .continuationReference(continuationReference)
      .result(result)
      .execute()
      .getScriptExecution();
  }

  protected Configuration getConfiguration() {
    return ((ScriptServiceImpl)scriptService).getConfiguration();
  }
}