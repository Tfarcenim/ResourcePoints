package tfar.resourcepoints.world;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.Nullable;
import tfar.resourcepoints.ResourcePoints;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ResourceSavedData extends SavedData {


    private final ServerLevel level;
    protected final List<Group> groups = new ArrayList<>();


    public ResourceSavedData(ServerLevel level) {
        this.level = level;
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        ListTag listTag = new ListTag();
        groups.forEach(group -> listTag.add(group.toTag()));
        compoundTag.put("groups",listTag);
        return compoundTag;
    }

    public boolean hasGroup(UUID uuid) {
        return getGroup(uuid).isPresent();
    }

    public Optional<Group> getGroup(UUID uuid) {
        return groups.stream().filter(group -> group.members.contains(uuid)).findFirst();
    }

    public void createGroup(UUID leader,String name) {
        groups.add(new Group(leader,name));
        setDirty();
    }

    @Nullable
    public static ResourceSavedData getInstance(ServerLevel serverLevel) {
        return serverLevel.getDataStorage()
                .get(compoundTag -> loadStatic(compoundTag, serverLevel), name(serverLevel));
    }

    public static ResourceSavedData getOrCreateInstance(ServerLevel serverLevel) {
        return serverLevel.getDataStorage()
                .computeIfAbsent(compoundTag -> loadStatic(compoundTag,serverLevel),
                        () -> new ResourceSavedData(serverLevel),name(serverLevel));
    }

    private static String name(ServerLevel level) {
        return  ResourcePoints.MOD_ID+"_"+level.dimension().location().toString().replace(':','.');
    }

    public static ResourceSavedData getOrCreateDefaultInstance(MinecraftServer server) {
        return getOrCreateInstance(server.overworld());
    }

    public static ResourceSavedData loadStatic(CompoundTag compoundTag,ServerLevel level) {
        ResourceSavedData id = new ResourceSavedData(level);
        id.load(compoundTag,level);
        return id;
    }

    public void load(CompoundTag tag,ServerLevel level) {
        ListTag groupTag = tag.getList("groups", ListTag.TAG_COMPOUND);
        groupTag.stream().map(Group::fromTag).forEach(groups::add);
    }

    public void destroyGroup(Group group) {
        groups.remove(group);
        setDirty();
    }
}
