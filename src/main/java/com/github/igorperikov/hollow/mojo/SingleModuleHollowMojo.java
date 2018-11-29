package com.github.igorperikov.hollow.mojo;

import com.github.igorperikov.hollow.HollowAPIGeneratorUtility;
import com.github.igorperikov.hollow.utils.FolderUtils;
import com.netflix.hollow.api.codegen.HollowAPIGenerator;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.IOException;
import java.util.List;

@Mojo(name = "generate-as-project-sources")
public class SingleModuleHollowMojo extends AbstractMojo {
    @Parameter(property = "packagesToScan", required = true)
    public List<String> packagesToScan;

    @Parameter(property = "apiClassName", required = true)
    public String apiClassName;

    @Parameter(property = "apiPackageName", required = true)
    public String apiPackageName;

    @Parameter(readonly = true, defaultValue = "${project}")
    private MavenProject project;

    public void execute() throws MojoExecutionException, MojoFailureException {
        String javaSourcesPath = project.getBasedir().getAbsolutePath() + "/src/main/java/";
        String apiTargetFolderPath = FolderUtils.buildPathToApiTargetFolder(apiPackageName, javaSourcesPath);
        HollowAPIGenerator generator = HollowAPIGeneratorUtility.createHollowAPIGenerator(
                project,
                packagesToScan,
                apiClassName,
                apiPackageName,
                getLog(),
                apiTargetFolderPath
        );

        FolderUtils.cleanupAndCreateFolders(apiTargetFolderPath);
        try {
            generator.generateSourceFiles();
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to generate consumer api", e);
        }
    }
}
