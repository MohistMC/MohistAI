package com.mohistmc.ai.sdk.qq.entity;

import com.mohistmc.mjson.ToJson;
import java.util.Objects;
import lombok.Data;

@Data
public class GetFile {

    @ToJson
    private String status;
    @ToJson
    private int retcode;
    @ToJson
    private Data data;
    @ToJson
    private String message;
    @ToJson
    private String wording;
    @ToJson
    private String echo;

    @lombok.Data
    public static class Data {
        @ToJson
        private String file;
        @ToJson
        private String file_size;
        @ToJson
        private String file_name;
    }

    public boolean isFailed() {
        return Objects.equals(status, "failed");
    }
}
