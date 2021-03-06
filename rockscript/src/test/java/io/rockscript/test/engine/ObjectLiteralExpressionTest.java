/*
 * Copyright (c) 2017 RockScript.io.
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package io.rockscript.test.engine;

import io.rockscript.Configuration;
import io.rockscript.Engine;
import io.rockscript.api.model.ScriptExecution;
import io.rockscript.api.model.ScriptVersion;
import io.rockscript.service.ImportObject;
import io.rockscript.service.ServiceFunctionOutput;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ObjectLiteralExpressionTest extends AbstractEngineTest {

  protected static Logger log = LoggerFactory.getLogger(ObjectLiteralExpressionTest.class);

  List<Object> capturedValues = new ArrayList<>();

  @Override
  protected Engine initializeEngine() {
    return new Configuration()
      .configureTest()
      .addImportProvider(new ImportObject("example.com/assert")
        .put("assertLiteralValue", input -> {
          capturedValues.add(input.getArgs().get(0));
          return ServiceFunctionOutput.endFunction();})
      )
      .build()
      .start();
  }

  @Test
  public void testSimpleStaticObjectLiteralExpressionWithIdentifier() {
    ScriptVersion scriptVersion = deployScript(
        "var o = {msg: 'hello'};");

    ScriptExecution scriptExecution = startScriptExecution(scriptVersion);
    @SuppressWarnings("unchecked")
    Map<String,Object> o = (Map<String, Object>) scriptExecution.getVariable("o");
    assertEquals("hello", o.get("msg"));
  }

  @Test
  public void testSimpleStaticObjectLiteralExpressionWithPropertyString() {
    ScriptVersion scriptVersion = deployScript(
        "var o = {'m s g': 'hello'};");

    ScriptExecution scriptExecution = startScriptExecution(scriptVersion);
    @SuppressWarnings("unchecked")
    Map<String,Object> o = (Map<String, Object>) scriptExecution.getVariable("o");
    assertEquals("hello", o.get("m s g"));
  }

  @Test
  public void testDynamicObjectLiteralExpression() {
    ScriptVersion scriptVersion = deployScript(
        "var greeting = 'hello'; \n"+
        "var o = {msg: greeting}; ");

    ScriptExecution scriptExecution = startScriptExecution(scriptVersion);
    @SuppressWarnings("unchecked")
    Map<String,Object> o = (Map<String, Object>) scriptExecution.getVariable("o");
    assertEquals("hello", o.get("msg"));
  }

}
