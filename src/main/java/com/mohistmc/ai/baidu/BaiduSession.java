package com.mohistmc.ai.baidu;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author Mgazul by MohistMC
 * @date 2023/11/15 0:30:03
 */
public class BaiduSession {

    private LinkedList<Map<String, String>> history = new LinkedList<>();
    private static HashMap<String, BaiduSession> sessions = new HashMap<>();

    public void addMessage(String role, String content) {
        HashMap<String, String> map = new HashMap<>();
        map.put("role", role);
        map.put("content", content);
        this.history.add(map);
    }

    public LinkedList<Map<String, String>> getHistory() {
        return this.history;
    }

    public void clear() {
        this.history.clear();
    }

    public void recall() {
        this.history.removeLast();
    }

    public static BaiduSession getSession(String name) {
        BaiduSession.sessions.putIfAbsent(name, new BaiduSession());
        return BaiduSession.sessions.get(name);
    }
}
