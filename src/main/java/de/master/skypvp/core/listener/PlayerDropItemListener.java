package de.master.skypvp.core.listener;

import de.master.skypvp.core.bootstrap.SkyPvp;
import de.master.skypvp.lib.CoreLib;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerDropItemListener implements Listener {
    
    public PlayerDropItemListener() {
        Bukkit.getPluginManager().registerEvents(this, JavaPlugin.getPlugin(SkyPvp.class));
    }
    
    public void onPlayerItemDrop(PlayerDropItemEvent e) {
    
        if (e.getPlayer().getItemInHand().equals(JavaPlugin.getPlugin(SkyPvp.class).getCoreLib().getKitConfiguration().kitItem)) {
            e.setCancelled(true);
        }
        
    }
    
}
