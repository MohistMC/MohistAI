package com.mohistmc.ai.live.entity;

import com.mohistmc.mjson.ToJson;
import lombok.Data;

@Data
public class RoomInfo {

    @ToJson
    private int live_status;
    @ToJson
    private String title;

    public boolean isLive() {
        return live_status == 1;
    }
}
