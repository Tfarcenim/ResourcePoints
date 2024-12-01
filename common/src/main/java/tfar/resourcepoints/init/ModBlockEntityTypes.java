package tfar.resourcepoints.init;

import net.minecraft.world.level.block.entity.BlockEntityType;
import tfar.resourcepoints.blockentity.DepositTerminalBlockEntity;

public class ModBlockEntityTypes {

    public static final BlockEntityType<DepositTerminalBlockEntity> DEPOSIT_TERMINAL = BlockEntityType.Builder.of(DepositTerminalBlockEntity::new,ModBlocks.DEPOSIT_TERMINAL).build(null);

}
