package com.mohistmc.ai;

import com.google.common.base.Throwables;
import com.mohistmc.yaml.InvalidConfigurationException;
import com.mohistmc.yaml.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MohistConfig {

    private static final List<String> HEADER = Arrays.asList("""
            This is the main configuration file for MohistAI.
            https://wiki.mohistmc.com/

            Discord: https://discord.gg/mohistmc
            Forums: https://mohistmc.com/
            Forums (CN): https://mohistmc.cn/
                        
            """.split("\\n"));
    /*========================================================================*/
    public static YamlConfiguration config;
    static int version;
    private static File CONFIG_FILE = new File("mohist-config", "mohist.yml");

    public static void init() {
        config = new YamlConfiguration();
        try {
            config.load(CONFIG_FILE);
        } catch (IOException | InvalidConfigurationException ex) {
           System.out.println("Could not load mohist.yml, please correct your syntax errors");
            Throwables.throwIfUnchecked(ex);
        }

        config.options().setHeader(HEADER);
        config.options().copyDefaults(true);

        version = getInt("config-version", 1);
        set("config-version", 1);
        readConfig();
    }

    public static void save() {
        readConfig();
    }

    static void readConfig() {
        for (Method method : MohistConfig.class.getDeclaredMethods()) {
            if (Modifier.isPrivate(method.getModifiers())) {
                if (method.getParameterTypes().length == 0 && method.getReturnType() == Void.TYPE) {
                    try {
                        method.setAccessible(true);
                        method.invoke(null);
                    } catch (InvocationTargetException ex) {
                        Throwables.throwIfUnchecked(ex.getCause());
                    } catch (Exception ex) {
                        System.out.println("Error invoking " + method);
                    }
                }
            }
        }

        try {
            config.save(CONFIG_FILE);
        } catch (IOException ex) {
            System.out.println("Could not save " + CONFIG_FILE);
        }
    }

    public static void set(String path, Object val) {
        config.set(path, val);
        save();
    }

    private static boolean getBoolean(String path, boolean def) {
        config.addDefault(path, def);
        return config.getBoolean(path, config.getBoolean(path));
    }

    private static int getInt(String path, int def) {
        config.addDefault(path, def);
        return config.getInt(path, config.getInt(path));
    }

    private static <T> List getList(String path, T def) {
        config.addDefault(path, def);
        return config.getList(path, config.getList(path));
    }

    private static String getString(String path, String def) {
        config.addDefault(path, def);
        return config.getString(path, config.getString(path));
    }

    private static double getDouble(String path, double def) {
        config.addDefault(path, def);
        return config.getDouble(path, config.getDouble(path));
    }

    public static boolean chatgpt;
    public static String chatgpt_api_key;

    public static boolean discord;
    public static String discord_token;
    public static String discord_proxy_address;
    public static int discord_proxy_port;
    public static boolean live_bilibili;
    public static boolean live_huya;
    public static boolean live_bilibili_pushqq;
    public static boolean live_huya_pushqq;

    public static List<String> fishQQG;

    public static String dashscope_apikey;

    private static void mohist() {
        chatgpt = getBoolean("chatgpt.enable", false);
        chatgpt_api_key = getString("chatgpt.api_key", "");
        discord = getBoolean("discord.enable", false);
        discord_token = getString("discord.token", "");
        discord_proxy_address = getString("discord.proxy.address", "127.0.0.1");
        discord_proxy_port = getInt("discord.proxy.port", 7890);
        live_bilibili = getBoolean("live.bilibili.enable", true);
        live_bilibili_pushqq = getBoolean("live.bilibili.pushqq", false);
        live_huya = getBoolean("live.huya.enable", true);
        live_huya_pushqq = getBoolean("live.huya.pushqq", false);
        fishQQG = getList("fish.qqg", new ArrayList<>());
        dashscope_apikey = getString("dashscope.apikey", "");
    }
}