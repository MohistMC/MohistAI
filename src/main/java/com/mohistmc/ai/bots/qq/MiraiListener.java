package com.mohistmc.ai.bots.qq;

import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.mohistmc.ai.Account;
import com.mohistmc.ai.HasteUtils;
import com.mohistmc.ai.IOUtil;
import com.mohistmc.ai.MohistAI;
import com.mohistmc.ai.MohistConfig;
import com.mohistmc.ai.dashscope.QianWen;
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
        Long group = event.getGroup().getId();
        String message = event.getMessage().contentToString();
        if (group == Account.mohistQQGGroup) {
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

        for (Object s : MohistConfig.fishQQG) {
            if (String.valueOf(s).contains(String.valueOf(group))) {
                if (event.getSender().getPermission().getLevel() > 0) {
                    if (message.equals("直播检测")) {
                        try {
                            String jsonText = IOUtil.readContent(IOUtil.getInputStream("https://www.huya.com/pinkfish")).split("TT_ROOM_DATA = ")[1].split("};")[0] + "}";
                            Json json = Json.read(jsonText);
                            String ms = ("""
                                    直播状态: %s
                                    直播标题: %s
                                    直播地址：https://www.huya.com/pinkfish
                                    """).formatted((json.at("state").asString().equals("REPLAY") ? "重播中" : "直播中"), json.at("introduction").asString());
                            MohistAI.sendMsgToFish(group, ms);
                        } catch (Exception ignored) {
                        }
                    }

                    if (message.equals("开启直播推送")) {
                        if (!MohistConfig.live_huya) {
                            MohistConfig.set("live.huya.enable", true);
                            MohistAI.sendMsgToFish(group, "开启成功!");
                        } else {
                            MohistAI.sendMsgToFish(group, "已经开启了哟!");
                        }
                    }
                    if (message.equals("关闭直播推送")) {
                        if (MohistConfig.live_huya) {
                            MohistConfig.set("live.huya.enable", false);
                            MohistAI.sendMsgToFish(group, "关闭成功!");
                        } else {
                            MohistAI.sendMsgToFish(group, "已经关闭了哟!");
                        }
                    }
                    if (message.equals("直播推送列表")) {
                        StringBuilder sb = new StringBuilder();
                        for (Object l : MohistConfig.fishQQG) {
                            sb.append(l).append("\n");
                        }
                        MohistAI.sendMsgToFish(group, "开启推送的QQ群!\n" + sb);
                    }

                    if (message.startsWith("鱼酱")) {
                        String messageStr = message.substring(2);
                        try {
                            MohistAI.sendMsgToFish(group, QianWen.callWithMessage(messageStr));
                        } catch (NoApiKeyException | InputRequiredException e) {
                            MohistAI.sendMsgToFish(group, "暂时无法回答!");
                        }
                    }
                }
                boolean bb = false;
                for (SingleMessage sm : event.getMessage()) {
                    if (sm instanceof At at) {
                        if (at.getTarget() == 1947585689L) {
                            if (!bb) {
                                bb = true;
                                MohistAI.sendMsgToFish(group, "麻麻不让我和陌生人讲话!");
                            }
                        }
                    }
                }
            }
        }
        return ListeningStatus.LISTENING; // 表示继续监听事件
    }
}
