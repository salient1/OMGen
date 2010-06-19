package com.omgen.generator;

import com.omgen.generator.builtin.DTypeVelocityGenerator;
import com.omgen.generator.builtin.MakeItEasyGenerator;

import java.util.HashMap;
import java.util.Map;

import static com.omgen.generator.GeneratorType.*;

/**
 * Constructs generators.
 */
public class GeneratorFactory {
    private static Map<GeneratorType, Class<? extends Generator>> generators = new HashMap<GeneratorType, Class<? extends Generator>>();

    static {
        generators.put(DEFAULT_VELOCITY_GENERATOR, DTypeVelocityGenerator.class);
        generators.put(MAKE_IT_EASY_GENERATOR, MakeItEasyGenerator.class);

    }

    public static Generator createGenerator(GeneratorType generatorType) {
        if (generatorType == null) {
            throw new IllegalArgumentException("Null generatorType not permitted");
        }

        return instantiateGenerator(generatorType, generators.get(generatorType));
    }

    public static Generator createGenerator(String generatorClassName) {
        if (generatorClassName == null) {
            throw new IllegalArgumentException("Null generatorClassName not permitted");
        }

        Class generatorClazz = loadClass(generatorClassName);

        if (generatorClazz.isAssignableFrom(Generator.class)) {
            return instantiateGenerator(GeneratorType.CUSTOM, generatorClazz);
        } else {
            throw new RuntimeException(String.format("Class %s does implement Generator", generatorClassName));
        }
    }

    private static Generator instantiateGenerator(GeneratorType generatorType, Class generatorClass) {
        Generator generator;

        try {
            generator = (Generator)generatorClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(String.format("Could not instantiate generator of type: %s, class: %s", generatorType.name(), generatorClass.getName()));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(String.format("Insufficient privileges to instantiate class: %s", generatorClass.getName()), e);
        }

        return generator;
    }

    private static Class loadClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Class not found: " + className);
        }
    }

}
