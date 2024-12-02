package tfar.resourcepoints.init;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import tfar.resourcepoints.block.DepositTerminalBlock;

public class ModBlocks {
    public static final Block DEPOSIT_TERMINAL = new DepositTerminalBlock(BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).isValidSpawn(ModBlocks::never));




    private static boolean never(BlockState $$0, BlockGetter $$1, BlockPos $$2, EntityType<?> $$3) {
        return false;
    }
}
