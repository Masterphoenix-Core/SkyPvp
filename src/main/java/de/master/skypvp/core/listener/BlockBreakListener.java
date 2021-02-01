package de.master.skypvp.core.listener;

import de.master.skypvp.core.bootstrap.SkyPvp;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockBreakListener implements Listener {
    
    public BlockBreakListener() {
        Bukkit.getPluginManager().registerEvents(this, JavaPlugin.getPlugin(SkyPvp.class));
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (!JavaPlugin.getPlugin(SkyPvp.class).getCoreLib().getStorage().buildList.contains(e.getPlayer().getName())) {
            e.setCancelled(true);
        }
    }
    
}
