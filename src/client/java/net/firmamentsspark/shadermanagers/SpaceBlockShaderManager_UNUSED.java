package net.firmamentsspark.shadermanagers;

import net.fabricmc.api.ClientModInitializer;
import net.firmamentsspark.HiiDoctor4t;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL32;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import static com.mojang.text2speech.Narrator.LOGGER;

public class SpaceBlockShaderManager_UNUSED {
    private static int shaderProgram;

    public void registering() {
        LOGGER.info("[NOT A REGISTRY] Initializing SpaceBlockShaderManager...");

        int vertexShader = loadShader(MinecraftClient.getInstance().getResourceManager(), "shaders/core/test_shader.vsh", GL32.GL_VERTEX_SHADER);
        int fragmentShader = loadShader(MinecraftClient.getInstance().getResourceManager(), "shaders/core/test_shader.fsh", GL32.GL_FRAGMENT_SHADER);

        shaderProgram = GL32.glCreateProgram();
        GL32.glAttachShader(shaderProgram, vertexShader);
        GL32.glAttachShader(shaderProgram, fragmentShader);
        GL32.glLinkProgram(shaderProgram);

        int linked = GL32.glGetProgrami(shaderProgram, GL32.GL_LINK_STATUS);
        if (linked == 0) {
            System.err.println("Failed to link shader program:");
            System.err.println(GL32.glGetProgramInfoLog(shaderProgram));
        }
    }

    private static int loadShader(ResourceManager resourceManager, String path, int type) {
        try {
            Identifier shaderPath = Identifier.of(HiiDoctor4t.MOD_ID, path);
            String shaderSource = new String(Files.readAllBytes(Paths.get(Arrays.toString(resourceManager.getResource(shaderPath).get().getInputStream().readAllBytes()))));
            int shader = GL32.glCreateShader(type);
            GL32.glShaderSource(shader, shaderSource);
            GL32.glCompileShader(shader);

            int compiled = GL32.glGetShaderi(shader, GL32.GL_COMPILE_STATUS);
            if (compiled == 0) {
                System.err.println("Failed to compile shader:");
                System.err.println(GL32.glGetShaderInfoLog(shader));
            }

            return shader;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load shader " + path, e);
        }
    }

    public void useShader() {
        GL32.glUseProgram(shaderProgram);
        MinecraftClient mc = MinecraftClient.getInstance();
        int cameraPosLocation = GL32.glGetUniformLocation(shaderProgram, "cameraPosition");
        assert mc.player != null;
        GL32.glUniform3f(cameraPosLocation, (float) mc.player.getX(), (float) mc.player.getY(), (float) mc.player.getZ());
    }

    public void stopUsingShader() {
        GL32.glUseProgram(0);
    }
}