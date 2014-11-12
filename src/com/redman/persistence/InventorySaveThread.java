package com.redman.persistence;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.bukkit.Bukkit;

import com.redman.inventoryschism.InventorySchism;
public class InventorySaveThread extends Thread
{
    private InventorySchism schism;
    private Lock lock = new ReentrantLock();
    public InventorySaveThread(InventorySchism schism)
    {
	this.schism=schism;
    }
    @SuppressWarnings("deprecation")
    public void run()
    {
	System.out.println(Bukkit.getOnlinePlayers().length);
	lock.lock();
	System.out.println("[Notice] InventorySchism: Saving inventories!");
	try
	{
	    schism.getPrimaryCache().saveAllInventories();
	}
	catch (SQLException e)
	{
	    e.printStackTrace();
	}
	lock.unlock();
    }
}
