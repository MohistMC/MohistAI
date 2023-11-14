package com.mohistmc.ai.live;

import com.mohistmc.ai.MohistAI;
import com.mohistmc.ai.MohistConfig;
import com.mohistmc.tools.IOUtil;
import com.mohistmc.tools.NamedThreadFactory;
import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import mjson.Json;

/**
 * @author Mgazul by MohistMC
 * @date 2023/9/7 3:49:35
 */
public class HuyaLive {

    public static ScheduledExecutorService LIVE = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("Huya - Live"));
    public static HuyaLive INSTANCE = new HuyaLive();

    public static void main(String[] args) throws IOException {
        String jsonText = IOUtil.readContent(IOUtil.getInputStream("https://www.huya.com/pinkfish")).split("TT_ROOM_DATA = ")[1].split("};")[0] + "}";
        Json json = Json.read(jsonText);
        System.out.println(jsonText);
        System.out.println("直播间: " + "https://www.huya.com/pinkfish");
        System.out.println("直播状态: " + (json.asString("state").equals("REPLAY") ? "重播中" : "直播中"));
        System.out.println("直播提醒: " + (json.asBoolean("isOn") ? "直播中" : "未开播"));
        System.out.println("标题: " + json.asString("introduction"));
        new HuyaLive().run();
    }

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
        boolean liveStatus = json.asBoolean("isOn");
        var title = json.asString("introduction");

        // TODO 添加艾特所有人的功能
        if (liveStatus) {
            if (!MohistConfig.live_huya_pushqq) {
                String ms = ("""
                        你关注的主播已开播
                                            
                        直播标题： %s
                        直播地址：https://www.huya.com/pinkfish
                        """).formatted(title);

                System.out.println(ms);
                MohistAI.sendAllAt(ms);
                MohistConfig.set("live.huya.pushqq", true);
                System.out.println("已推送至QQ");
            }
        } else {
            if (MohistConfig.live_huya_pushqq) {
                System.out.println("已初始化推送(HuYa)");
                MohistConfig.set("live.huya.pushqq", false);
            }
        }
    }
}
