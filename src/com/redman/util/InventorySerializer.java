package com.redman.util;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
public class InventorySerializer
{
    @SuppressWarnings("deprecation")
    public static String InventoryToString(Inventory invInventory)
    {
	String serialization = invInventory.getSize() + ";";
	for (int i = 0; i < invInventory.getSize(); i++)
	{
	    ItemStack is = invInventory.getItem(i);
	    if (is != null)
	    {
		String serializedItemStack = new String();
		String isType = String.valueOf(is.getType().getId());
		serializedItemStack += "t@" + isType;
		if (is.getDurability() != 0)
		{
		    String isDurability = String.valueOf(is.getDurability());
		    serializedItemStack += ":d@" + isDurability;
		}

		if (is.getAmount() != 1)
		{
		    String isAmount = String.valueOf(is.getAmount());
		    serializedItemStack += ":a@" + isAmount;
		}

		Map<Enchantment, Integer> isEnch = is.getEnchantments();
		if (isEnch.size() > 0)
		{
		    for (Entry<Enchantment, Integer> ench : isEnch.entrySet())
		    {
			serializedItemStack += ":e@" + ench.getKey().getId() + "@" + ench.getValue();
		    }
		}
		if(is.hasItemMeta())
		{
		    ItemMeta imeta = is.getItemMeta();
		    if(imeta.hasDisplayName())
		    {
			serializedItemStack += ":dn@"+imeta.getDisplayName();
		    }
		    if(imeta.hasLore())
		    {
			serializedItemStack += ":l@"+imeta.getLore();
		    }
		}
		serialization += i + "#" + serializedItemStack + ";";
	    }
	}
	return serialization;
    }

    @SuppressWarnings("deprecation")
    public static String ItemstacksToString(ItemStack[] invInventory)
    {
	String serialization = invInventory.length + ";";
	for (int i = 0; i < invInventory.length; i++)
	{
	    ItemStack is = invInventory[i];
	    if (is != null)
	    {
		String serializedItemStack = new String();
		String isType = String.valueOf(is.getType().getId());
		serializedItemStack += "t@" + isType;
		if (is.getDurability() != 0)
		{
		    String isDurability = String.valueOf(is.getDurability());
		    serializedItemStack += ":d@" + isDurability;
		}

		if (is.getAmount() != 1)
		{
		    String isAmount = String.valueOf(is.getAmount());
		    serializedItemStack += ":a@" + isAmount;
		}

		Map<Enchantment, Integer> isEnch = is.getEnchantments();
		if (isEnch.size() > 0)
		{
		    for (Entry<Enchantment, Integer> ench : isEnch.entrySet())
		    {
			serializedItemStack += ":e@" + ench.getKey().getId() + "@" + ench.getValue();
		    }
		}
		if(is.hasItemMeta())
		{
		    ItemMeta imeta = is.getItemMeta();
		    if(imeta.hasDisplayName())
		    {
			serializedItemStack += ":dn@"+imeta.getDisplayName();
		    }
		    if(imeta.hasLore())
		    {
			serializedItemStack += ":l@"+imeta.getLore();
		    }
		}
		serialization += i + "#" + serializedItemStack + ";";
	    }
	}
	return serialization;
    }
    
    @SuppressWarnings({ "deprecation"})
    public static Inventory StringToInventory(String invString)
    {
	if(invString==null){return null;}
	String[] serializedBlocks = invString.split(";");
	String invInfo = serializedBlocks[0];
	Inventory deserializedInventory = Bukkit.getServer().createInventory(null, Integer.valueOf(invInfo));

	for (int i = 1; i < serializedBlocks.length; i++)
	{
	    String[] serializedBlock = serializedBlocks[i].split("#");
	    int stackPosition = Integer.valueOf(serializedBlock[0]);

	    if (stackPosition >= deserializedInventory.getSize())
	    {
		continue;
	    }

	    ItemStack is = null;
	    Boolean createdItemStack = false;
	    ItemMeta im = null;
	    String[] serializedItemStack = serializedBlock[1].split(":");
	    for (String itemInfo : serializedItemStack)
	    {
		String[] itemAttribute = itemInfo.split("@");
		if (itemAttribute[0].equals("t"))
		{
		    is = new ItemStack(Material.getMaterial(Integer.valueOf(itemAttribute[1])));
		    createdItemStack = true;
		    im=is.getItemMeta();
		}
		else if (itemAttribute[0].equals("d") && createdItemStack)
		{
		    is.setDurability(Short.valueOf(itemAttribute[1]));
		}
		else if (itemAttribute[0].equals("a") && createdItemStack)
		{
		    is.setAmount(Integer.valueOf(itemAttribute[1]));
		}
		else if (itemAttribute[0].equals("e") && createdItemStack)
		{
		    is.addEnchantment(Enchantment.getById(Integer.valueOf(itemAttribute[1])), Integer.valueOf(itemAttribute[2]));
		}
		else if (itemAttribute[0].equalsIgnoreCase("dn"))
		{
		    im.setDisplayName(itemAttribute[1]);
		}
		else if (itemAttribute[0].equalsIgnoreCase("l"))
		{
		    List<String> lore = new ArrayList<String>();
		    itemAttribute[1]=itemAttribute[1].replace("[", "");
		    itemAttribute[1]=itemAttribute[1].replace("]", "");
		    lore=Arrays.asList(itemAttribute[1].split(","));
		    for(int x = 0; x<lore.size(); x++)
		    {
			String s = lore.get(x);
			if(s.charAt(0)==' ')
			{
			    s=s.replaceFirst(" ", "");
			}
			lore.set(x, s);
		    }
		    im.setLore(lore);
		}
	    }
	    if(im.hasDisplayName()||im.hasLore())
	    {
		is.setItemMeta(im);    
	    }
	    deserializedInventory.setItem(stackPosition, is);
	}
	return deserializedInventory;
    }
    @SuppressWarnings({ "deprecation"})
    public static ItemStack[] StringToItemstacks(String invString)
    {
	if(invString==null){return null;}
	String[] serializedBlocks = invString.split(";");
	String invInfo = serializedBlocks[0];
	Inventory deserializedInventory = Bukkit.getServer().createInventory(null, Integer.valueOf(invInfo));

	for (int i = 1; i < serializedBlocks.length; i++)
	{
	    String[] serializedBlock = serializedBlocks[i].split("#");
	    int stackPosition = Integer.valueOf(serializedBlock[0]);

	    if (stackPosition >= deserializedInventory.getSize())
	    {
		continue;
	    }

	    ItemStack is = null;
	    Boolean createdItemStack = false;
	    ItemMeta im = null;
	    String[] serializedItemStack = serializedBlock[1].split(":");
	    for (String itemInfo : serializedItemStack)
	    {
		String[] itemAttribute = itemInfo.split("@");
		if (itemAttribute[0].equals("t"))
		{
		    is = new ItemStack(Material.getMaterial(Integer.valueOf(itemAttribute[1])));
		    createdItemStack = true;
		    im=is.getItemMeta();
		}
		else if (itemAttribute[0].equals("d") && createdItemStack)
		{
		    is.setDurability(Short.valueOf(itemAttribute[1]));
		}
		else if (itemAttribute[0].equals("a") && createdItemStack)
		{
		    is.setAmount(Integer.valueOf(itemAttribute[1]));
		}
		else if (itemAttribute[0].equals("e") && createdItemStack)
		{
		    is.addEnchantment(Enchantment.getById(Integer.valueOf(itemAttribute[1])), Integer.valueOf(itemAttribute[2]));
		}
		else if (itemAttribute[0].equalsIgnoreCase("dn"))
		{
		    im.setDisplayName(itemAttribute[1]);
		}
		else if (itemAttribute[0].equalsIgnoreCase("l"))
		{
		    List<String> lore = new ArrayList<String>();
		    itemAttribute[1]=itemAttribute[1].replace("[", "");
		    itemAttribute[1]=itemAttribute[1].replace("]", "");
		    lore=Arrays.asList(itemAttribute[1].split(","));
		    for(int x = 0; x<lore.size(); x++)
		    {
			String s = lore.get(x);
			if(s.charAt(0)==' ')
			{
			    s=s.replaceFirst(" ", "");
			}
			lore.set(x, s);
		    }
		    im.setLore(lore);
		}
	    }
	    if(im.hasDisplayName()||im.hasLore())
	    {
		is.setItemMeta(im);    
	    }
	    deserializedInventory.setItem(stackPosition, is);
	}
	return deserializedInventory.getContents();
    }

}