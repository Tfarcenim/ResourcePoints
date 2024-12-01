package tfar.resourcepoints.network.client;

import net.minecraft.network.FriendlyByteBuf;
import tfar.nations3.client.ClientPacketHandler;
import tfar.nations3.world.TownInfo;

public class S2CResourcePointsValuesPacket implements S2CModPacket {

    public final int containerId;
    public final int index;
    public final TownInfo value;

    public S2CResourcePointsValuesPacket(int pContainerId, int pIndex, TownInfo pValue) {
        this.containerId = pContainerId;
        this.index = pIndex;
        this.value = pValue;
    }

    public S2CResourcePointsValuesPacket(FriendlyByteBuf buf) {
        containerId = buf.readInt();
        index = buf.readInt();
        value = TownInfo.fromPacket(buf);
    }

    @Override
    public void handleClient() {
        ClientPacketHandler.handleTownInfoPacket(this);
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeInt(containerId);
        to.writeInt(index);
        value.toPacket(to);
    }
}
