package com.mohistmc.ai.dashscope;

import com.mohistmc.ai.MohistConfig;
import com.mohistmc.ai.baidu.Baidu;
import com.mohistmc.ai.baidu.BaiduSession;

/**
 * @author Mgazul by MohistMC
 * @date 2023/9/16 4:07:36
 */
public class ChatAPI {

    public static String send(Long id, String message, ChatApiType chatApiType) {
        if (chatApiType == ChatApiType.BAIDU) {
            BaiduSession session = BaiduSession.getSession(String.valueOf(id));
            return Baidu.aichat(session, message);
        } else if (chatApiType == ChatApiType.ALIBABA) {
            return QianWen.sendText(message);
        }
        return message;
    }

    public static String send(Long id, String message) {
        ChatApiType DEFAULT = MohistConfig.ai_type;
        if (DEFAULT == ChatApiType.BAIDU) {
            BaiduSession session = BaiduSession.getSession(String.valueOf(id));
            return Baidu.aichat(session, message);
        } else if (DEFAULT == ChatApiType.ALIBABA) {
            return QianWen.sendText(message);
        }
        return message;
    }
}
