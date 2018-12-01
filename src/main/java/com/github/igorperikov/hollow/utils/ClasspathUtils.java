package com.github.igorperikov.hollow.utils;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class ClasspathUtils {
    public static Collection<Class<?>> extractClasses(
            MavenProject project,
            List<String> packagesToScan
    ) throws MojoExecutionException {
        Set<Class<?>> classes = new HashSet<>();
        ClassLoader projectClassloader = getProjectClassloader(project);

        FastClasspathScanner scanner = new FastClasspathScanner(packagesToScan.toArray(new String[packagesToScan.size()])).addClassLoader(projectClassloader);
        List<String> classNames = scanner.scan().getNamesOfAllClasses();
        for (String className : classNames) {
            try {
                Class<?> clazz = Class.forName(className, false, projectClassloader);
                if (isValidClass(clazz)) {
                    classes.add(clazz);
                }
            } catch (ClassNotFoundException e) {
                throw new MojoExecutionException("Couldn't add class " + className + " to the set of classes", e);
            }
        }
        return classes;
    }
    
    private static boolean isValidClass(Class<?> clazz) {
        return !clazz.isAnnotation() && !clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers());
    }

    private static ClassLoader getProjectClassloader(MavenProject project) {
        List<String> classpathElements;
        try {
            classpathElements = project.getCompileClasspathElements();
            List<URL> projectClasspathList = new ArrayList<>();
            for (String element : classpathElements) {
                try {
                    projectClasspathList.add(new File(element).toURI().toURL());
                } catch (MalformedURLException e) {
                    throw new MojoExecutionException(element + " is an invalid classpath element", e);
                }
            }

            return URLClassLoader.newInstance(
                    projectClasspathList.toArray(new URL[0]),
                    Thread.currentThread().getContextClassLoader()
            );

        } catch (Exception e) {
            throw new RuntimeException("Dependency resolution failed", e);
        }
    }
}
