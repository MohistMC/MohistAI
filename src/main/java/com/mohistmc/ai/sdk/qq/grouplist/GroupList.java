package com.mohistmc.ai.sdk.qq.grouplist;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class GroupList {

    private String status;
    private int retcode;
    private List<Data> data;
    private String message;
    private String wording;
    private String echo;
}
