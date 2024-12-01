package tfar.resourcepoints.client;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import tfar.resourcepoints.ResourcePoints;

import java.util.List;

public class ModClient {
    static void tooltip(ItemStack stack, List<Component> tooltip) {
        if (ResourcePoints.RESOURCE_POINTS.containsKey(stack.getItem())) {
            tooltip.add(Component.literal("Resource points: "+ResourcePoints.RESOURCE_POINTS.getInt(stack.getItem())));
        }
    }
}
