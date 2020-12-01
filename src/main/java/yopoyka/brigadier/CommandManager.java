package yopoyka.brigadier;

import net.minecraft.command.ICommand;
import net.minecraft.util.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CommandEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import yopoyka.brigadier.com.mojang.brigadier.CommandDispatcher;
import yopoyka.brigadier.com.mojang.brigadier.StringReader;
import yopoyka.brigadier.com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import yopoyka.brigadier.event.CommandWrapper;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class CommandManager implements ICommandManager {
    protected final Supplier<CommandDispatcher<ICommandSender>> dispatcher;
    protected final ICommandManager delegate;
    protected static final Logger logger = LogManager.getLogger("Brigadier");

    public CommandManager(ICommandManager delegate, Supplier<CommandDispatcher<ICommandSender>> dispatcher) {
        this.delegate = delegate;
        this.dispatcher = dispatcher;
    }

    @Override
    public int executeCommand(ICommandSender sender, String input) {
        final StringReader reader = new StringReader(input);

        if (reader.canRead() && reader.peek() == '/')
            reader.skip();

        MinecraftServer.getServer().theProfiler.startSection(input);
        try {
            return dispatcher.get().execute(reader, sender);
        } catch (CommandException e) {
            final ChatComponentTranslation message = new ChatComponentTranslation(e.getMessage(), e.getErrorOjbects());
            message.getChatStyle().setColor(EnumChatFormatting.RED);
            sender.addChatMessage(message);
        } catch (CommandSyntaxException e) {
            if (e.getType().equals(CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand())) {
                return delegate.executeCommand(sender, reader.getRemaining());
            }
            else {
                if (e.getRawMessage() instanceof ChatComponentMessage) {
                    final IChatComponent component = ((ChatComponentMessage) e.getRawMessage()).getComponent();
                    component.getChatStyle().setColor(EnumChatFormatting.RED);
                    sender.addChatMessage(component);
                }
                else
                    sender.addChatMessage(new ChatComponentText(e.getMessage()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
            }
        } catch (Throwable t) {
            sender.addChatMessage(new ChatComponentTranslation("commands.generic.exception").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
            logger.error("Couldn't process command: '" + input + "'", t);
        } finally {
            MinecraftServer.getServer().theProfiler.endSection();
        }
        return 0;
    }

    @Override
    public List<String> getPossibleCommands(ICommandSender sender, String input) {
        return delegate.getPossibleCommands(sender, input);
    }

    @Override
    public List<String> getPossibleCommands(ICommandSender p_71557_1_) {
        return delegate.getPossibleCommands(p_71557_1_);
    }

    @Override
    public Map<String, ICommand> getCommands() {
        return delegate.getCommands();
    }
}
