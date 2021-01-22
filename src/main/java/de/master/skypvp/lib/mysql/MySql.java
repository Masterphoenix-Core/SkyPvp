/*
 * Copyright (c) 2020. All rights preserved.
 * Creator Masterphoenix
 * Contact: Discord: Masterphoenix#8969
 */

package de.master.skypvp.lib.mysql;

import de.master.skypvp.lib.CoreLib;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.sql.*;

public class MySql {
    
    @Getter
    private boolean isConnected = false;
    
    private final String mySqlPrefix = CoreLib.prefix + "§8[§b§lMySQL§8] ";
    
    private String host = "localhost", database = "skypvp", user = "root", password = "";
    private Connection con;
    
    @Getter
    private SqlStats sqlStats;
    
    public MySql() {
        connect();
        sqlStats = new SqlStats(this);
    }
    
    public void connect() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://" + host + ":3306/" + database + "?autoReconnect=true", user, password);
            Bukkit.getConsoleSender().sendMessage(mySqlPrefix + "§aVerbindung hergestellt!");
            
            isConnected = true;
            
        } catch (SQLException throwables) {
            Bukkit.getConsoleSender().sendMessage(mySqlPrefix+ "§cVerbindung konnte nicht hergestellt werden!");
            Bukkit.getConsoleSender().sendMessage(mySqlPrefix + "§cError:" + throwables.getMessage());
        }
    }
    
    public void disconnect() {
        try {
            if (con != null) {
                con.close();
                Bukkit.getConsoleSender().sendMessage(mySqlPrefix + "§cVerbindung unterbrochen!");
                
                isConnected = false;
            }
        } catch (SQLException throwables) {
            Bukkit.getConsoleSender().sendMessage(mySqlPrefix + "§cVerbindung konnte nicht unterbrochen werden!");
            Bukkit.getConsoleSender().sendMessage(mySqlPrefix + "§cError:" + throwables.getMessage());
        }
    }
    
    public void update(String qry) {
        try {
            Statement st = con.createStatement();
            st.executeUpdate(qry);
            st.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    
    public ResultSet query(String qry) {
        ResultSet rs = null;
        try {
            Statement st = con.createStatement();
            rs = st.executeQuery(qry);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return rs;
    }
}
