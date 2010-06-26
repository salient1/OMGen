package com.omgen.discovery;

import com.omgen.generator.ConstructorInfo;
import com.omgen.generator.ParameterInfo;
import com.omgen.generator.SetterMethod;
import com.omgen.generator.exception.NoSetterMethodsException;
import org.apache.commons.lang.WordUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class MethodDiscoveryUtils {
    public static List<ConstructorInfo> buildConstructorList(Class<?> clazz) {
        List<ConstructorInfo> constructors = new ArrayList<ConstructorInfo>();
        for (Constructor<?> constructor : clazz.getConstructors()) {
            if (Modifier.isPublic(constructor.getModifiers())) {
                buildConstructorInfo(constructors, constructor);
            }
        }
        return constructors;        
    }

    private static void buildConstructorInfo(List<ConstructorInfo> constructors, Constructor<?> constructor) {
        ConstructorInfo constructorInfo = new ConstructorInfo();
        if (constructor.getParameterTypes().length > 0) {
            List<ParameterInfo> parameterInfos = new ArrayList<ParameterInfo>();
            for (int i = 0, parameterTypesLength = constructor.getParameterTypes().length; i < parameterTypesLength; i++) {
                Class<?> parmClass = constructor.getParameterTypes()[i];
                ParameterInfo parameterInfo = new ParameterInfo();
                parameterInfo.setArgType(parmClass.getName());
                parameterInfo.setArgName(WordUtils.uncapitalize(parmClass.getSimpleName()) + i);
                parameterInfos.add(parameterInfo);
            }
            constructorInfo.setParameterInfos(parameterInfos);
            constructors.add(constructorInfo);
        } else {
            constructors.add(0, constructorInfo); // make zero arg constructors come first
        }
    }

    public static List<SetterMethod> buildMethodList(Class<?> clazz) {
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

    public static String getShortName(Method method) {
        return WordUtils.uncapitalize(method.getName().substring(3));
    }

    public static List<Method> getWriteMethods(Class<?> clazz) {
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

    protected static String getFormattedType(Class<?> varClass, final String generifiedParameterType) {
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
    protected static String getSimplestClassName(Class<?> varClass) {
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
     * For generic types, returns short class name for java.* classes, otherwise fully qualified class name.
     */
    private static String getSimplestClassNameForGeneric(String genericClassName) {
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
