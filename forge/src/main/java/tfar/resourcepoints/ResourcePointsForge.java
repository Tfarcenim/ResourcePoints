package tfar.resourcepoints;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.RegisterEvent;
import org.apache.commons.lang3.tuple.Pair;
import tfar.resourcepoints.client.ModClient;
import tfar.resourcepoints.client.ModClientForge;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Mod(ResourcePoints.MOD_ID)
public class ResourcePointsForge {
    
    public ResourcePointsForge() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::registerObjs);
        bus.addListener(this::onInitialize);
        if (FMLEnvironment.dist.isClient()) {
            ModClientForge.init(bus);
        }
        // This method is invoked by the Forge mod loader when it is ready
        // to load your mod. You can access Forge and Common code in this
        // project.
        MinecraftForge.EVENT_BUS.addListener((AddReloadListenerEvent event) -> event.addListener(new ResourcePointsValuesReloadListener()));
        MinecraftForge.EVENT_BUS.addListener((OnDatapackSyncEvent event) -> ResourcePoints.sync(event.getPlayer()));
        MinecraftForge.EVENT_BUS.addListener((RegisterCommandsEvent event) -> ModCommands.register(event.getDispatcher(),event.getBuildContext()));
        // Use Forge to bootstrap the Common mod.
        ResourcePoints.init();
    }

    public static Map<Registry<?>, List<Pair<ResourceLocation, Supplier<Object>>>> registerLater = new HashMap<>();

    void registerObjs(RegisterEvent event) {
        for (Map.Entry<Registry<?>,List<Pair<ResourceLocation, Supplier<Object>>>> entry : registerLater.entrySet()) {
            Registry<?> registry = entry.getKey();
            List<Pair<ResourceLocation, Supplier<Object>>> toRegister = entry.getValue();
            for (Pair<ResourceLocation,Supplier<Object>> pair : toRegister) {
                event.register((ResourceKey<? extends Registry<Object>>)registry.key(),pair.getLeft(), pair.getValue());
            }
        }
    }

    void onInitialize(FMLCommonSetupEvent e) {
        registerLater.clear();
    }


}