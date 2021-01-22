package de.master.skypvp.lib.kit;

import de.master.skypvp.lib.CoreLib;
import de.master.skypvp.lib.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;
import java.util.List;

public class KitConfiguration {
    
    private File file;
    private YamlConfiguration config;
    
    public ItemStack kitItem;
    
    private final String separator = "%%";
    
    public KitConfiguration() {
    
        file = new File("plugins/SkyPvp", "kits.yml");
        config = YamlConfiguration.loadConfiguration(file);
        
        addDefaults();
        
        kitItem = new ItemBuilder(Material.BLAZE_ROD).setName("§6Wähle dein Kit").build();
        
    }
    
    private void addDefaults() {
        
        
        for (int i = 0; i < CoreLib.maxKits; i++) {
            config.addDefault("kit." + i + ".enable", false);
            config.addDefault("kit." + i + ".name", "DisplayName");
            config.addDefault("kit." + i + ".displayItem", "DisplayItem");
            config.addDefault("kit." + i + ".slot", i + 1);
            config.addDefault("kit." + i + ".items", new String[]{"Amount1" + separator + "Item1", "Amount2" + separator + "Item2"});
            config.addDefault("kit." + i + ".lore", new String[]{"DisplayAmount DisplayName1", "DisplayAmount DisplayName2"});
        }
        
        config.options().copyDefaults(true);
        
        try {
            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public boolean getKitIsEnabled(int kitNumber) {
        return config.getBoolean("kit." + kitNumber + ".enable");
    }
    
    public String getKitName(int kitNumber) {
        return config.getString("kit." + kitNumber + ".name").replaceAll("&", "§");
    }
    
    public Material getKitDisplayItemMaterial(int kitNumber) {
        return Material.getMaterial(config.getString("kit." + kitNumber + ".displayItem"));
    }
    
    public int getKitSlot(int kitNumber) {
        return config.getInt("kit." + kitNumber + ".slot") - 1;
    }
    
    public List<String> getKitDisplayNames(int kitNumber) {
        return config.getStringList("kit." + kitNumber + ".lore");
    }
    
    public int[] getKitItemAmounts(int kitNumber) {
        
        List<String> amounts = config.getStringList("kit." + kitNumber + ".items");
        int[] itemAmounts = new int[amounts.size()];
        
        for (int i = 0; i < amounts.size(); i++) {
            itemAmounts[i] = Integer.parseInt(amounts.get(i).split(separator)[0]);
        }
        
        return itemAmounts;
    }
    
    public List<Material> getKitItemMaterials(int kitNumber) {
        
        List<String> materials = config.getStringList("kit." + kitNumber + ".items");
        List<Material> itemMaterials = new ArrayList<>();
        
        for (int i = 0; i < materials.size(); i++) {
            itemMaterials.add(i, Material.getMaterial(materials.get(i).split(separator)[1]));
        }
        
        return itemMaterials;
    }
    
    public void giveKit(Player p, int kitNumber) {
        
        List<Material> materials = getKitItemMaterials(kitNumber);
    
        for (int i = 0; i < materials.size(); i++) {
    
            if (materials.get(i).equals(Material.LEATHER_HELMET) || materials.get(i).equals(Material.CHAINMAIL_HELMET) || materials.get(i).equals(Material.GOLD_HELMET) || materials.get(i).equals(Material.IRON_HELMET) || materials.get(i).equals(Material.DIAMOND_HELMET)) {
                p.getInventory().setHelmet(new ItemStack(materials.get(i)));
                continue;
    
            } else if (materials.get(i).equals(Material.LEATHER_CHESTPLATE) || materials.get(i).equals(Material.CHAINMAIL_CHESTPLATE) || materials.get(i).equals(Material.GOLD_CHESTPLATE) || materials.get(i).equals(Material.IRON_CHESTPLATE) || materials.get(i).equals(Material.DIAMOND_CHESTPLATE)) {
                p.getInventory().setChestplate(new ItemStack(materials.get(i)));
                continue;
                
            } else if (materials.get(i).equals(Material.LEATHER_LEGGINGS) || materials.get(i).equals(Material.CHAINMAIL_LEGGINGS) || materials.get(i).equals(Material.GOLD_LEGGINGS) || materials.get(i).equals(Material.IRON_LEGGINGS) || materials.get(i).equals(Material.DIAMOND_LEGGINGS)) {
                p.getInventory().setLeggings(new ItemStack(materials.get(i)));
                continue;
    
            } else if (materials.get(i).equals(Material.LEATHER_BOOTS) || materials.get(i).equals(Material.CHAINMAIL_BOOTS) || materials.get(i).equals(Material.GOLD_BOOTS) || materials.get(i).equals(Material.IRON_BOOTS) || materials.get(i).equals(Material.DIAMOND_BOOTS)) {
                p.getInventory().setBoots(new ItemStack(materials.get(i)));
                continue;
    
            }
            
            p.getInventory().addItem(new ItemBuilder(materials.get(i)).setAmount(getKitItemAmounts(kitNumber)[i]).build());
        }
        
    }
    
}
