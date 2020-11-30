package yopoyka.brigadier;

import net.minecraft.server.MinecraftServer;
import yopoyka.brigadier.com.mojang.brigadier.arguments.ArgumentType;
import yopoyka.brigadier.com.mojang.brigadier.builder.LiteralArgumentBuilder;
import yopoyka.brigadier.com.mojang.brigadier.builder.RequiredArgumentBuilder;
import yopoyka.brigadier.com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.ICommandSender;

import java.util.Locale;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Commands {
    public static LiteralArgumentBuilder<ICommandSender> literal(String name) {
        return LiteralArgumentBuilder.literal(name);
    }

    public static <T> RequiredArgumentBuilder<ICommandSender, T> argument(String name, ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }

    /**
     * Use this method only for constant arrays
     * @param strings
     * @return
     */
    public static SuggestionProvider<ICommandSender> suggestConst(String... strings) {
        return suggest(() -> strings);
    }

    public static SuggestionProvider<ICommandSender> suggestIter(Supplier<Iterable<String>> strings) {
        return (context, builder) -> {
            final String remain = builder.getRemaining().toLowerCase(Locale.ROOT);
            for (String string : strings.get()) {
                if (match(remain, string.toLowerCase(Locale.ROOT)))
                    builder.suggest(string);
            }
            return builder.buildFuture();
        };
    }

    public static SuggestionProvider<ICommandSender> suggest(Supplier<String[]> strings) {
        return (context, builder) -> {
            final String remain = builder.getRemaining().toLowerCase(Locale.ROOT);
            for (String string : strings.get()) {
                if (match(remain, string.toLowerCase(Locale.ROOT)))
                    builder.suggest(string);
            }
            return builder.buildFuture();
        };
    }

    public static SuggestionProvider<ICommandSender> suggestStream(Supplier<Stream<String>> strings) {
        return (context, builder) -> {
            final String remain = builder.getRemaining().toLowerCase(Locale.ROOT);
            strings.get()
                    .filter(s -> match(remain, s))
                    .forEach(builder::suggest);
            return builder.buildFuture();
        };
    }

    static boolean match(String remain, String match) {
        for(int i = 0; !match.startsWith(remain, i); ++i) {
            i = match.indexOf(95, i);
            if (i < 0) {
                return false;
            }
        }

        return true;
    }

    public static SuggestionProvider<ICommandSender> suggestPlayer() {
        return suggest(() -> MinecraftServer.getServer().getConfigurationManager().getAllUsernames());
    }
}
