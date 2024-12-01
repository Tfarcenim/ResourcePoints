package tfar.resourcepoints.blockentity;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import tfar.resourcepoints.init.ModBlockEntityTypes;
import tfar.resourcepoints.menu.DepositTerminalMenu;

public class DepositTerminalBlockEntity extends BlockEntity implements MenuProvider {

    public final Container container = new SimpleContainer(9);

    public final Object2IntMap<Item> MULTIPLIERS = new Object2IntOpenHashMap<>();

    public DepositTerminalBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.DEPOSIT_TERMINAL, pos, state);
    }

    @Override
    public Component getDisplayName() {
        return getBlockState().getBlock().getName();
    }

    @Override
    protected void saveAdditional(CompoundTag $$0) {
        super.saveAdditional($$0);
    }

    @Override
    public void load(CompoundTag $$0) {
        super.load($$0);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new DepositTerminalMenu(i,inventory,container);
    }
}
