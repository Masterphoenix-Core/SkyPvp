/*
 * Copyright (c) 2020. All rights preserved.
 * Creator Masterphoenix
 * Contact: Discord: Masterphoenix#8969
 */

package de.master.skypvp.lib.mysql;

import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class SqlStats {
    
    private final String tableName = "stats";
    
    private final MySql mySQL;
    
    public SqlStats(MySql mySQL) {
        this.mySQL = mySQL;
    }
    
    @SneakyThrows
    public boolean playerExists(String uuid) {
        ResultSet rs = mySQL.query("SELECT * FROM " + tableName + " WHERE UUID= '" + uuid + "'");
        
        if (rs == null) {
            try {
                if (mySQL.mySqlThread != null && mySQL.mySqlThread.isAlive()) {
                    mySQL.mySqlThread.join();
                } else {
                    mySQL.connect().join();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            rs = mySQL.query("SELECT * FROM " + tableName + " WHERE UUID= '" + uuid + "'");
            
        }
        
        if (rs != null) {
            if (rs.next()) {
                return rs.getString("UUID") != null;
            }
        }
    
        return false;
    }
    
    public void createPlayer(String uuid) {
        if (!playerExists(uuid)) {
            mySQL.update("INSERT INTO " + tableName + "(UUID, KILLS, DEATHS) VALUES('" + uuid + "', '0', '0');");
        }
    }
    
    public int getPlayerKills(String uuid) {
        int[] kills = {0};
        
        Thread t = new Thread(() -> {
            if (playerExists(uuid)) {
                
                try {
                    ResultSet rs = mySQL.query("SELECT * FROM " + tableName + " WHERE UUID= '" + uuid + "'");
                    if ((rs.next()) || rs.getInt("KILLS") == 0) {
                        kills[0] = rs.getInt("KILLS");
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                
            } else {
                if (!mySQL.errored) {
                    createPlayer(uuid);
                    getPlayerKills(uuid);
                }
            }
        });
        
        t.setName("GetPlayerKillsThread");
        t.start();
        
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        return kills[0];
    }
    
    public int getPlayerDeaths(String uuid) {
        int[] deaths = {0};
        
        Thread t = new Thread(() -> {
            if (playerExists(uuid)) {
                
                try {
                    ResultSet rs = mySQL.query("SELECT * FROM " + tableName + " WHERE UUID= '" + uuid + "'");
                    if ((rs.next()) || rs.getInt("DEATHS") == 0) {
                        deaths[0] = rs.getInt("DEATHS");
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                
            } else {
                if (!mySQL.errored) {
                    createPlayer(uuid);
                    getPlayerDeaths(uuid);
                }
            }
            
        });
        
        t.setName("GetPlayerDeathsThread");
        t.start();
        
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        return deaths[0];
    }
    
    public OfflinePlayer getTopPlayer(int top) {
        OfflinePlayer[] offlinePlayer = {null};
        
        Thread t = new Thread(() -> {
            ResultSet rs = mySQL.query("SELECT * FROM " + tableName + " ORDER BY KILLS DESC LIMIT " + (top - 1) + ",1");
            try {
                if (rs != null) {
                    if (rs.next()) {
                        offlinePlayer[0] = Bukkit.getOfflinePlayer(UUID.fromString(rs.getString("UUID")));
                    }
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            
        });
        
        t.setName("GetTopPlayerThread");
        t.start();
        
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        return offlinePlayer[0];
    }
    
    public void setKills(String uuid, int kills) {
        
        if (playerExists(uuid)) {
            mySQL.update("UPDATE " + tableName + " SET KILLS= '" + kills + "' WHERE UUID= '" + uuid + "';");
        } else {
            createPlayer(uuid);
            setKills(uuid, kills);
        }
        
    }
    
    public void setDeaths(String uuid, int deaths) {
        
        if (playerExists(uuid)) {
            mySQL.update("UPDATE " + tableName + " SET DEATHS= '" + deaths + "' WHERE UUID= '" + uuid + "';");
        } else {
            createPlayer(uuid);
            setDeaths(uuid, deaths);
        }
    }
    
    public void addKills(String uuid, int kills) {
        if (playerExists(uuid)) {
            setKills(uuid, getPlayerKills(uuid) + kills);
        } else {
            createPlayer(uuid);
            addKills(uuid, kills);
        }
    }
    
    public void addDeaths(String uuid, int deaths) {
        if (playerExists(uuid)) {
            setDeaths(uuid, getPlayerDeaths(uuid) + deaths);
        } else {
            createPlayer(uuid);
            addDeaths(uuid, deaths);
        }
    }
    
    public void removeKills(String uuid, int kills) {
        if (playerExists(uuid)) {
            setKills(uuid, getPlayerKills(uuid) - kills);
        } else {
            createPlayer(uuid);
            removeKills(uuid, kills);
        }
    }
    
    public void removeDeaths(String uuid, int deaths) {
        if (playerExists(uuid)) {
            setDeaths(uuid, getPlayerDeaths(uuid) - deaths);
        } else {
            createPlayer(uuid);
            removeDeaths(uuid, deaths);
        }
    }
    
}