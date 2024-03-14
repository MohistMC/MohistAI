package com.mohistmc.ai.bots.discord;

import com.mohistmc.ai.MohistAI;
import com.mohistmc.ai.MohistConfig;
import java.net.InetSocketAddress;
import java.net.Proxy;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.intent.Intent;
import retrofit2.http.HEAD;

/**
 * @author Mgazul by MohistMC
 * @date 2023/7/14 15:07:41
 */
public class DiscordBot {

    public static void init() {
        if (!MohistConfig.discord) return;
        DiscordApiBuilder builder = new DiscordApiBuilder();
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(MohistConfig.discord_proxy_address, MohistConfig.discord_proxy_port));
        if (MohistConfig.discord_proxy_enable) builder.setProxy(proxy);
        builder.addIntents(Intent.MESSAGE_CONTENT);
        builder.addMessageCreateListener(MohistGitHub::onMessage);
        System.out.println("尝试登入Discord...");
        discord = builder.setToken(MohistConfig.discord_token).login().join();
        System.out.println("Discord 登录成功");
    }
}
