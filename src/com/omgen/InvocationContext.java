package com.omgen;

import com.omgen.generator.GeneratorType;
import com.omgen.generator.builtin.DTypeVelocityGenerator;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;

/**
 *
 */
public class InvocationContext {
    private boolean simulation;
    private String generatorClassName = DTypeVelocityGenerator.class.getName();
    private GeneratorType generatorType;
    private String[] args;

    public InvocationContext(CommandLine cmd) {
        args = cmd.getArgs();

        for (Option option : cmd.getOptions()) {
            if (option.getOpt().equals(CommandLineProcessor.OPTION_GENERATOR)) {
                generatorClassName = option.getValue();
            } else if (option.getOpt().equals(CommandLineProcessor.OPTION_SIMULATION)) {
                simulation = true;
            }
        }
        generatorType = GeneratorType.of(getGeneratorClassName());
    }

    public String[] getArgs() {
        return args;
    }

    public boolean isSimulation() {
        return simulation;
    }

    public String getGeneratorClassName() {
        return generatorClassName;
    }

    public GeneratorType getGeneratorType() {
        return generatorType;
    }
}
