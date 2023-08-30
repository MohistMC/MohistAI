package com.mohistmc.ai.live.douyu;

import com.alibaba.fastjson.JSON;
import com.mohistmc.ai.live.douyu.entry.DouyuApiRootBean;
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

/**
 * @author Mgazul by MohistMC
 * @date 2023/3/27 23:17:09
 */
public class DouyuLive implements PageProcessor {

    public static final ScheduledExecutorService LIVE = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("Douyu - Live"));
    public static DouyuApiRootBean liveApiBody;
    @Setter
    public static boolean isPushQQ = false;
    private final Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10000).setCharset(StandardCharsets.UTF_8.displayName());

    @Override
    public void process(Page page) {
        Json content = page.getJson();
        liveApiBody = JSON.parseObject(content.toString(), DouyuApiRootBean.class);

        //直播状态码 1为开播
        String live_status = liveApiBody.getState();
        if (live_status.equalsIgnoreCase("no")) {
            isPushQQ = false;
            page.setSkip(true);
        }

        page.putField("DouyuApiRootBean", content);
    }

    @Override
    public Site getSite() {
        return site;
    }

    public void run() {
        LIVE.scheduleAtFixedRate(this::run0, 1000, 1000 * 3, TimeUnit.MILLISECONDS);
    }

    private void run0() {
        Spider.create(new DouyuLive())
                .addUrl("https://web.sinsyth.com/lxapi/douyujx.x?rid=5753281")
                .addPipeline(new DouyuPipeline())
                .runAsync();
    }
}
