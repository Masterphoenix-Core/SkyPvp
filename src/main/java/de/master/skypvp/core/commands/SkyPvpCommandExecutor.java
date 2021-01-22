package de.master.skypvp.core.commands;

import de.master.skypvp.core.bootstrap.SkyPvp;
import de.master.skypvp.lib.CoreLib;
import de.master.skypvp.lib.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class SkyPvpCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    
        if (sender instanceof Player) {
            Player p = (Player) sender;
            
            //skypvp set spawn
            if (args.length == 2) {
    
                if (args[0].equalsIgnoreCase("set")) {
                    if (args[1].equals("spawn")) {
    
                        JavaPlugin.getPlugin(SkyPvp.class).getCoreLib().getLocationConfiguration().addLocation("spawn", p.getLocation());
                        p.sendMessage(CoreLib.prefix + "Du hast den Spawn gesetzt.");
                        
                    }
                }
                
            } else
            
            //skypvp giveitem MATERIAL NAME
            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("giveItem")) {
    
                    Material material = Material.getMaterial(args[1]);
    
                    if (material == null) {
                        p.sendMessage(CoreLib.prefix + "Es wurde §ckein Item §7mit dem Namen §6" + args[1] + " §7gefunden.");
                        return false;
                    }
                    
                    ItemStack item = new ItemBuilder(material).setName(args[2].replaceAll("&", "§")).build();
                    p.getInventory().addItem(item);
                    p.sendMessage(CoreLib.prefix + "Du hast ein §b" + material.name() + " §7mit dem Namen §e" + item.getItemMeta().getDisplayName() + " §7erschaffen.");
    
                } else
                    p.sendMessage(CoreLib.prefix + "Bitte benutze §c/skypvp <giveItem>");
            } else
                p.sendMessage(CoreLib.prefix + "Bitte benutze §c/SkyPvP Help §7wenn du Hilfe benötigst.");
            
            
        } else
            sender.sendMessage("Dieser Command ist nur für Spieler!");
        
        return false;
    }
}