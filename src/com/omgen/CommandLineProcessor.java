package com.omgen;

import org.apache.commons.cli.*;

import static com.omgen.generator.OptionSetting.*;

public class CommandLineProcessor {

    public boolean isValidInvocation(CommandLine cmd) {
        return !(cmd == null || cmd.getArgList().size() < 1);
    }

    public void dumpHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("OMGen <args> ClassToProcess or package.name.", getCommandLineOptions());
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
        options.addOption(PACKAGE.optionKey(), true, "package name to inject into generated OMs");
        options.addOption(SIMULATION.optionKey(), false, "run simulation (output to console only)");
        options.addOption(TEMPLATE.optionKey(), false, "override default template with supplied one");
        options.addOption(SCAN_SUBPACAKGES.optionKey(), false, "indicates subpackages should be traversed when a package is given");
        return options;
    }
}