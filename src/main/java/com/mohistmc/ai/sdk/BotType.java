package com.mohistmc.ai.sdk;


import com.mohistmc.ai.MohistConfig;
import lombok.Getter;

@Getter
public enum BotType {
    MOHIST(MohistConfig.qq_request_api_mohist),
    FISH(MohistConfig.qq_request_api_fish);

    final String api;

    BotType(String qqRequestApiMohist) {
        this.api = qqRequestApiMohist;
    }
}
