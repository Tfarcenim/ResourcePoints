package tfar.resourcepoints.client;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class ModClientForge {
    public static void init(IEventBus bus) {
        MinecraftForge.EVENT_BUS.addListener((ItemTooltipEvent event) ->ModClient.tooltip(event.getItemStack(),event.getToolTip()));
    }
}
