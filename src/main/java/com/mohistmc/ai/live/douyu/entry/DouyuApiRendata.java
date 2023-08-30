package com.mohistmc.ai.live.douyu.entry;

import lombok.Data;

@Data
public class DouyuApiRendata {

    private String media_type;
    private DouyuApiData data;
    private String link;
    private long time;

}