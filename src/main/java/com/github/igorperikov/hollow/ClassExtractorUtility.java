package com.github.igorperikov.hollow;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class ClassExtractorUtility {

    public static Collection<Class<?>> extractClasses(
            MavenProject project,
            List<String> packagesToScan
    )
            throws MojoExecutionException
    {
        Set<Class<?>> classes = new HashSet<>();
        ClassLoader projectClassloader = getProjectClassloader(project);

        FastClasspathScanner scanner =
                new FastClasspathScanner(packagesToScan.toArray(new String[packagesToScan.size()])).addClassLoader(projectClassloader);
        List<String> classNames = scanner.scan().getNamesOfAllClasses();
        for (String className : classNames) {
            try {
                classes.add(Class.forName(className, false, projectClassloader));
            } catch (ClassNotFoundException e) {
                throw new MojoExecutionException("Couldn't add class " + className + " to the set of classes for some reason", e);
            }
        }
        return classes;
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
