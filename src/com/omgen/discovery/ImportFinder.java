package com.omgen.discovery;

import org.apache.commons.lang.ClassUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImportFinder {
    public static final String AUTO_IMPORTED_PACKAGE = "java.lang";

    public List<String> findRequiredImports(List<Method> writeMethods) {
        List<String> imports = new ArrayList<String>();
        for (Method writeMethod : writeMethods) {
            for (Class<?> paramType : writeMethod.getParameterTypes()) {
                if (!paramType.isPrimitive()) {
                    String packageName = ClassUtils.getPackageCanonicalName(paramType);
                    if (!packageName.startsWith(AUTO_IMPORTED_PACKAGE)) {
                        String importString = getImport(paramType);
                        if (!imports.contains(importString)) {
                            imports.add(importString);
                        }
                    }
                    String importGeneric = getImportForGeneric(writeMethod);
                    if (isValidGenericImport(imports, importGeneric)) {
                        imports.add(importGeneric);
                    }
                }
            }
        }
        return Collections.unmodifiableList(imports);
    }

    private boolean isValidGenericImport(List<String> imports, String importGeneric) {
        return importGeneric != null
                && !importGeneric.startsWith(AUTO_IMPORTED_PACKAGE)
                && !imports.contains(importGeneric);
    }

    private String getImport(Class<?> paramType) {
        if (paramType.isArray()) {
            String typeName = paramType.getCanonicalName();
            return typeName.substring(0, typeName.indexOf('['));
        } else {
            return paramType.getCanonicalName();
        }
    }

    private String getImportForGeneric(Method method) {
        Type[] types = method.getGenericParameterTypes();
        String type = types[0].toString();
        int ltIndex = type.indexOf('<');
        if (ltIndex > -1) {
            return type.substring(ltIndex + 1, type.indexOf('>'));
        } else {
            return null;
        }
    }
}