package com.mohistmc.ai.dashscope;

import com.mohistmc.ai.MohistConfig;

/**
 * @author Mgazul by MohistMC
 * @date 2023/9/16 4:07:36
 */
public class ChatAPI {

    public static String send(String message, ChatApiType chatApiType) {
        if (chatApiType == ChatApiType.BAIDU) {
            return Baidu.main(message);
        } else if (chatApiType == ChatApiType.ALIBABA) {
            return QianWen.sendText(message);
        }
        return message;
    }

    public static String send(String message) {
        ChatApiType DEFAULT = MohistConfig.ai_type;
        System.out.println(DEFAULT);
        if (DEFAULT == ChatApiType.BAIDU) {
            return Baidu.main(message);
        } else if (DEFAULT == ChatApiType.ALIBABA) {
            return QianWen.sendText(message);
        }
        return message;
    }
}
