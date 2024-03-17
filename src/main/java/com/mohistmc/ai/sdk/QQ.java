package com.mohistmc.ai.sdk;

import com.mohistmc.ai.Account;
import com.mohistmc.ai.MohistAI;
import com.mohistmc.ai.MohistConfig;
import com.mohistmc.ai.network.HttpRequestUtils;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Objects;
import mjson.Json;

public class QQ {

    public static final boolean debug = MohistConfig.qq_request_debug;

    public static void sendToMohistGroup(String message) {
        send_group_msg(BotType.MOHIST, Account.mohistQQGGroup, message);
    }

    public static void sendToFishGroup(String message) {
        send_group_msg(BotType.FISH, Account.上线了, message);
    }

    public static void send_group_msg(BotType botType, String group_id, String message) {
        debug(message);
        try {
            HashMap<String, Object> param = new HashMap<>();
            param.put("group_id", group_id);
            param.put("message", message);
            var string = HttpRequestUtils.post(botType, "/send_group_msg", param)
                    .thenApplyAsync(HttpResponse::body)
                    .get();

            var json = Json.read(string);
            if (Objects.equals(json.asString("status"), "failed")) {
                debug("发送失败");
            } else {
                debug("发送成功");
            }
            debug("返回数据: " + json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void debug(String debug_message){
        if (debug) MohistAI.LOGGER.debug(debug_message);
    }
}
