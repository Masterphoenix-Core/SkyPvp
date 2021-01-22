package de.master.skypvp.core.listener;

import de.master.skypvp.core.bootstrap.SkyPvp;
import de.master.skypvp.lib.CoreLib;
import de.master.skypvp.lib.mysql.SqlStats;
import org.bukkit.Bukkit;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

public class PlayerDeathListener implements Listener {
    
    public PlayerDeathListener() {
        Bukkit.getPluginManager().registerEvents(this, JavaPlugin.getPlugin(SkyPvp.class));
    }
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
    
        Player p = e.getEntity();
        p.setTotalExperience(0);
    
        if (JavaPlugin.getPlugin(SkyPvp.class).getCoreLib().getStorage().killer.containsKey(p.getName())) {
            Player k = Bukkit.getPlayer(JavaPlugin.getPlugin(SkyPvp.class).getCoreLib().getStorage().killer.get(p.getName()));
            e.setDeathMessage(CoreLib.prefix + "§6" + p.getName() + " §7wurde von §c" + k.getName() + " §7getötet.");
            updateStats(p, k);
            return;
        }
    
        if (p.getLastDamageCause().getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK) && e.getEntity().getKiller() != null) {
            Player k = e.getEntity().getKiller();
            e.setDeathMessage(CoreLib.prefix + "§6" + e.getEntity().getName() + " §7wurde von §c" + k.getName() + " §7getötet.");
            updateStats(p, k);
            return;
        }
    
        JavaPlugin.getPlugin(SkyPvp.class).getMySql().getSqlStats().addDeaths(p.getUniqueId().toString(), 1);
        e.setDeathMessage(CoreLib.prefix + "§6" + e.getEntity().getName() + " §7ist gestorben.");
        
    }
    
    private void updateStats(Player p, Player k) {
        
        JavaPlugin.getPlugin(SkyPvp.class).getMySql().getSqlStats().addDeaths(p.getUniqueId().toString(), 1);
        JavaPlugin.getPlugin(SkyPvp.class).getMySql().getSqlStats().addKills(k.getUniqueId().toString(), 1);
        
        JavaPlugin.getPlugin(SkyPvp.class).getCoreLib().getStorage().killer.remove(p.getName());
        
        updateSigns();
    }
    
    private void updateSigns() {
        Map<Sign, Integer> signs = JavaPlugin.getPlugin(SkyPvp.class).getCoreLib().getLocationConfiguration().getSigns();
        SqlStats sqlStats = JavaPlugin.getPlugin(SkyPvp.class).getMySql().getSqlStats();
        
        for (Sign sign : signs.keySet()) {
            
            sign.setLine(1, "Kills: " + sqlStats.getPlayerKills(sqlStats.getTopPlayer(signs.get(sign)).getUniqueId().toString()));
            sign.setLine(2, "Deaths: " + sqlStats.getPlayerDeaths(sqlStats.getTopPlayer(signs.get(sign)).getUniqueId().toString()));
            sign.setLine(3, "K/D: " + (sqlStats.getPlayerKills(sqlStats.getTopPlayer(signs.get(sign)).getUniqueId().toString()) /
                    sqlStats.getPlayerDeaths(sqlStats.getTopPlayer(signs.get(sign)).getUniqueId().toString())));
            
            sign.update();
    
        }
        
    }
    
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        e.getPlayer().getInventory().addItem(JavaPlugin.getPlugin(SkyPvp.class).getCoreLib().getKitConfiguration().kitItem);
    
        e.getPlayer().teleport(JavaPlugin.getPlugin(SkyPvp.class).getCoreLib().getLocationConfiguration().getLocation("spawn"));
    }
}
