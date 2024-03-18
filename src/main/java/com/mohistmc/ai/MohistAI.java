package com.mohistmc.ai;

import com.alibaba.dashscope.utils.Constants;
import com.mohistmc.ai.bots.discord.DiscordBot;
import com.mohistmc.ai.bots.mcbots.Main;
import com.mohistmc.ai.live.BiliBiliLive;
import com.mohistmc.ai.live.HuyaLive;
import com.mohistmc.ai.minecraft.VersionsCheck;
import com.mohistmc.ai.mysql.MySqlInit;
import com.mohistmc.ai.sdk.BotType;
import com.mohistmc.ai.sdk.qq.QQ;
import com.mohistmc.ai.teamspeak3.TS3;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import retrofit2.http.HEAD;

/**
 * @author Mgazul by MohistMC
 * @date 2023/7/14 15:06:30
 */
public class MohistAI {
    public static MohistAI INSTANCE = new MohistAI();

    public void onEnable() {

    public static Logger LOGGER = LogManager.getLogger();

    @PostConstruct
    public void init() {
        Thread.ofVirtual().name("MohistAI").start(this::onEnable);
    }

    public static MohistAI INSTANCE = new MohistAI();

    @SneakyThrows
    public void onEnable() {
        LOGGER.info("初始化后端");
        MohistConfig.init();
        DiscordBot.init();
        Constants.apiKey = MohistConfig.dashscope_apikey;
        BiliBiliLive.INSTANCE.run();
        HuyaLive.INSTANCE.run();
        VersionsCheck.INSTANCE.run();
        TS3.init();
        LOGGER.info("初始化后端完毕");
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
