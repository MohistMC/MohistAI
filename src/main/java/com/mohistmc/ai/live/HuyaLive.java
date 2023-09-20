package com.mohistmc.ai.live;

import com.mohistmc.ai.IOUtil;
import com.mohistmc.ai.MohistAI;
import com.mohistmc.ai.MohistConfig;
import com.mohistmc.ai.utils.NamedThreadFactory;
import lombok.SneakyThrows;
import mjson.Json;

import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Mgazul by MohistMC
 * @date 2023/9/7 3:49:35
 */
public class HuyaLive {

    public static HuyaLive INSTANCE = new HuyaLive();

    public static final ScheduledExecutorService LIVE = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("Huya - Live"));

    public void run() {
        if (!MohistConfig.live_huya) return;
        System.out.println("虎牙开播推送服务已启用");
        LIVE.scheduleAtFixedRate(this::run0, 1000, 1000 * 5, TimeUnit.MILLISECONDS);
    }

    public void shutdown() {
        LIVE.shutdown();
    }

    @SneakyThrows
    private void run0() {
        String jsonText = IOUtil.readContent(IOUtil.getInputStream("https://www.huya.com/pinkfish")).split("TT_ROOM_DATA = ")[1].split("};")[0] + "}";
        Json json = Json.read(jsonText);
        boolean liveStatus = json.at("isOn").asBoolean();
        var title = json.at("introduction").asString();

        // TODO 添加艾特所有人的功能
        if (liveStatus && !MohistConfig.live_huya_pushqq) {
            String ms = ("""
                    你关注的主播已开播
                                        
                    直播标题： %s
                    直播地址：https://www.huya.com/pinkfish
                    """).formatted(title);

            System.out.println(ms);
            MohistAI.sendAll(ms);
            MohistConfig.set("live.huya.pushqq", true);
            System.out.println("已推送至QQ");
        } else {
            if (MohistConfig.live_huya_pushqq) MohistConfig.set("live.huya.pushqq", false);
        }
    }

    public static void main(String[] args) throws IOException {
        String jsonText = IOUtil.readContent(IOUtil.getInputStream("https://www.huya.com/pinkfish")).split("TT_ROOM_DATA = ")[1].split("};")[0] + "}";
        Json json = Json.read(jsonText);
        System.out.println(jsonText);
        System.out.println("直播间: " + "https://www.huya.com/pinkfish");
        System.out.println("直播状态: " + (json.at("state").asString().equals("REPLAY") ? "重播中" : "直播中"));
        System.out.println("直播提醒: " + (json.at("isOn").asBoolean() ? "直播中" : "未开播"));
        System.out.println("标题: " + json.at("introduction").asString());
        new HuyaLive().run();
    }
}
