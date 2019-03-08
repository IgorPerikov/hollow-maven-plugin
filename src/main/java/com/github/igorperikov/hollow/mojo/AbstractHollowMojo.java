package com.github.igorperikov.hollow.mojo;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.util.List;

public abstract class AbstractHollowMojo extends AbstractMojo {
    @Parameter(property = "packagesToScan", required = true)
    public List<String> packagesToScan;

    @Parameter(property = "apiClassName", required = true)
    public String apiClassName;

    @Parameter(property = "apiPackageName", required = true)
    public String apiPackageName;

    @Parameter(readonly = true, defaultValue = "${project}")
    public MavenProject project;

    @Parameter(property = "parameterizeAllClassNames", required = false, defaultValue = "false")
    public boolean parameterizeAllClassNames;

    @Parameter(property = "useAggressiveSubstitutions", required = false, defaultValue = "false")
    public boolean useAggressiveSubstitutions;

    @Parameter(property = "useBooleanFieldErgonomics", required = false, defaultValue = "true")
    public boolean useBooleanFieldErgonomics;

    @Parameter(property = "reservePrimaryKeyIndexForTypeWithPrimaryKey", required = false, defaultValue = "true")
    public boolean reservePrimaryKeyIndexForTypeWithPrimaryKey;

    @Parameter(property = "useHollowPrimitiveTypes", required = false, defaultValue = "true")
    public boolean useHollowPrimitiveTypes;

    @Parameter(property = "useVerboseToString", required = false, defaultValue = "true")
    public boolean useVerboseToString;

    @Parameter(property = "useErgonomicShortcuts", required = false, defaultValue = "true")
    public boolean useErgonomicShortcuts;

    @Parameter(property = "usePackageGrouping", required = false, defaultValue = "true")
    public boolean usePackageGrouping;

    @Parameter(property = "restrictApiToFieldType", required = false, defaultValue = "true")
    public boolean restrictApiToFieldType;

    @Parameter(property = "classPostfix", required = false, defaultValue = "")
    public String classPostfix;
}
