package com.mohistmc.ai.api;

import com.mohistmc.ai.MohistAI;
import com.mohistmc.ai.MohistConfig;
import com.mohistmc.ai.network.HttpRequestUtils;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Objects;
import mjson.Json;

public class QQ {

    public static final String mohist = MohistConfig.QQ_REQUEST_API_MOHIST;

    public static void send_group_msg(String group_id, String message) {
        MohistAI.LOGGER.info(message);
        try {
            HashMap<String, Object> param = new HashMap<>();
            param.put("group_id", group_id);
            param.put("message", message);
            var string = HttpRequestUtils.post("/send_group_msg", param)
                    .thenApplyAsync(HttpResponse::body)
                    .get();

            var json = Json.read(string);
            if (Objects.equals(json.asString("status"), "failed")) {
                MohistAI.LOGGER.info("发送失败");
            } else {
                MohistAI.LOGGER.info("发送成功");
            }
            MohistAI.LOGGER.info("返回数据: " + json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
