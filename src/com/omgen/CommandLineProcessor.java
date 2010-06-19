package com.omgen;

import org.apache.commons.cli.*;

public class CommandLineProcessor {
    public static final String OPTION_GENERATOR = "g";
    public static final String OPTION_SIMULATION = "s";

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

        options.addOption(OPTION_GENERATOR, true, "generator type (args example: DEFAULT_VELOCITY_GENERATOR)");
        options.addOption(OPTION_SIMULATION, false, "run simulation (output to console only)");
        return options;
    }
}