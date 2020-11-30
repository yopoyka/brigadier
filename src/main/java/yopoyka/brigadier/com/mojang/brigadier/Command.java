// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT license.

package yopoyka.brigadier.com.mojang.brigadier;

import yopoyka.brigadier.com.mojang.brigadier.context.CommandContext;
import yopoyka.brigadier.com.mojang.brigadier.exceptions.CommandSyntaxException;

@FunctionalInterface
public interface Command<S> {
    int SINGLE_SUCCESS = 1;

    int run(CommandContext<S> ctx) throws CommandSyntaxException;
}
