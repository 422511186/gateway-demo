package com.cmgzs.utils;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.GZIPInputStream;


/**
 * 网络资源下载 工具类
 *
 * @author huangzhenyu
 * @date 2023/3/17
 */
public class DownloadUtils {

    /**
     * 下载文件到本地
     *
     * @param urlString 下载的url资源路径
     * @param filename  存储的位置
     */
    public static void download(String urlString, String filename) throws Exception {
        URL url = new URL(urlString);// 构造URL
        URLConnection con = url.openConnection();// 打开连接
        InputStream is = con.getInputStream();// 输入流
        String code = con.getHeaderField("Content-Encoding");
        if ((null != code) && code.equals("gzip")) {
            GZIPInputStream gis = new GZIPInputStream(is);
            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流
            OutputStream os = new FileOutputStream(filename);
            // 开始读取
            while ((len = gis.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            // 完毕，关闭所有链接
            gis.close();
            os.close();
            is.close();
        } else {
            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流
            OutputStream os = new FileOutputStream(filename);
            // 开始读取
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            // 完毕，关闭所有链接
            os.close();
            is.close();
        }
    }

    public static void main(String[] args) {
        try {
            download("http://127.0.0.1:9300/statics/2023/03/16/latex%E7%BC%96%E8%AF%91%E5%99%A8%20%E7%B3%BB%E7%BB%9F%E8%AE%BE%E8%AE%A1_20230316163738A005.png", "D:\\WorkSpace\\简历\\latex%E7%BC%96%E8%AF%91%E5%99%A8%20%E7%B3%BB%E7%BB%9F%E8%AE%BE%E8%AE%A1_20230316163738A005.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}