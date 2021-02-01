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
import java.util.concurrent.atomic.AtomicReference;

public class MySql {
    
    @Getter
    private boolean isConnected = false;
    
    private final String mySqlPrefix = CoreLib.prefix + "§8[§b§lMySQL§8] ";
    
    private Connection con;
    
    @Getter
    private final SqlStats sqlStats;
    
    private final MySqlConfiguration mySqlConfig;
    
    public MySql() {
        mySqlConfig = new MySqlConfiguration();
        sqlStats = new SqlStats(this);
    }
    
    Thread mySqlThread;
    
    public Thread connect() {
        
        //con = DriverManager.getConnection("jdbc:mysql://" + mySqlConfig.getHost() + ":3306/" + mySqlConfig.getDatabase(), mySqlConfig.getUser(), mySqlConfig.getPassword());
        mySqlThread = new Thread(() -> {
            try {
                con = DriverManager.getConnection("jdbc:mysql://" + mySqlConfig.getHost() + ":3306/" + mySqlConfig.getDatabase() + "?autoReconnect=true", mySqlConfig.getUser(), mySqlConfig.getPassword());
                //con = DriverManager.getConnection("jdbc:mysql://" + mySqlConfig.getHost() + ":3306/" + mySqlConfig.getDatabase(), mySqlConfig.getUser(), mySqlConfig.getPassword());
                Bukkit.getConsoleSender().sendMessage(mySqlPrefix + "§aVerbindung hergestellt!");
                
                isConnected = true;
                
            } catch (SQLException throwables) {
                Bukkit.getConsoleSender().sendMessage(mySqlPrefix + "§cVerbindung konnte nicht hergestellt werden!");
                Bukkit.getConsoleSender().sendMessage(mySqlPrefix + "§cError:" + throwables.getMessage());
            }
        });
        
        mySqlThread.start();
        return mySqlThread;
    }
    
/*
    public void disconnect() {
    
        try {
            connect().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    
        try {
            if (con != null) {
                con.close();
                Bukkit.getConsoleSender().sendMessage(mySqlPrefix + "§cVerbindung unterbrochen!");
                
                mySqlThread.stop();
                
                isConnected = false;
            }
        } catch (SQLException throwables) {
            Bukkit.getConsoleSender().sendMessage(mySqlPrefix + "§cVerbindung konnte nicht unterbrochen werden!");
            Bukkit.getConsoleSender().sendMessage(mySqlPrefix + "§cError:" + throwables.getMessage());
        }
    }
 */
    
    public void update(String qry) {
        
        if (con != null) {
    
            try {
                mySqlThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    
            try {
                Statement st = con.createStatement();
                st.executeUpdate(qry);
                st.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else {
    
            System.err.println("Connection == null");
            
            try {
                connect().join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            update(qry);
        }
    }
    
    public ResultSet query(String qry) {
        
        if (con != null) {
    
            try {
                mySqlThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            try {
                Statement st = con.createStatement();
                return st.executeQuery(qry);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            
            
        } else {
    
            System.err.println("Connection == null");
    
            try {
                connect().join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    
            query(qry);
        }
        
        return null;
        
    }
}
