package com.omgen.discovery;

import com.omgen.generator.ConstructorInfo;
import com.omgen.generator.SetterMethod;

import java.util.List;

/**
 *
 */
public class ClassExplorer {
    private Class clazz;
    private ConstructorFinder constructorFinder;
    private ImportFinder importExporer;
    private MethodFinder methodFinder;

    public ClassExplorer(Class clazz) {
        this.clazz = clazz;
        importExporer = new ImportFinder();
        methodFinder = new MethodFinder(clazz);
        constructorFinder = new ConstructorFinder(clazz);
    }

    public List<ConstructorInfo> findConstructors() {
        return constructorFinder.findConstructors();
    }

    public List<String> findRequiredImports() {
        return importExporer.findRequiredImports(methodFinder.getWriteMethods());
    }

    public List<SetterMethod> findSetterMethods() {
        return methodFinder.findSetterMethods();
    }
}
