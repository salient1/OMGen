package com.omgen;

import com.omgen.generator.Generator;
import com.omgen.generator.GeneratorFactory;
import com.omgen.generator.GeneratorType;
import org.apache.commons.cli.CommandLine;

/**
 * Main entry point to generator.
 */
public class OMGen {
    public static final String APP_NAME = "OMGen";
    private static final CommandLineProcessor cmdProcessor = new CommandLineProcessor();

    private boolean simulationMode;

    public static void main(String[] args) {
        CommandLine cmd = null;

        cmd = cmdProcessor.parseCommandLine(args);

        if (!cmdProcessor.validArguments(cmd))
            abend();

        new OMGen().run(cmd);
    }

    private void run(CommandLine cmd) {
        simulationMode = cmd.hasOption("s");
        Generator generator = GeneratorFactory.createGenerator(GeneratorType.DEFAULT_VELOCITY_GENERATOR);
        try {
            String result = generator.generate(loadClass(cmd.getArgs()[0]));
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Class loadClass(String classToProcess) {
        try {
            return Class.forName(classToProcess);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Class not found: " + classToProcess);
        }
    }

    private static void abend() {
        System.out.println("ERROR: Invalid parameters specified\n");
        cmdProcessor.dumpHelp();
        System.exit(-1);
    }
}
