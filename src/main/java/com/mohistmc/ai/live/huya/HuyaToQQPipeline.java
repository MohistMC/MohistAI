package com.mohistmc.ai.live.huya;

import com.mohistmc.ai.MohistAI;
import com.mohistmc.ai.MohistConfig;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

public class HuyaToQQPipeline implements Pipeline {

    public HuyaToQQPipeline() {
    }

    public void process(ResultItems resultItems, Task task) {

        boolean liveStatus = HuyaLive.json.at("isOn").asBoolean();

        if (liveStatus && !MohistConfig.live_huya_pushqq) {
            String ms = ("""
                    你关注的主播已开播
                                        
                    直播标题： %s
                    直播地址：https://www.huya.com/pinkfish
                    """).formatted(HuyaLive.json.at("introduction").asString());

            System.out.println(ms);
            MohistAI.sendMsgToFish(ms);
            MohistConfig.set("live.huya.pushqq", true);
            System.out.println("已推送至QQ");
        }
    }
}
