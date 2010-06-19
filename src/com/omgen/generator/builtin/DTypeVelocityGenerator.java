package com.omgen.generator.builtin;

import com.omgen.generator.Generator;
import com.omgen.generator.util.VelocityGeneratorUtil;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Implementation of the D-Type object mother generator.
 */
public class DTypeVelocityGenerator implements Generator {

    public String generate(Class classToProcess) throws Exception {
        VelocityContext context = getVelocityContext(classToProcess);

        addClassInfoToContext(classToProcess, context);

        return mergeVelocityTemplate(context);
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

    protected String mergeVelocityTemplate(VelocityContext context) throws Exception {
        StringWriter stringWriter = new StringWriter();

        Velocity.mergeTemplate("en/dTypeObjectMother.vo", "UTF-8", context, stringWriter);

        return stringWriter.toString();
    }

    protected void addClassInfoToContext(Class<?> classToProcess, VelocityContext context) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
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

    protected List<SetterMethod> buildMethodList(Class<?> clazz) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        List<SetterMethod> setterMethods = new ArrayList<SetterMethod>();

        BeanMap beanMap = new BeanMap(clazz.newInstance());
        for (Iterator<?> i = beanMap.keyIterator(); i.hasNext();) {
            String key = (String) i.next();
            Method writeMethod = beanMap.getWriteMethod(key);
            if (writeMethod != null) {
                Class<?> varClass = beanMap.getType(key);

                SetterMethod setterMethod = new SetterMethod(writeMethod.getName(), key,
                        getFormattedType(varClass, writeMethod.getGenericParameterTypes()[0].toString()));

                setterMethods.add(setterMethod);
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
