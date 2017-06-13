package com.github.igorperikov.hollow;

import com.netflix.hollow.api.codegen.HollowAPIGenerator;
import com.netflix.hollow.core.write.HollowWriteStateEngine;
import com.netflix.hollow.core.write.objectmapper.HollowObjectMapper;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

@Mojo(name = "generate-here")
public class SingleModuleHollowMojo extends AbstractMojo {

    private final String relativeJavaSourcesPath = "/src/main/java/";

    @Parameter(property = "packagesToScan", required = true)
    public List<String> packagesToScan;

    @Parameter(property = "apiClassName", required = true)
    public String apiClassName;

    @Parameter(property = "apiPackageName", required = true)
    public String apiPackageName;

    @Parameter(readonly = true, defaultValue = "${project}")
    private MavenProject project;

    private File projectDirFile;
    private String projectDirPath;
    private String javaSourcesPath;
    private String compiledClassesPath;

    private URLClassLoader urlClassLoader;

    public void execute() throws MojoExecutionException, MojoFailureException {
        projectDirFile = project.getBasedir();
        projectDirPath = projectDirFile.getAbsolutePath();
        javaSourcesPath = projectDirPath + relativeJavaSourcesPath;
        compiledClassesPath = projectDirPath + "/target/classes/";

        initClassLoader();

        HollowWriteStateEngine writeEngine = new HollowWriteStateEngine();
        HollowObjectMapper mapper = new HollowObjectMapper(writeEngine);

        Collection<Class<?>> datamodelClasses = extractClasses(packagesToScan);
        for (Class<?> clazz : datamodelClasses) {
            getLog().debug("Initialize schema for class " + clazz.getName());
            mapper.initializeTypeState(clazz);
        }

        HollowAPIGenerator generator =
                new HollowAPIGenerator(
                        apiClassName,
                        apiPackageName,
                        writeEngine
                );

        String apiTargetPath = buildPathToApiTargetFolder(apiPackageName);

        cleanupAndCreateFolders(apiTargetPath);
        try {
            generator.generateFiles(apiTargetPath);
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to generate consumer api", e);
        }
    }

    private Collection<Class<?>> extractClasses(List<String> packagesToScan) {
        Set<Class<?>> classes = new HashSet<>();

        for (String packageToScan : packagesToScan) {
            File packageFile = buildPackageFile(packageToScan);

            List<File> allFilesInPackage = findFilesRecursively(packageFile);
            List<String> classNames = new ArrayList<>();
            for (File file : allFilesInPackage) {
                String filePath = file.getAbsolutePath();
                getLog().debug("Candidate for schema initialization " + filePath);
                if (filePath.endsWith(".java") &&
                        !filePath.endsWith("package-info.java") &&
                        !filePath.endsWith("module-info.java")
                        ) {
                    String relativePathToFile = removeSubstrings(filePath, projectDirPath, relativeJavaSourcesPath);
                    classNames.add(convertFolderPathToPackageName(removeSubstrings(relativePathToFile, ".java")));
                }
            }

            for (String fqdn : classNames) {
                try {
                    Class<?> clazz = urlClassLoader.loadClass(fqdn);
                    classes.add(clazz);
                } catch (ClassNotFoundException e) {
                    getLog().warn("{} class not found " + fqdn);
                }
            }
        }
        return classes;
    }

    private List<File> findFilesRecursively(File packageFile) {
        List<File> foundFiles = new ArrayList<>();
        if (packageFile.exists()) {
            for (File file : packageFile.listFiles()) {
                if (file.isDirectory()) {
                    foundFiles.addAll(findFilesRecursively(file));
                } else {
                    foundFiles.add(file);
                }
            }
        }
        return foundFiles;
    }

    private File buildPackageFile(String packageName) {
        return new File(javaSourcesPath + convertPackageNameToFolderPath(packageName));
    }

    private String buildPathToApiTargetFolder(String apiPackageName) {
        return javaSourcesPath + convertPackageNameToFolderPath(apiPackageName);
    }

    private String convertPackageNameToFolderPath(String packageName) {
        return packageName.replaceAll("\\.", "/");
    }

    private String convertFolderPathToPackageName(String folderName) {
        return folderName.replaceAll("/", "\\.");
    }

    private void cleanupAndCreateFolders(String generatedApiTarget) {
        File apiCodeFolder = new File(generatedApiTarget);
        apiCodeFolder.mkdirs();
        for (File f : apiCodeFolder.listFiles()) {
            f.delete();
        }
    }

    private String removeSubstrings(String initial, String... substrings) {
        for (String substring : substrings) {
            initial = initial.replace(substring, "");
        }
        return initial;
    }

    private void initClassLoader() throws MojoFailureException {
        URL url;
        try {
            url = new File(compiledClassesPath).toURI().toURL();
        } catch (MalformedURLException e) {
            throw new MojoFailureException("Path to classes is missed");
        }
        URL[] urls = new URL[1];
        urls[0] = url;
        urlClassLoader = new URLClassLoader(urls);
    }
}
