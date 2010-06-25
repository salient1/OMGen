package com.omgen;

import com.omgen.discovery.ClassUtils;
import com.omgen.generator.GeneratorType;
import com.omgen.generator.OptionSetting;
import com.omgen.generator.dtype.DTypeVelocityGenerator;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class InvocationContext {
    private boolean simulation;
    private boolean scanSubPackages;
    private String generatorClassName = DTypeVelocityGenerator.class.getName();
	private String template;
    private GeneratorType generatorType;
    private String[] args;
    private List<String> classList = new ArrayList<String>();

    public InvocationContext(CommandLine cmd) {

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

        args = cmd.getArgs();
        classList.addAll(computeClassesToProcess(args));
    }

    private List<String> computeClassesToProcess(String[] args) {
        List<String> classes = new ArrayList<String>();
        for (String arg : args) {
            if (arg.endsWith(".")) {
                try {
                    classes.addAll(ClassUtils.getClasses(arg, this));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                classes.add(arg);
            }
        }
        return classes;
    }

    public List<String> getClassList() {
        return classList;
    }

    public void setClassList(List<String> classList) {
        this.classList = classList;
    }

    public boolean isScanSubPackages() {
        return scanSubPackages;
    }

    public void setScanSubPackages(boolean scanSubPackages) {
        this.scanSubPackages = scanSubPackages;
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
