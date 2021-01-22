package de.master.skypvp.lib.util;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class ItemBuilder {
    
    private ItemStack is;
    private ItemMeta im;
    
    public ItemBuilder(Material material) {
        is = new ItemStack(material);
        im = is.getItemMeta();
    }
    
    public ItemBuilder setAmount(int amount) {
        is.setAmount(amount);
        return this;
    }
    
    public ItemBuilder setName(String name) {
        im.setDisplayName(name);
        return this;
    }
    
    public ItemBuilder setLore(String... lore) {
        im.setLore(Arrays.asList(lore));
        return this;
    }
    
    public ItemBuilder setLore(List<String> lore) {
        im.setLore(lore);
        return this;
    }
    
    public ItemStack build() {
        is.setItemMeta(im);
        return is;
    }
    
}
