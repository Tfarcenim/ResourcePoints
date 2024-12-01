package tfar.resourcepoints.client;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import tfar.resourcepoints.ResourcePoints;
import tfar.resourcepoints.init.ModMenuTypes;

import java.util.List;

public class ModClient {
    static void tooltip(ItemStack stack, List<Component> tooltip) {
        if (ResourcePoints.RESOURCE_POINTS.containsKey(stack.getItem())) {
            tooltip.add(Component.literal("Resource points: "+ResourcePoints.RESOURCE_POINTS.getInt(stack.getItem())));
        }
    }

    public static void setup(){
        MenuScreens.register(ModMenuTypes.DEPOSIT_TERMINAL,DepositTerminalScreen::new);
    }
}
