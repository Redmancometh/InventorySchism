package com.redman.persistence;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.bukkit.Bukkit;
import org.bukkit.World;

import com.redman.inventoryschism.InventorySchism;

public class WorldSaveThread extends Thread
{
    private InventorySchism schism;
    private Lock lock = new ReentrantLock();
    public WorldSaveThread(InventorySchism schism)
    {
	this.schism = schism;
    }
    public void run()
    {
	if (!schism.properShutdown())
	{
	    System.out.println("[WARNING]: Server was shutdown improperly, so InventorySchism will now save the world to prevent desync!");
	    lock.lock();
	    for(World w : Bukkit.getWorlds())
	    {
		w.save();
	    }
	    lock.unlock();
	}
    }
}
