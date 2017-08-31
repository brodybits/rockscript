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

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import io.rockscript.activity.Activity;
import io.rockscript.activity.NamedActivity;

import java.lang.reflect.Type;

public class ActivitySerializer implements JsonSerializer<Activity> {

  @Override
  public JsonElement serialize(Activity src, Type typeOfSrc, JsonSerializationContext context) {
    String name = null;
    if (src instanceof NamedActivity) {
      name = ((NamedActivity) src).getName();
    } else {
      name = src.getClass().getName();
    }
    return new JsonPrimitive(name);
  }
}
