package com.mohistmc.ai;

import com.mohistmc.ai.log.Log;
import com.mohistmc.yml.Yaml;
import com.mohistmc.yml.YamlSection;
import java.util.List;
import lombok.SneakyThrows;

public class MohistConfig {

    public static Yaml yaml = new Yaml("mohist-config/mohist.yml");
    
    public static YamlSection chatgpt;
    public static YamlSection chatgpt_api_key;
    public static YamlSection discord;
    public static YamlSection discord_token;
    public static YamlSection discord_proxy_enable;
    public static YamlSection discord_proxy_address;
    public static YamlSection discord_proxy_port;

    public static YamlSection live_bilibili;
    public static YamlSection live_bilibili_pushqq;
    public static YamlSection live_bilibili_concerns;

    public static YamlSection live_huya;
    public static YamlSection live_huya_pushqq;
    // minecraft
    public static YamlSection minecraft_versionscheck;
    public static YamlSection minecraft_release;
    public static YamlSection minecraft_snapshot;

    public static YamlSection mysql_host;
    public static YamlSection mysql_username;
    public static YamlSection mysql_database;
    public static YamlSection mysql_password;
    public static YamlSection mysql_port;

    public static YamlSection qq_request_api_mohist;
    public static YamlSection qq_request_api_fish;
    public static YamlSection qq_request_debug;
    public static YamlSection http_server_port;

    public static void init() {
        try {
            yaml.load();
            mohist();
            yaml.save();
        } catch (Exception ex) {
            Log.info("mohist.yml初始化失败");
        }

        Log.info("配置文件初始化完毕");
    }

    @SneakyThrows
    public static void save() {
        yaml.save();
    }

    @SneakyThrows
    private static void mohist() {
        chatgpt = yaml.put("chatgpt", "enable").setDefValues(false);
        chatgpt_api_key = yaml.put("chatgpt", "api_key").setDefValues("");
        discord = yaml.put("discord", "enable").setDefValues(false);
        discord_token = yaml.put("discord", "token").setDefValues("");
        discord_proxy_enable = yaml.put("discord", "proxy", "enable").setDefValues(false);
        discord_proxy_address = yaml.put("discord", "proxy", "address").setDefValues("127.0.0.1");
        discord_proxy_port = yaml.put("discord", "proxy", "port").setDefValues(7890);

        live_bilibili = yaml.put("live", "bilibili", "enable").setDefValues(true);
        live_bilibili_pushqq = yaml.put("live", "bilibili", "pushqq").setDefValues(false);
        live_bilibili_concerns = yaml.put("live", "bilibili", "concerns").setDefValues(List.of("43087", "43087"));

        live_huya = yaml.put("live", "huya", "enable").setDefValues(true);
        live_huya_pushqq = yaml.put("live", "huya", "pushqq").setDefValues(false);

        minecraft_versionscheck = yaml.put("minecraft", "versions-check").setDefValues(false);
        minecraft_release = yaml.put("minecraft", "release").setDefValues("");
        minecraft_snapshot = yaml.put("minecraft", "snapshot").setDefValues("");

        qq_request_api_mohist = yaml.put("qq", "request", "api", "mohist").setDefValues("http://localhost:3000");
        qq_request_api_fish = yaml.put("qq", "request", "api", "fish").setDefValues("http://localhost:3000");
        qq_request_debug = yaml.put("qq", "request", "debug").setDefValues(true);

        mysql_host = yaml.put("mysql", "host").setDefValues("");
        mysql_username = yaml.put("mysql", "username").setDefValues("");
        mysql_database = yaml.put("mysql", "database").setDefValues("");
        mysql_password = yaml.put("mysql", "password").setDefValues("");
        mysql_port = yaml.put("mysql", "port").setDefValues("");

        http_server_port = yaml.put("http_server", "port").setDefValues("2024");
    }
}