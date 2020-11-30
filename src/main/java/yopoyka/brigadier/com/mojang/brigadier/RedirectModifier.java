// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT license.

package yopoyka.brigadier.com.mojang.brigadier;

import yopoyka.brigadier.com.mojang.brigadier.context.CommandContext;
import yopoyka.brigadier.com.mojang.brigadier.exceptions.CommandSyntaxException;

import java.util.Collection;

@FunctionalInterface
public interface RedirectModifier<S> {
    Collection<S> apply(CommandContext<S> context) throws CommandSyntaxException;
}
