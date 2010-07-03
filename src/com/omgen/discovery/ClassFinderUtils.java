package com.omgen.discovery;

import com.omgen.InvocationContext;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public final class ClassFinderUtils {
    public static final char SLASH_CHAR = System.getProperty("file.separator").charAt(0);

    private ClassFinderUtils() {}

    public static List<String> getClasses(String packageName, InvocationContext invocationContext) throws ClassNotFoundException {
        ArrayList<String> classes;

        // Get a File object for the package
        File directory = null;
        String path = packageName.replace('.', '/');

        directory = convertPackageToDir(packageName, directory, path);

        if (!directory.exists()) {
            throw new ClassNotFoundException(packageName + " does not appear to be a valid package");
        }

        classes = buildClassList(packageName, invocationContext.isScanSubPackages(), directory);

        return Collections.unmodifiableList(classes);
    }

    private static ArrayList<String> buildClassList(String rootPackage, boolean scanSubPackages, File directory) throws ClassNotFoundException {
        ArrayList<String> classes = new ArrayList<String>();

        // Get the list of the files contained in the package
        File[] files = directory.listFiles();
        for (File file : files) {
            // we are only interested in .class files for non-inner classes
            String filename = file.getName();
            if (filename.endsWith(".class") && !filename.contains("$")) {
                String slashyClassName = null;
                try {
                    slashyClassName = slashesToDots(file.getCanonicalPath().substring(0, file.getCanonicalPath().length() - 6));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String trimmedClassName = slashyClassName.substring(slashyClassName.lastIndexOf(rootPackage), slashyClassName.length());
                classes.add(trimmedClassName);
            } else if (scanSubPackages && file.isDirectory()) {
                classes.addAll(buildClassList(rootPackage, scanSubPackages, file));
            }
        }
        return classes;
    }

    private static String slashesToDots(String slashyString) {
        return slashyString.replace(SLASH_CHAR, '.');
    }

    private static File convertPackageToDir(String packageName, File directory, String path) throws ClassNotFoundException {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            URL resource = classLoader.getResource(path);
            directory = new File(resource.getFile());
        } catch (NullPointerException x) {
            throw new ClassNotFoundException(packageName + " (" + directory + ") does not appear to be a valid package");
        }
        return directory;
    }
}
