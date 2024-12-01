package tfar.resourcepoints;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.world.item.Item;

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
        );
    }

    private static int addResource(CommandContext<CommandSourceStack> context) {
        Item item = ItemArgument.getItem(context,"item").getItem();
        int value = IntegerArgumentType.getInteger(context,"points");
        ResourcePoints.RESOURCE_POINTS.put(item,value);
        ResourcePoints.syncToAll(context.getSource().getServer());
        return 1;
    }
}
