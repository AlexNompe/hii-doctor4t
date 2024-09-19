package net.firmamentsspark.registry;

import net.firmamentsspark.HiiDoctor4t;
import net.firmamentsspark.render.block.entity.SpaceBlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

import static com.mojang.text2speech.Narrator.LOGGER;
import static net.firmamentsspark.registry.BlockEntityRegistry.*;

public class RenderFactories {

    public static void registering() {
        HiiDoctor4t.LOGGER.info("Initializing RenderFactories...");

        BlockEntityRendererFactories.register(SPACE_BLOCK_ENTITY, SpaceBlockEntityRenderer::new);
    }
}
