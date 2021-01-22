/*
 * Copyright (c) 2020. All rights preserved.
 * Creator Masterphoenix
 * Contact: Discord: Masterphoenix#8969
 */

package de.master.skypvp.lib.mysql;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SqlStats {
    
    private String tableName = "stats";
    
    private MySql mySQL;
    
    public SqlStats(MySql mySQL) {
        this.mySQL = mySQL;
        
        mySQL.update("CREATE TABLE IF NOT EXISTS " + tableName + " (UUID VARCHAR(36), KILLS INT, DEATHS INT);");
        mySQL.update("ALTER TABLE " + tableName + " ADD UNIQUE(UUID);");
    }
    
    public boolean playerExists(String uuid) {
        
        try {
            ResultSet rs = mySQL.query("SELECT * FROM " + tableName + " WHERE UUID= '" + uuid + "'");
            
            if (rs.next()) {
                return rs.getString("UUID") != null;
            }
            return false;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }
    
    public void createPlayer(String uuid) {
        if (!(playerExists(uuid))) {
            mySQL.update("INSERT INTO " + tableName + "(UUID, KILLS, DEATHS) VALUES('" + uuid + "', '0', '0');");
        }
    }
    
    public int getPlayerKills(String uuid) {
        int kills = 0;
        if (playerExists(uuid)) {
            
            try {
                ResultSet rs = mySQL.query("SELECT * FROM " + tableName + " WHERE UUID= '" + uuid + "'");
                if ((rs.next()) || rs.getInt("KILLS") == 0) {
                    kills = rs.getInt("KILLS");
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            
        } else {
            createPlayer(uuid);
            getPlayerKills(uuid);
        }
        
        return kills;
    }
    
    public int getPlayerDeaths(String uuid) {
        int deaths = 0;
        if (playerExists(uuid)) {
            
            try {
                ResultSet rs = mySQL.query("SELECT * FROM " + tableName + " WHERE UUID= '" + uuid + "'");
                if ((rs.next()) || rs.getInt("DEATHS") == 0) {
                    deaths = rs.getInt("DEATHS");
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            
        } else {
            createPlayer(uuid);
            getPlayerKills(uuid);
        }
        
        return deaths;
    }
    
    public OfflinePlayer getTopPlayer(int top) {
        ResultSet rs = mySQL.query("SELECT * FROM " + tableName + " ORDER BY KILLS DESC LIMIT " + (top - 1) + ",1");
        try {
            if (rs.next()) {
                return Bukkit.getOfflinePlayer(UUID.fromString(rs.getString("UUID")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
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