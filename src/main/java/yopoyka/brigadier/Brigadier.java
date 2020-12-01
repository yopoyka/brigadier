package yopoyka.brigadier;

import net.minecraft.command.CommandException;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import yopoyka.brigadier.com.mojang.brigadier.CommandDispatcher;
import yopoyka.brigadier.com.mojang.brigadier.arguments.StringArgumentType;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.*;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import yopoyka.brigadier.com.mojang.brigadier.exceptions.CommandSyntaxException;
import yopoyka.brigadier.com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import java.util.Arrays;
import java.util.Objects;

import static yopoyka.brigadier.Commands.literal;

@Mod(modid = Brigadier.MODID, version = Brigadier.VERSION, name = Brigadier.NAME, acceptableRemoteVersions = "*")
public final class Brigadier {
    public static final String MODID = "brigadier";
    public static final String VERSION = "1.0.3";
    public static final String NAME = "Brigadier";
    private static CommandDispatcher<ICommandSender> commandDispatcher = new CommandDispatcher<>();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        getDispatcher().register(literal("brigadier")
                .then(literal("throw").executes(ctx -> {
                    throw new CommandException("Command Exception");
                }))
                .then(literal("thrownew").executes(ctx -> {
                    throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedBool().create();
                }))
                .then(literal("throwstyled").executes(ctx -> {
                    throw new SimpleCommandExceptionType(new ChatComponentMessage(new ChatComponentText(EnumChatFormatting.LIGHT_PURPLE + "Styled exception"))).create();
                }))
        );
    }

    @EventHandler
    public void serverStarted(FMLServerStartedEvent event) {
        MinecraftServer.getServer().commandManager = new CommandManager(
                MinecraftServer.getServer().getCommandManager(),
                Brigadier::getDispatcher
        );
        System.out.println(getDispatcher().getSmartUsage(getDispatcher().getRoot(), MinecraftServer.getServer()));
        System.out.println(Arrays.toString(getDispatcher().getAllUsage(getDispatcher().getRoot(), MinecraftServer.getServer(), true)));
    }

    public static CommandDispatcher<ICommandSender> getDispatcher() {
        return commandDispatcher;
    }

    public static void setDispatcher(CommandDispatcher<ICommandSender> commandDispatcher) {
        Brigadier.commandDispatcher = Objects.requireNonNull(commandDispatcher);
    }
}
