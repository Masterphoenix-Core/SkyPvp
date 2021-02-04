package de.master.skypvp.lib.npc;

import de.master.skypvp.core.bootstrap.SkyPvp;
import lombok.SneakyThrows;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class NpcConfiguration {
    
    public boolean npcLoaded;
    
    private final YamlConfiguration config;
    private final File file;
    
    public NpcConfiguration() {
        npcLoaded = false;
        
        file = new File("plugins/SkyPvP", "npcs.yml");
        config = YamlConfiguration.loadConfiguration(file);
    }
    
    public void addNpc(Npc npc) {
        
        config.set("npc." + npc.getEntityId() + ".name", npc.getGameProfile().getName().replaceAll("§", "&"));
        config.set("npc." + npc.getEntityId() + ".World", npc.getLocation().getWorld().getName());
        config.set("npc." + npc.getEntityId() + ".X", npc.getLocation().getX());
        config.set("npc." + npc.getEntityId() + ".Y", npc.getLocation().getY());
        config.set("npc." + npc.getEntityId() + ".Z", npc.getLocation().getZ());
        config.set("npc." + npc.getEntityId() + ".Yaw", npc.getLocation().getYaw());
        config.set("npc." + npc.getEntityId() + ".Pitch", npc.getLocation().getPitch());
        
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    public void setNpcSkin(Npc npc, String skinPlayerName) {
        config.set("npc." + npc.getEntityId() + ".playerSkinName", skinPlayerName);
        
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void loadNpcs() {
        npcLoaded = true;
        
        if (config.getConfigurationSection("npc") != null) {
            config.getConfigurationSection("npc").getKeys(false).forEach(npcIdString -> {
    
                int npcId = Integer.parseInt(npcIdString);
                
                //config.getConfigurationSection("npc." + npcId).getKeys(false).contains("playerSkinName") ||
                if ((npcId == 1 || npcId == 2 || npcId == 3))
                    reloadNpcConfig(npcId);
                
                String section = "npc." + npcId + ".";
                Location loc = new Location(Bukkit.getWorld(section + "World"), config.getDouble(section + "X"), config.getDouble(section + "Y"), config.getDouble(section + "Z"), Float.parseFloat(config.getString(section + "Yaw")), Float.parseFloat(config.getString(section + "Pitch")));
    
                OfflinePlayer op = JavaPlugin.getPlugin(SkyPvp.class).getMySql().getSqlStats().getTopPlayer(npcId);
    
                if (op != null && op.hasPlayedBefore()) {
                    Npc npc = new Npc(config.getString(section + "name").replaceAll("&", "§"), npcId, JavaPlugin.getPlugin(SkyPvp.class).getMySql().getSqlStats().getTopPlayer(npcId).getUniqueId(), loc);
                    if (config.getConfigurationSection("npc." + npcId).getKeys(false).contains("playerSkinName")) {
                        npc.setSkin(config.getString(section + "playerSkinName"));
                    }
                    npc.spawn();
    
                } else {
                    Npc npc = new Npc(config.getString(section + "name").replaceAll("&", "§"), npcId, UUID.randomUUID(), loc);
                    npc.spawn();
                }
                
                System.out.println("Created NPC: " + config.getString(section + "name"));
                
            });
            
        }
        
    }
    
    public void removeNpc(int id) {
        config.set("npc." + id, null);
    
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void reloadNpcs() {
    
        for (int i = 1; i < 4; i++) {
            reloadNpcConfig(i);
    
            if (JavaPlugin.getPlugin(SkyPvp.class).getMySql().getSqlStats().getTopPlayer(i) == null) {
                return;
            }
            
            String currentPlayerSkinName = config.getString("npc." + i + ".playerSkinName"),
                    newPlayerSkinName = JavaPlugin.getPlugin(SkyPvp.class).getMySql().getSqlStats().getTopPlayer(i).getName();
    
            if (!currentPlayerSkinName.equals(newPlayerSkinName)) {
                JavaPlugin.getPlugin(SkyPvp.class).getCoreLib().getStorage().npcList.get(i).setSkin(newPlayerSkinName);
            }
            
        }
        
    }
    
    @SneakyThrows
    private void reloadNpcConfig(int entityId) {
        OfflinePlayer op = JavaPlugin.getPlugin(SkyPvp.class).getMySql().getSqlStats().getTopPlayer(entityId);
        
        if (op != null && op.hasPlayedBefore()) {
            config.set("npc." + entityId + ".playerSkinName", JavaPlugin.getPlugin(SkyPvp.class).getMySql().getSqlStats().getTopPlayer(entityId).getName());
            config.save(file);
        }
    }
    
}
