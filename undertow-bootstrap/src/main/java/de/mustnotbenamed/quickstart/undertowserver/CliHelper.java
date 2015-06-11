package de.mustnotbenamed.quickstart.undertowserver;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;

/**
 * @author schrader
 */
public class CliHelper {

    public static <T> T option(CommandLine cmd, Option option, T defaultValue) throws ParseException {
        T result = defaultValue;

        if (cmd.hasOption(option.getOpt())) {
            String optionValue = cmd.getOptionValue(option.getOpt());
            result = (T) TypeHandler.createValue(optionValue, option.getType());
        }

        return result;
    }

}
