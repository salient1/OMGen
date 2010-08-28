package com.omgen.discovery;

import com.omgen.generator.SetterMethod;
import com.omgen.generator.exception.NoSetterMethodsException;
import org.apache.commons.lang.WordUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * key test
 */
public final class MethodDiscoveryUtils {
    private MethodDiscoveryUtils() {}

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
            if (isSetter(method)) {
                setterMethods.add(method);
            }
        }
        return setterMethods;
    }

    public static boolean isSetter(Method method) {
        return method.getName().startsWith("set")
                && method.getParameterTypes().length == 1
                && Modifier.isPublic(method.getModifiers())
                && method.getReturnType().getName().equals("void");
    }

    protected static String getFormattedType(Class<?> varClass, final String generifiedParameterType) {
        boolean isGenericParameterType = generifiedParameterType.indexOf('<') > -1;

        String formattedType;
        if (isGenericParameterType) {
            int startIndex = generifiedParameterType.indexOf('<');
            String generic = generifiedParameterType.substring(startIndex);
            formattedType = varClass.getSimpleName() + getSimplestClassNameForGeneric(generic);
        } else {
            formattedType = varClass.getSimpleName();
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
        if (fullName.startsWith(ImportUtils.AUTO_IMPORTED_PACKAGE)) {
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
        int indexOfLastDot = genericClassName.lastIndexOf('.');
        int indexOfGt = genericClassName.lastIndexOf('>');
        formattedType = String.format("<%s>", genericClassName.substring(indexOfLastDot + 1, indexOfGt));
        return formattedType;
    }


}
