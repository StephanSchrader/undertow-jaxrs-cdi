/**
 * This is free and unencumbered software released into the public domain.
 *
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors
 * of this software dedicate any and all copyright interest in the
 * software to the public domain. We make this dedication for the benefit
 * of the public at large and to the detriment of our heirs and
 * successors. We intend this dedication to be an overt act of
 * relinquishment in perpetuity of all present and future rights to this
 * software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, please refer to <http://unlicense.org>
 */
package de.mustnotbenamed.quickstart.undertowserver;

import org.apache.commons.cli.*;

/**
 * @author schrader
 */
public class Main {

    private static final Option OPTION_HOST = new Option("h", "host", true, "The host");
    private static final Option OPTION_PORT = new Option("p", "port", true, "The port");
    private static final Option OPTION_CONTEXT_PATH = new Option("c", "context-path", true, "The Webapp Context Path");

    public static void main(String[] args) {
        CommandLine cmd = null;

        try {
            CommandLineParser parser = new GnuParser();
            cmd = parser.parse(createCliOption(), args);
        } catch (Exception e) {
            System.err.println("Failed to parse options: " + e.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("webapp", createCliOption());

            System.err.println("\n");
            System.err.println("### DEV ###");
            e.printStackTrace();

            System.exit(-1);
        }

        // start Webserver
        try {
            UndertowBootstrap.UndertowOptions options = new UndertowBootstrap.UndertowOptions();

            options.setHost(CliHelper.option(cmd, OPTION_HOST, options.getHost()));
            options.setPort(CliHelper.option(cmd, OPTION_PORT, options.getPort()));
            options.setContextPath(CliHelper.option(cmd, OPTION_CONTEXT_PATH, options.getContextPath()));

            new WeldUndertowBootstrap(options).startup();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static Options createCliOption() {
        return new Options()
                .addOption(OPTION_HOST)
                .addOption(OPTION_PORT)
                .addOption(OPTION_CONTEXT_PATH);
    }

}
