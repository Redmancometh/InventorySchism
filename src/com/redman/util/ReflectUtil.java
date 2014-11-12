package com.redman.util;
import java.lang.reflect.Field;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import net.minecraft.server.v1_7_R4.Block;
import net.minecraft.server.v1_7_R4.NBTTagCompound;
import net.minecraft.server.v1_7_R4.TileEntity;
import net.minecraft.server.v1_7_R4.World;
public class ReflectUtil
{
    @SuppressWarnings("unchecked")
    public static void setTileFields(TileEntity t, org.bukkit.block.Block block, Block nmsBlock) throws IllegalArgumentException, IllegalAccessException
    {
	World w = ((CraftWorld) block.getWorld()).getHandle();
	NBTTagCompound nbt = new NBTTagCompound();
	nbt.setString("id", "Chest");
	nbt.setBoolean("creative", true);
	nbt.setInt("x", block.getX());
	nbt.setInt("y", block.getY());
	nbt.setInt("z", block.getZ());
	t.a(w);
	t.a(nbt);
	w.tileEntityList.add(t);
	for (Field field : t.getClass().getDeclaredFields())
	{
	    if (field.getName().equalsIgnoreCase("x"))
	    {
		field.setInt(t, block.getX());
	    }
	    if (field.getName().equalsIgnoreCase("y"))
	    {
		field.setInt(t, (int) block.getY());
	    }
	    if (field.getName().equalsIgnoreCase("z"))
	    {
		field.setInt(t, (int) block.getZ());
	    }
	    if (field.getName().equalsIgnoreCase("h"))
	    {
		field.set(t, nmsBlock);
	    }
	    if (field.getName().equalsIgnoreCase("g"))
	    {
		field.setInt(t, 0);
	    }
	}
    }
}
