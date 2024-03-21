package com.mohistmc.ai.network;

import java.util.HashMap;
import java.util.Map;

public enum RequestPath {
    DEFAULT("/"),
    QQ("/qq"),
    QQ_DEBUG("/qq_debug"),
    UNKNOWN(null);

    final String path;
    private static final Map<String, RequestPath> parse = new HashMap<>();

    static {
        for (RequestPath r : RequestPath.values()) {
            if (r.path != null) {
                parse.put(r.path, r);
            }
        }
    }

    RequestPath(String path) {
        this.path = path;
    }

    public static RequestPath as(String path) {
        return parse.getOrDefault(path, UNKNOWN);
    }

    public boolean isUnknown() {
        return this == UNKNOWN;
    }
}
