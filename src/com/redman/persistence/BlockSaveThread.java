package com.redman.persistence;

import java.sql.SQLException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import com.redman.inventoryschism.InventorySchism;
import com.redman.listeners.WorldSaveListener;

public class BlockSaveThread extends Thread
{
    private InventorySchism schism;
    private Lock lock = new ReentrantLock();

    public BlockSaveThread(InventorySchism schism)
    {
	this.schism = schism;
    }

    public void run()
    {
	lock.lock();
	System.out.println("[WARNING]: InventorySchism is Saving blocks!");
	new WorldSaveListener(schism.getPrimaryDB(), schism).saveBlocksSync();
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
