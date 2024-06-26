package com.mohistmc.ai.bots.discord;

import com.mohistmc.ai.MohistConfig;
import com.mohistmc.ai.log.Log;
import java.net.InetSocketAddress;
import java.net.Proxy;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.intent.Intent;

/**
 * @author Mgazul by MohistMC
 * @date 2023/7/14 15:07:41
 */
public class DiscordBot {

    public static void init() {
        if (!MohistConfig.discord.asBoolean()) return;
        DiscordApiBuilder builder = new DiscordApiBuilder();
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(MohistConfig.discord_proxy_address.asString(), MohistConfig.discord_proxy_port.asInt()));
        if (MohistConfig.discord_proxy_enable.asBoolean()) builder.setProxy(proxy);
        builder.addIntents(Intent.MESSAGE_CONTENT);
        builder.addMessageCreateListener(DiscordListener::onMessage);

        Log.info("尝试登入Discord...");
        builder.setToken(MohistConfig.discord_token.asString()).login().join();
        Log.info("Discord 登录成功");
    }
}
