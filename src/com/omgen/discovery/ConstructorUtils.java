package com.omgen.discovery;

import com.omgen.generator.ConstructorInfo;
import com.omgen.generator.ParameterInfo;
import org.apache.commons.lang.WordUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ConstructorUtils {
    private ConstructorUtils() {}

    public static List<ConstructorInfo> buildConstructorList(Class<?> clazz) {
        List<ConstructorInfo> constructors = new ArrayList<ConstructorInfo>();
        for (Constructor<?> constructor : clazz.getConstructors()) {
            if (isPublic(constructor)) {
                buildConstructorInfo(constructors, constructor);
            }
        }
        return Collections.unmodifiableList(constructors);
    }

    public static boolean isPublic(Constructor<?> constructor) {
        return Modifier.isPublic(constructor.getModifiers());
    }

    private static void buildConstructorInfo(List<ConstructorInfo> constructors, Constructor<?> constructor) {
        ConstructorInfo constructorInfo = new ConstructorInfo();
        if (constructor.getParameterTypes().length > 0) {
            List<ParameterInfo> parameterInfos = buildParameterInfos(constructor);
            constructorInfo.setParameterInfos(parameterInfos);
            constructors.add(constructorInfo);
        } else {
            constructors.add(0, constructorInfo); // make zero arg constructors come first
        }
    }

    private static List<ParameterInfo> buildParameterInfos(Constructor<?> constructor) {
        List<ParameterInfo> parameterInfos = new ArrayList<ParameterInfo>();
        for (int i = 0, parameterTypesLength = constructor.getParameterTypes().length; i < parameterTypesLength; i++) {
            Class<?> parmClass = constructor.getParameterTypes()[i];
            ParameterInfo parameterInfo = new ParameterInfo();
            parameterInfo.setArgType(parmClass.getName());
            parameterInfo.setArgName(WordUtils.uncapitalize(parmClass.getSimpleName()) + i);
            parameterInfos.add(parameterInfo);
        }
        return parameterInfos;
    }
}