package tfar.resourcepoints.world;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;

public class ResourceSavedData extends SavedData {

    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        return compoundTag;
    }
}
