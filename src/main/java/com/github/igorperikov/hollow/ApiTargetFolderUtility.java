package com.github.igorperikov.hollow;

public class ApiTargetFolderUtility {

    public static String buildPathToApiTargetFolder(String apiPackageName, String path) {
        return path + convertPackageNameToFolderPath(apiPackageName);
    }

    private static String convertPackageNameToFolderPath(String packageName) {
        return packageName.replaceAll("\\.", "/");
    }
}
