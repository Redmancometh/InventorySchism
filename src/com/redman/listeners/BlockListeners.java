package com.redman.listeners;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.metadata.FixedMetadataValue;

import com.redman.caching.CacheProcessor;
import com.redman.inventoryschism.InventorySchism;

public class BlockListeners implements Listener
{
    private InventorySchism schism;
    private CacheProcessor cache;

    public BlockListeners(InventorySchism schism)
    {
	this.schism = schism;
	this.cache = schism.getPrimaryCache();
    }
    /*@EventHandler
    public void physics(BlockFromToEvent e)
    {
	Material type = e.getBlock().getType();
	if(type==Material.LAVA||type==Material.WATER)
	{
	    if(e.getToBlock().getType()==Material.DOUBLE_PLANT)
	    {
		e.setCancelled(true);
	    }
	}
    }*/
    @EventHandler
    public void blockPlace(BlockPlaceEvent e) throws InstantiationException, IllegalAccessException
    {
	if (e.getPlayer().getGameMode() == GameMode.CREATIVE)
	{
	    e.getBlock().setMetadata("creative", new FixedMetadataValue(schism, true));
	    cache.addPlaced(e.getBlock().getLocation());
	    cache.removeFromRemoved(e.getBlock().getLocation());
	}
    }

    @EventHandler
    public void onFall(EntityChangeBlockEvent event)
    {
	if (event.getBlock().getMetadata("creative").size() > 0)
	{
	    if (event.getBlock().getMetadata("creative").get(0).asBoolean())
	    {
		if(event.getTo()==Material.AIR)
		{
		    event.setCancelled(true);
		}
	    }
	}
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void breakBlock(BlockBreakEvent e)
    {
	if (e.getBlock().getMetadata("creative").size() > 0 && !e.isCancelled())
	{
	    if (e.getBlock().getMetadata("creative").get(0).asBoolean())
	    {
		e.setCancelled(true);
		e.getBlock().setType(Material.AIR);
		e.getBlock().setMetadata("creative", new FixedMetadataValue(schism, false));
		cache.removeFromPlaced(e.getBlock().getLocation());
		cache.addRemoved(e.getBlock().getLocation());
	    }
	}
    }
}
