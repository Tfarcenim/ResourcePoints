package tfar.resourcepoints.network.client;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.Item;
import tfar.resourcepoints.ResourcePoints;

public class S2CResourcePointsValuesPacket implements S2CModPacket {

    public final Object2IntMap<Item> values;

    public S2CResourcePointsValuesPacket(Object2IntMap<Item> values) {
        this.values = values;
    }

    public S2CResourcePointsValuesPacket(FriendlyByteBuf buf) {
        values = buf.readMap(Object2IntOpenHashMap::new,buf1 -> buf1.readById(BuiltInRegistries.ITEM), FriendlyByteBuf::readInt);
    }

    @Override
    public void handleClient() {
        if (!Minecraft.getInstance().isLocalServer()) {
            ResourcePoints.RESOURCE_POINTS = values;
        }
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeMap(values,(buf, item) -> buf.writeId(BuiltInRegistries.ITEM,item), FriendlyByteBuf::writeInt);
    }
}
