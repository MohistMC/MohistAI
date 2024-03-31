package com.mohistmc.ai;

import com.alibaba.dashscope.utils.Constants;
import com.mohistmc.ai.bots.discord.DiscordBot;
import com.mohistmc.ai.live.BiliBiliLive;
import com.mohistmc.ai.live.HuyaLive;
import com.mohistmc.ai.log.Log;
import com.mohistmc.ai.minecraft.VersionsCheck;
import com.mohistmc.ai.minecraft.mohistchain.MohistChinaAPI;
import com.mohistmc.ai.mysql.MySqlInit;
import com.mohistmc.ai.network.ApiController;
import com.mohistmc.ai.teamspeak3.TS3;
import com.mohistmc.tools.JavaVersion;
import lombok.SneakyThrows;
/**
 * @author Mgazul by MohistMC
 * @date 2023/9/10 23:44:25
 */
public class MohistAI {
    public static MohistAI INSTANCE = new MohistAI();

    public void onEnable() {

    public static void main(String[] args) {
        onEnable();
    }

    @SneakyThrows
    public static void onEnable() {
        Log.info("初始化后端");
        Log.info("Java: %s %s".formatted(JavaVersion.as(), JavaVersion.asClass()));
        MohistConfig.init();
        DiscordBot.init();
        Constants.apiKey = MohistConfig.dashscope_apikey;
        BiliBiliLive.INSTANCE.run();
        HuyaLive.INSTANCE.run();
        VersionsCheck.INSTANCE.run();
        TS3.init();
        ApiController.init();
        MohistChinaAPI.init();
        Log.info("初始化后端完毕");
    }

    public void connectMySql() {
        String host = MohistConfig.mysql_host.asString();
        String user = MohistConfig.mysql_username.asString();
        String database = MohistConfig.mysql_database.asString();
        String password = MohistConfig.mysql_password.asString();
        String port = MohistConfig.mysql_port.asString();
        MySqlInit.connect(host, user, database, password, port);
    }
}
