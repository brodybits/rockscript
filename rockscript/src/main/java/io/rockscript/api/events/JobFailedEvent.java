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
package io.rockscript.api.events;

import io.rockscript.engine.job.Job;

import java.time.Instant;

public class JobFailedEvent extends JobEvent {

  String jobId;
  String error;
  Instant nextRetryTime;

  JobFailedEvent() {
  }

  public JobFailedEvent(String jobId, String error) {
    this(jobId, error, null);
  }

  public JobFailedEvent(String jobId, String error, Instant nextRetryTime) {
    this.jobId = jobId;
    this.error = error;
    this.nextRetryTime = nextRetryTime;
  }

  public String getJobId() {
    return jobId;
  }

  public String getError() {
    return error;
  }

  public Instant getNextRetryTime() {
    return nextRetryTime;
  }
}
