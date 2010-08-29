package com.omgen;

import com.omgen.discovery.PackageExplorer;
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
    private String omPackageName;
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

                case SCAN_SUBPACAKGES:
                    scanSubPackages = true;
                    break;

                case PACKAGE:
                    omPackageName = option.getValue();
                    break;
			}
        }
        generatorType = GeneratorType.of(getGeneratorClassName());

        args = cmd.getArgs();
        classList.addAll(classesToProcess(args));
    }

    private List<String> classesToProcess(String[] args) {
        List<String> classes = new ArrayList<String>();
        for (String arg : args) {
            if (isArgAPackage(arg)) {
                try {
                    classes.addAll(new PackageExplorer(arg).getClasses(this));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                classes.add(arg);
            }
        }
        return classes;
    }

    private boolean isArgAPackage(String arg) {
        return arg.endsWith(".");
    }

    public String getOmPackageName() {
        return omPackageName;
    }

    public List<String> getClassList() {
        return classList;
    }

    public boolean isScanSubPackages() {
        return scanSubPackages;
    }

	public String getTemplate() {
		return template;
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
