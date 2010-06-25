package com.omgen.generator.dtype;

import com.omgen.InvocationContext;
import com.omgen.generator.Generator;
import com.omgen.generator.SetterMethod;
import com.omgen.generator.exception.NoSetterMethodsException;
import com.omgen.generator.util.VelocityGeneratorUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.StringWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * Implementation of the D-Type object mother generator.
 */
public class DTypeVelocityGenerator implements Generator {
	private static final String DEFAULT_TEMPLATE = "en/dTypeObjectMother.vo";

	public String generate(Class classToProcess, InvocationContext invocationContext) throws Exception {
        VelocityContext velocityContext = getVelocityContext(classToProcess);

        addClassInfoToContext(classToProcess, velocityContext);

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

	protected void addClassInfoToContext(Class<?> classToProcess, VelocityContext context) {
        context.put("package", classToProcess.getPackage().getName());
        context.put("doClassName", classToProcess.getSimpleName());
        context.put("omClassName", getObjectMotherClassName(classToProcess));
        context.put("doName", StringUtils.uncapitalize(classToProcess.getSimpleName()));
        context.put("methods", buildMethodList(classToProcess));
        context.put("date", (new Date()).toString());
    }

    protected String getObjectMotherClassName(Class<?> classToProcess) {
        return classToProcess.getSimpleName() + "ObjectMother";
    }

    protected List<SetterMethod> buildMethodList(Class<?> clazz) {
        List<Method> writeMethods = getWriteMethods(clazz);
        List<SetterMethod> setterMethods = new ArrayList<SetterMethod>();
        for (Method method : writeMethods) {
            if (method != null) {
                Class<?> argumentClass = method.getParameterTypes()[0];

                SetterMethod setterMethod = new SetterMethod(method.getName(), getShortName(method),
                        getFormattedType(argumentClass, method.getGenericParameterTypes()[0].toString()));

                setterMethods.add(setterMethod);
            }
        }
        if (setterMethods.size() == 0) {
            throw new NoSetterMethodsException();
        }
        return setterMethods;
    }

    private String getShortName(Method method) {
        return WordUtils.uncapitalize(method.getName().substring(3));
    }

    private List<Method> getWriteMethods(Class<?> clazz) {
        List<Method> setterMethods = new ArrayList<Method>();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("set")
                    && method.getParameterTypes().length == 1
                    && Modifier.isPublic(method.getModifiers())
                    && method.getReturnType().getName().equals("void")) {
                setterMethods.add(method);
            }
        }
        return setterMethods;
    }


    /**
     * Warning: package info is not available for array types for some reason...
     */
    protected String getFormattedType(Class<?> varClass, final String generifiedParameterType) {
        boolean isGenericParameterType = generifiedParameterType.indexOf('<') > -1;

        String formattedType;
        if (isGenericParameterType) {
            int startIndex = generifiedParameterType.indexOf('<');
            String generic = generifiedParameterType.substring(startIndex);
            formattedType = getSimplestClassName(varClass) + getSimplestClassNameForGeneric(generic);
        } else {
            formattedType = getSimplestClassName(varClass);
        }

        return formattedType.replace("$", ".");  // for inner classes
    }

    /**
     * Returns short class name for java.* classes, otherwise fully qualified class name.
     */
    protected String getSimplestClassName(Class<?> varClass) {
        String simpleName = varClass.getSimpleName();
        String fullName = varClass.getCanonicalName();

        String formattedType;
        if (fullName.startsWith("java.")) {
            formattedType = simpleName;
        } else {
            formattedType = fullName;
        }
        return formattedType;
    }

    /**
     * Returns short class name for java.* classes, otherwise fully qualified class name.
     */
    private String getSimplestClassNameForGeneric(String genericClassName) {
        String formattedType;
        if (genericClassName.startsWith("<java.")) {
            int indexOfLastDot = genericClassName.lastIndexOf('.');
            int indexOfGt = genericClassName.lastIndexOf('>');
            formattedType = String.format("<%s>", genericClassName.substring(indexOfLastDot + 1, indexOfGt));
        } else {
            formattedType = genericClassName;
        }
        return formattedType;
    }
}
