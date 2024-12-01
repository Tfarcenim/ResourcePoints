package tfar.resourcepoints.network.server;

import net.minecraft.server.level.ServerPlayer;
import tfar.resourcepoints.network.ModPacket;

public interface C2SModPacket extends ModPacket {

    void handleServer(ServerPlayer player);

}
