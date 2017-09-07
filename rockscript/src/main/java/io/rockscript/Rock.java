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
package io.rockscript;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.rockscript.http.GsonCodec;
import io.rockscript.http.Http;
import org.apache.commons.cli.*;

import java.util.Map;

import static io.rockscript.Server.createCommandsTypeAdapterFactory;
import static io.rockscript.engine.impl.Event.createEventJsonTypeAdapterFactory;
import static io.rockscript.util.Maps.entry;
import static io.rockscript.util.Maps.hashMap;

public abstract class Rock {

  static Map<String,Class<? extends Rock>> COMMAND_CLASSES = hashMap(
    entry("server", Server.class),
    entry("ping", Ping.class),
    entry("deploy", Deploy.class)
  );

  public static void main(String[] args) {
    try {
      if (args==null
          || args.length==0) {
        showCommandsOverview();
      } else {
        String command = args[0];
        if ("help".equals(command)) {
          if (args.length>=2) {
            String helpCommand = args[1];
            Rock rock = getRock(helpCommand);
            if (rock!=null) {
              rock.showCommandUsage();
            }
          } else {
            showCommandsOverview();
          }
        } else {
          Rock rock = getRock(command);
          if (rock!=null) {
            rock.args = args;
            Options options = rock.getOptions();
            if (options!=null) {
              CommandLineParser commandLineParser = new DefaultParser();
              CommandLine commandLine = commandLineParser.parse(options, args);
              rock.parse(commandLine);
            }
            rock.execute();
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  static Rock getRock(String command) {
    try {
      Class<? extends Rock> rockClass = COMMAND_CLASSES.get(command);
      if (rockClass==null) {
        showCommandsOverview(command);
        return null;
      }
      return rockClass.newInstance();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  static void showCommandsOverview() {
    showCommandsOverview(null);
  }

  static void showCommandsOverview(String invalidCommand) {
    if (invalidCommand!=null) {
      log("Invalid command: " + invalidCommand);
      log();
    }
    log("Usage: rock [command] [command options]");
    log();
    log("rock help [command]          | Shows help on a particular command");
    log("rock server [server options] | Start the rockscript server");
    log("rock ping [ping options]     | Test the connection with the server");
    log("rock deploy [deploy options] | Deploy script files to the server");
    log("rock                         | Shows this help message");
    log();
    log("More details at https://github.com/rockscript/rockscript/wiki/RockScript-API");
  }

  protected String[] args;

  protected abstract void execute() throws Exception;
  protected abstract void showCommandUsage();
  protected abstract Options getOptions();
  protected abstract void parse(CommandLine commandLine);

  protected void logCommandUsage(String usage) {
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp(usage, getOptions());
  }

  protected static void log() {
    System.out.println();
  }

  protected static void log(String text) {
    System.out.println(text);
  }

  protected Gson createCommonGson() {
    return new GsonBuilder()
      .registerTypeAdapterFactory(createCommandsTypeAdapterFactory())
      .registerTypeAdapterFactory(createEventJsonTypeAdapterFactory())
      .create();
  }

  protected Http createHttp() {
    return new Http(new GsonCodec(createCommonGson()));
  }
}
