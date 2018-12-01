package com.github.igorperikov.hollow.utils;

import java.io.File;

public class FolderUtils {
    public static String buildPathToApiTargetFolder(String apiPackageName, String path) {
        return path + convertPackageNameToFolderPath(apiPackageName);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void cleanupAndCreateFolders(String folderPath) {
        File apiCodeFolder = new File(folderPath);
        apiCodeFolder.mkdirs();
        File[] files = apiCodeFolder.listFiles();
        if (files == null) {
            throw new IllegalArgumentException("Incorrect path provided");
        }
        for (File f : files) {
            f.delete();
        }
    }

    private static String convertPackageNameToFolderPath(String packageName) {
        return packageName.replaceAll("\\.", "/");
    }
}
