package com.mohistmc.ai;

import com.alibaba.dashscope.utils.Constants;
import com.mohistmc.ai.bots.discord.DiscordBot;
import com.mohistmc.ai.bots.gpt.OpenAI;
import com.mohistmc.ai.bots.qq.MiraiListener;
import com.mohistmc.ai.live.bilibili.BiliBiliLive;
import com.mohistmc.ai.live.huya.HuyaLive;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.message.data.MessageContent;

import java.util.List;

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
        MohistConfig.init();
        DiscordBot.init();
        OpenAI.init();
        Constants.apiKey = MohistConfig.dashscope_apikey;
        GlobalEventChannel.INSTANCE.registerListenerHost(new MiraiListener());
        getLogger().info("Plugin loaded!");
        new BiliBiliLive().run();
        new HuyaLive().run();
    }

    public static void sendMsgToGroup(Long g, String msg) {
        if (MohistAI.INSTANCE.QQ != null && MohistAI.INSTANCE.QQ.getGroup(g) != null) {
            MohistAI.INSTANCE.QQ.getGroup(g).sendMessage(msg);
        }
    }

    public static void sendMsgToFish(String msg) {
        for (Bot bot : Bot.getInstances()) {
            for (Group group : bot.getGroups()) {
                group.sendMessage(msg);
            }
        }
    }
}
