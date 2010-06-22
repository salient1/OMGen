package com.omgen;

import com.omgen.generator.GeneratorType;
import com.omgen.generator.OptionSetting;
import com.omgen.generator.builtin.DTypeVelocityGenerator;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;

/**
 *
 */
public class InvocationContext {
    private boolean simulation;
    private String generatorClassName = DTypeVelocityGenerator.class.getName();
	private String template;
    private GeneratorType generatorType;
    private String[] args;

    public InvocationContext(CommandLine cmd) {
        args = cmd.getArgs();

        for (Option option : cmd.getOptions()) {
			switch(OptionSetting.of(option)) {
				case GENERATOR:
					generatorClassName = option.getValue();
					break;

				case SIMULATION:
					simulation = true;
					break;

				case TEMPLATE:
					template = option.getValue();
					break;
			}
        }
        generatorType = GeneratorType.of(getGeneratorClassName());
    }

	private boolean isOption(Option option, String optionKey) {
		return option.getOpt().equals(optionKey);
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
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
