package de.master.skypvp.core.listener;

import de.master.skypvp.core.bootstrap.SkyPvp;
import de.master.skypvp.lib.CoreLib;
import de.master.skypvp.lib.mysql.SqlStats;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerJoinListener implements Listener {
    
    public PlayerJoinListener() {
        Bukkit.getPluginManager().registerEvents(this, JavaPlugin.getPlugin(SkyPvp.class));
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
    
        e.setJoinMessage(CoreLib.prefix + "§e" + e.getPlayer().getName() + " §7ist dem Spiel §abeigetreten§7.");
        
        CoreLib coreLib = JavaPlugin.getPlugin(SkyPvp.class).getCoreLib();
        SqlStats sqlStats = JavaPlugin.getPlugin(SkyPvp.class).getMySql().getSqlStats();
        
        e.getPlayer().getInventory().clear();
        e.getPlayer().getInventory().addItem(coreLib.getKitConfiguration().kitItem);
    
        if (!sqlStats.playerExists(e.getPlayer().getUniqueId().toString())) {
            sqlStats.createPlayer(e.getPlayer().getUniqueId().toString());
        }
        
        if (!coreLib.getNpcConfiguration().npcLoaded) {
            coreLib.getNpcConfiguration().loadNpcs();
        }
    
        for (int n : coreLib.getStorage().npcList.keySet()) {
            coreLib.getStorage().npcList.get(n).spawn(e.getPlayer());
        }
    
        try {
            e.getPlayer().teleport(coreLib.getLocationConfiguration().getLocation("spawn"));
        } catch (Exception exception) {
            e.getPlayer().sendMessage(CoreLib.prefix + "§cDer Spawn wurde noch nicht gesetzt!");
        }
    }
}
