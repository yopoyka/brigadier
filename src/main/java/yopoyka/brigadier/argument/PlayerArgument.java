package yopoyka.brigadier.argument;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.PlayerSelector;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import yopoyka.brigadier.com.mojang.brigadier.context.CommandContext;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class PlayerArgument {
    /**
     *
     * @param ctx
     * @param argName
     * @return matched player
     * @throws PlayerNotFoundException if player not found
     */
    public static EntityPlayerMP getOne(CommandContext<ICommandSender> ctx, String argName) throws PlayerNotFoundException {
        final String argument = ctx.getArgument(argName, String.class);

        final EntityPlayerMP match = PlayerSelector.matchOnePlayer(ctx.getSource(), argument);
        if (match == null) {
            return ((List<EntityPlayerMP>) MinecraftServer.getServer().getConfigurationManager().playerEntityList)
                    .stream()
                    .filter(p -> argument.equalsIgnoreCase(p.getCommandSenderName()))
                    .findFirst()
                    .orElseThrow(PlayerNotFoundException::new);
        }

        return match;
    }

    /**
     *
     * @param ctx
     * @param argName
     * @return immutable collection of matched players
     * @throws PlayerNotFoundException if none matched
     */
    public static Collection<EntityPlayerMP> getMany(CommandContext<ICommandSender> ctx, String argName) throws PlayerNotFoundException {
        final String argument = ctx.getArgument(argName, String.class);

        final EntityPlayerMP[] matches = PlayerSelector.matchPlayers(ctx.getSource(), argument);

        if (matches == null)
            throw new PlayerNotFoundException();

        return Arrays.asList(matches);
    }
}
