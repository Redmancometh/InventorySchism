package com.redman.listeners;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.metadata.FixedMetadataValue;
import com.redman.inventoryschism.InventorySchism;
public class GrowthListener implements Listener
{
    private InventorySchism schism;

    public GrowthListener(InventorySchism schism)
    {
	this.schism = schism;
    }
    @EventHandler
    public void plantGrowth(StructureGrowEvent e)
    {
	for(BlockState state : e.getBlocks())
	{
	    state.getBlock().setMetadata("creative", new FixedMetadataValue(schism, true));
	    schism.getPrimaryCache().addPlaced(state.getBlock().getLocation());
	}
    }
}
