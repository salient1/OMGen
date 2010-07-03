package com.omgen;

import com.omgen.generator.Generator;
import com.omgen.generator.GeneratorFactory;
import com.omgen.generator.GeneratorType;
import com.omgen.generator.exception.NoSetterMethodsException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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

    private void run(InvocationContext invocationContext) {
        Generator generator = createGenerator(invocationContext);
        try {
            for (String className : invocationContext.getClassList()) {
                String result = null;
                Class classToProcess = null;
                try {
                    System.out.print("\nProcessing class: " + className + "...");
                    classToProcess = loadClass(className);
                    result = generator.generate(classToProcess, invocationContext);
                } catch (NoSetterMethodsException e) {
                    System.out.println("no setter methods found, skipped.");
                }

                if (result != null) {
                    if (invocationContext.isSimulation()) {
                        System.out.println(result);
                    } else {
                        writeFile(invocationContext, result, classToProcess);
                    }
                }
            }
            System.out.println("\nFinished.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeFile(InvocationContext invocationContext, String result, Class clazz) {
        FileWriter fileWriter = null;
        File file = new File(clazz.getSimpleName() + "ObjectMother.java");

        try {
            System.out.print("Writing file: "  + file.getCanonicalPath());
            fileWriter = new FileWriter(file);
            fileWriter.write(result);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(fileWriter);
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
