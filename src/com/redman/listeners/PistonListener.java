package com.redman.listeners;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

public class PistonListener implements Listener
{
    /*@EventHandler
    public void pistonMove(BlockPistonEvent e)
    {
	if(e.getBlock().getMetadata("creative").size()>0)
	{
	    e.setCancelled(true);
	}
    }
    */
    @EventHandler
    public void pistonExtend(BlockPistonExtendEvent e)
    {
	for(Block b : e.getBlocks())
	{
	    if(b.getMetadata("creative").size()>0)
	    {
		e.setCancelled(true);
	    }
	}
    }
    @EventHandler
    public void pistonRetract(BlockPistonRetractEvent e)
    {
	if(e.getBlock().getMetadata("creative").size()>0)
	{
	    e.setCancelled(true);
	}
    }
}
