package com.mohistmc.ai;

import com.alibaba.dashscope.utils.Constants;
import com.mohistmc.ai.bots.discord.DiscordBot;
import com.mohistmc.ai.bots.gpt.OpenAI;
import com.mohistmc.ai.live.BiliBiliLive;
import com.mohistmc.ai.minecraft.VersionsCheck;
import com.mohistmc.ai.mysql.MySqlInit;
import com.mohistmc.ai.pfcraft.config.GameID;
/**
 * @author Mgazul by MohistMC
 * @date 2023/7/14 15:06:30
 */
public class MohistAI {
    public static MohistAI INSTANCE = new MohistAI();

    public void onEnable() {
        MohistConfig.init();
        GameID.init();
        DiscordBot.init();
        OpenAI.init();
        Constants.apiKey = MohistConfig.dashscope_apikey;
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
