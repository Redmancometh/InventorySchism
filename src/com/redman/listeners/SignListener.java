package com.redman.listeners;
import net.ess3.api.events.SignEvent;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SignListener implements Listener
{
    @EventHandler
    public void stopEssentials(SignEvent e)
    {	    
	if(e.getUser().getBase().getGameMode()==GameMode.CREATIVE)
	{
	    String s = ChatColor.stripColor(e.getEssentialsSign().getSuccessName());
	    if(s.equalsIgnoreCase("[sell]")||s.equalsIgnoreCase("[buy]"))
	    {
		e.setCancelled(true);
	    }
	}
    }
}
