package com.mohistmc.ai.sdk.qq;

import com.mohistmc.ai.Account;
import com.mohistmc.ai.MohistConfig;
import com.mohistmc.ai.log.Log;
import com.mohistmc.ai.network.HttpRequestUtils;
import com.mohistmc.ai.sdk.BotType;
import com.mohistmc.ai.sdk.qq.grouplist.GroupList;
import com.mohistmc.ai.sdk.qq.grouplist.GroupList.Data;
import com.mohistmc.mjson.Json;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.SneakyThrows;

public class QQ {

    public static final boolean debug = MohistConfig.qq_request_debug.asBoolean();

    public static void sendToMohistGroup(String message) {
        send_group_msg(BotType.MOHIST, Account.mohistQQGGroup, message);
    }

    public static void sendToFishGroup(String message) {
        send_group_msg(BotType.FISH, Account.上线了, message);
    }

    public static void sendToFishAllGroup(String message) {
        for (Integer groupId : get_group_list(BotType.FISH)) {
            send_group_msg(BotType.FISH, String.valueOf(groupId), message);
        }
    }

    public static void send_group_msg(BotType botType, String group_id, String message) {
        debug(message);
        try {
            HashMap<String, Object> param = new HashMap<>();
            param.put("group_id", group_id);
            param.put("message", message);
            var string = HttpRequestUtils.post(botType, "/send_group_msg", param).get();

            var json = Json.read(string);
            if (Objects.equals(json.asString("status"), "failed")) {
                debug("发送失败");
                return;
            }
            debug("返回数据: " + json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    public static List<Integer> get_group_list(BotType botType) {
        List<Integer> groups = new ArrayList<>();
        var string = HttpRequestUtils.post(botType, "/get_group_list", Map.of()).get();
        if (string == null) {
            return groups;
        }
        var jsonRoot = Json.read(string);
        GroupList groupList = jsonRoot.asBean(GroupList.class);
        if (groupList.isFailed()) {
            debug("获取失败");
            return groups;
        }
        groups = Arrays.stream(groupList.getData()).map(Data::getGroup_id).collect(Collectors.toList());
        debug(groups.toString());
        return groups;
    }

    public static void debug(String debug_message){
        if (debug) Log.info(debug_message);
    }
}
