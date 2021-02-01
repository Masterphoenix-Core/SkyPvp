package de.master.skypvp.lib;

import de.master.skypvp.lib.npc.Npc;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Storage {
    
    public Map<String, Integer> playerKit;
    public Map<String, String> killer;
    public Map<Location, ItemStack> itemSign;
    
    public List<String> setItemSign, buildList;
    public List<Sign> topSigns;
    
    public Map<Integer, Npc> npcList;
    
    public Storage() {
        npcList = new HashMap<>();
        itemSign = new HashMap<>();
        topSigns = new ArrayList<>();
    
        setItemSign = new ArrayList<>();
        killer = new HashMap<>();
        playerKit = new HashMap<>();
    
        buildList = new ArrayList<>();
        
    }
    
}
