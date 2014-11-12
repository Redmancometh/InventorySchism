package com.redman.caching;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.redman.inventoryschism.InventorySchism;
import com.redman.persistence.DBProcessor;
import com.redman.util.InventorySerializer;

public class CacheProcessor
{
    private List<Location> blocksPlaced = new ArrayList<Location>();
    private List<Location> blocksRemoved = new ArrayList<Location>();
    private Multimap<Chunk, Location> chunkMap = ArrayListMultimap.create();
    private HashMap<UUID, String> survivalInventories = new HashMap<UUID, String>();
    private HashMap<UUID, String> creativeInventories = new HashMap<UUID, String>();
    private HashMap<UUID, String> survivalArmor = new HashMap<UUID, String>();
    private HashMap<UUID, String> creativeArmor = new HashMap<UUID, String>();
    private InventorySchism schism;
    public CacheProcessor(InventorySchism schism)
    {
	this.schism = schism;
    }

    public void clearRemoved()
    {
	blocksRemoved.clear();
    }

    public void clearPlaced()
    {
	blocksPlaced.clear();
    }

    public List<Location> getPlaced()
    {
	return blocksPlaced;
    }

    public void removeFromRemoved(Location loc1)
    {
	blocksRemoved.remove(loc1);
    }

    public void removeFromPlaced(Location loc1)
    {
	blocksPlaced.remove(loc1);
    }

    public void addPlaced(Location loc1)
    {
	blocksPlaced.add(loc1);
    }

    public void addRemoved(Location loc1)
    {
	blocksRemoved.add(loc1);
    }

    public List<Location> getRemoved()
    {
	return blocksRemoved;
    }
    public void cacheArmor(Player p, String serializedArmor, GameMode type)
    {
	if (type == GameMode.CREATIVE)
	{
	    survivalArmor.put(p.getUniqueId(), serializedArmor);
	}
	else if (type == GameMode.SURVIVAL)
	{
	    creativeArmor.put(p.getUniqueId(), serializedArmor);
	}
    }
    public void cacheInventory(Player p, String serializedInv, GameMode type)
    {
	if (type == GameMode.CREATIVE)
	{
	    survivalInventories.put(p.getUniqueId(), serializedInv);
	}
	else if (type == GameMode.SURVIVAL)
	{
	    creativeInventories.put(p.getUniqueId(), serializedInv);
	}
    }

    public String getSerializedInventory(Player p, GameMode type)
    {
	if (type == GameMode.CREATIVE)
	{
	    return creativeInventories.get(p.getUniqueId());
	}
	else if (type == GameMode.SURVIVAL)
	{
	    return survivalInventories.get(p.getUniqueId());
	}
	return null;
    }
    public String getSerializedArmor(Player p, GameMode type)
    {
	if (type == GameMode.CREATIVE)
	{
	    return creativeArmor.get(p.getUniqueId());
	}
	else if (type == GameMode.SURVIVAL)
	{
	    return survivalArmor.get(p.getUniqueId());
	}
	return null;
    }
    public Inventory getArmor(Player p, GameMode type)
    {
	if (type == GameMode.CREATIVE)
	{
	    return InventorySerializer.StringToInventory(creativeArmor.get(p.getUniqueId()));
	}
	else if (type == GameMode.SURVIVAL)
	{
	    return InventorySerializer.StringToInventory(survivalArmor.get(p.getUniqueId()));
	}
	return null;
    }

    public Inventory getInventory(Player p, GameMode type)
    {
	if (type == GameMode.CREATIVE)
	{
	    return InventorySerializer.StringToInventory(creativeInventories.get(p.getUniqueId()));
	}
	else if (type == GameMode.SURVIVAL)
	{
	    return InventorySerializer.StringToInventory(survivalInventories.get(p.getUniqueId()));
	}
	return null;
    }

    public void setInventory(Player p, Inventory inv)
    {
	p.getInventory().clear();
	p.getInventory().setContents(inv.getContents());
	p.updateInventory();
    }
    public void setArmor(Player p, Inventory inv)
    {
	ItemStack[] armor = inv.getContents();
        p.getInventory().setHelmet(armor[3]);
        p.getInventory().setChestplate(armor[1]);
        p.getInventory().setLeggings(armor[2]);
        p.getInventory().setBoots(armor[0]);
	p.updateInventory();
    }
    public void clearArmor(Player p)
    {
        p.getInventory().setHelmet(null);
        p.getInventory().setChestplate(null);
        p.getInventory().setLeggings(null);
        p.getInventory().setBoots(null);
        p.updateInventory();
    }
    public void clearInventory(Player p)
    {
	p.getInventory().clear();
	p.updateInventory();
    }
    public boolean hasCachedArmor(Player p, GameMode type)
    {
	if (type == GameMode.CREATIVE)
	{
	    return creativeArmor.containsKey(p.getUniqueId());
	}
	else if (type == GameMode.SURVIVAL)
	{
	    return survivalArmor.containsKey(p.getUniqueId());
	}
	return false;
    }
    
    public boolean hasCachedInventory(Player p, GameMode type)
    {
	if (type == GameMode.CREATIVE)
	{
	    return creativeInventories.containsKey(p.getUniqueId());
	}
	else if (type == GameMode.SURVIVAL)
	{
	    return survivalInventories.containsKey(p.getUniqueId());
	}
	return false;
    }

    public void switchGM(Player p, GameMode type) throws SQLException
    {
	boolean isCreative = type == GameMode.CREATIVE;
	cacheInventory(p, InventorySerializer.InventoryToString(p.getInventory()), type);
	if (hasCachedInventory(p, type))
	{
	    setInventory(p, getInventory(p, type));
	}
	else
	{
	    Inventory dbInv = schism.getPrimaryDB().getInventory(isCreative, p);
	    if (dbInv == null)
	    {
		clearInventory(p);
	    }
	    else
	    {
		setInventory(p, dbInv);
	    }
	}
	Inventory armor = Bukkit.createInventory(p, 9);
	armor.setItem(0, p.getInventory().getBoots());
	armor.setItem(1, p.getInventory().getChestplate());
	armor.setItem(2, p.getInventory().getLeggings());
	armor.setItem(3, p.getInventory().getHelmet());
	cacheArmor(p, InventorySerializer.InventoryToString(armor), type);
	if (hasCachedArmor(p, type))
	{
	    setArmor(p, getArmor(p, type));
	}
	else
	{
	    Inventory armorInv = schism.getPrimaryDB().getArmor(isCreative, p);
	    if (armorInv == null)
	    {
		clearArmor(p);
	    }
	    else
	    {
		setArmor(p, armorInv);
	    }
	}
    }

    public void addMetaBlock(Location loc1)
    {
	chunkMap.put(loc1.getChunk(), loc1);
    }

    public Collection<Location> getChunkBlocks(Chunk c)
    {
	return chunkMap.get(c);
    }

    @SuppressWarnings("deprecation")
    public void saveAllInventories() throws SQLException
    {
	CacheProcessor cache = schism.getPrimaryCache();
	DBProcessor db = schism.getPrimaryDB();
	for (Player p : Bukkit.getOnlinePlayers())
	{
	    if(p.getGameMode()==GameMode.CREATIVE)
		{
		    if(cache.hasCachedInventory(p, GameMode.SURVIVAL))
		    {
			db.saveInventory(false, p, cache.getSerializedInventory(p, GameMode.SURVIVAL), cache.getSerializedArmor(p, GameMode.SURVIVAL));
		    }
		}
		else
		{
		    if(cache.hasCachedInventory(p, GameMode.CREATIVE))
		    {
			db.saveInventory(true, p, cache.getSerializedInventory(p, GameMode.CREATIVE), cache.getSerializedArmor(p, GameMode.CREATIVE));
		    }
		}
	}
    }
}
