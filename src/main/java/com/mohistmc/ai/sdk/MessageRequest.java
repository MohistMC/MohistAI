package com.mohistmc.ai.sdk;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
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
}

record Message(Data data, String type) {
}

record Data(String text, String id, String file, String url, String file_size) {
}

record Sender(long user_id, String nickname, String card, String role) {
}
