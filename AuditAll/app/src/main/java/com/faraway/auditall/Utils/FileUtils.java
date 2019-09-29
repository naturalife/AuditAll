package com.faraway.auditall.Utils;

import java.io.File;

/**
 * @author Fan
 * @description文件处理类
 * @date 2019-09-16
 * @time 9:51
 **/
public class FileUtils {

    /**
     * 删除指定目录下所有文件夹与文件
     *
     * @param path  路径
     */
    @SuppressWarnings("unchecked")
    public static void deleteDir(String path) {
        File dir = new File(path);
        deleteDirWihtFile(dir);
    }


    /**
     * 删除指定文件夹下所有文件夹与文件
     *
     * @param dir  文件夹
     */
    @SuppressWarnings("unchecked")
    public static void deleteDirWihtFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDirWihtFile(file); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }
}
