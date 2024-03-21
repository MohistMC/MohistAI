package com.mohistmc.ai.network.event;

import com.mohistmc.mjson.Json;
import java.util.EventObject;
import lombok.Getter;

@Getter
public class HttpPostEvent extends EventObject {

    private final Json json;

    public HttpPostEvent(Object source, Json json) {
        super(source);
        this.json = json;
    }
}
