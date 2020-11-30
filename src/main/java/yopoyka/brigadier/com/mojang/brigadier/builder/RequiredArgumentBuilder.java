// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT license.

package yopoyka.brigadier.com.mojang.brigadier.builder;

import yopoyka.brigadier.com.mojang.brigadier.arguments.ArgumentType;
import yopoyka.brigadier.com.mojang.brigadier.suggestion.SuggestionProvider;
import yopoyka.brigadier.com.mojang.brigadier.tree.ArgumentCommandNode;
import yopoyka.brigadier.com.mojang.brigadier.tree.CommandNode;

public class RequiredArgumentBuilder<S, T> extends ArgumentBuilder<S, RequiredArgumentBuilder<S, T>> {
    private final String name;
    private final ArgumentType<T> type;
    private SuggestionProvider<S> suggestionsProvider = null;

    private RequiredArgumentBuilder(final String name, final ArgumentType<T> type) {
        this.name = name;
        this.type = type;
    }

    public static <S, T> RequiredArgumentBuilder<S, T> argument(final String name, final ArgumentType<T> type) {
        return new RequiredArgumentBuilder<>(name, type);
    }

    public RequiredArgumentBuilder<S, T> suggests(final SuggestionProvider<S> provider) {
        this.suggestionsProvider = provider;
        return getThis();
    }

    public SuggestionProvider<S> getSuggestionsProvider() {
        return suggestionsProvider;
    }

    @Override
    protected RequiredArgumentBuilder<S, T> getThis() {
        return this;
    }

    public ArgumentType<T> getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public ArgumentCommandNode<S, T> build() {
        final ArgumentCommandNode<S, T> result = new ArgumentCommandNode<>(getName(), getType(), getCommand(), getRequirement(), getRedirect(), getRedirectModifier(), isFork(), getSuggestionsProvider());

        for (final CommandNode<S> argument : getArguments()) {
            result.addChild(argument);
        }

        return result;
    }
}
