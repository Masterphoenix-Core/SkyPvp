package de.master.skypvp.lib.location;

import de.master.skypvp.core.bootstrap.SkyPvp;
import de.master.skypvp.lib.Storage;
import lombok.SneakyThrows;
import org.bukkit.*;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class LocationConfiguration {
    
    private File file;
    private YamlConfiguration config;
    
    public LocationConfiguration(Storage storage) {
        file = new File("plugins/SkyPvp", "locations.yml");
        config = YamlConfiguration.loadConfiguration(file);
    
        loadSigns(storage);
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
    
    public void addSign(String name, Sign loc) {
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
    
    private void loadSigns(Storage storage) {
    
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
    
    public Sign getSign(String name) {
        String section = "sign." + name + ".";
        Location location = new Location(Bukkit.getWorld(config.getString(section + "World")), config.getDouble(section + "X"), config.getDouble(section + "Y"), config.getDouble(section + "Z"));
    
        if (location.getBlock().getType().equals(Material.SIGN) || location.getBlock().getType().equals(Material.SIGN_POST) || location.getBlock().getType().equals(Material.WALL_SIGN)) {
            return (Sign) location.getBlock().getState();
        }
        
        return null;
    }
    
    public Map<Sign, Integer> getSigns() {
        Map<Sign, Integer> signs = new HashMap<>();
    
        for (String signSection : config.getConfigurationSection("sign").getKeys(false)) {
    
            String section = "sign." + signSection + ".";
            Location loc = new Location(Bukkit.getWorld(config.getString(section + "World")), config.getDouble(section + "X"), config.getDouble(section + "Y"), config.getDouble(section + "Z"));
            if (loc.getBlock().getType().equals(Material.SIGN) || loc.getBlock().getType().equals(Material.SIGN_POST) || loc.getBlock().getType().equals(Material.WALL_SIGN)) {
                signs.put((Sign) loc.getBlock().getState(), Integer.valueOf(signSection));
            }
            
        }
        
        return signs;
        
    }
    
    private int signCount = 0;
    
    @SneakyThrows
    public void addItemSign(Location loc, ItemStack item) {
        
        
        config.set("itemSign." + signCount + ".World", loc.getWorld().getName());
        config.set("itemSign." + signCount + ".X", loc.getX());
        config.set("itemSign." + signCount + ".Y", loc.getY());
        config.set("itemSign." + signCount + ".Z", loc.getZ());
        config.set("itemSign." + signCount + ".itemStack", item.toString());
        
        signCount++;
    
        config.save(file);
    }
    
    public void loadItemSigns(Storage storage) {
    
        if (config.getConfigurationSection("itemSign") != null) {
            for (String signCount : config.getConfigurationSection("itemSign").getKeys(false)) {
        
                String section = "itemSign." + signCount + ".";
                Location loc = new Location(Bukkit.getWorld(config.getString(section + "World")), config.getDouble(section + "X"), config.getDouble(section + "Y"), config.getDouble(section + "Z"));
        
                storage.itemSign.put(loc, config.getItemStack(section + "itemStack"));
            }
        }
    
    }
    
}
