package tfar.resourcepoints.menu;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import tfar.resourcepoints.init.ModMenuTypes;

public class DepositTerminalMenu extends AbstractContainerMenu {
    public DepositTerminalMenu(int id, Inventory inventory, Container container) {
        super(ModMenuTypes.DEPOSIT_TERMINAL, id);

        int x;
        int y;
        for(x = 0; x < 3; ++x) {
            for(y = 0; y < 3; ++y) {
                this.addSlot(new Slot(container, y + x * 3, 62 + y * 18, 17 + x * 18));
            }
        }

        for(x = 0; x < 3; ++x) {
            for(y = 0; y < 9; ++y) {
                this.addSlot(new Slot(inventory, y + x * 9 + 9, 8 + y * 18, 84 + x * 18));
            }
        }

        for(x = 0; x < 9; ++x) {
            this.addSlot(new Slot(inventory, x, 8 + x * 18, 142));
        }

    }

    public DepositTerminalMenu(int id, Inventory inv) {
        this(id, inv,new SimpleContainer(9));
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
