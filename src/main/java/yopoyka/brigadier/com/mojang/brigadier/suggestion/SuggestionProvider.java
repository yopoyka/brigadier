// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT license.

package yopoyka.brigadier.com.mojang.brigadier.suggestion;

import yopoyka.brigadier.com.mojang.brigadier.context.CommandContext;
import yopoyka.brigadier.com.mojang.brigadier.exceptions.CommandSyntaxException;

import java.util.concurrent.CompletableFuture;

@FunctionalInterface
public interface SuggestionProvider<S> {
    CompletableFuture<Suggestions> getSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) throws CommandSyntaxException;
}
