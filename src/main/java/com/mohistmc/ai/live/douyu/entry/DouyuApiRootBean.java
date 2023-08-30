package com.mohistmc.ai.live.douyu.entry;

import lombok.Data;

@Data
public class DouyuApiRootBean {

    private String type;
    private DouyuApiRendata Rendata;
    private String aio;
    private String info;
    private String state;

}