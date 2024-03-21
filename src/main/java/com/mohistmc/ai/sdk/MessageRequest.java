package com.mohistmc.ai.sdk;

import com.mohistmc.mjson.ToJson;
import java.util.List;
import lombok.Data;

@Data
public class MessageRequest {
    @ToJson
    private long self_id;
    @ToJson
    private long user_id;
    @ToJson
    private long time;
    @ToJson
    private long message_id;
    @ToJson
    private long real_id;
    @ToJson
    private String message_type;
    @ToJson
    private Sender sender;
    @ToJson
    private String raw_message;
    @ToJson
    private long font;
    @ToJson
    private String sub_type;
    @ToJson
    private List<Message> message;
    @ToJson
    private String message_format;
    @ToJson
    private String post_type;
    @ToJson
    private long group_id;

    @lombok.Data
    public static class Message {
        @ToJson
        private Data data;
        @ToJson
        private String type;
    }

    @lombok.Data
    public static class Data {
        @ToJson
        private String text;
        @ToJson
        private String id;
        @ToJson
        private String file;
        @ToJson
        private String url;
        @ToJson
        private String file_size;
    }

    @lombok.Data
    public static class Sender {
        @ToJson
        private long user_id;
        @ToJson
        private String nickname;
        @ToJson
        private String card;
        @ToJson
        private String role;
    }
}


