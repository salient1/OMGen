package com.omgen.generator.makeiteasy;

import com.omgen.InvocationContext;
import com.omgen.generator.DefaultVelocityGenerator;
import com.omgen.generator.Generator;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;

import java.util.Date;

import static com.omgen.discovery.ConstructorUtils.buildConstructorList;
import static com.omgen.discovery.ImportUtils.getRequiredImports;
import static com.omgen.discovery.MethodDiscoveryUtils.buildMethodList;
import static com.omgen.discovery.MethodDiscoveryUtils.getWriteMethods;

/**
 *
 */
public class MakeItEasyGenerator extends DefaultVelocityGenerator implements Generator {
    public static final String DEFAULT_TEMPLATE = "en/MakeItEasyObjectMother.vo";

    @Override
    protected String getTemplateName() {
        return DEFAULT_TEMPLATE;
    }

    @Override
    protected void addClassInfoToContext(VelocityContext velocityContext, InvocationContext invocationContext, Class<?> classToProcess) {
        velocityContext.put("package", getPackageName(classToProcess, invocationContext));
        velocityContext.put("doClassName", classToProcess.getSimpleName());
        velocityContext.put("omClassName", getObjectMotherClassName(classToProcess));
        velocityContext.put("doName", StringUtils.uncapitalize(classToProcess.getSimpleName()));
        velocityContext.put("methods", buildMethodList(classToProcess));
        velocityContext.put("constructors", buildConstructorList(classToProcess));
        velocityContext.put("imports", getRequiredImports(getWriteMethods(classToProcess)));
        velocityContext.put("date", (new Date()).toString());
    }

    protected String getObjectMotherClassName(Class<?> classToProcess) {
        return classToProcess.getSimpleName() + "Maker";
    }

}
