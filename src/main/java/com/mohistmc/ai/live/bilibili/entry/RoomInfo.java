package com.mohistmc.ai.live.bilibili.entry;

import lombok.Data;

@Data
public class RoomInfo {
    private long uid;
    private long room_id;
    private int short_id;
    private String title;
    private String cover;
    private String tags;
    private String background;
    private String description;
    private int live_status;
    private int live_start_time;
    private int live_screen_type;
    private int lock_status;
    private int lock_time;
    private int hidden_status;
    private int hidden_time;
    private int area_id;
    private int online;
}