package de.master.skypvp.lib.location;

import de.master.skypvp.core.bootstrap.SkyPvp;
import de.master.skypvp.lib.CoreLib;
import de.master.skypvp.lib.Storage;
import de.master.skypvp.lib.mysql.SqlStats;
import de.master.skypvp.lib.util.ItemBuilder;
import lombok.SneakyThrows;
import org.bukkit.*;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class LocationConfiguration {
    
    private File file;
    private YamlConfiguration config;
    
    public LocationConfiguration(Storage storage) {
        file = new File("plugins/SkyPvP", "locations.yml");
        config = YamlConfiguration.loadConfiguration(file);
    
        loadTopSigns(storage);
        loadItemSigns(storage);
    }
    
    public void addLocation(String name, Location loc) {
        config.set("location." + name + ".World", loc.getWorld().getName());
        config.set("location." + name + ".X", loc.getX());
        config.set("location." + name + ".Y", loc.getY());
        config.set("location." + name + ".Z", loc.getZ());
        config.set("location." + name + ".Yaw", loc.getYaw());
        config.set("location." + name + ".Pitch", loc.getPitch());
    
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public Location getLocation(String name) {
        return new Location(Bukkit.getWorld(config.getString("location." + name + ".World")), config.getDouble("location." + name + ".X"), config.getDouble("location." + name + ".Y"), config.getDouble("location." + name + ".Z"), Float.parseFloat(config.getString("location." + name + ".Yaw")), Float.parseFloat(config.getString("location." + name + ".Pitch")));
    }
    
    public void addTopSign(String name, Sign loc) {
        config.set("sign." + name + ".World", loc.getWorld().getName());
        config.set("sign." + name + ".X", loc.getX());
        config.set("sign." + name + ".Y", loc.getY());
        config.set("sign." + name + ".Z", loc.getZ());
    
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void loadTopSigns(Storage storage) {
    
        if (config.getConfigurationSection("sign") != null) {
            for (String signString : config.getConfigurationSection("sign").getKeys(false)) {
        
                String section = "sign." + signString + ".";
                Location loc = new Location(Bukkit.getWorld(config.getString(section + "World")), config.getDouble(section + "X"), config.getDouble(section + "Y"), config.getDouble(section + "Z"));
                if (loc.getBlock().getType().equals(Material.SIGN) || loc.getBlock().getType().equals(Material.SIGN_POST) || loc.getBlock().getType().equals(Material.WALL_SIGN)) {
                    storage.topSigns.add((Sign) loc.getBlock().getState());
                }
            }
        }
        
    }
    
    public Sign getTopSign(String name) {
        String section = "sign." + name + ".";
        Location location = new Location(Bukkit.getWorld(config.getString(section + "World")), config.getDouble(section + "X"), config.getDouble(section + "Y"), config.getDouble(section + "Z"));
    
        if (location.getBlock().getType().equals(Material.SIGN) || location.getBlock().getType().equals(Material.SIGN_POST) || location.getBlock().getType().equals(Material.WALL_SIGN)) {
            return (Sign) location.getBlock().getState();
        }
        return null;
    }
    
    public void reloadSigns() {
        
        SqlStats sqlStats = JavaPlugin.getPlugin(SkyPvp.class).getMySql().getSqlStats();
    
        for (int i = 1; i < 4; i++) {
    
            Sign sign = getTopSign("Top" + i);
    
            if (sqlStats.getTopPlayer(i) == null) {
                sign.setLine(1, "Noch keine Daten...");
                sign.setLine(2, "");
                sign.setLine(3, "");
                
            } else {
                int kills = sqlStats.getPlayerKills(sqlStats.getTopPlayer(i).getUniqueId().toString());
                int deaths = sqlStats.getPlayerDeaths(sqlStats.getTopPlayer(i).getUniqueId().toString());
                sign.setLine(1, "Kills: " + kills);
                sign.setLine(2, "Deaths: " + deaths);
                sign.setLine(3, "K/D: " + CoreLib.round((double) kills / deaths, 2));
                
            }
            sign.update();
        }
        
    }
    
    
    @SneakyThrows
    public void addItemSign(Location loc, ItemStack item) {
        
        int signCount = 0;
        
        if (config.getConfigurationSection("itemSign") != null) {
            signCount = config.getConfigurationSection("itemSign").getKeys(false).size();
        }
        
        config.set("itemSign." + signCount + ".World", loc.getWorld().getName());
        config.set("itemSign." + signCount + ".X", loc.getX());
        config.set("itemSign." + signCount + ".Y", loc.getY());
        config.set("itemSign." + signCount + ".Z", loc.getZ());
        config.set("itemSign." + signCount + ".item.material", item.getType().toString());
        if (item.getItemMeta().getDisplayName() != null) {
            config.set("itemSign." + signCount + ".item.name", item.getItemMeta().getDisplayName().replaceAll("§", "&"));
        } else
            config.set("itemSign." + signCount + ".item.name", item.getType().name());
    
        config.save(file);
    }
    
    public void loadItemSigns(Storage storage) {
    
        if (config.getConfigurationSection("itemSign") != null) {
            for (String signCount : config.getConfigurationSection("itemSign").getKeys(false)) {
        
                String section = "itemSign." + signCount + ".";
                Location loc = new Location(Bukkit.getWorld(config.getString(section + "World")), config.getDouble(section + "X"), config.getDouble(section + "Y"), config.getDouble(section + "Z"));
                storage.itemSign.put(loc, new ItemBuilder(Material.getMaterial(config.getString(section + "item.material"))).setName(config.getString(section + "item.name").replaceAll("&", "§")).build());
            }
        }
    
    }
    
}
