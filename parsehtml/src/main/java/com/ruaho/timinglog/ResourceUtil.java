package com.ruaho.timinglog;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * @projectName: parsehtml
 * @package: com.ruaho.timinglog
 * @className: ResourceUtil
 * @author: tomdayu
 * @description: 读取配置信息
 * @date: 2022/8/2 17:44
 * @version: 1.0
 */
public class ResourceUtil {
    private static ResourceBundle resourceBundle;
    private static BufferedInputStream inputStream;
    // 从jar包外读取配置文件
    static {
        String proFilePath = "/home/monitortools/config/" + "ipconfig.properties";
        try {
            inputStream = new BufferedInputStream(new FileInputStream(proFilePath));
            resourceBundle = new PropertyResourceBundle(inputStream);
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> getValues(){
        ArrayList<String> ips = new ArrayList<>();
        Enumeration e = resourceBundle.getKeys();
        while (e.hasMoreElements()){
            ips.add(resourceBundle.getString(e.nextElement().toString()));
        }
        return ips;
    }

    public static String getValue(String key){
        return resourceBundle.getString(key);
    }
}