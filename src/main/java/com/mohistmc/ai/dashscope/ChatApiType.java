package com.mohistmc.ai.dashscope;

/**
 * @author Mgazul by MohistMC
 * @date 2023/9/16 4:09:06
 */
public enum ChatApiType {

    BAIDU("Mohist思维框架"),
    ALIBABA("通义千问");

    String name;

    ChatApiType(String name) {
        this.name = name;
    }

    public String asName() {
        return name;
    }

}
