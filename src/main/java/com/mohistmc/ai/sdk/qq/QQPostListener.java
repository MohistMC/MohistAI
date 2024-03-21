package com.mohistmc.ai.sdk.qq;

import com.mohistmc.ai.log.Log;
import com.mohistmc.ai.network.event.BaseListener;
import com.mohistmc.ai.network.event.HttpPostEvent;
import com.mohistmc.ai.sdk.MessageRequest;
import com.mohistmc.ai.teamspeak3.TS3;
import com.mohistmc.mjson.Json;

public class QQPostListener implements BaseListener {
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
                if (request.getGroup_id() == 743486411L) {
                    if (request.getMessage()[0].getType().equals("text")) {
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
            debug(event);
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
