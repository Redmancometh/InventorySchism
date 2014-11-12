package com.redman.listeners;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.metadata.FixedMetadataValue;

import com.redman.caching.CacheProcessor;
import com.redman.inventoryschism.InventorySchism;

public class ChunkListener implements Listener
{
    private CacheProcessor cache;
    private InventorySchism schism;
    public ChunkListener(InventorySchism schism, CacheProcessor cache)
    {
	this.cache=cache;
    }
    @EventHandler
    public void onLoad(ChunkLoadEvent e)
    {
	for(Location loc1 : cache.getChunkBlocks(e.getChunk()))
	{
	    loc1.getBlock().setMetadata("creative", new FixedMetadataValue(schism, true));
	}
    }
}
