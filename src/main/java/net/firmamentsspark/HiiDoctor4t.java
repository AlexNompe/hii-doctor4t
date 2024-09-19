package net.firmamentsspark;

import net.fabricmc.api.ModInitializer;

import net.firmamentsspark.registry.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HiiDoctor4t implements ModInitializer {
	public static final String MOD_ID = "hii-doctor4t";
	public static final Logger LOGGER = LoggerFactory.getLogger("hii-doctor4t");

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Firmament's Spark...");

		BlockRegistry.registering();
		BlockEntityRegistry.registering();
	}
}