package com.omgen.generator;

import com.omgen.generator.builtin.DTypeVelocityGenerator;

import java.util.HashMap;
import java.util.Map;

import static com.omgen.generator.GeneratorType.DEFAULT_VELOCITY_GENERATOR;

/**
 * Constructs generators.
 */
public class GeneratorFactory {
    private static Map<GeneratorType, Class<? extends Generator>> generators = new HashMap<GeneratorType, Class<? extends Generator>>();

    static {
        generators.put(DEFAULT_VELOCITY_GENERATOR, DTypeVelocityGenerator.class);
    }

    public static Generator createGenerator(GeneratorType generatorType) {
        if (generatorType == null) {
            throw new IllegalArgumentException("Null generatorType not permitted");
        }

        return instantiateGenerator(generatorType, generators.get(generatorType));
    }

    private static Generator instantiateGenerator(GeneratorType generatorType, Class generatorClass) {
        Generator generator;

        try {
            generator = (Generator)generatorClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("Could not instantiate generator of type: " + generatorType);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Insufficient privileges to create generator of type: " + generatorType, e);
        }

        if (generator == null) {
            throw new RuntimeException("No implementation exists on the classpath for generator of type: " + generatorType);
        }

        return generator;
    }
}
