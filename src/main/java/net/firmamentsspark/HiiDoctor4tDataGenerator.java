package net.firmamentsspark;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

import static com.mojang.text2speech.Narrator.LOGGER;

public class HiiDoctor4tDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		HiiDoctor4t.LOGGER.info("Initializing DataGen...");
	}
}
