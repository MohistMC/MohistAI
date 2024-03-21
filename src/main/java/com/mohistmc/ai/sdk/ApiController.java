package com.mohistmc.ai.sdk;

import com.mohistmc.ai.log.Log;
import com.mohistmc.ai.teamspeak3.TS3;
import lombok.SneakyThrows;

public class ApiController {

    public MessageRequest qq(MessageRequest request) {
        String t = request.getMessage_type();
        if (t == null) {
            System.out.print("==================================================\n");
            System.out.printf(request.toString());
            System.out.print("\n==================================================\n");
        }
        if (t != null && t.equals("group")) {
            Log.info("[群消息] 群号<%s> 发言者<%s>: %s".formatted(
                    request.getGroup_id(),
                    request.getUser_id(),
                    request.getRaw_message()));
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
        return request;
    }

    /**
     * 用于DEBUG调试
     */
    @SneakyThrows
    public String qq_string(String request) {
        System.out.printf(request);
        return request;
    }
}
