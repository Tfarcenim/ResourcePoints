package tfar.resourcepoints.client;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ModClientForge {
    public static void init(IEventBus bus) {
        bus.addListener(ModClientForge::setup);
        MinecraftForge.EVENT_BUS.addListener((ItemTooltipEvent event) ->ModClient.tooltip(event.getItemStack(),event.getToolTip()));
    }

    static void setup(FMLClientSetupEvent event) {
        ModClient.setup();
    }
}
