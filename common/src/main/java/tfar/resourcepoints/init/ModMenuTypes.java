package tfar.resourcepoints.init;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import tfar.resourcepoints.menu.DepositTerminalMenu;

public class ModMenuTypes {
    public static final MenuType<DepositTerminalMenu> DEPOSIT_TERMINAL = new MenuType<>((int $$0, Inventory id) -> new DepositTerminalMenu($$0, id), FeatureFlags.VANILLA_SET);
}
