package galvin.timedisplay;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.world.ClientWorld;

public class TimeDisplayClient implements ClientModInitializer {
    private static GuiDisplay guiDisplay;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(server -> HudRenderCallback.EVENT.register((context, tickDelta) -> {
            if (guiDisplay == null) {
                guiDisplay = new GuiDisplay();
            }
            ClientWorld world = server.world;
            if (world != null) {
                long time = world.getTimeOfDay();
                guiDisplay.renderText(0, context, String.format("Current Game Time: %s", time));
                guiDisplay.renderText(1, context, String.format("Minutes: %s", time / 1200));
                guiDisplay.renderText(2, context, String.format("Seconds: %s", time / 20));
            }
        }));
    }
}
