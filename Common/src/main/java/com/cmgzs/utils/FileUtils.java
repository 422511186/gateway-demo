package com.cmgzs.utils;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * 文件操作工具类
 */
public class FileUtils {

    /**
     * 删除文件夹（包括其中的文件）
     *
     * @param dirFile 文件路径
     * @return 删除结果
     */
    public static boolean deleteFile(File dirFile) {
        // 如果传进来的dir为空
        if (dirFile == null)
            return false;
        // 如果dir对应的文件不存在，则退出
        if (!dirFile.exists()) {
            return false;
        }
        if (dirFile.isFile()) {
            return dirFile.delete();
        } else {
            for (File file : dirFile.listFiles()) {
                deleteFile(file);
            }
        }
        return dirFile.delete();
    }

    /**
     * 数据写入文件
     *
     * @param filePath 文件路径
     * @param data     数据
     * @return 是否执行成功
     */
    public static boolean writeToFile(String filePath, String data) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    throw new IOException("Unable to create file");
                }
            }
            if (!file.canWrite()) {
                throw new IOException("Unable to write to file");
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(data);
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 复制文件
     *
     * @param sourcePath 源路径
     * @param targetPath 目标路径
     * @throws IOException 异常
     */
    public static void copyFiles(String sourcePath, String targetPath) throws IOException {
        File sourceDirectory = new File(sourcePath);
        File[] files = sourceDirectory.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            Path source = file.toPath();
            Path target = new File(targetPath, file.getName()).toPath();
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
