package com.mohistmc.ai;

import com.alibaba.dashscope.utils.Constants;
import com.mohistmc.ai.bots.discord.DiscordBot;
import com.mohistmc.ai.live.BiliBiliLive;
import com.mohistmc.ai.minecraft.VersionsCheck;
import com.mohistmc.ai.mysql.MySqlInit;
import com.mohistmc.ai.pfcraft.config.GameID;
import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
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

    public void onEnable() {
        LOGGER.info("初始化后端");
        //System.setProperty("https.proxyHost", "127.0.0.1");
        //System.setProperty("https.proxyPort", "7890");
        MohistConfig.init();
        GameID.init();
        DiscordBot.init();
        Constants.apiKey = MohistConfig.dashscope_apikey;
        BiliBiliLive.INSTANCE.run();
        VersionsCheck.INSTANCE.run();
        connectMySql();
        LOGGER.info("初始化后端完毕");
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
