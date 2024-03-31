package com.mohistmc.ai.network.event;

import java.util.EventListener;

public interface BaseListener extends EventListener {
    void onEvent(HttpPostEvent e);
    void onEvent(HttpGetEvent e);
}
