package com.mohistmc.ai.dashscope;

/**
 * @author Mgazul by MohistMC
 * @date 2023/9/16 4:09:06
 */
public enum ChatApiType {

    BAIDU("文心一言"),
    ALIBABA("通义千问");

    String name;

    ChatApiType(String name) {
        this.name = name;
    }

    public String asName() {
        return name;
    }

}
