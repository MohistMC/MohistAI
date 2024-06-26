package com.mohistmc.ai.sdk.qq;

import com.mohistmc.ai.Account;
import com.mohistmc.ai.MohistConfig;
import com.mohistmc.ai.log.Log;
import com.mohistmc.ai.network.HttpRequestUtils;
import com.mohistmc.ai.sdk.BotType;
import com.mohistmc.ai.sdk.qq.entity.GetFile;
import com.mohistmc.ai.sdk.qq.entity.GroupList;
import com.mohistmc.ai.sdk.qq.entity.GroupList.Data;
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
        send_group_msg(BotType.MOHIST, String.valueOf(Account.mohistQQGGroup), message);
    }

    public static void sendToACGroup(String message) {
        send_group_msg(BotType.MOHIST, "432790401", message);
    }

    public static void sendToFishGroup(String message) {
        send_group_msg(BotType.FISH, Account.上线了, message);
    }

    public static void sendToFishAllGroup(String message) {
        get_group_list(BotType.FISH).forEach(groupId -> send_group_msg(BotType.FISH, String.valueOf(groupId), message));
    }

    @SneakyThrows
    public static void send_group_msg(BotType botType, String group_id, String message) {
        HashMap<String, String> param = new HashMap<>();
        param.put("group_id", group_id);
        param.put("message", message);
        var string = HttpRequestUtils.post(botType, "/send_group_msg", param).get();
        if (string == null) return;
        debug(message);
        var json = Json.read(string);
        if (Objects.equals(json.asString("status"), "failed")) {
            debug("发送失败");
            return;
        }
        debug("返回数据: " + json);
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

    @SneakyThrows
    public static GetFile get_file(BotType botType, String file_id) {
        HashMap<String, String> param = new HashMap<>();
        param.put("file_id", file_id);
        var string = HttpRequestUtils.post(botType, "/get_file", param).get();
        if (string == null) {
            return null;
        }
        var jsonRoot = Json.read(string);
        GetFile groupList = jsonRoot.asBean(GetFile.class);
        if (groupList.isFailed()) {
            debug("获取失败");
            return null;
        }
        debug(groupList.toString());
        return groupList;
    }

    public static void debug(String debug_message){
        if (debug) Log.info(debug_message);
    }
}
