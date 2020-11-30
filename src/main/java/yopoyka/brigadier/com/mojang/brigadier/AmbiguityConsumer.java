// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT license.

package yopoyka.brigadier.com.mojang.brigadier;

import yopoyka.brigadier.com.mojang.brigadier.tree.CommandNode;

import java.util.Collection;

@FunctionalInterface
public interface AmbiguityConsumer<S> {
    void ambiguous(final CommandNode<S> parent, final CommandNode<S> child, final CommandNode<S> sibling, final Collection<String> inputs);
}
