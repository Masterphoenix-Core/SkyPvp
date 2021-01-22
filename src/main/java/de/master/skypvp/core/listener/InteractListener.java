package de.master.skypvp.core.listener;

import de.master.skypvp.core.bootstrap.SkyPvp;
import de.master.skypvp.lib.CoreLib;
import de.master.skypvp.lib.kit.KitConfiguration;
import de.master.skypvp.lib.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class InteractListener implements Listener {
    
    public InteractListener() {
        Bukkit.getPluginManager().registerEvents(this, JavaPlugin.getPlugin(SkyPvp.class));
    }
    
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
    
            CoreLib coreLib = JavaPlugin.getPlugin(SkyPvp.class).getCoreLib();
            
            if (e.getPlayer().getItemInHand().equals(JavaPlugin.getPlugin(SkyPvp.class).getCoreLib().getKitConfiguration().kitItem)) {
        
                Inventory kitInv = Bukkit.createInventory(null, CoreLib.maxKits, CoreLib.kitInventoryName);
                KitConfiguration kitConfig = JavaPlugin.getPlugin(SkyPvp.class).getCoreLib().getKitConfiguration();
                for (int i = 0; i < CoreLib.maxKits; i++) {
                    if (kitConfig.getKitIsEnabled(i)) {
                
                        List<Material> materials = kitConfig.getKitItemMaterials(i);
                
                        List<String> loreList = new ArrayList<>(materials.size());
                        loreList.add("§8"); // Placeholder
                        for (int j = 0; j < materials.size(); j++) {
                            loreList.add(kitConfig.getKitDisplayNames(i).get(j).replaceAll("&", "§"));
                        }
                
                        kitInv.setItem(kitConfig.getKitSlot(i), new ItemBuilder(kitConfig.getKitDisplayItemMaterial(i)).setName(kitConfig.getKitName(i)).setLore(loreList).build());
                
                    }
                }
        
                e.getPlayer().openInventory(kitInv);
                return;
            }
    
            if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                
                if (e.getClickedBlock().getType().equals(Material.WALL_SIGN) || e.getClickedBlock().getType().equals(Material.SIGN_POST) || e.getClickedBlock().getType().equals(Material.SIGN)) {
        
                    if (coreLib.getStorage().setItemSign.contains(e.getPlayer().getName()) && e.getPlayer().getItemInHand() != null) {
                        ItemStack item = e.getPlayer().getItemInHand();
            
                        Sign sign = (Sign) e.getClickedBlock().getState();
                        sign.setLine(0, "§0---§e*§0---");
                        sign.setLine(1, "§0[§2Gratis§0]");
            
                        if (item.getItemMeta().getDisplayName() != null)
                            sign.setLine(2, item.getItemMeta().getDisplayName());
                        else
                            sign.setLine(2, "§0" + item.getType().name());
            
                        sign.setLine(3, "§0---§e*§0---");
                        sign.update();
            
                        e.getPlayer().sendMessage(CoreLib.prefix + "Du hast ein §eItemSign §7mit dem Item §b" + item.getType().name() + " §7erstellt.");
            
                        coreLib.getStorage().setItemSign.remove(e.getPlayer().getName());
                        coreLib.getStorage().itemSign.put(e.getClickedBlock().getLocation(), item);
                        
                        JavaPlugin.getPlugin(SkyPvp.class).getCoreLib().getLocationConfiguration().addItemSign(e.getClickedBlock().getLocation(), item);
            
                    } else
                        e.getPlayer().getInventory().addItem(coreLib.getStorage().itemSign.get(e.getClickedBlock().getLocation()));
        
                }
    
            }
            
        }
        
    }
}
