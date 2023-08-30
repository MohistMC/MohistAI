package com.mohistmc.ai.live.douyu;

import com.mohistmc.ai.Account;
import com.mohistmc.ai.MohistAI;
import com.mohistmc.ai.live.douyu.entry.DouyuApiData;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * @author Mgazul by MohistMC
 * @date 2023/3/27 23:27:31
 */
public class DouyuPipeline implements Pipeline {

    public DouyuPipeline() {
    }

    public void process(ResultItems resultItems, Task task) {
        DouyuApiData liveApiData = DouyuLive.liveApiBody.getRendata().getData();

        String liveStatus = DouyuLive.liveApiBody.getState();
        String title = liveApiData.getRoomName();

        if (liveStatus.equalsIgnoreCase("SUCCESS")) {
            if (DouyuLive.isPushQQ) return;
            String ms = ("""
                    你关注的主播已开播
                                        
                    直播标题： %s
                    直播地址：https://www.douyu.com/5753281
                    """).formatted(title);

            System.out.println(ms);
            MohistAI.sendMsgToGroup(Account.mohistQQGGroup, ms);
            //Utils.sendMessageToGroup(937872056, stringBuilder);
            DouyuLive.setPushQQ(true);
        } else {
            DouyuLive.setPushQQ(false);
        }
    }
}
