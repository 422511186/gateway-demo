package com.cmgzs.utils.cmd;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;

@Slf4j
public class TermUtils {


    public static int execute(List<String> command, String directory) throws IOException, InterruptedException {
        // 创建 ProcessBuilder 对象
        ProcessBuilder builder = new ProcessBuilder(command);

        // 设置工作目录
        if (directory != null) {
            builder.directory(new File(directory));
        }

        // 重定向错误输出到标准输出
        builder.redirectErrorStream(true);

        // 启动进程并等待其完成
        Process process = builder.start();

        // 读取进程输出并输出到控制台
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), Charset.defaultCharset()));
        while ((line = reader.readLine()) != null) {
            log.debug(line);
        }
        return process.waitFor();
    }
}
