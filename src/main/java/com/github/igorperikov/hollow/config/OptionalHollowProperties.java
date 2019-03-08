package com.github.igorperikov.hollow.config;

public class OptionalHollowProperties {
    public OptionalHollowProperties(
            boolean parameterizeAllClassNames,
            boolean useAggressiveSubstitutions,
            boolean useBooleanFieldErgonomics,
            boolean reservePrimaryKeyIndexForTypeWithPrimaryKey,
            boolean useHollowPrimitiveTypes,
            boolean useVerboseToString,
            boolean useErgonomicShortcuts,
            boolean usePackageGrouping,
            boolean restrictApiToFieldType,
            String classPostfix
    ) {
        this.parameterizeAllClassNames = parameterizeAllClassNames;
        this.useAggressiveSubstitutions = useAggressiveSubstitutions;
        this.useBooleanFieldErgonomics = useBooleanFieldErgonomics;
        this.reservePrimaryKeyIndexForTypeWithPrimaryKey = reservePrimaryKeyIndexForTypeWithPrimaryKey;
        this.useHollowPrimitiveTypes = useHollowPrimitiveTypes;
        this.useVerboseToString = useVerboseToString;
        this.useErgonomicShortcuts = useErgonomicShortcuts;
        this.usePackageGrouping = usePackageGrouping;
        this.restrictApiToFieldType = restrictApiToFieldType;
        this.classPostfix = classPostfix;
    }

    public final boolean parameterizeAllClassNames;
    public final boolean useAggressiveSubstitutions;
    public final boolean useBooleanFieldErgonomics;
    public final boolean reservePrimaryKeyIndexForTypeWithPrimaryKey;
    public final boolean useHollowPrimitiveTypes;
    public final boolean useVerboseToString;
    public final boolean useErgonomicShortcuts;
    public final boolean usePackageGrouping;
    public final boolean restrictApiToFieldType;
    public final String classPostfix;
}
