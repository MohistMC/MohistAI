package com.mohistmc.ai.utils;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadFactory;

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
        Thread thread = new Thread(r);
        thread.setName(name + " - " + (++id));
        thread.setPriority(4);
        return thread;
    }

    public static class AssignableThread extends Thread {
        public AssignableThread(Runnable run) {
            super(run);
        }

        public AssignableThread() {
            super();
        }
    }
}
