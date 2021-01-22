package de.master.skypvp.lib;

import de.master.skypvp.lib.kit.KitConfiguration;
import de.master.skypvp.lib.location.LocationConfiguration;
import de.master.skypvp.lib.npc.NpcConfiguration;
import lombok.Getter;

@Getter
public class CoreLib {
    
    public static String prefix = "§7[§aSkyPVP§7] §7", noPerms = prefix + "§cDazu hast du keine Rechte!";
    
    public static int maxKits = 27, spawnHeight = 200;
    public static String kitInventoryName = "§6Kits";
    
    private Storage storage;
    private KitConfiguration kitConfiguration;
    private NpcConfiguration npcConfiguration;
    private LocationConfiguration locationConfiguration;
    
    public CoreLib() {
        storage = new Storage();
        kitConfiguration = new KitConfiguration();
        npcConfiguration = new NpcConfiguration();
        locationConfiguration = new LocationConfiguration(storage);
    }
    
}
