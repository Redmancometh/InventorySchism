package com.redman.listeners;

import java.sql.SQLException;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.redman.inventoryschism.InventorySchism;
import com.redman.persistence.DBProcessor;

public class WorldSaveListener implements Listener
{
	private DBProcessor database;
	private InventorySchism schism;

	public WorldSaveListener(DBProcessor database, InventorySchism schism)
	{
		this.database = database;
		this.schism = schism;
	}
	@EventHandler
	public void saveBlocks(ServerCommandEvent e) throws SQLException
	{
		if (!e.getCommand().equalsIgnoreCase("save-all") && !(e.getCommand().equalsIgnoreCase("stop")))
		{
			return;
		}
		if (e.getCommand().equalsIgnoreCase("save-all"))
		{
			schism.getPrimaryCache().saveAllInventories();
			saveBlocksAsync();
		}
		if (e.getCommand().equalsIgnoreCase("stop"))
		{
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "save-all");
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "stop");
		}
	}
	public void saveBlocksAsync()
	{
		Bukkit.getLogger().log(Level.INFO, "Saving schism blocks");
		new BukkitRunnable()
		{
			public void run()
			{
				try
				{
					database.savePlacedBlocks();
				}
				catch (SQLException e)
				{
					e.printStackTrace();
				}
			}
		}.runTaskAsynchronously(schism);
		Bukkit.getLogger().log(Level.INFO, "Saved.");
	}
	@EventHandler
	public void worldSave(WorldUnloadEvent e) throws SQLException
	{
		saveBlocksSync();
		schism.getPrimaryCache().saveAllInventories();
	}
	public void saveBlocksSync()
	{
		try
		{
			System.out.println("[WARNING]: Saving placed blocks synchronously due to server shutdown!");
			database.savePlacedBlocks();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
}
