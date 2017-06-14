package com.github.igorperikov.hollow;

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
            Log log
    )
            throws MojoExecutionException
    {
        HollowWriteStateEngine writeEngine = new HollowWriteStateEngine();
        HollowObjectMapper mapper = new HollowObjectMapper(writeEngine);

        Collection<Class<?>> datamodelClasses = ClassExtractorUtility.extractClasses(project, packagesToScan);
        log.info("Found " + datamodelClasses.size() + " classes");
        for (Class<?> clazz : datamodelClasses) {
            log.debug("Initialize schema for class " + clazz.getName());
            mapper.initializeTypeState(clazz);
        }

        return new HollowAPIGenerator(apiClassName, apiPackageName, writeEngine);
    }
}
