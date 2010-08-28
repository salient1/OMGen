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

        if (!cmdProcessor.isValidInvocation(cmd))
            abend();

        new OMGen().run(new InvocationContext(cmd));
    }

    private void run(InvocationContext invocationContext) {
        Generator generator = createGenerator(invocationContext);
        try {
            for (String className : invocationContext.getClassList()) {
                System.out.print("\nProcessing class: " + className + "...");

                Class classToProcess = loadClass(className);

                String result = invokeGenerator(invocationContext, generator, classToProcess);

                writeResult(invocationContext.isSimulation(), result, classToProcess);
            }
            System.out.println("\nFinished.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String invokeGenerator(InvocationContext invocationContext, Generator generator, Class classToProcess) throws Exception {
        String result = null;
        try {
            result = generator.generate(classToProcess, invocationContext);
            System.out.print("\n");
        } catch (NoSetterMethodsException e) {
            System.out.println("no setter methods found, skipped.");
        }
        return result;
    }

    private void writeResult(boolean writeToConsole, String result, Class classToProcess) {
        if (result != null) {
            if (writeToConsole) {
                System.out.println(result);
            } else {
                writeFile(result, classToProcess);
            }
        }
    }

    private void writeFile(String result, Class clazz) {
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
