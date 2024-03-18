package com.mohistmc.ai.minecraft;

import com.mohistmc.ai.MohistAI;
import com.mohistmc.ai.MohistConfig;
import com.mohistmc.tools.NamedThreadFactory;
import java.net.URI;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import mjson.Json;

/**
 * @author Mgazul by MohistMC
 * @date 2023/9/21 2:32:40
 */
public class VersionsCheck {

    public static ScheduledExecutorService LIVE = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("Minecraft - versions check"));
    public static VersionsCheck INSTANCE = new VersionsCheck();

    public void run() {
        if (!MohistConfig.minecraft_versionscheck.asBoolean()) return;
        MohistAI.LOGGER.info("MC新版本推送服务已启用");
        LIVE.scheduleAtFixedRate(this::run0, 1000 * 10, 1000 * 20, TimeUnit.MILLISECONDS);
    }

    @SneakyThrows
    public void run0() {
        Json json = Json.read(URI.create("https://launchermeta.mojang.com/mc/game/version_manifest.json").toURL());
        var release = json.at("latest").asString("release");
        var snapshot = json.at("latest").asString("snapshot");
        List<Json> versions = json.at("versions").asJsonList();

        // 初始化版本缓存
        if (MohistConfig.minecraft_release.asString().isEmpty() && MohistConfig.minecraft_snapshot.asString().isEmpty()) {
            MohistConfig.minecraft_release.setValues(release);
            MohistConfig.minecraft_snapshot.setValues(snapshot);
            MohistConfig.save();
            MohistAI.LOGGER.info("初始化MC版本完成!");
            System.out.printf("Release: %s%n", MohistConfig.minecraft_release.asString());
            System.out.printf("Snapshot: %s%n", MohistConfig.minecraft_snapshot.asString());
        }
        if (!MohistConfig.minecraft_release.asString().equals(release)) {
            f(release, versions);
        }
        if (!MohistConfig.minecraft_snapshot.asString().equals(snapshot)) {
            f(snapshot, versions);
        }
    }

    private void f(String version, List<Json> versions) {
        for (Json f : versions) {
            var id = f.asString("id");
            if (id.equals(version)) {
                var type = f.asString("type");
                String sendMsg = ("""
                        ======MC新版本推送======
                        类型: %s
                        版本号: %s
                        发布时间: %s""").formatted(type, id, f.asString("releaseTime"));
                MohistAI.LOGGER.info(sendMsg);
                if (type.equals("release")) {
                    MohistConfig.minecraft_release.setValues(version);
                    MohistConfig.save();
                }
                if (type.equals("snapshot")) {
                    MohistConfig.minecraft_snapshot.setValues(version);
                    MohistConfig.save();
                }
            }
        }
    }
}
