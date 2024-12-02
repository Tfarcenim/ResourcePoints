package tfar.resourcepoints;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import tfar.resourcepoints.blockentity.DepositTerminalBlockEntity;
import tfar.resourcepoints.world.ResourceSavedData;

import java.util.Collection;

public class ModCommands {
    static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        dispatcher.register(Commands.literal(ResourcePoints.MOD_ID).requires(commandSourceStack -> commandSourceStack.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.literal("add")
                        .then(Commands.argument("item", ItemArgument.item(context))
                                .then(Commands.argument("points", IntegerArgumentType.integer(1))
                                        .executes(ModCommands::addResource)
                                )
                        )
                )
                .then(Commands.literal("add_multiplier")
                        .then(Commands.argument("pos", BlockPosArgument.blockPos())
                                .then(Commands.argument("item", ItemArgument.item(context))
                                        .then(Commands.argument("points", IntegerArgumentType.integer(-1))
                                                .executes(ModCommands::addMultiplier)
                                        )
                                )
                        )
                )
                .then(Commands.literal("assign")
                        .then(Commands.argument("pos", BlockPosArgument.blockPos())
                                .then(Commands.argument("group", StringArgumentType.greedyString())
                                                .executes(ModCommands::assignGroup)
                                )
                        )
                )
        );

        dispatcher.register(Commands.literal("group")
                .then(Commands.literal("create")
                        .then(Commands.argument("name", StringArgumentType.greedyString())
                                .executes(ModCommands::createGroup)
                        )
                )
                .then(Commands.literal("destroy")
                        .executes(ModCommands::destroyGroup)
                )
                .then(Commands.literal("info")
                        .executes(ModCommands::groupInfo)
                )
                .then(Commands.literal("invite")
                        .then(Commands.argument("players", EntityArgument.players())
                                .executes(ModCommands::inviteToGroup)
                        )
                )
                .then(Commands.literal("kick")
                        .then(Commands.argument("players", EntityArgument.players())
                                .executes(ModCommands::kickGroup)
                        )
                )
                .then(Commands.literal("leave")
                        .executes(ModCommands::leaveGroup)
                )
        );
    }

    private static int addResource(CommandContext<CommandSourceStack> context) {
        Item item = ItemArgument.getItem(context, "item").getItem();
        int value = IntegerArgumentType.getInteger(context, "points");
        ResourcePoints.RESOURCE_POINTS.put(item, value);
        ResourcePoints.syncToAll(context.getSource().getServer());
        ConfigHandler.save();
        return 1;
    }

    private static int addMultiplier(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        BlockPos blockPos = BlockPosArgument.getLoadedBlockPos(context, "pos");
        ServerLevel level = context.getSource().getLevel();
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (!(blockEntity instanceof DepositTerminalBlockEntity depositTerminalBlockEntity)) {
            context.getSource().sendFailure(Component.literal("No deposit terminal at " + blockPos));
            return 0;
        }
        Item item = ItemArgument.getItem(context, "item").getItem();
        int value = IntegerArgumentType.getInteger(context, "points");
        depositTerminalBlockEntity.multipliers.put(item, value);
        depositTerminalBlockEntity.setChanged();
        ResourcePoints.syncToAll(context.getSource().getServer());
        return 1;
    }

    private static int assignGroup(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        BlockPos blockPos = BlockPosArgument.getLoadedBlockPos(context, "pos");
        ServerLevel level = context.getSource().getLevel();
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (!(blockEntity instanceof DepositTerminalBlockEntity depositTerminalBlockEntity)) {
            context.getSource().sendFailure(Component.literal("No deposit terminal at " + blockPos));
            return 0;
        }
        String group = StringArgumentType.getString(context, "group");
        depositTerminalBlockEntity.setGroup(group);
        return 1;
    }

    static int createGroup(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        ServerPlayer player = source.getPlayerOrException();
        String name = StringArgumentType.getString(context, "name");
        ResourceSavedData data = ResourceSavedData.getOrCreateDefaultInstance(player.server);
        if (!data.hasGroup(player.getUUID())) {
            data.createGroup(player.getUUID(),name);
        } else {
            source.sendFailure(Component.literal("You are already in a group"));
            return 0;
        }
        return 1;
    }

    static int destroyGroup(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        ServerPlayer player = source.getPlayerOrException();
        ResourceSavedData data = ResourceSavedData.getOrCreateDefaultInstance(player.server);
        if (!data.hasGroup(player.getUUID())) {
            source.sendFailure(Component.literal("You are not in a group"));
            return 0;
        } else {
            data.getGroup(player.getUUID()).ifPresent(group -> {
                if (!group.leader.equals(player.getUUID())) {
                    source.sendFailure(Component.literal("Only the owner can destroy groups"));
                } else {
                    data.destroyGroup(group);
                }
            });
        }
        return 1;
    }

    static int inviteToGroup(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        ServerPlayer player = source.getPlayerOrException();
        Collection<ServerPlayer> players = EntityArgument.getPlayers(context,"players");
        ResourceSavedData data = ResourceSavedData.getOrCreateDefaultInstance(player.server);
        if (!data.hasGroup(player.getUUID())) {
            source.sendFailure(Component.literal("You are not in a group"));
            return 0;
        } else {
            data.getGroup(player.getUUID()).ifPresent(group -> {
                if (!group.leader.equals(player.getUUID())) {
                    source.sendFailure(Component.literal("Only the owner can invite players"));
                } else {
                    players.forEach(player1 -> {
                        if (!data.hasGroup(player1.getUUID())) {
                            group.members.add(player1.getUUID());
                        }
                    });
                    data.setDirty();
                }
            });
        }
        return 1;
    }

    static int kickGroup(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        ServerPlayer player = source.getPlayerOrException();
        Collection<ServerPlayer> players = EntityArgument.getPlayers(context,"players");
        ResourceSavedData data = ResourceSavedData.getOrCreateDefaultInstance(player.server);
        if (!data.hasGroup(player.getUUID())) {
            source.sendFailure(Component.literal("You are not in a group"));
            return 0;
        } else {
            data.getGroup(player.getUUID()).ifPresent(group -> {
                if (!group.leader.equals(player.getUUID())) {
                    source.sendFailure(Component.literal("Only the owner can kick players"));
                } else {
                    players.forEach(player1 -> {
                        group.members.remove( player1.getUUID());
                    });
                    data.setDirty();
                }
            });
        }
        return 1;
    }

    static int leaveGroup(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        ServerPlayer player = source.getPlayerOrException();
        ResourceSavedData data = ResourceSavedData.getOrCreateDefaultInstance(player.server);
        if (!data.hasGroup(player.getUUID())) {
            source.sendFailure(Component.literal("You are not in a group"));
            return 0;
        } else {
            data.getGroup(player.getUUID()).ifPresent(group -> {
                if (group.leader.equals(player.getUUID())) {
                    source.sendFailure(Component.literal("The leader cannot leave groups"));
                } else {
                    group.members.remove(player.getUUID());
                    data.setDirty();
                }
            });
        }
        return 1;
    }

    static int groupInfo(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        ServerPlayer player = source.getPlayerOrException();
        ResourceSavedData data = ResourceSavedData.getOrCreateDefaultInstance(player.server);
        if (!data.hasGroup(player.getUUID())) {
            source.sendFailure(Component.literal("You are not in a group"));
            return 0;
        } else {
            data.getGroup(player.getUUID()).ifPresent(group -> {
                group.info().forEach(component -> source.sendSuccess(() -> component,false));
            });
        }
        return 1;
    }

}
