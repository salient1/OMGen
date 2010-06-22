package com.omgen;

import org.apache.commons.cli.*;

import static com.omgen.generator.OptionSetting.*;

public class CommandLineProcessor {

    public boolean isValidArguments(CommandLine cmd) {
        if (cmd == null || cmd.getArgList().size() < 1) {
            return false;
        }

        return true;
    }

    public void dumpHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("OMGen <args> ClassToProcess", getCommandLineOptions());
    }

    public CommandLine parseCommandLine(String[] args) {
        CommandLine cmd = null;
        try {
            Options options = getCommandLineOptions();

            CommandLineParser parser = new PosixParser();
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cmd;
    }

    private Options getCommandLineOptions() {
        Options options = new Options();

        options.addOption(GENERATOR.optionKey(), true, "generator type (args example: DEFAULT_VELOCITY_GENERATOR)");
        options.addOption(SIMULATION.optionKey(), false, "run simulation (output to console only)");
        options.addOption(TEMPLATE.optionKey(), false, "override default template with supplied one");
        return options;
    }
}