package com.omgen.generator;

import com.omgen.InvocationContext;
import com.omgen.generator.util.VelocityGeneratorUtil;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.StringWriter;
import java.util.Properties;

/**
 *
 */
public abstract class DefaultVelocityGenerator implements Generator {
    protected abstract String getTemplateName();

    public String generate(Class classToProcess, InvocationContext invocationContext) throws Exception {
        VelocityContext velocityContext = getVelocityContext(classToProcess);

        addClassInfoToContext(velocityContext, invocationContext, classToProcess);

        return mergeVelocityTemplate(velocityContext, invocationContext);
    }

    protected VelocityContext getVelocityContext(Class classToProcess) throws Exception {
        Velocity.init(getVelocityProperties());

        return VelocityGeneratorUtil.getDefaultVelocityContext(classToProcess);
    }

    protected Properties getVelocityProperties() {
        Properties properties = new Properties();
        properties.setProperty("resource.loader", "class");
        properties.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        return properties;
    }

    protected String mergeVelocityTemplate(VelocityContext velocityContext, InvocationContext invocationContext) throws Exception {
        StringWriter stringWriter = new StringWriter();

        Velocity.mergeTemplate(getTemplate(invocationContext), "UTF-8", velocityContext, stringWriter);

        return stringWriter.toString();
    }

    protected String getTemplate(InvocationContext invocationContext) {
        String template = getTemplateName();
        if (invocationContext.getTemplate() != null) {
            template = invocationContext.getTemplate();
        }
        return template;
    }

    protected abstract void addClassInfoToContext(VelocityContext velocityContext, InvocationContext invocationContext, Class<?> classToProcess);

    protected String getPackageName(Class<?> classToProcess, InvocationContext invocationContext) {
        return invocationContext.getOmPackageName() != null ? invocationContext.getOmPackageName() : classToProcess.getPackage().getName();
    }

    protected String getObjectMotherClassName(Class<?> classToProcess) {
        return classToProcess.getSimpleName() + "ObjectMother";
    }
}
