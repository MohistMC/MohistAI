package com.mohistmc.ai.live.huya;

import com.mohistmc.ai.MohistConfig;
import com.mohistmc.ai.utils.NamedThreadFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Json;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Mgazul by MohistMC
 * @date 2023/9/7 3:49:35
 */
public class HuyaLive implements PageProcessor {

    public static final ScheduledExecutorService LIVE = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("Huya - Live"));
    private final Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10000).setCharset(StandardCharsets.UTF_8.displayName());
    public static mjson.Json json;

    @Override
    public void process(Page page) {
        Json content = page.getJson();

        json = mjson.Json.read(content.toString().split("TT_ROOM_DATA = ")[1].split("};")[0] + "}");
        //直播状态码 1为开播
        if (!json.at("isOn").asBoolean()) {
            if (MohistConfig.live_huya_pushqq) MohistConfig.set("live.huya.pushqq", false);
            page.setSkip(true);
        }

        page.putField("HuyaApiRootBean", content);
    }

    @Override
    public Site getSite() {
        return site;
    }

    public void run() {
        if (!MohistConfig.live_huya) return;
        System.out.println("虎牙开播推送服务已启用");
        LIVE.scheduleAtFixedRate(this::run0, 1000, 1000 * 5, TimeUnit.MILLISECONDS);
    }

    private void run0() {
        Spider.create(new HuyaLive())
                .addUrl("https://www.huya.com/pinkfish")
                .addPipeline(new HuyaToQQPipeline())
                .runAsync();
    }
}
