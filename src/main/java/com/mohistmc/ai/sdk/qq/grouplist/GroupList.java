package com.mohistmc.ai.sdk.qq.grouplist;

import com.mohistmc.mjson.ToJson;
import java.util.Objects;
import lombok.Data;

@Data
public class GroupList {

    @ToJson
    private String status;
    @ToJson
    private int retcode;
    @ToJson
    private Data[] data;
    @ToJson
    private String message;
    @ToJson
    private String wording;
    @ToJson
    private String echo;

    @lombok.Data
    public static class Data {
        @ToJson
        private int group_id;
        @ToJson
        private String group_name;
        @ToJson
        private int member_count;
        @ToJson
        private int max_member_count;
    }

    public boolean isFailed() {
        return Objects.equals(status, "failed");
    }

}
