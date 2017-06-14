package com.github.igorperikov.hollow;

import com.netflix.hollow.api.codegen.HollowAPIGenerator;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.*;
import org.apache.maven.project.MavenProject;

import java.io.IOException;
import java.util.List;

@Mojo(name = "generate-as-target-sources",
        requiresDependencyCollection = ResolutionScope.COMPILE,
        requiresDependencyResolution = ResolutionScope.COMPILE,
        defaultPhase = LifecyclePhase.GENERATE_SOURCES,
        threadSafe = true
)
@Execute(goal = "generate-as-target-sources", phase = LifecyclePhase.GENERATE_SOURCES)
public class MultiModuleHollowMojo extends AbstractMojo {

    @Parameter(
            property = "outputDirectory",
            defaultValue = "${project.build.directory}/generated-sources/hollow/")
    public String outputDirectory;

    @Parameter(property = "packagesToScan", required = true)
    public List<String> packagesToScan;

    @Parameter(property = "apiClassName", required = true)
    public String apiClassName;

    @Parameter(property = "apiPackageName", required = true)
    public String apiPackageName;

    @Parameter(readonly = true, defaultValue = "${project}")
    private MavenProject project;

    public void execute() throws MojoExecutionException, MojoFailureException {
        HollowAPIGenerator generator = HollowAPIGeneratorUtility.createHollowAPIGenerator(
                project,
                packagesToScan,
                apiClassName,
                apiPackageName,
                getLog()
        );

        String apiTargetFolderPath = ApiTargetFolderUtility.buildPathToApiTargetFolder(apiPackageName, outputDirectory);

        project.addCompileSourceRoot(outputDirectory);
        try {
            generator.generateFiles(apiTargetFolderPath);
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to generate consumer api", e);
        }
    }
}
