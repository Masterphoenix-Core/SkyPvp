package de.master.skypvp.core.listener;

import de.master.skypvp.core.bootstrap.SkyPvp;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.*;

public class PlayerMoveListener implements Listener {
    
    public PlayerMoveListener() {
        Bukkit.getPluginManager().registerEvents(this, JavaPlugin.getPlugin(SkyPvp.class));
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        
        if (JavaPlugin.getPlugin(SkyPvp.class).getCoreLib().getStorage().killer.containsKey(e.getPlayer().getName())) {
            if (e.getPlayer().isOnGround()) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(JavaPlugin.getPlugin(SkyPvp.class), () -> JavaPlugin.getPlugin(SkyPvp.class).getCoreLib().getStorage().killer.remove(e.getPlayer().getName()), 10);
            }
        }
        
        Player p = e.getPlayer();
        Material type = new Location(p.getLocation().getWorld(), p.getLocation().getX(), p.getLocation().getY() - 1, p.getLocation().getZ()).getBlock().getType();
        
        if (type == Material.IRON_BLOCK) {
            addEffect(p, Potion.getBrewer().createEffect(PotionEffectType.JUMP, 5 * 20, 1));
        } else if (type == Material.GOLD_BLOCK) {
            addEffect(p, Potion.getBrewer().createEffect(PotionEffectType.REGENERATION, 5 * 4 * 20, 1));
        } else if (type == Material.DIAMOND_BLOCK) {
            addEffect(p, Potion.getBrewer().createEffect(PotionEffectType.SPEED, 5 * 20, 1));
        } else if (type == Material.LAPIS_BLOCK) {
            addEffect(p, Potion.getBrewer().createEffect(PotionEffectType.INCREASE_DAMAGE, 5 * 20, 1));
        } else if (type == Material.SPONGE) {// JUMP PAD
            p.setVelocity(p.getLocation().getDirection().add(p.getVelocity().multiply(0.5)).setY(1).multiply(0.9));
        } else if (type == Material.BEDROCK) {
            addEffect(p, Potion.getBrewer().createEffect(PotionEffectType.HARM, 5 * 2 * 20, 1));
        } else if (type == Material.PUMPKIN) {
            addEffect(p, Potion.getBrewer().createEffect(PotionEffectType.CONFUSION, 10 * 4 * 20, 2));
        } else if (type == Material.EMERALD_BLOCK) {
            addEffect(p, Potion.getBrewer().createEffect(PotionEffectType.POISON, 5 * 4 * 20, 1));
        }
    }
    
    private void addEffect(Player p, PotionEffect effect) {
        p.removePotionEffect(effect.getType());
        
        Bukkit.getScheduler().scheduleSyncDelayedTask(JavaPlugin.getPlugin(SkyPvp.class), () -> p.addPotionEffect(effect), 1);
    }
    
}
