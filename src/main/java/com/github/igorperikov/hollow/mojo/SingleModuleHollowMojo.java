package com.github.igorperikov.hollow.mojo;

import com.github.igorperikov.hollow.HollowAPIGeneratorUtility;
import com.github.igorperikov.hollow.config.OptionalHollowProperties;
import com.github.igorperikov.hollow.utils.FolderUtils;
import com.netflix.hollow.api.codegen.HollowAPIGenerator;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

import java.io.IOException;

@Mojo(name = "generate-as-project-sources")
public class SingleModuleHollowMojo extends AbstractHollowMojo {
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        String javaSourcesPath = project.getBasedir().getAbsolutePath() + "/src/main/java/";
        String apiTargetFolderPath = FolderUtils.buildPathToApiTargetFolder(apiPackageName, javaSourcesPath);
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

        FolderUtils.cleanupAndCreateFolders(apiTargetFolderPath);
        try {
            generator.generateSourceFiles();
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to generate consumer api", e);
        }
    }
}
