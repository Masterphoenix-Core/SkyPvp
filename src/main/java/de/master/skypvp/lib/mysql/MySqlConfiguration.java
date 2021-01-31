package de.master.skypvp.lib.mysql;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class MySqlConfiguration {
    
    private File file;
    private YamlConfiguration config;
    
    public MySqlConfiguration() {
    
        file = new File("plugins/skypvp", "mysql.yml");
        config = YamlConfiguration.loadConfiguration(file);
        
        addDefaults();
    }
    
    private void addDefaults() {
        config.addDefault("mysql.host", "localhost");
        config.addDefault("mysql.database", "skypvp");
        config.addDefault("mysql.user", "skypvp");
        config.addDefault("mysql.password", "password");
    
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public String getHost() {
        return config.getString("mysql.host");
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
