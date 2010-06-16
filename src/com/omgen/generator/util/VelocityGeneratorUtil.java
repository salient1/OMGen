package com.omgen.generator.util;

import com.omgen.OMGen;
import org.apache.velocity.VelocityContext;

public final class VelocityGeneratorUtil {
    private VelocityGeneratorUtil() {}

    public static VelocityContext getDefaultVelocityContext(Class classToProcess) {
        VelocityContext context = new VelocityContext();

        context.put("appName", OMGen.APP_NAME);
        context.put("doClassName", classToProcess);

        return context;
    }
}