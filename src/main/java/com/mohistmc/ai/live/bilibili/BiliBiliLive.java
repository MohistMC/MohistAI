package com.mohistmc.ai.live.bilibili;

import com.alibaba.fastjson.JSON;
import com.mohistmc.ai.MohistConfig;
import com.mohistmc.ai.live.bilibili.entry.LiveApiBody;
import com.mohistmc.ai.utils.NamedThreadFactory;
import lombok.Setter;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Json;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class BiliBiliLive implements PageProcessor {

    public static final ScheduledExecutorService LIVE = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("BiliBili - Live"));
    public static LiveApiBody liveApiBody;
    @Setter
    public static boolean isPushQQ = false;
    private final Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10000).setCharset(StandardCharsets.UTF_8.displayName());

    @Override
    public void process(Page page) {
        Json content = page.getJson();
        liveApiBody = JSON.parseObject(content.toString(), LiveApiBody.class);

        //直播状态码 1为开播
        int live_status = liveApiBody.getData().getRoom_info().getLive_status();
        if (live_status == 0) {
            isPushQQ = false;
            page.setSkip(true);
        }

        page.putField("LiveApiBody", content);
    }

    @Override
    public Site getSite() {
        return site;
    }

    public void run() {
        LIVE.scheduleAtFixedRate(this::run0, 1000, 1000 * 3, TimeUnit.MILLISECONDS);
    }

    private void run0() {
        if (!MohistConfig.live_bilibili) return;
        Spider.create(new BiliBiliLive())
                .addUrl("https://api.live.bilibili.com/xlive/web-room/v1/index/getInfoByRoom?room_id=43087")
                .addPipeline(new QQPipeline())
                .runAsync();
        System.out.println("B站开播推送服务已启用");
    }
}
