package com.omgen.generator.dtype;

import com.omgen.InvocationContext;
import com.omgen.discovery.MethodDiscoveryUtils;
import com.omgen.generator.Generator;
import com.omgen.generator.util.VelocityGeneratorUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.StringWriter;
import java.util.Date;
import java.util.Properties;

/**
 * Implementation of the D-Type object mother generator.
 */
public class DTypeVelocityGenerator implements Generator {
	private static final String DEFAULT_TEMPLATE = "en/dTypeObjectMother.vo";

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

	private String getTemplate(InvocationContext invocationContext) {
		String template = DEFAULT_TEMPLATE;
		if (invocationContext.getTemplate() != null) {
			template = invocationContext.getTemplate();
		}
		return template;
	}

	protected void addClassInfoToContext(VelocityContext velocityContext, InvocationContext invocationContext, Class<?> classToProcess) {
        velocityContext.put("package", getPackageName(classToProcess, invocationContext));
        velocityContext.put("doClassName", classToProcess.getSimpleName());
        velocityContext.put("omClassName", getObjectMotherClassName(classToProcess));
        velocityContext.put("doName", StringUtils.uncapitalize(classToProcess.getSimpleName()));
        velocityContext.put("methods", MethodDiscoveryUtils.buildMethodList(classToProcess));
        velocityContext.put("constructors", MethodDiscoveryUtils.buildConstructorList(classToProcess));
        velocityContext.put("date", (new Date()).toString());
    }

    private String getPackageName(Class<?> classToProcess, InvocationContext invocationContext) {
        return invocationContext.getOmPackageName() != null ? invocationContext.getOmPackageName() : classToProcess.getPackage().getName();
    }

    protected String getObjectMotherClassName(Class<?> classToProcess) {
        return classToProcess.getSimpleName() + "ObjectMother";
    }




    /**
     * Warning: package info is not available for array types for some reason...
     */

}
