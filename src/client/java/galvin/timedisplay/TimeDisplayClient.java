package galvin.timedisplay;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;

public class TimeDisplayClient implements ClientModInitializer {
    private static GuiDisplay guiDisplay;

    // Define the variables
    long localGameTime;
    long savedGameTime;
    long savedDayTime;
    long savedHourTime;
    long savedMinuteTime;

    @Override
    public void onInitializeClient() {
        // Register the HUD render callback
        HudRenderCallback.EVENT.register((matrices, tickDelta) -> {

            // Only run if the Minecraft world exists
            if (MinecraftClient.getInstance().world != null) {

                // Get the tick time (Game starts at 6am so we add 6000 ticks for adjustment)
                localGameTime = MinecraftClient.getInstance().world.getTimeOfDay() + 6000;

                // Update the time every real-time second
                // And calculate it
                // TODO: Fix the times to be accurate
                if (localGameTime % 20 == 0) {
                    savedGameTime = localGameTime;
                    savedDayTime = localGameTime / 24000;
                    savedHourTime = localGameTime / 1000;
                    savedMinuteTime = localGameTime / (1000/ 17); //TODO: Check if this works, unable to right now
                }

                // For some reason I have to do this otherwise it crashes
                if (guiDisplay == null) {
                    guiDisplay = new GuiDisplay();
                }

                // Display the time
                guiDisplay.renderText(0, matrices, String.format("Current Game Time: %s", savedGameTime));
                guiDisplay.renderText(1, matrices, String.format("Days: %s", savedDayTime));
                guiDisplay.renderText(2, matrices, String.format("Hours: %s", savedHourTime));
                guiDisplay.renderText(3, matrices, String.format("Minutes: %s", savedMinuteTime));
            }
        });
    }
}
