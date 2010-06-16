package com.omgen.generator;

import com.omgen.generator.builtin.DTypeVelocityGenerator;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.*;

public class GeneratorFactoryTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testCreateDefaultGenerator() throws Exception {
        Generator generator = GeneratorFactory.createGenerator(GeneratorType.DEFAULT_VELOCITY_GENERATOR);
        assertNotNull(generator);
        assertEquals(DTypeVelocityGenerator.class, generator.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateNullGenerator() throws Exception {
        Generator generator = GeneratorFactory.createGenerator(null);
        assertNotNull(generator);
    }
}
