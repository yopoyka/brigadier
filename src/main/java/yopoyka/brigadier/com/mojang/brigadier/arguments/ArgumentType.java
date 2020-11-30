// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT license.

package yopoyka.brigadier.com.mojang.brigadier.arguments;

import yopoyka.brigadier.com.mojang.brigadier.StringReader;
import yopoyka.brigadier.com.mojang.brigadier.context.CommandContext;
import yopoyka.brigadier.com.mojang.brigadier.exceptions.CommandSyntaxException;
import yopoyka.brigadier.com.mojang.brigadier.suggestion.Suggestions;
import yopoyka.brigadier.com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public interface ArgumentType<T> {
    T parse(StringReader reader) throws CommandSyntaxException;

    default <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        return Suggestions.empty();
    }

    default Collection<String> getExamples() {
        return Collections.emptyList();
    }
}
