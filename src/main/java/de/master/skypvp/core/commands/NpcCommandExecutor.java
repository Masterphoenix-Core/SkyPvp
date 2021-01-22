package de.master.skypvp.core.commands;

import de.master.skypvp.core.bootstrap.SkyPvp;
import de.master.skypvp.lib.CoreLib;
import de.master.skypvp.lib.mysql.SqlStats;
import de.master.skypvp.lib.npc.Npc;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class NpcCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (sender instanceof Player) {
            Player p = (Player) sender;
            
            if (p.hasPermission("skypvp.npc")) {
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("list")) {
                        
                        p.sendMessage(CoreLib.prefix + "§bNPC-List");
                        
                        if (JavaPlugin.getPlugin(SkyPvp.class).getCoreLib().getStorage().npcList.isEmpty()) {
                            p.sendMessage(CoreLib.prefix + "Es wurden noch keine NPCs erschaffen.");
                            return false;
                        }
                        
                        JavaPlugin.getPlugin(SkyPvp.class).getCoreLib().getStorage().npcList.forEach(n -> {
                            
                            p.sendMessage("§8§k----------------");
                            p.sendMessage("§7Name: §6" + n.getGameProfile().getName());
                            p.sendMessage("§7World: §6" + n.getLocation().getWorld().getName());
                            p.sendMessage("§7X: §6" + n.getLocation().getX());
                            p.sendMessage("§7Y: §6" + n.getLocation().getY());
                            p.sendMessage("§7Z: §6" + n.getLocation().getZ());
                            
                        });
                        
                    } else
                        sendHelpMessage(p);
                    
                } else if (args.length == 3) { // npc spawn/create top <num>
                    if (args[0].equalsIgnoreCase("spawn") || args[0].equalsIgnoreCase("create")) {
                        if (args[1].equalsIgnoreCase("top")) {
                            
                            int top = 0;
                            try {
                                top = Integer.parseInt(args[2]);
                            } catch (NumberFormatException e) {
                                p.sendMessage(CoreLib.prefix + "§7Du musst eine §cZahl §7als Top-Platzierung angeben!");
                            }
                            
                            if (top == 0) {
                                return false;
                            }
                            
                            SqlStats sqlStats = JavaPlugin.getPlugin(SkyPvp.class).getMySql().getSqlStats();
                            
                            OfflinePlayer topPlayer = sqlStats.getTopPlayer(top);
                            
                            if (topPlayer != null && topPlayer.hasPlayedBefore()) {
                                Npc npc = new Npc(topPlayer.getName(), top, topPlayer.getUniqueId(), p.getLocation());
                                npc.setSkin(topPlayer.getName());
                                npc.spawn();
                                JavaPlugin.getPlugin(SkyPvp.class).getCoreLib().getNpcConfiguration().addNpc(npc);
                            } else {
                                Npc npc = new Npc("--- None ---", top, UUID.randomUUID(), p.getLocation());
                                npc.setSkin("Masterphoenix_");
                                npc.spawn();
                                JavaPlugin.getPlugin(SkyPvp.class).getCoreLib().getNpcConfiguration().addNpc(npc);
                            }
                            
                            
                            // SPAWN TOP X
                            
                            p.sendMessage(CoreLib.prefix + "Du hast den Top-Player §6Nummer " + top + " §7gespawnt!");
                            
                        } else
                            p.sendMessage(CoreLib.prefix + "§7Bitte benutze §c/npc spawn top <Nummer>§7.");
                    } else
                        sendHelpMessage(p);
                } else
                    sendHelpMessage(p);
                
            } else
                p.sendMessage(CoreLib.noPerms);
        }
        
        
        return false;
        
    }
    
    private void sendHelpMessage(Player p) {
        
        p.sendMessage(CoreLib.prefix + "Hilfe:");
        p.sendMessage("§e/npc list §8> §7Für eine Auflistung von NPCs.");
        p.sendMessage("§e/npc <spawn/create> top <Nummer> §8> §7Für eine Spawnen eines Top-Player NPCs.");
        
    }
    
}
