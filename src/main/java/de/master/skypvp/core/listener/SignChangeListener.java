package de.master.skypvp.core.listener;

import de.master.skypvp.core.bootstrap.SkyPvp;
import de.master.skypvp.lib.CoreLib;
import de.master.skypvp.lib.mysql.SqlStats;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SignChangeListener implements Listener {
    
    public SignChangeListener() {
        Bukkit.getPluginManager().registerEvents(this, JavaPlugin.getPlugin(SkyPvp.class));
    }
    
    @EventHandler
    public void onSign(SignChangeEvent e) {
    
        Player p = e.getPlayer();
    
        String line1 = e.getLine(0), line2 = e.getLine(1);
    
        if (line1.equalsIgnoreCase("[SkyPvp]")) {
            if (line2.equalsIgnoreCase("Item")) {
                
                JavaPlugin.getPlugin(SkyPvp.class).getCoreLib().getStorage().setItemSign.add(p.getName());
                p.sendMessage(CoreLib.prefix + "Klicke mit einem §bItem §7auf das Schild, um ein §eItemSign §7zu erstellen.");
                System.out.println("Create Sign " + e.getPlayer().getName());
    
            } else if (line2.equals("Top 1") || line2.equals("Top 2") || line2.equals("Top 3")) {
    
                JavaPlugin.getPlugin(SkyPvp.class).getCoreLib().getStorage().topSigns.add((Sign) e.getBlock().getState());
                int top = Integer.parseInt(line2.replace("Top ", ""));
    
                SqlStats sqlStats = JavaPlugin.getPlugin(SkyPvp.class).getMySql().getSqlStats();
                OfflinePlayer offlinePlayer = sqlStats.getTopPlayer(top);
    
                if (offlinePlayer != null && offlinePlayer.hasPlayedBefore()) {
                    e.setLine(0, "--- " + line2 + " ---");
                    e.setLine(1, "Kills: " + sqlStats.getPlayerKills(offlinePlayer.getUniqueId().toString()));
                    e.setLine(2, "Deaths: " + sqlStats.getPlayerDeaths(offlinePlayer.getUniqueId().toString()));
    
    
                    if (sqlStats.getPlayerDeaths(offlinePlayer.getUniqueId().toString()) != 0) {
                        double kd = sqlStats.getPlayerKills(offlinePlayer.getUniqueId().toString()) / sqlStats.getPlayerDeaths(offlinePlayer.getUniqueId().toString());
                        e.setLine(3, "K/D: " + kd);
                    } else
                        e.setLine(3, "K/D: " + sqlStats.getPlayerKills(offlinePlayer.getUniqueId().toString()));
                }
                
                e.setLine(0, "--- " + line2 + " ---");
                e.setLine(1, "Noch keine Daten...");
                
                JavaPlugin.getPlugin(SkyPvp.class).getCoreLib().getLocationConfiguration().addSign(line2, (Sign) e.getBlock().getState());
                
            } else
                p.sendMessage(CoreLib.prefix + "Mache ein §bSkyPvp §7Schild, indem in der 2. Zeile <§eItem§8/§e'Top 1/2/3'>  §7steht.");
        }
        
    }
    
}
