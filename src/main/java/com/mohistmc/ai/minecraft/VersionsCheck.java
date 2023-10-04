package com.mohistmc.ai.minecraft;

import com.mohistmc.ai.MohistAI;
import com.mohistmc.ai.MohistConfig;
import com.mohistmc.ai.utils.NamedThreadFactory;
import lombok.SneakyThrows;
import mjson.Json;

import java.net.URI;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Mgazul by MohistMC
 * @date 2023/9/21 2:32:40
 */
public class VersionsCheck {

    public static VersionsCheck INSTANCE = new VersionsCheck();

    public static final ScheduledExecutorService LIVE = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("Minecraft - versions check"));

    public void run() {
        if (!MohistConfig.minecraft_versionscheck) return;
        System.out.println("MC新版本推送服务已启用");
        LIVE.scheduleAtFixedRate(this::run0, 1000 * 10, 1000 * 20, TimeUnit.MILLISECONDS);
    }

    @SneakyThrows
    public void run0() {
        Json json = Json.read(URI.create("https://launchermeta.mojang.com/mc/game/version_manifest.json").toURL());
        var release = json.at("latest").asString("release");
        var snapshot = json.at("latest").asString("snapshot");
        List<Json> versions = json.at("versions").asJsonList();

        // 初始化版本缓存
        if (MohistConfig.minecraft_release.isEmpty() && MohistConfig.minecraft_snapshot.isEmpty()) {
            MohistConfig.set("minecraft.release", release);
            MohistConfig.set("minecraft.snapshot", snapshot);
            System.out.println("初始化MC版本完成!");
            System.out.printf("Release: %s%n", MohistConfig.minecraft_release);
            System.out.printf("Snapshot: %s%n", MohistConfig.minecraft_snapshot);
        }
        if (!MohistConfig.minecraft_release.equals(release)) {
            f(release, versions);
        }
        if (!MohistConfig.minecraft_snapshot.equals(snapshot)) {
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
                System.out.println(sendMsg);
                MohistConfig.set("minecraft." + type, version);
                MohistAI.sendAll(sendMsg);
            }
        }
    }
}
