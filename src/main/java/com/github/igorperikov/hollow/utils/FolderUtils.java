package com.github.igorperikov.hollow.utils;

import java.io.File;

public class FolderUtils {
    public static String buildPathToApiTargetFolder(String apiPackageName, String path) {
        return path + convertPackageNameToFolderPath(apiPackageName);
    }

    public static void cleanupAndCreateFolders(String folderPath) {
        File apiCodeFolder = new File(folderPath);
        apiCodeFolder.mkdirs();
        for (File f : apiCodeFolder.listFiles()) {
            f.delete();
        }
    }

    private static String convertPackageNameToFolderPath(String packageName) {
        return packageName.replaceAll("\\.", "/");
    }
}
