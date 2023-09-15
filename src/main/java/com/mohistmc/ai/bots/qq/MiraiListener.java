package com.mohistmc.ai.bots.qq;

import com.alibaba.dashscope.exception.NoApiKeyException;
import com.mohistmc.ai.Account;
import com.mohistmc.ai.HasteUtils;
import com.mohistmc.ai.IOUtil;
import com.mohistmc.ai.MohistAI;
import com.mohistmc.ai.MohistConfig;
import com.mohistmc.ai.dashscope.ChatAPI;
import com.mohistmc.ai.dashscope.ChatApiType;
import com.mohistmc.ai.dashscope.QianWen;
import mjson.Json;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.BotOnlineEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.FileMessage;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.OfflineAudio;
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
        int permission = event.getSender().getPermission().getLevel();

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

        boolean bb = false;
        for (SingleMessage sm : event.getMessage()) {
            if (sm instanceof At at) {
                if (at.getTarget() == event.getBot().getId()) {
                    if (!bb) {
                        bb = true;
                        String atMessage = message.replace("@" + at.getTarget(), "").trim();

                        if (atMessage.equals("帮助")) {
                            if (permission > 0) {
                                String sb = """
                                        ====== 口令 ======
                                        开启直播推送
                                        关闭直播推送
                                        直播推送列表
                                        开启自由对话
                                        关闭自由对话
                                        更改对话模型
                                        艾特机器人""";
                                event.getGroup().sendMessage(sb);
                            }

                        } else if (atMessage.equals("开启直播推送")) {
                            if (permission > 0) {
                                if (!MohistConfig.live_huya) {
                                    MohistConfig.set("live.huya.enable", true);
                                    event.getGroup().sendMessage("开启成功!");
                                } else {
                                    event.getGroup().sendMessage("已经开启了哟!");
                                }
                            }
                        } else if (atMessage.equals("关闭直播推送")) {
                            if (permission > 0) {
                                if (MohistConfig.live_huya) {
                                    MohistConfig.set("live.huya.enable", false);
                                    event.getGroup().sendMessage("关闭成功!");
                                } else {
                                    event.getGroup().sendMessage("已经关闭了哟!");
                                }
                            }
                        } else if (atMessage.equals("直播推送列表")) {
                            if (permission > 0) {
                                StringBuilder sb = new StringBuilder();
                                for (Object l : MohistConfig.fishQQG) {
                                    sb.append(l).append("\n");
                                }
                                event.getGroup().sendMessage("开启推送的QQ群!\n" + sb);
                            }
                        } else if (atMessage.startsWith("语音")) {
                            if (permission > 0) {
                                if (atMessage.length() > 2) {
                                    String messageStr = atMessage.substring(2);
                                    try (ExternalResource externalResource = ExternalResource.create(QianWen.SyncAudioDataToFile(event.getSender().getId(), messageStr))) {
                                        OfflineAudio offlineAudio = event.getGroup().uploadAudio(externalResource);
                                        event.getGroup().sendMessage(offlineAudio);
                                    } catch (IOException e) {
                                        event.getGroup().sendMessage("暂时无法回答!");
                                    }
                                }
                            }
                        } else if (atMessage.startsWith("画")) {
                            if (permission > 0) {
                                if (atMessage.length() > 1) {
                                    String messageStr = atMessage.substring(1);
                                    try (ExternalResource externalResource = ExternalResource.create(QianWen.basicCall(messageStr).openStream())) {
                                        Image offlineAudio = event.getGroup().uploadImage(externalResource);
                                        event.getGroup().sendMessage(offlineAudio);
                                    } catch (IOException | NoApiKeyException e) {
                                        event.getGroup().sendMessage("暂时无法回答!");
                                    }
                                }
                            }
                        } else if (atMessage.equals("开启自由对话")) {
                            if (permission > 0) {
                                ziyou.add(group);
                                event.getGroup().sendMessage("开启成功!");
                            }
                        } else if (atMessage.equals("关闭自由对话")) {
                            if (permission > 0) {
                                ziyou.remove(group);
                                event.getGroup().sendMessage("关闭成功!");
                            }
                        } else if (atMessage.equals("更改对话模型")) {
                            if (permission > 0) {
                                if (MohistConfig.ai_type == ChatApiType.ALIBABA) {
                                    MohistConfig.set("ai_type", ChatApiType.BAIDU.name());
                                } else {
                                    MohistConfig.set("ai_type", ChatApiType.ALIBABA.name());
                                }
                                event.getGroup().sendMessage("已更改为: " + MohistConfig.ai_type.asName());
                            }
                        } else {
                            if (permission > 0 || ziyou.contains(group)) {
                                if (!atMessage.equals("[动画表情]") && !atMessage.equals("[图片]") && !atMessage.equals("[表情]") && !atMessage.isEmpty()) {
                                    String m = ChatAPI.send(atMessage);
                                    if (m != null) {
                                        event.getGroup().sendMessage(m);
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }

        return ListeningStatus.LISTENING; // 表示继续监听事件
    }

}
