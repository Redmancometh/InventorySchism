package com.redman.listeners;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener
{
    @EventHandler
    public void onDeath(PlayerDeathEvent e)
    {
	if(e.getEntity().getGameMode()==GameMode.CREATIVE)
	{
	    e.setKeepInventory(true);
	}
    }
}
