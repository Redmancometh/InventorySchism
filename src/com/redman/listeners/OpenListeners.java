package com.redman.listeners;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;

public class OpenListeners implements Listener
{
    @EventHandler
    public void stopContainerOpen(InventoryOpenEvent e)
    {
	Player p = (Player) e.getPlayer();
	if(p.getGameMode()==GameMode.CREATIVE)
	{
	    if(!(e.getInventory().getType()==InventoryType.CREATIVE))
	    {
		e.setCancelled(true);
	    }
	}
    }
}
