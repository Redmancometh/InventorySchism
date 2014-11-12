package com.redman.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandListeners implements Listener
{
    @EventHandler
    public void stopCommands(PlayerCommandPreprocessEvent e)
    {
	String command = e.getMessage();
	if (command.equalsIgnoreCase("/stop") && e.getPlayer().isOp())
	{
	    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "save-all");
	    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "/save-all");
	}
	boolean forbidden = false;
	switch (command.toLowerCase())
	{
	case "/ehat":
	    forbidden = true;
	    break;
	case "/hat":
	    forbidden = true;
	    break;
	case "/essentials:hat":
	    forbidden = true;
	    break;
	case "/suicide":
	    forbidden = true;
	    break;
	case "/esuicide":
	    forbidden = true;
	    break;
	case "/essentials:suicide":
	    forbidden = true;
	    break;
	default:
	    return;
	}
	if (forbidden)
	{
	    e.getPlayer().sendMessage(ChatColor.GOLD + "[InventorySchism] " + ChatColor.AQUA + "You cannot use this command in creative mode!");
	    e.setCancelled(true);
	}
    }
}
