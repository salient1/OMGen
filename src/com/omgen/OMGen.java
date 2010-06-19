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

    public static void main(String[] args) {
        CommandLine cmd = cmdProcessor.parseCommandLine(args);

        if (!cmdProcessor.isValidArguments(cmd))
            abend();

        new OMGen().run(new InvocationContext(cmd));
    }

    private void run(InvocationContext context) {
        Generator generator = createGenerator(context);
        try {
            String result = generator.generate(loadClass(context.getArgs()[0]));  // currently, only one arg is supported

            if (context.isSimulation()) {
                System.out.println(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Generator createGenerator(InvocationContext context) {
        Generator generator;
        if (context.getGeneratorType() == GeneratorType.CUSTOM) {
            generator = GeneratorFactory.createGenerator(context.getGeneratorClassName());
        } else {
            generator = GeneratorFactory.createGenerator(context.getGeneratorType());
        }
        return generator;
    }

    private Class loadClass(String classToProcess) {
        try {
            return Class.forName(classToProcess);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Class not found: " + classToProcess);
        }
    }

    private static void abend() {
        System.out.println("ERROR: Invalid parameters or arguments specified\n");
        cmdProcessor.dumpHelp();
        System.exit(-1);
    }
}
