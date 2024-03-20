package com.mohistmc.ai.sdk;

import java.util.List;
import lombok.Data;

@Data
public class MessageRequest {
    private long self_id;
    private long user_id;
    private long time;
    private long message_id;
    private long real_id;
    private String message_type;
    private Sender sender;
    private String raw_message;
    private long font;
    private String sub_type;
    private List<Message> message;
    private String message_format;
    private String post_type;
    private long group_id;

    @lombok.Data
    public static class Message {
        private Data data;
        private  String type;
    }

    @lombok.Data
    public static class Data {
        private String text;
        private String id;
        private String file;
        private String url;
        private String file_size;
    }

    @lombok.Data
    public static class Sender {
        private long user_id;
        private String nickname;
        private String card;
        private String role;
    }
}


