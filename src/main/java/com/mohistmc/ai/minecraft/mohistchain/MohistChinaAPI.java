package com.mohistmc.ai.minecraft.mohistchain;

import com.mohistmc.mjson.Json;
import com.mohistmc.tools.ConnectionUtil;
import com.mohistmc.tools.MD5Util;
import com.mohistmc.tools.NamedThreadFactory;
import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;

/**
 * @author Mgazul by MohistMC
 * @date 2023/9/10 23:44:25
 */

public class MohistChinaAPI {

    public static Map<String, String> dataMap = new HashMap<>();
    public static List<String> versionList = List.of("1.16.5", "1.18.2", "1.19.2", "1.19.4", "1.20.1", "1.20.2");

    public static final ScheduledExecutorService LIVE = new ScheduledThreadPoolExecutor(4, new NamedThreadFactory("syncMohistAPI - "));

    public static Map<String, Boolean> canDownload = new HashMap<>();

    public static void init() {
        dataMap.put("1.7.10", "Mohist-1.7.10-46-server.jar");
        dataMap.put("1.12.2", "mohist-1.12.2-343-server.jar");
        run0();
        syncMohistAPI();
    }

    public static void syncMohistAPI() {
        LIVE.scheduleAtFixedRate(MohistChinaAPI::run0, 1000, 1000 * 5 * 60 * 3, TimeUnit.MILLISECONDS); // 3分钟同步一次 时间单位毫秒
    }

    @SneakyThrows
    private static void run0() {
        for (String version : versionList) {
            String v2 = "https://mohistmc.com/api/v2/projects/mohist/%s/builds/latest";
            Json json = Json.read(new URL(v2.formatted(version))).at("build");
            String url = json.asString("url");
            int number = json.asInteger("number");
            String jarName = "mohist-%s-%s-server.jar".formatted(version, number);
            File mohist = new File("mohistchinaapi/" + version, jarName);
            File folder = new File("mohistchinaapi", version);
            if (folder.exists() && folder.isDirectory()) {
                File[] files = folder.listFiles();

                if (files != null) {
                    for (File file : files) {
                        if (!file.getName().equals(mohist.getName())) {
                            file.delete(); // 删除旧版本jar
                        } else {
                            // 添加一次MD5检测
                            if (!MD5Util.get(file).equals(json.asString("fileMd5"))) {
                                canDownload.put(version, false);
                                downloadFile(url, mohist);
                                canDownload.put(version, true);
                            }
                            canDownload.put(version, true);
                            dataMap.put(version, file.getName());
                        }

                    }
                }
            } else {
                mohist.getParentFile().mkdirs();
            }
            if (!mohist.exists()) {
                canDownload.put(version, false);
                downloadFile(url, mohist);
                dataMap.put(version, mohist.getName());
                canDownload.put(version, true);
            }
        }
    }

    public static void downloadFile(String URL, File f) throws Exception {
        System.out.println("下载文件中: " + URL);
        URLConnection conn = ConnectionUtil.getConn(URL);
        ReadableByteChannel rbc = Channels.newChannel(conn.getInputStream());
        FileChannel fc = FileChannel.open(f.toPath(), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);

        fc.transferFrom(rbc, 0, Long.MAX_VALUE);
        fc.close();
        rbc.close();
        System.out.println("下载完毕: " + URL);
    }
}
