package com.mohistmc.ai.network.event;

import com.mohistmc.ai.network.RequestPath;
import com.mohistmc.mjson.Json;
import java.util.EventObject;
import lombok.Getter;

@Getter
public class HttpPostEvent extends EventObject {

    private final Json json;
    private final RequestPath requestPath;

    public HttpPostEvent(Object source, Json json, RequestPath requestPath) {
        super(source);
        this.json = json;
        this.requestPath = requestPath;
    }

    public boolean isQQ() {
        return requestPath == RequestPath.QQ;
    }
}
