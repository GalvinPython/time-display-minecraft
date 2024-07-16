package galvin.timedisplay;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

public class GuiDisplay {
    private final TextRenderer textRenderer;

    private static final int xPos = 5;
    private static final int yPos = 5;

    public GuiDisplay() {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        this.textRenderer = minecraftClient.textRenderer;
    }

    public void renderText(int line, DrawContext context, String text) {
        context.drawTextWithShadow(textRenderer, text, xPos, yPos + (10 * line), 0xFFFFFF);
    }
}