package com.redman.listeners;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
public class ItemStackListeners implements Listener
{
    @EventHandler
    public void stuff(PlayerDropItemEvent e)
    {
	if (e.getPlayer().getGameMode() == GameMode.CREATIVE)
	{
	    e.setCancelled(true);
	}
    }
}