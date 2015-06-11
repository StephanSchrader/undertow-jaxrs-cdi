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
