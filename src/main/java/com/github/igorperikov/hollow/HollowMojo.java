package com.github.igorperikov.hollow;

import com.netflix.hollow.api.codegen.HollowAPIGenerator;
import com.netflix.hollow.core.write.HollowWriteStateEngine;
import com.netflix.hollow.core.write.objectmapper.HollowObjectMapper;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mojo(name = "generate",
        requiresDependencyCollection = ResolutionScope.COMPILE,
        requiresDependencyResolution = ResolutionScope.COMPILE,
        defaultPhase = LifecyclePhase.GENERATE_SOURCES,
        threadSafe = true
)
@Execute(goal = "generate", phase=LifecyclePhase.GENERATE_SOURCES)
public class HollowMojo extends AbstractMojo {

    /**
     * Location of the output directory.
     */
    @Parameter(
            property = "outputDirectory",
            defaultValue = "${project.build.directory}/generated-sources/hollow/")
    public String outputDirectory;

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
    private String targetSourcesPath;

    
    public void execute() throws MojoExecutionException, MojoFailureException {
        projectDirFile = project.getBasedir();
        projectDirPath = projectDirFile.getAbsolutePath();
        targetSourcesPath = outputDirectory;


        HollowWriteStateEngine writeEngine = new HollowWriteStateEngine();
        HollowObjectMapper mapper = new HollowObjectMapper(writeEngine);

        Collection<Class<?>> datamodelClasses = extractClasses(packagesToScan);
        for (Class<?> clazz : datamodelClasses) {
            getLog().info("Initialize schema for class " + clazz.getName());
            mapper.initializeTypeState(clazz);
        }

        HollowAPIGenerator generator =
                new HollowAPIGenerator(
                        apiClassName,
                        apiPackageName,
                        writeEngine
                );

        String apiTargetPath = buildPathToApiTargetFolder(apiPackageName);

        this.project.addCompileSourceRoot( outputDirectory );
        try {
            generator.generateFiles(apiTargetPath);
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to generate consumer api", e);
        }
    }

    private Collection<Class<?>> extractClasses(List<String> packagesToScan) {
        Set<Class<?>> classes = new HashSet<>();
        Set<URL> urlsToScan = new HashSet<>();
        ClassLoader projectClassloader = getProjectClassloader();

        FastClasspathScanner scanner = new FastClasspathScanner(packagesToScan.toArray(new String[packagesToScan.size()])).addClassLoader(projectClassloader);
        List<String> classNames = scanner.scan().getNamesOfAllClasses();
        getLog().info("Found " + classNames.size() + " classes");
        for (String className : classNames) {
            try {
                classes.add(Class.forName(className, false, projectClassloader));
            } catch (ClassNotFoundException e) {
                getLog().error("couldn't add class " + className + " to the set of classes for some reason");
            }
        }
        return classes;
    }

    private String buildPathToApiTargetFolder(String apiPackageName) {
        return targetSourcesPath + convertPackageNameToFolderPath(apiPackageName);
    }

    private String convertPackageNameToFolderPath(String packageName) {
        return packageName.replaceAll("\\.", "/");
    }


    // get a classloader that has the projects compile time dependencies included with it
    private ClassLoader getProjectClassloader() {
        List<String> classpathElements = null;
        try {
            classpathElements = project.getCompileClasspathElements();
            List<URL> projectClasspathList = new ArrayList<URL>();
            for (String element : classpathElements) {
                try {
                    projectClasspathList.add(new File(element).toURI().toURL());
                } catch (MalformedURLException e) {
                    throw new MojoExecutionException(element + " is an invalid classpath element", e);
                }
            }

            URLClassLoader loader = new URLClassLoader(projectClasspathList.toArray(new URL[0]));
            return loader;

        } catch (Exception e) {
            throw new RuntimeException("Dependency resolution failed", e);
        }
    }
}
