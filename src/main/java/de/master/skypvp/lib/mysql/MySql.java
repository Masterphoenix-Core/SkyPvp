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
    boolean errored = false;
    
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
    
        if (!errored) {
            mySqlThread = new Thread(() -> {
                try {
                    try {
                        Class.forName("com.mysql.jdbc.Driver").newInstance();
                    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    con = DriverManager.getConnection("jdbc:mysql://" + mySqlConfig.getHost() + ":" + mySqlConfig.getPort() + "/" + mySqlConfig.getDatabase() + "?autoReconnect=true", mySqlConfig.getUser(), mySqlConfig.getPassword());
                    //con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3307/" + "smash" + "?autoReconnect=true", "root", "");
                    Bukkit.getConsoleSender().sendMessage(mySqlPrefix + "§aVerbindung hergestellt!");
            
                    isConnected = true;
            
                } catch (SQLException throwables) {
                    Bukkit.getConsoleSender().sendMessage(mySqlPrefix + "§cVerbindung konnte nicht hergestellt werden! Stoppe MySQL!");
                    Bukkit.getConsoleSender().sendMessage(mySqlPrefix + "§cFehler: ");
                    throwables.printStackTrace();
            
                    errored = true;
                }
            });
            
            mySqlThread.setName("MySqlThread");
            mySqlThread.start();
    
            return mySqlThread;
        } else {
            return null;
        }
    }
    

    public void disconnect() {
    
        if (mySqlThread != null && mySqlThread.isAlive()) {
            try {
                mySqlThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
        
        if (con != null) {
            try {
                Statement st = con.createStatement();
                st.executeUpdate(qry);
                st.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            
        } else {
            if (errored) {
                return;
            }
            try {
                if (mySqlThread != null && mySqlThread.isAlive()) {
                    mySqlThread.join();
                    
                } else {
                    System.out.println("Trying to connect MySql from update()");
                    connect().join();
                    
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    
            if (!errored) {
                update(qry);
            }
        }
    }
    
    public ResultSet query(String qry) {
        
        if (con != null) {
            try {
                Statement st = con.createStatement();
                return st.executeQuery(qry);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            
        } else {
            if (errored) {
                return null;
            }
            
            try {
                if (mySqlThread != null && mySqlThread.isAlive()) {
                    mySqlThread.join();
                } else {
                    connect().join();
                    System.out.println("Trying to connect MySql from query()");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    
            if (!errored) {
                query(qry);
            }
            
        }
        return null;
        
    }
}
