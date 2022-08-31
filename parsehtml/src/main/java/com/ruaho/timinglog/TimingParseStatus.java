package com.ruaho.timinglog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.select.Elements;
import sun.java2d.pipe.AAShapePipe;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author jiaoweilin
 * @date 2022/8/2
 */
public class TimingParseStatus {
    /**
     * log
     */
    private static Log log = LogFactory.getLog(TimingParseStatus.class);

    // 入口
    public static void main(String[] args) throws IOException {
        // 获取ip
        ResourceUtil resourceUtil = new ResourceUtil();
        ArrayList<String> ipValues = ResourceUtil.getValues();
        // 过滤非ip属性
        Iterator iter = ipValues.iterator();
        while (iter.hasNext()) {
            String value = (String)iter.next();
            if(!value.contains("http")){
                iter.remove();
            }
        }

        // 根据ip获取任务
        ArrayList<Runnable> tasks = new ArrayList<>();
        for (String ip : ipValues) {
            //tasks.add(getTask(ip));
            tasks.add(new Task(ip));
        }
        // 执行任务
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        timingParse(tasks, ses);
    }

    /**
     * @param cfgFile:
     * @return void
     * @author jwl12
     * @description 获取配置文件信息
     * @date 2022/8/2 17:29
     */
    public static void readConfigFile(String cfgFile) {
        try {
            InputStream in = TimingParseStatus.class.getClassLoader().getResource(cfgFile).openStream();
            Properties prop = new Properties();
            prop.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param tasks:
     * @param ses:
     * @return void
     * @author jwl12
     * @description 执行任务
     * @date 2022/8/3 11:22
     */
    private static void timingParse(ArrayList<Runnable> tasks, ScheduledExecutorService ses){
        //立即执行，并且每分钟执行一次
        for (Runnable task : tasks) {
            ses.scheduleAtFixedRate(task, 0, 60*1000, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * @param key:
     * @param ip:
     * @return Runnable
     * @author jwl12
     * @description 创建Runnable任务：解析html并输出日志
     * @date 2022/8/3 11:23
     */
    private static Runnable getTask(final String ip) throws IOException {
        // 创建需要定时执行的任务,注意，如果ip写错，线程会被阻塞
        Runnable runnable = new Runnable() {
                public void run() {
                try {
                    // 获取页面
                    Document document = Jsoup.connect(ip).get();

                    // 日志格式初始化
                    StringBuilder logtext = new StringBuilder();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,");
                    String date = format.format(new Date(System.currentTimeMillis()));
                    logtext.append(date);
                    
                    // 取出table
                    Elements tbs = document.select("table");
                    // 遍历table
                    for (int i = 0; i < 3; i++) {
                        // 取出table中的td元素
                        Elements tds = tbs.get(i).select("td");
                        switch (i) {
                            case 0:
                                logtext.append(tds.get(7).text().substring(0, tds.get(7).text().length() - 3) + ",");
                                logtext.append(tds.get(9).text().substring(0, tds.get(9).text().length() - 3) + ",");
                                break;
                            case 1:
                                logtext.append(tds.get(0).text() + ",");
                                logtext.append(tds.get(1).text() + ",");
                                logtext.append(tds.get(2).text() + ",");
                                logtext.append(tds.get(3).text() + ",");
                                break;
                            case 2:
                                logtext.append(tds.get(1).text() + ",");
                                logtext.append(tds.get(2).text() + ",");
                                logtext.append(tds.get(3).text() + ",");
                                logtext.append(tds.get(4).text());
                                break;
                        }
                    }
                    log.info(logtext);
                    // 输出到日志文件
                    LogUtil.Log(ip, logtext.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        return runnable;
    }

    /**
     * @author jwl12
     * @description 创建需要定时执行的任务,注意，如果ip写错，线程会被阻塞
     * @date 2022/8/31 16:33
     */
    public static class Task implements Runnable {

        private String ip;

        public Task(String ip) {
            this.ip = ip;
        }

        @Override
        public void run() {
            try {
                // 获取页面
                Document document = Jsoup.connect(ip).get();

                // 日志格式初始化
                StringBuilder logtext = new StringBuilder();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,");
                String date = format.format(new Date(System.currentTimeMillis()));
                logtext.append(date);

                // 取出table
                Elements tbs = document.select("table");
                // 遍历table
                for (int i = 0; i < 3; i++) {
                    // 取出table中的td元素
                    Elements tds = tbs.get(i).select("td");
                    switch (i) {
                        case 0:
                            logtext.append(tds.get(7).text().substring(0, tds.get(7).text().length() - 3) + ",");
                            logtext.append(tds.get(9).text().substring(0, tds.get(9).text().length() - 3) + ",");
                            break;
                        case 1:
                            logtext.append(tds.get(0).text() + ",");
                            logtext.append(tds.get(1).text() + ",");
                            logtext.append(tds.get(2).text() + ",");
                            logtext.append(tds.get(3).text() + ",");
                            break;
                        case 2:
                            logtext.append(tds.get(1).text() + ",");
                            logtext.append(tds.get(2).text() + ",");
                            logtext.append(tds.get(3).text() + ",");
                            logtext.append(tds.get(4).text());
                            break;
                    }
                }
                log.info(logtext);
                // 输出到日志文件
                LogUtil.Log(ip, logtext.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }
}
