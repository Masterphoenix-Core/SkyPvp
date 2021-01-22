package de.master.skypvp.core.commands;

import de.master.skypvp.core.bootstrap.SkyPvp;
import de.master.skypvp.lib.CoreLib;
import de.master.skypvp.lib.mysql.SqlStats;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class StatsCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (sender instanceof Player) {
            Player p = (Player) sender;
    
            SqlStats sqlStats = JavaPlugin.getPlugin(SkyPvp.class).getMySql().getSqlStats();
            
            if (args.length == 0) {
                
                p.sendMessage(CoreLib.prefix + "Deine Stats:");
                p.sendMessage("§8» §7Kills: §a" + sqlStats.getPlayerKills(p.getUniqueId().toString()));
                p.sendMessage("§8» §7Deaths: §c" + sqlStats.getPlayerDeaths(p.getUniqueId().toString()));
    
                double kd = sqlStats.getPlayerKills(p.getUniqueId().toString()) / sqlStats.getPlayerDeaths(p.getUniqueId().toString());
                
                p.sendMessage("§8» §7K/D: §e" + kd);
                
            } else if (args.length == 1) {
    
                OfflinePlayer op = Bukkit.getOfflinePlayer(args[0]);
                if (op == null || !op.hasPlayedBefore()) {
                    p.sendMessage(CoreLib.prefix + "Wir haben §ckeine Daten §7über diesen Spieler.");
                    return false;
                }
    
                p.sendMessage(CoreLib.prefix + "Stats von §a" + op.getName() + "§7:");
                p.sendMessage("§8» §7Kills: §a" + sqlStats.getPlayerKills(op.getUniqueId().toString()));
                p.sendMessage("§8» §7Deaths: §c" + sqlStats.getPlayerDeaths(op.getUniqueId().toString()));
    
                double kd = sqlStats.getPlayerKills(op.getUniqueId().toString()) / sqlStats.getPlayerDeaths(op.getUniqueId().toString());
    
                p.sendMessage("§8» §7K/D: §e" + kd);
                
            } else
                p.sendMessage(CoreLib.prefix + "Bitte benutze §c/stats (<Spieler>)§7.");
            
        }
        
        return false;
    }
}
