package com.mohistmc.ai.sdk.qq.grouplist;

import com.mohistmc.json4bean.JSON;
import java.util.Objects;
import lombok.Data;

@Data
public class GroupList {

    @JSON
    private String status;
    @JSON
    private int retcode;
    @JSON
    private Data[] data;
    @JSON
    private String message;
    @JSON
    private String wording;
    @JSON
    private String echo;

    @lombok.Data
    public static class Data {
        @JSON
        private int group_id;
        @JSON
        private String group_name;
        @JSON
        private int member_count;
        @JSON
        private int max_member_count;
    }

    public boolean isFailed() {
        return Objects.equals(status, "failed");
    }

}
