package com.mohistmc.ai.sdk.qq.grouplist;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Data {
    private int group_id;
    private String group_name;
    private int member_count;
    private int max_member_count;
}
