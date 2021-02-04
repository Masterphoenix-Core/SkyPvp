package de.master.skypvp.lib.mysql;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class MySqlConfiguration {
    
    private final File file;
    private final YamlConfiguration config;
    
    public MySqlConfiguration() {
    
        file = new File("plugins/SkyPvP", "mysql.yml");
        
        config = YamlConfiguration.loadConfiguration(file);
        
        addDefaults();
    }
    
    private void addDefaults() {
        config.addDefault("mysql.host", "localhost");
        config.addDefault("mysql.port", "3306");
        config.addDefault("mysql.database", "skypvp");
        config.addDefault("mysql.user", "skypvp");
        config.addDefault("mysql.password", "password");
    
        config.options().copyDefaults(true);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public String getHost() {
        return config.getString("mysql.host");
    }
    public int getPort() {
        return config.getInt("mysql.port");
    }
    public String getDatabase() {
        return config.getString("mysql.database");
    }
    public String getUser() {
        return config.getString("mysql.user");
    }
    public String getPassword() {
        return config.getString("mysql.password");
    }
    
}