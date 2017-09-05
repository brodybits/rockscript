/*
 * Copyright ©2017, RockScript.io. All rights reserved.
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

import com.google.gson.Gson;
import io.rockscript.Server;
import io.rockscript.engine.ScriptService;
import io.rockscript.http.Http;
import io.rockscript.http.HttpRequest;
import io.rockscript.http.HttpResponse;
import io.rockscript.netty.router.AsyncHttpServerConfiguration;
import io.rockscript.netty.router.Interceptor;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;

import java.io.IOException;

public abstract class AbstractServerTest {

  /** if you add interceptor {@link ServerExceptionInterceptor}
   * with {@link AsyncHttpServerConfiguration#interceptor(Interceptor)}
   * to your test asyncHttpServer, this member field will contain the latest
   * asyncHttpServer side exception.  If a test request fails or if it does
   * not get the expected response status, this asyncHttpServer exception is
   * used as the cause. */
  static Throwable serverException;

  public static Http http;
  static TestServer server;
  static String baseUrl;

  @BeforeClass
  public static void setUpStatic() {
    if (server==null) {
      server = new TestServer();
      server.execute();
      http = server.getHttp();
      baseUrl = "http://localhost:"+server.getPort()+"/";
    }
  }

  public static class TestServer extends Server {
    public Http getHttp() {
      return serviceConfiguration.getHttp();
    }
    public int getPort() {
      return serverConfiguration.getAsyncHttpServerConfiguration().getPort();
    }
    @Override
    protected AsyncHttpServerConfiguration createAsyncHttpServerConfiguration(Gson commonGson, ScriptService scriptService) {
      return super.createAsyncHttpServerConfiguration(commonGson, scriptService)
        .interceptor(new ServerExceptionInterceptor());
    }
  }

  @AfterClass
  public static void tearDownStatic() {
    server.shutdown();
    server.waitForShutdown();
  }

  @Rule
  public TestLogger testMethodNameAccessor = new TestLogger();

  @Before
  public void setUp() {
    serverException = null;
  }

  public String createUri(final String path) {
    return baseUrl+path;
  }

  public HttpRequest newGet(final String path) {
    return new TestHttpRequest(http, Http.Methods.GET, createUri(path));
  }

  public HttpRequest newPost(final String path) {
    return new TestHttpRequest(http, Http.Methods.POST, createUri(path));
  }

  public HttpRequest newPut(final String path) {
    return new TestHttpRequest(http, Http.Methods.PUT, createUri(path));
  }

  public HttpRequest newDelete(final String path) {
    return new TestHttpRequest(http, Http.Methods.DELETE, createUri(path));
  }

  static class TestHttpRequest extends HttpRequest {
    public TestHttpRequest(Http http, String method, String url) {
      super(http, method, url);
    }
    @Override
    protected HttpResponse createHttpResponse() throws IOException {
      return new TestHttpResponse(this);
    }
  }

  static class TestHttpResponse extends HttpResponse {
    public TestHttpResponse(TestHttpRequest testHttpRequest) throws IOException {
      super(testHttpRequest);
    }
    @Override
    public HttpResponse assertStatus(int expectedStatus) {
      if (status!=expectedStatus) {
        Throwable serverCause = serverException;
        throw new RuntimeException("Status was " + status + ", expected " + expectedStatus, serverCause);
      }
      return this;
    }
  }

  public String getMethodName() {
    return testMethodNameAccessor.getMethodName();
  }
}