package com.mohistmc.ai.bots.qq;

import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.mohistmc.ai.Account;
import com.mohistmc.ai.HasteUtils;
import com.mohistmc.ai.IOUtil;
import com.mohistmc.ai.MohistAI;
import com.mohistmc.ai.MohistConfig;
import com.mohistmc.ai.dashscope.ChatAPI;
import com.mohistmc.ai.dashscope.QianWen;
import mjson.Json;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.BotOnlineEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Audio;
import net.mamoe.mirai.message.data.FileMessage;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageContent;
import net.mamoe.mirai.message.data.OfflineAudio;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.message.data.SingleMessage;
import net.mamoe.mirai.utils.ExternalResource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mgazul by MohistMC
 * @date 2023/7/12 16:00:30
 */
public class MiraiListener extends SimpleListenerHost {

    public static List<Long> ziyou = new ArrayList<>();
    public static List<String> commands = List.of("鱼酱帮助列表", "直播检测", "开启直播推送", "关闭直播推送", "开启自由对话", "关闭自由对话");
    public static List<String> sss = List.of("鱼酱", "语音", "画");

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
        if (event.getSender().getId() == 1947585689L) {
            return ListeningStatus.STOPPED;
        }
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

        if (event.getSender().getPermission().getLevel() > 0) {
            if (message.equals("鱼酱帮助列表")) {
                String sb = """
                        ====== 口令 ======
                        直播检测
                        开启直播推送
                        关闭直播推送
                        直播推送列表
                        开启自由对话
                        关闭自由对话
                        鱼酱 + <内容>""";
                event.getGroup().sendMessage(sb);

            } else if (message.equals("直播检测")) {
                try {
                    String jsonText = IOUtil.readContent(IOUtil.getInputStream("https://www.huya.com/pinkfish")).split("TT_ROOM_DATA = ")[1].split("};")[0] + "}";
                    Json json = Json.read(jsonText);
                    String ms = ("""
                            直播状态: %s
                            直播标题: %s
                            直播地址：https://www.huya.com/pinkfish
                            """).formatted((json.at("state").asString().equals("REPLAY") ? "重播中" : "直播中"), json.at("introduction").asString());
                    event.getGroup().sendMessage(ms);
                } catch (Exception ignored) {
                }
            } else if (message.equals("开启直播推送")) {
                if (!MohistConfig.live_huya) {
                    MohistConfig.set("live.huya.enable", true);
                    event.getGroup().sendMessage("开启成功!");
                } else {
                    event.getGroup().sendMessage("已经开启了哟!");
                }
            } else if (message.equals("关闭直播推送")) {
                if (MohistConfig.live_huya) {
                    MohistConfig.set("live.huya.enable", false);
                    event.getGroup().sendMessage("关闭成功!");
                } else {
                    event.getGroup().sendMessage("已经关闭了哟!");
                }
            } else if (message.equals("直播推送列表")) {
                StringBuilder sb = new StringBuilder();
                for (Object l : MohistConfig.fishQQG) {
                    sb.append(l).append("\n");
                }
                event.getGroup().sendMessage("开启推送的QQ群!\n" + sb);
            } else if (message.startsWith("鱼酱") || message.startsWith("小小墨")) {
                if (message.length() > 2) {
                    String messageStr = message.substring(2);
                    String m = ChatAPI.send(messageStr);
                    if (m != null) {
                        event.getGroup().sendMessage(m);
                    }
                }
            } else if (message.startsWith("语音")) {
                if (message.length() > 2) {
                    String messageStr = message.substring(2);
                    try (ExternalResource externalResource = ExternalResource.create(QianWen.SyncAudioDataToFile(event.getSender().getId(), messageStr))) {
                        OfflineAudio offlineAudio = event.getGroup().uploadAudio(externalResource);
                        event.getGroup().sendMessage(offlineAudio);
                    } catch (IOException e) {
                        event.getGroup().sendMessage("暂时无法回答!");
                    }
                }
            } else if (message.startsWith("画")) {
                if (message.length() > 1) {
                    String messageStr = message.substring(1);
                    try (ExternalResource externalResource = ExternalResource.create(QianWen.basicCall(messageStr).openStream())) {
                        Image offlineAudio = event.getGroup().uploadImage(externalResource);
                        event.getGroup().sendMessage(offlineAudio);
                    } catch (IOException | NoApiKeyException e) {
                        event.getGroup().sendMessage("暂时无法回答!");
                    }
                }
            } else if (message.equals("开启自由对话")) {
                ziyou.add(group);
                event.getGroup().sendMessage("开启成功!");
            } else if (message.equals("关闭自由对话")) {
                ziyou.remove(group);
                event.getGroup().sendMessage("关闭成功!");
            }
        }
        if (ziyou.contains(group)) {
            if (event.getMessage() instanceof FileMessage) {
                System.out.println("这是一个文件");
                return ListeningStatus.STOPPED;
            } else if (event.getMessage() instanceof Image) {
                System.out.println("这是一个图片");
                return ListeningStatus.STOPPED;
            } else if (event.getMessage() instanceof Audio) {
                System.out.println("这是一个语音");
                return ListeningStatus.STOPPED;
            } else if (!commands.contains(message) && !message.equals("[动画表情]") && !message.equals("[图片]")) {
                String m = ChatAPI.send(message);
                if (m != null) {
                    event.getGroup().sendMessage(m);
                }
            }
        }

        return ListeningStatus.LISTENING; // 表示继续监听事件
    }

}
