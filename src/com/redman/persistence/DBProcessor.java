package com.redman.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import com.redman.caching.CacheProcessor;
import com.redman.inventoryschism.InventorySchism;
import com.redman.util.InventorySerializer;

public class DBProcessor
{
    String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private String DB_URL;
    private String user;
    private String password;
    CacheProcessor cache;
    public DBProcessor(CacheProcessor cache, InventorySchism schism)
    {
	FileConfiguration conf = schism.getConfiguration();
	user = conf.getString("DBUser");
	password = conf.getString("DBPassword");
	DB_URL = conf.getString("DBURL");
	this.cache = cache;
    }

    public void createBlockTable()
    {
	Connection conn;
	try
	{
	    conn = DriverManager.getConnection(DB_URL, user, password);
	    conn.prepareStatement("CREATE table Blocks(World varchar(50),x int, y int, z int);").execute();
	}
	catch (com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException e)
	{
	    return;
	}
	catch (SQLException e)
	{
	    e.printStackTrace();
	}
    }

    public void createInventoryTable()
    {
	Connection conn;
	try
	{
	    conn = DriverManager.getConnection(DB_URL, user, password);
	    conn.prepareStatement("CREATE table Inventories(creative BOOLEAN,uuid varchar(40),inventory varchar(1024), armor varchar(250));").execute();
	}
	catch (com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException e)
	{
	    return;
	}
	catch (SQLException e)
	{
	    e.printStackTrace();
	}
    }

    public void createTables()
    {
	createInventoryTable();
	createBlockTable();
    }

    public Inventory getInventory(boolean isCreative, Player p) throws SQLException
    {
	Connection conn = DriverManager.getConnection(DB_URL, user, password);
	PreparedStatement inventoryQuery = conn.prepareStatement("select inventory from Inventories where uuid=? and creative=?");
	inventoryQuery.setString(1, p.getUniqueId().toString());
	inventoryQuery.setBoolean(2, isCreative);
	ResultSet rs = inventoryQuery.executeQuery();
	if (rs.first())
	{
	    return InventorySerializer.StringToInventory(rs.getString(1));
	}
	else
	{
	    return null;
	}
    }

    public Inventory getArmor(boolean isCreative, Player p) throws SQLException
    {
	Connection conn = DriverManager.getConnection(DB_URL, user, password);
	PreparedStatement inventoryQuery = conn.prepareStatement("select armor from Inventories where uuid=? and creative=?");
	inventoryQuery.setString(1, p.getUniqueId().toString());
	inventoryQuery.setBoolean(2, isCreative);
	ResultSet rs = inventoryQuery.executeQuery();
	if (rs.first())
	{
	    //conn.close();
	    return InventorySerializer.StringToInventory(rs.getString(1));
	}
	else
	{
	    //conn.close();
	    return null;
	}
    }

    public void saveInventory(boolean isCreative, Player p, String inv, String armor) throws SQLException
    {
	Connection conn = DriverManager.getConnection(DB_URL, user, password);
	PreparedStatement inventoryQuery = conn.prepareStatement("select * from Inventories where uuid=? AND creative=?;");
	inventoryQuery.setString(1, p.getUniqueId().toString());
	inventoryQuery.setBoolean(2, isCreative);
	ResultSet rs = inventoryQuery.executeQuery();
	if (rs.next())
	{
	    PreparedStatement updateQuery = conn.prepareStatement("UPDATE Inventories set inventory=?, armor=? WHERE uuid=? AND creative=?;");
	    updateQuery.setString(1, inv);
	    updateQuery.setString(2, armor);
	    updateQuery.setString(3, p.getUniqueId().toString());
	    updateQuery.setBoolean(4, isCreative);
	    updateQuery.execute();
	}
	else
	{
	    PreparedStatement insertInventory = conn.prepareStatement("INSERT INTO Inventories(creative,uuid,inventory,armor) VALUES(?,?,?,?) ON DUPLICATE KEY UPDATE creative=?,uuid=?,inventory=?, armor=?;");
	    insertInventory.setBoolean(1, isCreative);
	    insertInventory.setString(2, p.getUniqueId().toString());
	    insertInventory.setString(3, inv);
	    insertInventory.setString(4, armor);
	    insertInventory.setBoolean(5, isCreative);
	    insertInventory.setString(6, p.getUniqueId().toString());
	    insertInventory.setString(7, inv);
	    insertInventory.setString(8, armor);
	    insertInventory.execute();
	}
	//conn.close();
    }

    public void saveInventory(boolean isCreative, Player p, Inventory inv, Inventory armor) throws SQLException
    {
	Connection conn = DriverManager.getConnection(DB_URL, user, password);
	PreparedStatement inventoryQuery = conn.prepareStatement("select * from Inventories where uuid=? AND creative=?;");
	inventoryQuery.setString(1, p.getUniqueId().toString());
	inventoryQuery.setBoolean(2, isCreative);
	ResultSet rs = inventoryQuery.executeQuery();
	if (rs.next())
	{
	    PreparedStatement updateQuery = conn.prepareStatement("UPDATE Inventories set inventory=?, armor=? WHERE uuid=? AND creative=?;");
	    updateQuery.setString(1, InventorySerializer.InventoryToString(inv));
	    updateQuery.setString(2, InventorySerializer.InventoryToString(armor));
	    updateQuery.setString(3, p.getUniqueId().toString());
	    updateQuery.setBoolean(4, isCreative);
	    updateQuery.execute();
	}
	else
	{
	    PreparedStatement insertInventory = conn.prepareStatement("INSERT INTO Inventories(creative,uuid,inventory,armor) VALUES(?,?,?,?) ON DUPLICATE KEY UPDATE creative=?,uuid=?,inventory=?, armor=?;");
	    insertInventory.setBoolean(1, isCreative);
	    insertInventory.setString(2, p.getUniqueId().toString());
	    insertInventory.setString(3, InventorySerializer.InventoryToString(inv));
	    insertInventory.setString(4, InventorySerializer.InventoryToString(armor));
	    insertInventory.setBoolean(5, isCreative);
	    insertInventory.setString(6, p.getUniqueId().toString());
	    insertInventory.setString(7, InventorySerializer.InventoryToString(inv));
	    insertInventory.setString(8, InventorySerializer.InventoryToString(armor));
	    insertInventory.execute();
	}
	//conn.close();
    }

    public void saveRemovedBlocks() throws SQLException
    {
	Connection conn = DriverManager.getConnection(DB_URL, user, password);
	conn.setAutoCommit(false);
	PreparedStatement deleteStatement = conn.prepareStatement("DELETE from Blocks where world=? AND x=? AND y=? AND z=?");
	for (Location loc1 : cache.getRemoved())
	{
	    deleteStatement.setString(1, loc1.getWorld().getName());
	    deleteStatement.setInt(2, loc1.getBlockX());
	    deleteStatement.setInt(3, loc1.getBlockY());
	    deleteStatement.setInt(4, loc1.getBlockZ());
	    deleteStatement.addBatch();
	}
	Bukkit.getLogger().log(Level.INFO, ChatColor.RED + "" + cache.getRemoved().size() + "Removed");
	deleteStatement.executeBatch();
	conn.commit();
	cache.clearRemoved();
	//conn.close();
    }

    public void savePlacedBlocks() throws SQLException
    {
	Connection conn = DriverManager.getConnection(DB_URL, user, password);
	conn.setAutoCommit(false);
	PreparedStatement insertStatement = conn.prepareStatement("INSERT into Blocks VALUES(?,?,?,?);");
	for (Location loc1 : cache.getPlaced())
	{
	    insertStatement.setString(1, loc1.getWorld().getName());
	    insertStatement.setInt(2, loc1.getBlockX());
	    insertStatement.setInt(3, loc1.getBlockY());
	    insertStatement.setInt(4, loc1.getBlockZ());
	    insertStatement.addBatch();
	}
	Bukkit.getLogger().log(Level.INFO, ChatColor.RED + "" + cache.getPlaced().size() + " Placed blocks inserted");
	insertStatement.executeBatch();
	conn.commit();
	insertStatement.close();
	cache.clearPlaced();
	//conn.close();
    }

    public void setBlocksMeta(final Plugin pl, World world)
    {
	try
	{
	    Connection conn = DriverManager.getConnection(DB_URL, user, password);
	    PreparedStatement psMeta = conn.prepareStatement("SELECT * from Blocks where world=?");
	    psMeta.setString(1, world.getName());
	    ResultSet rs = psMeta.executeQuery();
	    while (rs.next())
	    {
		try
		{
		    int x = rs.getInt(2);
		    int y = rs.getInt(3);
		    int z = rs.getInt(4);
		    Location loc1 = new Location(world, x, y, z);
		    loc1.getBlock().setMetadata("creative", new FixedMetadataValue(pl, true));
		}
		catch (NullPointerException e)
		{
		    System.out.println("Error setting metadata on block at: world: " + rs.getString(1) + "x:" + rs.getInt(2) + " y:" + rs.getInt(3) + " z:" + rs.getInt(4));
		    continue;
		}
	    }
	    //conn.close();
	}
	catch (SQLException e)
	{
	    e.printStackTrace();
	}
    }
}
