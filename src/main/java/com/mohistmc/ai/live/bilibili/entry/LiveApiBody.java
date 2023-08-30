package com.mohistmc.ai.live.bilibili.entry;

import lombok.Data;

@Data
public class LiveApiBody {
    private String code;
    private String message;
    private LiveApiData data;
}
