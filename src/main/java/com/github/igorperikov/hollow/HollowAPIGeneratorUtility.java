package com.github.igorperikov.hollow;

import com.github.igorperikov.hollow.config.OptionalHollowProperties;
import com.github.igorperikov.hollow.utils.ClasspathUtils;
import com.netflix.hollow.api.codegen.HollowAPIGenerator;
import com.netflix.hollow.core.write.HollowWriteStateEngine;
import com.netflix.hollow.core.write.objectmapper.HollowObjectMapper;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import java.util.Collection;
import java.util.List;

public class HollowAPIGeneratorUtility {
    public static HollowAPIGenerator createHollowAPIGenerator(
            MavenProject project,
            List<String> packagesToScan,
            String apiClassName,
            String apiPackageName,
            Log log,
            String apiTargetPath,
            OptionalHollowProperties properties
    ) throws MojoExecutionException {
        HollowWriteStateEngine writeStateEngine = new HollowWriteStateEngine();
        HollowObjectMapper mapper = new HollowObjectMapper(writeStateEngine);

        Collection<Class<?>> datamodelClasses = ClasspathUtils.extractClasses(project, packagesToScan);
        log.info("Found " + datamodelClasses.size() + " classes");
        for (Class<?> clazz : datamodelClasses) {
            log.info("Initialize schema for class " + clazz.getName());
            mapper.initializeTypeState(clazz);
        }

        HollowAPIGenerator.Builder builder = new HollowAPIGenerator.Builder()
                .withAPIClassname(apiClassName)
                .withPackageName(apiPackageName)
                .withDataModel(writeStateEngine)
                .withDestination(apiTargetPath)
                .withParameterizeAllClassNames(properties.parameterizeAllClassNames)
                .withAggressiveSubstitutions(properties.useAggressiveSubstitutions)
                .withBooleanFieldErgonomics(properties.useBooleanFieldErgonomics)
                .reservePrimaryKeyIndexForTypeWithPrimaryKey(properties.reservePrimaryKeyIndexForTypeWithPrimaryKey)
                .withHollowPrimitiveTypes(properties.useHollowPrimitiveTypes)
                .withVerboseToString(properties.useVerboseToString);

        if (properties.useErgonomicShortcuts) {
            builder.withErgonomicShortcuts();
        }
        if (properties.usePackageGrouping) {
            builder.withPackageGrouping();
        }
        if (properties.restrictApiToFieldType) {
            builder.withRestrictApiToFieldType();
        }
        return builder.build();
    }
}
