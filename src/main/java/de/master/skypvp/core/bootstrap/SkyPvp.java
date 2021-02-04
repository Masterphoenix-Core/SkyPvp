package de.master.skypvp.core.bootstrap;

import de.master.skypvp.core.commands.*;
import de.master.skypvp.core.listener.*;
import de.master.skypvp.lib.CoreLib;
import de.master.skypvp.lib.mysql.MySql;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class SkyPvp extends JavaPlugin {
    
    private CoreLib coreLib;
    
    private MySql mySql;
    
    @Override
    public void onEnable() {
    
        mySql = new MySql();
        coreLib = new CoreLib();
        
        initCommands();
        initListener();
    }
    
    
    @Override
    public void onDisable() {
        mySql.disconnect();
    }
    
    private void initCommands() {
        this.getCommand("skypvp").setExecutor(new SkyPvpCommandExecutor());
        this.getCommand("npc").setExecutor(new NpcCommandExecutor());
        this.getCommand("stats").setExecutor(new StatsCommandExecutor());
        
    }
    
    private void initListener() {
        new BlockBreakListener();
        new BlockPlaceListener();
        new EntityDamageListener();
        new InteractListener();
        new InventoryClickListener();
        new PlayerDeathListener();
        new PlayerDropItemListener();
        new PlayerJoinListener();
        new PlayerMoveListener();
        new PlayerQuitListener();
        new SignChangeListener();
        new WeatherChangeListener();
    }
    
}
