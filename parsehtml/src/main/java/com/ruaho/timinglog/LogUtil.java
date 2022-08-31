package com.ruaho.timinglog;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @projectName: parsehtml
 * @package: com.ruaho.timinglog
 * @className: LogUtil
 * @author: tomdayu
 * @description: 输出日志到文件
 * @date: 2022/8/3 9:23
 * @version: 1.0
 */
public class LogUtil {

    public static void Log(String ip, String msg){
        try {
            // 文件名
            StringBuilder fileName = new StringBuilder();
            // 记录ip和端口号
            URI uri = URI.create(ip);
            String host = uri.getHost();
            int port = uri.getPort();
            fileName.append(host).append("-").append(port).append("-");

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String day = format.format(new Date(System.currentTimeMillis()));
            fileName.append(day).append(".log");

            //文件路径
            String filePath;
            ResourceUtil resourceUtil = new ResourceUtil();
            filePath = ResourceUtil.getValue("logpath");
            File file = new File(filePath + fileName);
            PrintStream out;
            if (file.exists()) {
                out = new PrintStream(new FileOutputStream(file,true));
            }else {
                file.createNewFile();
                out = new PrintStream(new FileOutputStream(file,true));
            }

            // 改变一个输出方向
            System.setOut(out);
            // 将日志输出到文件中
            System.out.println(msg);

        } catch (
                FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}