package de.master.skypvp.core.listener;

import de.master.skypvp.core.bootstrap.SkyPvp;
import de.master.skypvp.lib.CoreLib;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class EntityDamageListener implements Listener {
    
    public EntityDamageListener() {
        Bukkit.getPluginManager().registerEvents(this, JavaPlugin.getPlugin(SkyPvp.class));
    }
    
    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
    
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
    
            if (p.getLocation().getY() >= CoreLib.spawnHeight) {
                e.setCancelled(true);
            }
    
            if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                if (e.getEntity().getFallDistance() >= (CoreLib.spawnHeight - e.getEntity().getLocation().getY())) {
                    e.setCancelled(true);
    
                    if (JavaPlugin.getPlugin(SkyPvp.class).getCoreLib().getStorage().playerKit.containsKey(p.getName())) {
                        int kitNumber = JavaPlugin.getPlugin(SkyPvp.class).getCoreLib().getStorage().playerKit.get(p.getName());
    
                        p.getInventory().remove(JavaPlugin.getPlugin(SkyPvp.class).getCoreLib().getKitConfiguration().kitItem);
                        
                        JavaPlugin.getPlugin(SkyPvp.class).getCoreLib().getKitConfiguration().giveKit(p, kitNumber);
                        JavaPlugin.getPlugin(SkyPvp.class).getCoreLib().getStorage().playerKit.remove(p.getName());
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
    
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            if (e.getDamager().getLocation().getY() >= CoreLib.spawnHeight || e.getEntity().getLocation().getY() >= CoreLib.spawnHeight) {
                e.setCancelled(true);
                return;
            }
    
            if (!e.isCancelled()) {
                JavaPlugin.getPlugin(SkyPvp.class).getCoreLib().getStorage().killer.put(e.getEntity().getName(), e.getDamager().getName());
            }
            
        }
    }
    
}
