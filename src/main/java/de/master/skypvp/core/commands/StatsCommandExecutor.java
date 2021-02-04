package de.master.skypvp.core.commands;

import de.master.skypvp.core.bootstrap.SkyPvp;
import de.master.skypvp.lib.CoreLib;
import de.master.skypvp.lib.mysql.SqlStats;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class StatsCommandExecutor implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (sender instanceof Player) {
            Player p = (Player) sender;
    
            
            if (args.length == 0) {
                
                p.sendMessage(CoreLib.prefix + "Deine Stats:");
                sendStats(p, p.getUniqueId());
    
            } else if (args.length == 1) {
    
                OfflinePlayer op = Bukkit.getOfflinePlayer(args[0]);
                if (op == null || !op.hasPlayedBefore()) {
                    p.sendMessage(CoreLib.prefix + "Wir haben §ckeine Daten §7über diesen Spieler.");
                    return false;
                }
    
                p.sendMessage(CoreLib.prefix + "Stats von §a" + op.getName() + "§7:");
    
                sendStats(p, op.getUniqueId());
    
            } else
                p.sendMessage(CoreLib.prefix + "Bitte benutze §c/stats (<Spieler>)§7.");
            
        } else
            sender.sendMessage(CoreLib.prefix + "Dieses Feature ist nur für Spieler.");
        
        return false;
    }
    
    private void sendStats(Player p, UUID id) {
        SqlStats sqlStats = JavaPlugin.getPlugin(SkyPvp.class).getMySql().getSqlStats();
    
        int kills = sqlStats.getPlayerKills(id.toString());
        p.sendMessage("§8» §7Kills: §a" + kills);
        
        int deaths = sqlStats.getPlayerDeaths(id.toString());
        p.sendMessage("§8» §7Deaths: §c" + deaths);
        
        if (deaths == 0) {
            p.sendMessage("§8» §7K/D: §e" + kills + ".00");
        } else {
            double kd = CoreLib.round((double) kills / deaths, 2);
            p.sendMessage("§8» §7K/D: §e" + kd);
        }
    }
}
