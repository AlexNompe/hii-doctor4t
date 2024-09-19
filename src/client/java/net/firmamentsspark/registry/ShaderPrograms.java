package net.firmamentsspark.registry;

import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import net.fabricmc.fabric.impl.client.rendering.FabricShaderProgram;
import net.firmamentsspark.HiiDoctor4t;
import net.firmamentsspark.shaderprograms.PositionedShaderProgram;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;

import static com.mojang.text2speech.Narrator.LOGGER;

public class ShaderPrograms {

    public static ShaderProgram TEST_SHADER_PROGRAM;

    public static void registering() {
        HiiDoctor4t.LOGGER.info("Initializing ShaderPrograms...");

        CoreShaderRegistrationCallback.EVENT.register(context -> {
            Identifier id = Identifier.of(HiiDoctor4t.MOD_ID, "test_shader");
            context.register(id, VertexFormats.BLIT_SCREEN, program -> TEST_SHADER_PROGRAM = program);
        });
    }
}
