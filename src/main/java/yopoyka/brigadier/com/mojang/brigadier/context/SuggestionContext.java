// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT license.

package yopoyka.brigadier.com.mojang.brigadier.context;

import yopoyka.brigadier.com.mojang.brigadier.tree.CommandNode;

public class SuggestionContext<S> {
    public final CommandNode<S> parent;
    public final int startPos;

    public SuggestionContext(CommandNode<S> parent, int startPos) {
        this.parent = parent;
        this.startPos = startPos;
    }
}
