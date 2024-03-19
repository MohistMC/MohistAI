package com.mohistmc.ai.mysql;

import com.mohistmc.ai.log.Log;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlInit {
    public static Connection con;
    public static String host;
    public static String user;
    public static String database;
    public static String password;
    public static String port;

    public static void connect(String host, String user, String database, String password, String port) {
        MySqlInit.host = host;
        MySqlInit.user = user;
        MySqlInit.database = database;
        MySqlInit.password = password;
        MySqlInit.port = port;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            MySqlInit.con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, user, password);
            createTable();
            Log.info("[MohistAI] MySQL 连接建立!");
        } catch (SQLException e) {
            Log.info("[MohistAI] MySQL 连接失败!" + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void createTable() {
        try {
            con.prepareStatement("CREATE TABLE IF NOT EXISTS rpginsider (NAME VARCHAR(100), value TINYINT(1), admin TINYINT(1))").executeUpdate();
            con.prepareStatement("CREATE TABLE IF NOT EXISTS scinsider (NAME VARCHAR(100), value TINYINT(1), admin TINYINT(1))").executeUpdate();
        } catch (SQLException e) {
            Log.info("[MohistAI] MySQL 创建表失败!" + e.getMessage());
        }
    }

}
