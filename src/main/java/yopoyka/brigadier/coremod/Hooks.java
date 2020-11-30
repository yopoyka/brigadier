package yopoyka.brigadier.coremod;

import yopoyka.brigadier.com.mojang.brigadier.ParseResults;
import yopoyka.brigadier.com.mojang.brigadier.StringReader;
import net.minecraft.command.ICommandSender;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.server.S3APacketTabComplete;
import net.minecraft.server.MinecraftServer;
import yopoyka.brigadier.Brigadier;

import java.util.List;

public class Hooks {
    public static void processTabComplete(NetHandlerPlayServer netHandler, C14PacketTabComplete packet) {
        final List<String> possibleCommands = MinecraftServer.getServer().getPossibleCompletions(netHandler.playerEntity, packet.getMessage());

        if (packet.getMessage().charAt(0) == '/') {
            boolean addSlash = packet.getMessage().indexOf(' ') < 0;
            StringReader stringreader = new StringReader(packet.getMessage());
            stringreader.skip();

            final ParseResults<ICommandSender> parse = Brigadier.getDispatcher().parse(stringreader, netHandler.playerEntity);
            Brigadier.getDispatcher().getCompletionSuggestions(parse).thenAccept(suggestions -> {
                suggestions.getList().forEach(suggestion -> possibleCommands.add(addSlash ? ("/" + suggestion.getText()) : suggestion.getText()));
                final S3APacketTabComplete p = new S3APacketTabComplete(possibleCommands.toArray(new String[0]));
                netHandler.playerEntity.playerNetServerHandler.sendPacket(p);
            });
        }
    }
}
