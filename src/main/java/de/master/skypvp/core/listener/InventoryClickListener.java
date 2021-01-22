package de.master.skypvp.core.listener;

import de.master.skypvp.core.bootstrap.SkyPvp;
import de.master.skypvp.lib.CoreLib;
import de.master.skypvp.lib.kit.KitConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class InventoryClickListener implements Listener {
    
    public InventoryClickListener() {
        Bukkit.getPluginManager().registerEvents(this, JavaPlugin.getPlugin(SkyPvp.class));
    }
    
    @EventHandler
    public void onClick(InventoryClickEvent e) {
    
        if (e.getClickedInventory() == null || e.getCurrentItem() == null) {
            return;
        }
        
        if (e.getWhoClicked() instanceof Player && e.getClickedInventory().getName().equals(CoreLib.kitInventoryName)) {
            Player p = (Player) e.getWhoClicked();
            e.setCancelled(true);
            
            KitConfiguration kitConfiguration = JavaPlugin.getPlugin(SkyPvp.class).getCoreLib().getKitConfiguration();
    
            for (int i = 0; i < CoreLib.maxKits; i++) {
    
                if (e.getCurrentItem().getItemMeta().getDisplayName().equals(kitConfiguration.getKitName(i))) {
                    JavaPlugin.getPlugin(SkyPvp.class).getCoreLib().getStorage().playerKit.put(p.getName(), i);
                    p.sendMessage(CoreLib.prefix + "Du hast das Kit: " + kitConfiguration.getKitName(i) + " §7ausgewählt.");
                    p.closeInventory();
                    break;
                }
                
            }
        }
        
    }
    
}
