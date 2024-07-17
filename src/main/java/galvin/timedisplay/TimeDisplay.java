package galvin.timedisplay;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import java.text.NumberFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class TimeDisplay implements ModInitializer {
	private static GuiDisplay guiDisplay;
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	// Declare variables to store the time
	long localGameTime;
	long savedDayTime;
	long savedHourTime;
	long savedMinuteTime;

	// Declare variables to store the formatted time
	String dayTimeString;
	String hourTimeString;
	String minuteTimeString;

	// Update the time
	public void updateTime() {
		// Only run if the world exists
		if (MinecraftClient.getInstance().world != null) {
			// Get the time and add 6000 for the offset
			localGameTime = MinecraftClient.getInstance().world.getTimeOfDay() + 6000;

			// Get the day, hour, and minute
			savedDayTime = localGameTime / 24000;
			savedHourTime = (localGameTime - (savedDayTime * 24000)) / 1000;
			savedMinuteTime = (localGameTime - (savedDayTime * 24000) - (savedHourTime * 1000)) / 17;

			// Format the time
			hourTimeString = String.format("%02d", savedHourTime);
			minuteTimeString = String.format("%02d", savedMinuteTime);
			NumberFormat numberFormat = NumberFormat.getInstance();
			dayTimeString = numberFormat.format(savedDayTime);
		}
	}

	@Override
	public void onInitialize() {
		// Schedule the time update task
		scheduler.scheduleAtFixedRate(this::updateTime, 0, 1, java.util.concurrent.TimeUnit.SECONDS);

		// Register the HUD render callback
		HudRenderCallback.EVENT.register((matrices, tickDelta) -> {
			// Only run if the Minecraft world exists
			if (MinecraftClient.getInstance().world != null) {
				if (guiDisplay == null) {
					guiDisplay = new GuiDisplay();
				}

				// Display the time
				guiDisplay.renderText(0, matrices, String.format("Day: %s", dayTimeString));
				guiDisplay.renderText(1, matrices, String.format("Time: %s:%s", hourTimeString, minuteTimeString));
			}
		});
	}
}