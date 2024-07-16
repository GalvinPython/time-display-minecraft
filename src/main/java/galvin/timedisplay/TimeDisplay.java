package galvin.timedisplay;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.LanguageManager;
import java.text.NumberFormat;
import java.util.Locale;

public class TimeDisplay implements ModInitializer {
	private static GuiDisplay guiDisplay;

	// Define the variables
	long localGameTime;
	long savedDayTime;
	long savedHourTime;
	long savedMinuteTime;

	String dayTimeString;
	String hourTimeString;
	String minuteTimeString;

	@Override
	public void onInitialize() {
		// Register the HUD render callback
		HudRenderCallback.EVENT.register((matrices, tickDelta) -> {

			// Only run if the Minecraft world exists
			if (MinecraftClient.getInstance().world != null) {

				// Get the tick time (Game starts at 6am, so we add 6000 ticks for adjustment)
				localGameTime = MinecraftClient.getInstance().world.getTimeOfDay() + 6000;

				// Update the time every real-time second and calculate the time
				if (localGameTime % 20 == 0) {
					savedDayTime = localGameTime / 24000;
					savedHourTime = (localGameTime - (savedDayTime * 24000)) / 1000;
					savedMinuteTime = (localGameTime - (savedDayTime * 24000) - (savedHourTime * 1000)) / 17;

					// Format hour and minute as strings
					hourTimeString = String.format("%02d", savedHourTime);
					minuteTimeString = String.format("%02d", savedMinuteTime);

					// Create a Locale object from the language code
					Locale currentLocale = new Locale("en", "gb");
					System.out.println(currentLocale);

					// Use NumberFormat to format the numbers according to the locale
					NumberFormat numberFormat = NumberFormat.getInstance();
					dayTimeString = numberFormat.format(savedDayTime);
				}

				// For some reason I have to do this otherwise it crashes
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
