package tfar.resourcepoints.world;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import tfar.resourcepoints.ResourcePoints;
import tfar.resourcepoints.platform.Services;

import java.util.*;

public class Group {

    public static <A> Codec<Set<A>> setCodec(Codec<A> codec) {
        return Codec.list(codec).xmap(HashSet::new, ArrayList::new);
    }

    public static final Codec<Group> CODEC = RecordCodecBuilder.create(groupInstance ->
            groupInstance.group(
                    UUIDUtil.CODEC.fieldOf("leader").forGetter(p -> p.leader),
                    Codec.STRING.fieldOf("name").forGetter(p -> p.name),
                    setCodec(UUIDUtil.CODEC).fieldOf("members").forGetter(p -> p.members)
            ).apply(groupInstance,Group::new)
    );

    public final Set<UUID> members;
    public UUID leader;
    public final String name;

    public Group(UUID leader,String name,Set<UUID> members) {
        this.members = members;
        this.leader = leader;
        this.name = name;
    }

    public Group(UUID leader,String name) {
        this(leader,name, Sets.newHashSet(leader));
    }

    Tag toTag() {
        return CODEC.encodeStart(NbtOps.INSTANCE,this).resultOrPartial(ResourcePoints.LOG::error).orElseThrow();
    }

    static Group fromTag(Tag tag) {
        return CODEC.parse(new Dynamic<>(NbtOps.INSTANCE,tag)).resultOrPartial(ResourcePoints.LOG::error).orElseThrow();
    }

    public List<Component> info() {
        List<Component> components = new ArrayList<>();
        components.add(Component.literal("Leader: "+ Services.PLATFORM.getLastKnownUserName(leader)));

        for (UUID uuid : members) {
            if (!Objects.equals(uuid,leader)) {
                components.add(Component.literal("Member: " + Services.PLATFORM.getLastKnownUserName(uuid)));
            }
        }
        return components;
    }

}
