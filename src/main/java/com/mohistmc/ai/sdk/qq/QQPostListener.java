package com.mohistmc.ai.sdk.qq;

import com.mohistmc.ai.network.event.BaseListener;
import com.mohistmc.ai.network.event.HttpPostEvent;

public class QQPostListener implements BaseListener {
    public void onEvent(HttpPostEvent event) {
        System.out.println("HttpPostEvent: " + event.getJson().toString());
    }
}
