package com.mohistmc.ai.live;

import com.mohistmc.ai.MohistConfig;
import com.mohistmc.ai.live.entity.RoomInfo;
import com.mohistmc.ai.log.Log;
import com.mohistmc.ai.sdk.qq.QQ;
import com.mohistmc.mjson.Json;
import com.mohistmc.tools.NamedThreadFactory;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;

public class BiliBiliLive {

    public static ScheduledExecutorService LIVE = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("BiliBili - Live"));
    public static BiliBiliLive INSTANCE = new BiliBiliLive();
    public static Set<String> concerns = new HashSet<>();

    public void run() {
        if (!MohistConfig.live_bilibili.asBoolean()) return;
        Log.info("B站开播推送服务已启用");
        concerns.addAll(MohistConfig.live_bilibili_concerns.asStringList());
        Log.info("关注列表: " + concerns);
        LIVE.scheduleAtFixedRate(this::run0, 1000, 1000 * 10, TimeUnit.MILLISECONDS);
    }


    @SneakyThrows
    private void run0() {
        for (var room_id : concerns) {
            Json json = Json.read(URI.create("https://api.live.bilibili.com/xlive/web-room/v1/index/getInfoByRoom?room_id=" + room_id).toURL());
            var data = json.at("data");
            if (data != null) {
                var room_info_json = data.at("room_info");
                RoomInfo roomInfo = room_info_json.asBean(RoomInfo.class);
                if (roomInfo.isLive()) {
                    if (!MohistConfig.live_bilibili_pushqq.asBoolean()) {
                        String ms = """
                                你关注的主播已开播
                                 
                                主播: %s
                                直播标题： %s
                                直播地址：https://live.bilibili.com/%s"""
                                .formatted(data.at("anchor_info").at("base_info").asString("uname"), roomInfo.getTitle(), room_id);

                        Log.info(ms);
                        MohistConfig.live_bilibili_pushqq.setValues(true);
                        MohistConfig.save();
                        QQ.sendToMohistGroup(ms);
                        Log.info("已推送至QQ");
                    }
                } else {
                    if (MohistConfig.live_bilibili_pushqq.asBoolean()) {
                        Log.info("已初始化推送(BiliBili)");
                        MohistConfig.live_bilibili_pushqq.setValues(false);
                        MohistConfig.save();
                    }
                }
            }
        }
    }
}
