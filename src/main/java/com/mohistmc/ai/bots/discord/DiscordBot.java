package com.mohistmc.ai.bots.discord;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.intent.Intent;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * @author Mgazul by MohistMC
 * @date 2023/7/14 15:07:41
 */
public class DiscordBot {

    private static DiscordApi discord;

    public static void init() {
        DiscordApiBuilder builder = new DiscordApiBuilder();
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
        builder.setProxy(proxy);
        builder.addIntents(Intent.MESSAGE_CONTENT);
        builder.addMessageCreateListener(event -> {
            MohistGitHub.onMessage(event);
        });

        System.out.println("尝试登入Discord...");
        discord = builder.setToken("").login().join();
        System.out.println("Discord 登录成功");
    }
}
