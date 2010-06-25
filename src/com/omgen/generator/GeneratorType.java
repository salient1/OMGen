package com.omgen.generator;

import com.omgen.generator.dtype.DTypeVelocityGenerator;
import com.omgen.generator.makeiteasy.MakeItEasyGenerator;

/**
 *
 */
public enum GeneratorType {
    DEFAULT_VELOCITY_GENERATOR(DTypeVelocityGenerator.class),
    MAKE_IT_EASY_GENERATOR(MakeItEasyGenerator.class),
    CUSTOM(null);

    private Class<? extends Generator> generatorClass;

    GeneratorType(Class<? extends Generator> generatorClass) {
        this.generatorClass = generatorClass;
    }

    public static GeneratorType of(String fullClassName) {
        for (GeneratorType generatorType : values()) {
            if (generatorType.getGeneratorClass() != null
                && generatorType.getGeneratorClass().getName().equals(fullClassName)) {
                return generatorType;
            }
        }
        return CUSTOM;
    }

    public Class<? extends Generator> getGeneratorClass() {
        return generatorClass;
    }
}
