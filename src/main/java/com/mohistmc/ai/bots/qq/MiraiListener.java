package com.mohistmc.ai.bots.qq;

import com.mohistmc.ai.Account;
import com.mohistmc.ai.HasteUtils;
import com.mohistmc.ai.IOUtil;
import com.mohistmc.ai.MohistAI;
import com.mohistmc.ai.MohistConfig;
import mjson.Json;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.BotOnlineEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.FileMessage;
import net.mamoe.mirai.message.data.SingleMessage;

import java.io.IOException;

/**
 * @author Mgazul by MohistMC
 * @date 2023/7/12 16:00:30
 */
public class MiraiListener extends SimpleListenerHost {

    @EventHandler
    public ListeningStatus onMessage(BotOnlineEvent event) {
        if (event.getBot().getId() == 2703566153L) {
            MohistAI.INSTANCE.QQ = event.getBot();
        }

        return ListeningStatus.LISTENING; // 表示继续监听事件
    }
    @EventHandler
    public ListeningStatus onMessage(GroupMessageEvent event) throws IOException {
        if (event.getGroup().getId() == Account.mohistQQGGroup) {
            for (SingleMessage s : event.getMessage()) {
                if (s instanceof FileMessage f) {
                    //获取文件的下载链接
                    if (f.getName().endsWith(".log") || f.getName().endsWith(".txt") || f.getName().endsWith(".yml")) {
                        String url = f.toAbsoluteFile(event.getSubject()).getUrl();
                        String read = IOUtil.readContent(IOUtil.getInputStream(url));
                        event.getGroup().sendMessage("文件名: " + f.getName() + " 已自动上传至: " + HasteUtils.pasteMohist(read));
                    }
                }
            }
        }
        if (event.getGroup().getId() == Account.fish0) {
            if (event.getMessage().contentToString().equals("直播检测")) {
                String jsonText = IOUtil.readContent(IOUtil.getInputStream("https://www.huya.com/pinkfish")).split("TT_ROOM_DATA = ")[1].split("};")[0] + "}";
                Json json = Json.read(jsonText);
                String ms = ("""
                    直播状态: %s
                    直播标题: %s
                    直播地址：https://www.huya.com/pinkfish
                    """).formatted((json.at("state").asString().equals("REPLAY") ? "重播中" : "直播中"), json.at("introduction").asString());
                MohistAI.sendMsgToFish(Account.fish0, ms);
            }
            boolean bb = false;
            for (SingleMessage s : event.getMessage()) {
                if (s instanceof At at) {
                    if (at.getTarget() == 1947585689L) {
                        if (!bb) {
                            bb = true;
                            MohistAI.sendMsgToFish(Account.fish0, "麻麻不让我和陌生人讲话!");
                        }
                    }
                }
            }
        }
        return ListeningStatus.LISTENING; // 表示继续监听事件
    }
}
