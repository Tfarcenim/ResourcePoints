package tfar.resourcepoints.blockentity;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import tfar.resourcepoints.ResourcePoints;
import tfar.resourcepoints.init.ModBlockEntityTypes;
import tfar.resourcepoints.menu.DepositTerminalMenu;

public class DepositTerminalBlockEntity extends BlockEntity implements MenuProvider, Nameable {

    public final Container container = new SimpleContainer(9) {
        @Override
        public boolean canPlaceItem(int slot, ItemStack stack) {
            return getValue(stack) > 0;
        }
    };
    @Nullable
    private Component name;
    private String group = "";

    public final Object2IntMap<Item> multipliers = new Object2IntOpenHashMap<>();

    public DepositTerminalBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.DEPOSIT_TERMINAL, pos, state);
    }

    public void setCustomName(Component $$0) {
        this.name = $$0;
    }

    public void setGroup(String group) {
        this.group = group;
        setChanged();
    }

    public Component getName() {
        return this.name != null ? this.name : this.getDefaultName();
    }

    public Component getDisplayName() {
        return this.getName();
    }

    @Nullable
    public Component getCustomName() {
        return this.name;
    }

    protected Component getDefaultName() {
        return getBlockState().getBlock().getName();
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        if (this.name != null) {
            compoundTag.putString("CustomName", Component.Serializer.toJson(this.name));
        }
        if (!multipliers.isEmpty()) {
            CompoundTag tag = new CompoundTag();
            for (Object2IntMap.Entry<Item> entry : multipliers.object2IntEntrySet()) {
                tag.putInt(BuiltInRegistries.ITEM.getKey(entry.getKey()).toString(),entry.getIntValue());
            }
            compoundTag.put("multipliers",tag);
        }
        if (!group.isEmpty()){
            compoundTag.putString("group",group);
        }
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        if (compoundTag.contains("CustomName", Tag.TAG_STRING)) {
            this.name = Component.Serializer.fromJson(compoundTag.getString("CustomName"));
        }
        if (compoundTag.contains("multipliers")) {
            CompoundTag tag = compoundTag.getCompound("multipliers");
            for (String s : tag.getAllKeys()) {
                multipliers.put(BuiltInRegistries.ITEM.get(new ResourceLocation(s)),tag.getInt(s));
            }
        }
        if (compoundTag.contains("group")) {
            group = compoundTag.getString("group");
        }
    }

    public int getValue(ItemStack stack) {
        Item item = stack.getItem();
        if (!ResourcePoints.RESOURCE_POINTS.containsKey(item)) {
            return 0;
        }

        if (multipliers.getInt(item) <= 0) {
            return 0;
        }
        return ResourcePoints.RESOURCE_POINTS.getInt(item) * (multipliers.containsKey(item) ? multipliers.getInt(item) : 1);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new DepositTerminalMenu(i,inventory,container);
    }
}
