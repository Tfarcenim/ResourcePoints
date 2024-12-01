package tfar.resourcepoints.network;

import net.minecraft.resources.ResourceLocation;
import tfar.resourcepoints.ResourcePoints;
import tfar.resourcepoints.network.client.S2CResourcePointsValuesPacket;
import tfar.resourcepoints.platform.Services;

import java.util.Locale;

public class PacketHandler {

    public static void registerPackets() {
        Services.PLATFORM.registerClientPacket(S2CResourcePointsValuesPacket.class, S2CResourcePointsValuesPacket::new);

    }

    public static ResourceLocation packet(Class<?> clazz) {
        return ResourcePoints.id(clazz.getName().toLowerCase(Locale.ROOT));
    }

}
