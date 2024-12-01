package tfar.resourcepoints;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tfar.resourcepoints.network.PacketHandler;
import tfar.resourcepoints.network.client.S2CResourcePointsValuesPacket;
import tfar.resourcepoints.platform.Services;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as Forge events
// however it will be compatible with all supported mod loaders.
public class ResourcePoints {

    public static final String MOD_ID = "resourcepoints";
    public static final String MOD_NAME = "ResourcePoints";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    public static Object2IntMap<Item> RESOURCE_POINTS = new Object2IntOpenHashMap<>();

    // The loader specific projects are able to import and use any code from the common project. This allows you to
    // write the majority of your code here and load it from your loader specific projects. This example has some
    // code that gets invoked by the entry point of the loader specific projects.
    public static void init() {
        PacketHandler.registerPackets();
        // It is common for all supported loaders to provide a similar feature that can not be used directly in the
        // common code. A popular way to get around this is using Java's built-in service loader feature to create
        // your own abstraction layer. You can learn more about this in our provided services class. In this example
        // we have an interface in the common code and use a loader specific implementation to delegate our call to
        // the platform specific approach.

    }

    public static void syncToAll(MinecraftServer server) {
        server.getPlayerList().getPlayers().forEach(ResourcePoints::sync);
    }

    public static void sync(ServerPlayer player) {
        Services.PLATFORM.sendToClient(new S2CResourcePointsValuesPacket(RESOURCE_POINTS),player);
    }

    public static ResourceLocation id(String key) {
        return new ResourceLocation(MOD_ID,key);
    }
}