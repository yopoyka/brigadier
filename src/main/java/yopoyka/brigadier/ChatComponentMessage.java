package yopoyka.brigadier;

import yopoyka.brigadier.com.mojang.brigadier.Message;
import net.minecraft.util.IChatComponent;

public class ChatComponentMessage implements Message {
    private final IChatComponent component;

    public ChatComponentMessage(IChatComponent component) {
        this.component = component;
    }

    public IChatComponent getComponent() {
        return component;
    }

    @Override
    public String getString() {
        return component.getUnformattedText();
    }
}
