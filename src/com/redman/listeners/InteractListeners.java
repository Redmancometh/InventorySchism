package com.redman.listeners;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractListeners implements Listener
{
    @SuppressWarnings("deprecation")
    @EventHandler
    public void stopStuff(PlayerInteractEvent e)
    {
	if (e.getItem() != null && e.getItem().getTypeId() != 0 && e.getPlayer().getGameMode() == GameMode.CREATIVE)
	{
	    switch (e.getItem().getType())
	    {
	    case INK_SACK:
		e.setCancelled(true);
	    case STONE_HOE:
		e.setCancelled(true);
	    case IRON_HOE:
		e.setCancelled(true);
	    case WOOD_HOE:
		e.setCancelled(true);
	    case GOLD_HOE:
		e.setCancelled(true);
	    case DIAMOND_HOE:
		e.setCancelled(true);
	    case BOW:
		e.setCancelled(true);
	    default:
		return;
	    }
	}
    }

    @EventHandler
    public void placeHanger(HangingPlaceEvent e)
    {
	if (e.getPlayer().getGameMode() == GameMode.CREATIVE)
	{
	    if (!(e.getBlock().getMetadata("creative").size() > 0))
	    {
		return;
	    }
	    else
	    {
		e.getPlayer().sendMessage(ChatColor.GOLD + "[InventorySchism]: " + ChatColor.GOLD + " You are in creative mode!");
		e.getPlayer().sendMessage(ChatColor.GOLD + "[InventorySchism]: " + ChatColor.GOLD + " This item must be placed on a block which was placed in creative mode!");
	    }
	}
	else
	{
	    if (e.getBlock().getMetadata("creative").size() > 0)
	    {
		e.getPlayer().sendMessage(ChatColor.GOLD + "[InventorySchism]: " + ChatColor.GOLD + " You are in survival mode!");
		e.getPlayer().sendMessage(ChatColor.GOLD + "[InventorySchism]: " + ChatColor.GOLD + " The block you are trying to place this on was placed in creative mode, try another block!");
		return;
	    }
	}
    }

    @EventHandler
    public void breakHanger(HangingBreakEvent e)
    {
	Block b = e.getEntity().getLocation().getBlock();
	Block attached = b.getRelative(e.getEntity().getAttachedFace());
	if (attached.getMetadata("creative").size() > 0)
	{
	    e.getEntity().remove();
	}
    }
}
