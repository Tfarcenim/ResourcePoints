package tfar.resourcepoints.datagen;

import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import tfar.resourcepoints.ResourcePoints;
import tfar.resourcepoints.init.ModBlocks;

public class ModBlockstateProvider extends BlockStateProvider {
    public ModBlockstateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, ResourcePoints.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(ModBlocks.DEPOSIT_TERMINAL, models().cubeBottomTop("block/deposit_terminal",
                ResourcePoints.id("block/deposit_terminal_sides"),ResourcePoints.id("block/deposit_terminal_bottom"),ResourcePoints.id("block/deposit_terminal_top")));
    }
}
