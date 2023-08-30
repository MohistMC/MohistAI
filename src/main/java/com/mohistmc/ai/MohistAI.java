package com.mohistmc.ai;

import com.mohistmc.ai.bots.discord.DiscordBot;
import com.mohistmc.ai.bots.gpt.OpenAI;
import com.mohistmc.ai.bots.qq.MiraiListener;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.GlobalEventChannel;

/**
 * @author Mgazul by MohistMC
 * @date 2023/7/14 15:06:30
 */
public class MohistAI extends JavaPlugin {
    public static final MohistAI INSTANCE = new MohistAI();
    public Bot QQ ;

    private MohistAI() {
        super(new JvmPluginDescriptionBuilder("com.example.demo1", "0.1.0")
                .name("Demo1")
                .author("admin")

                .build());
    }

    @Override
    public void onEnable() {
        DiscordBot.init();
        OpenAI.init(null);
        GlobalEventChannel.INSTANCE.registerListenerHost(new MiraiListener());
        getLogger().info("Plugin loaded!");
    }
}
