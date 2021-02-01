package de.master.skypvp.core.listener;

import de.master.skypvp.core.bootstrap.SkyPvp;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockPlaceListener implements Listener {
    
    public BlockPlaceListener() {
        Bukkit.getPluginManager().registerEvents(this, JavaPlugin.getPlugin(SkyPvp.class));
    }
    
    public void onBlockPlace(BlockPlaceEvent e) {
        if (!JavaPlugin.getPlugin(SkyPvp.class).getCoreLib().getStorage().buildList.contains(e.getPlayer().getName())) {
            e.setCancelled(true);
        }
    }
}
