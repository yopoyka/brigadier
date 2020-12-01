package yopoyka.brigadier.event;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class CommandWrapper extends CommandBase {
    private final String name;

    public CommandWrapper(String name) {
        this.name = name;
    }

    @Override
    public String getCommandName() {
        return name;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {

    }
}
