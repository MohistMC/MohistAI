package com.mohistmc.ai.utils;

import java.util.concurrent.ThreadFactory;
import org.jetbrains.annotations.NotNull;

/**
 * @Author Mgazul
 * @create 2019/9/11 20:57
 */
public class NamedThreadFactory implements ThreadFactory {
    private static int id = 0;
    private final String name;

    public NamedThreadFactory(String name) {
        this.name = name;
    }

    @Override
    public Thread newThread(@NotNull Runnable r) {
        return Thread.ofVirtual().name(name + " - " + (++id)).unstarted(r);
    }
}
