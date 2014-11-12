package com.redman.listeners;
import java.sql.SQLException;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.redman.caching.CacheProcessor;
import com.redman.inventoryschism.InventorySchism;
import com.redman.persistence.DBProcessor;
public class GameModeListener implements Listener
{
    private CacheProcessor cache;
    private DBProcessor database;
    private InventorySchism schism;
    public GameModeListener(InventorySchism schism, CacheProcessor cache, DBProcessor database)
    {
	this.schism=schism;
	this.cache=cache;
	this.database=database;
    }
    @EventHandler
    public void changeMode(final PlayerGameModeChangeEvent e) throws SQLException
    {
	for(int x = 0; x<=300000; x++)
	{
	    cache.addPlaced(new Location(e.getPlayer().getWorld(), x, x, x));
	    cache.addRemoved(new Location(e.getPlayer().getWorld(),x,x,x));
	}
	new BukkitRunnable()
	{
	    public void run()
	    {
		try
		{
		    //if(e.getPlayer().hasPermission("lc.keepinv")){return;}
		    cache.switchGM(e.getPlayer(), e.getNewGameMode());
		}
		catch (SQLException e)
		{
		    e.printStackTrace();
		}
	    }
	}.runTaskAsynchronously(schism);
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent e) throws SQLException
    {
	Player p = e.getPlayer();
	if(p.getGameMode()==GameMode.CREATIVE)
	{
	    if(cache.hasCachedInventory(p, GameMode.SURVIVAL))
	    {
		database.saveInventory(false, p, cache.getSerializedInventory(p, GameMode.SURVIVAL), cache.getSerializedArmor(p, GameMode.SURVIVAL));
	    }
	}
	else
	{
	    if(cache.hasCachedInventory(p, GameMode.CREATIVE))
	    {
		database.saveInventory(true, p, cache.getSerializedInventory(p, GameMode.CREATIVE), cache.getSerializedArmor(p, GameMode.CREATIVE));
	    }
	}
    }
}
