package com.mohistmc.ai.network.event;

import com.mohistmc.ai.network.ContentType;
import com.mohistmc.ai.network.RequestPath;
import java.io.File;
import java.util.EventObject;
import lombok.Getter;
import lombok.Setter;

@Getter
public class HttpGetEvent extends EventObject {

    @Setter
    private byte[] bytes;
    @Setter
    private ContentType contenttype = ContentType.JSON;
    @Setter
    private File file;
    private final RequestPath requestPath;
    private final String o;

    public HttpGetEvent(Object source, byte[] bytes, RequestPath requestPath, String o) {
        super(source);
        this.bytes = bytes;
        this.requestPath = requestPath;
        this.o = o;
    }
}
