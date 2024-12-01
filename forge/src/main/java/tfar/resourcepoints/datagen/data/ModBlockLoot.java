package tfar.resourcepoints.datagen.data;

import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.world.level.block.Block;
import tfar.resourcepoints.ResourcePoints;
import tfar.resourcepoints.init.ModBlocks;

public class ModBlockLoot extends VanillaBlockLoot {

    @Override
    protected void generate() {
        dropSelf(ModBlocks.DEPOSIT_TERMINAL);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ResourcePoints.getKnownBlocks().toList();
    }
}
