package com.mohistmc.ai.live.bilibili;

import com.mohistmc.ai.Account;
import com.mohistmc.ai.MohistAI;
import com.mohistmc.ai.live.bilibili.entry.LiveApiData;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

public class QQPipeline implements Pipeline {

    public QQPipeline() {
    }

    public void process(ResultItems resultItems, Task task) {
        LiveApiData liveApiData = BiliBiliLive.liveApiBody.getData();

        int liveStatus = liveApiData.getRoom_info().getLive_status();
        String title = liveApiData.getRoom_info().getTitle();

        if (liveStatus == 1) {
            if (BiliBiliLive.isPushQQ) return;
            String ms = ("""
                    你关注的主播已开播
                                        
                    直播标题： %s
                    直播地址：https://live.bilibili.com/43087
                    """).formatted(title);

            System.out.println(ms);
            MohistAI.sendMsgToGroup(Account.mohistQQGGroup, ms);
            BiliBiliLive.setPushQQ(true);
        } else {
            BiliBiliLive.setPushQQ(false);
        }
    }
}
