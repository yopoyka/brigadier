package yopoyka.brigadier;

import yopoyka.brigadier.com.mojang.brigadier.Message;
import net.minecraft.util.IChatComponent;

import java.util.function.Supplier;

public class ChatComponentMessage implements Message {
    private final Supplier<IChatComponent> component;

    @Deprecated
    public ChatComponentMessage(IChatComponent component) {
        this(component::createCopy);
    }

    public ChatComponentMessage(Supplier<IChatComponent> component) {
        this.component = component;
    }

    public IChatComponent getComponent() {
        return component.get();
    }

    @Override
    public String getString() {
        return component.get().getUnformattedText();
    }
}
