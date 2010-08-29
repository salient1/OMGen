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
public class PackageExplorer {
    public static final char SLASH_CHAR = System.getProperty("file.separator").charAt(0);

    private String packageName;

    public PackageExplorer(String packageName) {
        this.packageName = packageName;
    }

    public List<String> getClasses(InvocationContext invocationContext) throws ClassNotFoundException {
        ArrayList<String> classes;

        // Get a File object for the package
        File directory = null;
        String path = packageName.replace('.', '/');

        directory = convertPackageToDir(directory, path);

        if (!directory.exists()) {
            throw new ClassNotFoundException(packageName + " does not appear to be a valid package");
        }

        classes = buildClassList(invocationContext.isScanSubPackages(), directory);

        return Collections.unmodifiableList(classes);
    }

    private ArrayList<String> buildClassList(boolean scanSubPackages, File directory) throws ClassNotFoundException {
        ArrayList<String> classes = new ArrayList<String>();

        File[] files = directory.listFiles();
        for (File file : files) {
            // we are only interested in .class files that are non-inner classes
            String filename = file.getName();
            if (isClassFile(filename) && !isInnerClass(filename)) {
                String fullyQualifiedClassName = getFullyQualifiedClassName(file);
                classes.add(fullyQualifiedClassName);
            } else if (scanSubPackages && file.isDirectory()) {
                classes.addAll(buildClassList(scanSubPackages, file));
            }
        }
        return classes;
    }

    private String getFullyQualifiedClassName(File file) {
        String slashyClassName = null;
        try {
            slashyClassName = slashesToDots(getCanonicalPath(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stripRootPath(slashyClassName); // leaves you with just package and classname
    }

    private String stripRootPath(String slashyClassName) {
        return slashyClassName.substring(slashyClassName.lastIndexOf(packageName), slashyClassName.length());
    }

    private String getCanonicalPath(File file) throws IOException {
        return file.getCanonicalPath().substring(0, file.getCanonicalPath().length() - 6);
    }

    private boolean isInnerClass(String filename) {
        return filename.contains("$");
    }

    private boolean isClassFile(String filename) {
        return filename.endsWith(".class");
    }

    private String slashesToDots(String slashyString) {
        return slashyString.replace(SLASH_CHAR, '.');
    }

    private File convertPackageToDir(File directory, String path) throws ClassNotFoundException {
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
