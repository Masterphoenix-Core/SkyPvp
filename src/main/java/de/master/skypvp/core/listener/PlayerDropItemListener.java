package de.master.skypvp.core.listener;

import de.master.skypvp.core.bootstrap.SkyPvp;
import de.master.skypvp.lib.CoreLib;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerDropItemListener implements Listener {
    
    public PlayerDropItemListener() {
        Bukkit.getPluginManager().registerEvents(this, JavaPlugin.getPlugin(SkyPvp.class));
    }
    
    @EventHandler
    public void onPlayerItemDrop(PlayerDropItemEvent e) {
    
        if (e.getItemDrop().getItemStack().equals(JavaPlugin.getPlugin(SkyPvp.class).getCoreLib().getKitConfiguration().kitItem) &&
                !JavaPlugin.getPlugin(SkyPvp.class).getCoreLib().getStorage().buildList.contains(e.getPlayer().getName())) {
            e.setCancelled(true);
        }
        
    }
    
    
}
