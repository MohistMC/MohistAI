package com.mohistmc.ai.live;

import com.mohistmc.ai.Account;
import com.mohistmc.ai.MohistAI;
import com.mohistmc.ai.MohistConfig;
import com.mohistmc.ai.utils.NamedThreadFactory;
import lombok.SneakyThrows;
import mjson.Json;

import java.net.URI;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class BiliBiliLive{

    public static BiliBiliLive INSTANCE = new BiliBiliLive();

    public static final ScheduledExecutorService LIVE = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("BiliBili - Live"));

    public void run() {
        if (!MohistConfig.live_bilibili) return;
        System.out.println("B站开播推送服务已启用");
        LIVE.scheduleAtFixedRate(this::run0, 1000, 1000 * 10, TimeUnit.MILLISECONDS);
    }


    @SneakyThrows
    private void run0() {
        Json json = Json.read(URI.create("https://api.live.bilibili.com/xlive/web-room/v1/index/getInfoByRoom?room_id=43087").toURL());
        var liveStatus = json.at("data").at("room_info").at("live_status").asInteger();
        var title = json.at("data").at("room_info").at("title").asInteger();

        if (liveStatus == 1) {
            // TODO 添加可配置Bot识别
            if (MohistAI.INSTANCE.QQ != null && !MohistConfig.live_bilibili_pushqq) {
                String ms = ("""
                    你关注的主播已开播
                                        
                    直播标题： %s
                    直播地址：https://live.bilibili.com/43087
                    """).formatted(title);

                System.out.println(ms);
                MohistAI.sendMsgToGroup(Account.mohistQQGGroup, ms);
                MohistConfig.set("live.bilibili.pushqq", true);
                System.out.println("已推送至QQ");
            }
        } else {
            if (MohistConfig.live_bilibili_pushqq) {
                MohistConfig.set("live.bilibili.pushqq", false);
            }
        }
    }
}