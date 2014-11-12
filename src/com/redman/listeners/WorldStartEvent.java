package com.redman.listeners;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

import com.redman.inventoryschism.InventorySchism;

public class WorldStartEvent implements Listener
{
    private InventorySchism schism;
    public WorldStartEvent(InventorySchism schism)
    {
	this.schism=schism;
    }
    @EventHandler
    public void startWorld(WorldLoadEvent e)
    {
	Bukkit.getLogger().log(Level.INFO, "Loading metadata blocks for world "+e.getWorld().getName());
	schism.getPrimaryDB().setBlocksMeta(schism, e.getWorld());
    }
}
