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

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import io.rockscript.EngineException;
import io.rockscript.activity.Activity;
import io.rockscript.activity.ActivityInput;
import io.rockscript.activity.ActivityOutput;
import io.rockscript.activity.http.HttpRequest;
import io.rockscript.activity.http.HttpResponse;

import static io.rockscript.activity.http.Http.ContentTypes.APPLICATION_JSON;
import static io.rockscript.activity.http.Http.Headers.CONTENT_TYPE;

public class RemoteActivity implements Activity {

  String url;
  String activityName;

  public RemoteActivity(String url, String activityName) {
    this.url = url;
    this.activityName = activityName;
  }

  @Override
  public ActivityOutput invoke(ActivityInput input) {
    Gson gson = input.getGson();
    String activityInputJson = gson.toJson(input);

    ContinuationReference continuationReference = input.getContinuationReference();
    String logPrefix = "["+continuationReference.getScriptExecutionId()+"|"+continuationReference.getExecutionId()+"]";

    HttpResponse httpResponse = HttpRequest.createPost(url + "/" + activityName)
        .header(CONTENT_TYPE, APPLICATION_JSON)
        .body(activityInputJson)
        .log(logPrefix)
        .execute()
        .log(logPrefix);

    int status = httpResponse.getStatus();
    if (status<200 || 300<status) {
      throw new EngineException("Remote HTTP activity did not return a status in the 200 range: "+status);
    }

    ActivityOutput activityOutput = null;
    Object body = trim(httpResponse.getBody());
    if (body!=null) {
      try {
        JsonElement activityOutputJsonElement = gson.toJsonTree(body);
        activityOutput = gson.fromJson(activityOutputJsonElement, ActivityOutput.class);
      } catch (Exception e) {
        throw new EngineException("Couldn't parse remote HTTP activity response as ActivityOutput: " + e.getMessage(), e);
      }
    }

    if (activityOutput!=null) {
      return activityOutput;
    } else {
      // The default async activity output is returned when the HTTP response is empty.
      return ActivityOutput.waitForFunctionToCompleteAsync();
    }
  }

  private Object trim(Object body) {
    if (!(body instanceof String)) {
      return body;
    }
    String trimmed = ((String)body).trim();
    if ("".equals(trimmed)) {
      return null;
    }
    return trimmed;
  }

  @Override
  public String toString() {
    return url+"/"+activityName;
  }
}
