package com.mohistmc.ai.sdk.qq;

import com.mohistmc.ai.Account;
import com.mohistmc.ai.log.Log;
import com.mohistmc.ai.network.event.BaseListener;
import com.mohistmc.ai.network.event.HttpPostEvent;
import com.mohistmc.ai.sdk.BotType;
import com.mohistmc.ai.sdk.qq.entity.MessageRequest;
import com.mohistmc.ai.sdk.qq.entity.GetFile;
import com.mohistmc.ai.teamspeak3.TS3;
import com.mohistmc.mjson.Json;
import com.mohistmc.tools.HasteUtils;
import com.mohistmc.tools.IOUtil;
import java.io.FileInputStream;
import lombok.SneakyThrows;

public class QQPostListener implements BaseListener {
    @SneakyThrows
    public void onEvent(HttpPostEvent event) {
        Json json = event.getJson();
        if (event.isQQ()) {
            MessageRequest request = json.asBean(MessageRequest.class);
            String t = request.getMessage_type();
            if (t == null) {
                debug(event);
            }
            if (t != null && t.equals("group")) {
                Log.info("[群消息] 群号<%s> 发言者<%s>: %s".formatted(request.getGroup_id(), request.getUser_id(), request.getRaw_message()));
                var group = request.getGroup_id();
                if (group == 743486411L) {
                    if (request.getMessage()[0].getType().equals("text")) {
                        if (request.getRaw_message().startsWith("ts ")) {
                            String message = request.getRaw_message().replace("ts ", "");
                            if (TS3.api != null) {
                                TS3.api.sendServerMessage("[%s]: %s".formatted(request.getSender().getNickname(), message));
                            }
                        }
                    }
                }
                if (group == Account.mohistQQGGroup) {
                    for (var s : request.getMessage()) {
                        if (s.getType().equals("file")) {
                            GetFile getFile = QQ.get_file(BotType.MOHIST, s.getData().getFile_id());
                            //获取文件的下载链接
                            if (getFile != null) {
                                String file_name = getFile.getData().getFile_name();
                                if (file_name.endsWith(".log") || file_name.endsWith(".txt") || file_name.endsWith(".yml")) {
                                    String url = getFile.getData().getFile();
                                    String read = IOUtil.readContent(new FileInputStream(url));
                                    String ms = """
                                            文件名: %s
                                            已自动上传至： %s"""
                                            .formatted(file_name, HasteUtils.pasteMohist(read));
                                    QQ.sendToMohistGroup(ms);
                                }
                            }

                        }
                    }
                }
            }
        } else {
            // debug(event);
        }
    }

    public void debug(HttpPostEvent event) {
        StringBuilder sb = new StringBuilder().repeat("=", 20);
        Log.info();
        Log.info(sb + " <<HttpPostEvent>> " + sb);
        Log.info("RequestPath: %s ".formatted(event.getRequestPath()));
        Log.info(event.getJson().toString());
        Log.info();
    }
}
