package com.mohistmc.ai.sdk.qq.grouplist;

import com.mohistmc.json4bean.JSON;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
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

    @Setter
    @Getter
    @ToString
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
