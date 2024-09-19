package net.firmamentsspark.registry;

import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.impl.client.rendering.FabricShaderProgram;
import net.fabricmc.fabric.mixin.blockrenderlayer.RenderLayersMixin;
import net.firmamentsspark.HiiDoctor4t;
import net.firmamentsspark.client.rendering.v1.FabricRenderLayers;
import net.firmamentsspark.impl.client.rendering.RenderLayerAccessors;
import net.firmamentsspark.render.block.entity.SpaceBlockEntityRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.util.Identifier;

import static com.mojang.text2speech.Narrator.LOGGER;
import static net.firmamentsspark.registry.BlockEntityRegistry.*;
import static net.firmamentsspark.registry.ShaderPrograms.*;
import static net.minecraft.client.render.RenderPhase.END_PORTAL_PROGRAM;

public class RenderLayers extends FabricRenderLayers {

    private static MultiPhaseParameters TEST_MULTIPHASE() {
        MultiPhaseParameters.Builder test = MultiPhaseParameters.builder();
        test.program(new ShaderProgram(() -> TEST_SHADER_PROGRAM));
        RenderPhase.Textures.Builder textures = RenderPhase.Textures.create();
        textures.add(Identifier.of(HiiDoctor4t.MOD_ID,"textures/spacemap/test.png"), false, false);
        //textures.add(Identifier.of(HiiDoctor4t.MOD_ID,"textures/spacemap/earth_normal.png"), false, false);
        test.texture(textures.build());
        return test.build(false);
    }

    public static final RenderLayer TEST_LAYER = RenderLayer.of("test_layer", VertexFormats.BLIT_SCREEN, VertexFormat.DrawMode.QUADS, 1536, false, false, TEST_MULTIPHASE());

    public static void registering() {
        HiiDoctor4t.LOGGER.info("Initializing RenderLayers...");
    }
}
