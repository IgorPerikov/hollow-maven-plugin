package com.github.igorperikov.hollow.mojo;

import com.github.igorperikov.hollow.HollowAPIGeneratorUtility;
import com.github.igorperikov.hollow.config.OptionalHollowProperties;
import com.github.igorperikov.hollow.utils.FolderUtils;
import com.netflix.hollow.api.codegen.HollowAPIGenerator;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.*;

import java.io.IOException;

@Mojo(name = "generate-as-target-sources",
        requiresDependencyCollection = ResolutionScope.COMPILE,
        requiresDependencyResolution = ResolutionScope.COMPILE,
        defaultPhase = LifecyclePhase.GENERATE_SOURCES,
        threadSafe = true
)
@Execute(goal = "generate-as-target-sources", phase = LifecyclePhase.GENERATE_SOURCES)
public class MultiModuleHollowMojo extends AbstractHollowMojo {
    @Parameter(
            property = "outputDirectory",
            defaultValue = "${project.build.directory}/generated-sources/hollow/"
    )
    public String outputDirectory;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        String apiTargetFolderPath = FolderUtils.buildPathToApiTargetFolder(apiPackageName, outputDirectory);
        HollowAPIGenerator generator = HollowAPIGeneratorUtility.createHollowAPIGenerator(
                project,
                packagesToScan,
                apiClassName,
                apiPackageName,
                getLog(),
                apiTargetFolderPath,
                new OptionalHollowProperties(
                        parameterizeAllClassNames,
                        useAggressiveSubstitutions,
                        useBooleanFieldErgonomics,
                        reservePrimaryKeyIndexForTypeWithPrimaryKey,
                        useHollowPrimitiveTypes,
                        useVerboseToString,
                        useErgonomicShortcuts,
                        usePackageGrouping,
                        restrictApiToFieldType,
						classPostfix
                )
        );

        project.addCompileSourceRoot(outputDirectory);
        try {
            generator.generateSourceFiles();
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to generate consumer api", e);
        }
    }
}
