package com.redman.listeners;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
public class ProjectileListener implements Listener
{
    @SuppressWarnings("deprecation")
    @EventHandler
    public void stopThrows(ProjectileLaunchEvent e)
    {
	Projectile projectile = e.getEntity();
	if(projectile.getShooter() instanceof Player)
	{
	    Player p = (Player) projectile.getShooter();
	    if(p.getGameMode()==GameMode.CREATIVE)
	    {
		e.setCancelled(true);
	    }
	}
    }
}
