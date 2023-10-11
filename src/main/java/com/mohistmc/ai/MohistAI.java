package com.mohistmc.ai;

import com.alibaba.dashscope.utils.Constants;
import com.mohistmc.ai.bots.discord.DiscordBot;
import com.mohistmc.ai.bots.gpt.OpenAI;
import com.mohistmc.ai.bots.qq.MiraiListener;
import com.mohistmc.ai.live.BiliBiliLive;
import com.mohistmc.ai.minecraft.VersionsCheck;
import com.mohistmc.ai.mysql.MySqlInit;
import com.mohistmc.ai.pfcraft.config.GameID;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.message.data.AtAll;
import net.mamoe.mirai.message.data.MessageUtils;
import net.mamoe.mirai.message.data.PlainText;

/**
 * @author Mgazul by MohistMC
 * @date 2023/7/14 15:06:30
 */
public class MohistAI extends JavaPlugin {
    public static final MohistAI INSTANCE = new MohistAI();
    public Bot QQ;

    private MohistAI() {
        super(new JvmPluginDescriptionBuilder("com.example.demo1", "0.1.0").name("Demo1").author("admin").build());
    }

    public static void sendMsgToGroup(Long g, String msg) {
        if (MohistAI.INSTANCE.QQ != null && MohistAI.INSTANCE.QQ.getGroup(g) != null) {
            MohistAI.INSTANCE.QQ.getGroup(g).sendMessage(msg);
        }
    }

    public static void sendAll(String msg) {
        for (Bot bot : Bot.getInstances()) {
            for (Group group : bot.getGroups()) {
                group.sendMessage(msg);
            }
        }
    }

    public static void sendAllAt(String msg) {
        for (Bot bot : Bot.getInstances()) {
            for (Group group : bot.getGroups()) {
                group.sendMessage(MessageUtils.newChain(new PlainText(msg), AtAll.INSTANCE));
            }
        }
    }

    @Override
    public void onEnable() {
        MohistConfig.init();
        GameID.init();
        DiscordBot.init();
        OpenAI.init();
        Constants.apiKey = MohistConfig.dashscope_apikey;
        GlobalEventChannel.INSTANCE.registerListenerHost(new MiraiListener());
        getLogger().info("Plugin loaded!");
        BiliBiliLive.INSTANCE.run();
        VersionsCheck.INSTANCE.run();
        connectMySql();
    }

    public void connectMySql() {
        String host = MohistConfig.config.getString("mysql.host");
        String user = MohistConfig.config.getString("mysql.username");
        String database = MohistConfig.config.getString("mysql.database");
        String password = MohistConfig.config.getString("mysql.password");
        String port = MohistConfig.config.getString("mysql.port");
        MySqlInit.connect(host, user, database, password, port);
    }
}
