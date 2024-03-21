package com.mohistmc.ai.sdk.qq;

import com.mohistmc.ai.log.Log;
import com.mohistmc.ai.network.event.BaseListener;
import com.mohistmc.ai.network.event.HttpPostEvent;
import com.mohistmc.ai.sdk.MessageRequest;
import com.mohistmc.ai.sdk.qq.grouplist.GroupList;
import com.mohistmc.ai.teamspeak3.TS3;

public class QQPostListener implements BaseListener {
    public void onEvent(HttpPostEvent event) {
        if (event.isQQ()) {
            MessageRequest request = event.getJson().asBean(MessageRequest.class);
            String t = request.getMessage_type();
            if (t == null) {
                Log.info(new StringBuilder().repeat("↓", 40).toString());
                Log.info(event.getJson().toString());
                Log.info(new StringBuilder().repeat("↑", 40).toString());
            }
            if (t != null && t.equals("group")) {
                Log.info("[群消息] 群号<%s> 发言者<%s>: %s".formatted(request.getGroup_id(), request.getUser_id(), request.getRaw_message()));
                if (request.getGroup_id() == 743486411L) {
                    if (request.getMessage().getFirst().getType().equals("text")) {
                        if (request.getRaw_message().startsWith("ts ")) {
                            String message = request.getRaw_message().replace("ts ", "");
                            if (TS3.api != null) {
                                TS3.api.sendServerMessage("[%s]: %s".formatted(request.getSender().getNickname(), message));
                            }
                        }
                    }
                }
            }
        } else {
            GroupList groupList = event.getJson().asBean(GroupList.class);
            Log.info("HttpPostEvent: %s -> %s".formatted(event.getRequestPath(), groupList.toString()));
        }
    }
}
