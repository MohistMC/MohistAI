package com.mohistmc.ai.pfcraft;

import com.mohistmc.ai.mysql.MySqlInit;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Mgazul by MohistMC
 * @date 2023/10/12 2:14:23
 */
public class RpgInsiderAPI {

    public static boolean get(String name) {
        try {
            PreparedStatement st = MySqlInit.con.prepareStatement("SELECT value FROM rpginsider WHERE NAME = ?");
            st.setString(1, name);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("value");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean admin(String name) {
        try {
            PreparedStatement st = MySqlInit.con.prepareStatement("SELECT admin FROM rpginsider WHERE NAME = ?");
            st.setString(1, name);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("admin");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void set(String name, boolean v, boolean isAdmin) {
        try {
            PreparedStatement st = MySqlInit.con.prepareStatement("INSERT INTO rpginsider (NAME,value,admin) VALUES (?,?,?)");
            st.setString(1, name);
            st.setBoolean(2, v);
            st.setBoolean(3, isAdmin);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
