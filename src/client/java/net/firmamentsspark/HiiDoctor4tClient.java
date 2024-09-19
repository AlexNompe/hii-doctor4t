package net.firmamentsspark;

import net.fabricmc.api.ClientModInitializer;
import net.firmamentsspark.registry.*;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

import net.firmamentsspark.render.block.entity.*;

import static com.mojang.text2speech.Narrator.LOGGER;
import static net.firmamentsspark.registry.BlockEntityRegistry.*;

public class HiiDoctor4tClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		HiiDoctor4t.LOGGER.info("Initializing Client...");

        RenderFactories.registering();
        RenderLayers.registering();
        ShaderPrograms.registering();
	}
}