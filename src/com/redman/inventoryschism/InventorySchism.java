package com.redman.inventoryschism;
import java.io.File;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.redman.caching.CacheProcessor;
import com.redman.listeners.BlockListeners;
import com.redman.listeners.ChunkListener;
import com.redman.listeners.CommandListeners;
import com.redman.listeners.DeathListener;
import com.redman.listeners.GameModeListener;
import com.redman.listeners.GrowthListener;
import com.redman.listeners.InteractListeners;
import com.redman.listeners.ItemStackListeners;
import com.redman.listeners.OpenListeners;
import com.redman.listeners.PistonListener;
import com.redman.listeners.ProjectileListener;
import com.redman.listeners.SignListener;
import com.redman.listeners.WorldSaveListener;
import com.redman.listeners.WorldStartEvent;
import com.redman.persistence.DBProcessor;
import com.redman.persistence.BlockSaveThread;
import com.redman.persistence.InventorySaveThread;
import com.redman.persistence.WorldSaveThread;
public class InventorySchism extends JavaPlugin
{
	private CacheProcessor cache;
	private DBProcessor database;
	private FileConfiguration config;
	private boolean properShutdown=false;
	public void onEnable()
	{
		File configFile = new File(this.getDataFolder(), "config.yml");
		if(!configFile.exists()){this.saveDefaultConfig();}
		config = this.getConfig();
		this.cache = new CacheProcessor(this);
		database = new DBProcessor(cache,this);
		database.createTables();
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new ItemStackListeners(), this);
		pm.registerEvents(new BlockListeners(this), this);
		pm.registerEvents(new OpenListeners(), this);
		pm.registerEvents(new WorldSaveListener(database, this), this);
		pm.registerEvents(new GameModeListener(this,cache,database), this);
		pm.registerEvents(new ChunkListener(this, cache), this);
		pm.registerEvents(new ProjectileListener(), this);
		pm.registerEvents(new InteractListeners(), this);
		pm.registerEvents(new WorldStartEvent(this), this);
		pm.registerEvents(new CommandListeners(), this);
		pm.registerEvents(new PistonListener(), this);
		pm.registerEvents(new SignListener(), this);
		pm.registerEvents(new GrowthListener(this), this);
		pm.registerEvents(new DeathListener(), this);
	}
	
	public void onDisable()
	{
		HandlerList.unregisterAll(this);
		new WorldSaveListener(database,this).saveBlocksSync();
		try
		{
			cache.saveAllInventories();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		new BlockSaveThread(this).run();
		new InventorySaveThread(this).run();
		new WorldSaveThread(this).run();
		
		properShutdown=true;
	}
	
	public CacheProcessor getPrimaryCache()
	{
		return this.cache;
	}
	public DBProcessor getPrimaryDB()
	{
		return this.database;
	}
	public FileConfiguration getConfiguration()
	{
		return config;
	}
	public boolean properShutdown()
	{
		return properShutdown;
	}
}
