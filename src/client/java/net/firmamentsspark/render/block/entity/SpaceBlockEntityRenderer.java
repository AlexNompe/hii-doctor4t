package net.firmamentsspark.render.block.entity;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.firmamentsspark.HiiDoctor4t;
import net.firmamentsspark.block.entity.SpaceBlockEntity;
import net.firmamentsspark.mixin.client.ExampleClientMixin;
import net.firmamentsspark.registry.RenderLayers;
import net.firmamentsspark.utils.EulerAngles;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.GlProgramManager;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.EulerAngle;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.Objects;

import static net.firmamentsspark.registry.RenderLayers.TEST_LAYER;
import static net.firmamentsspark.registry.ShaderPrograms.TEST_SHADER_PROGRAM;

@Environment(EnvType.CLIENT)
public class SpaceBlockEntityRenderer implements BlockEntityRenderer<SpaceBlockEntity> {

    public Vec3d cameraPos;
    public Vec3d cameraRot = new Vec3d(0,0,0);
    public float cameraFov;

    public SpaceBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public void render(SpaceBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        RenderSystem.setShader(() -> TEST_SHADER_PROGRAM);

        assert MinecraftClient.getInstance().cameraEntity != null;
        cameraPos = MinecraftClient.getInstance().cameraEntity.getEyePos();
        cameraRot = MinecraftClient.getInstance().cameraEntity.getRotationVecClient();
        cameraFov = (float) MinecraftClient.getInstance().options.getFov().getValue();
        assert MinecraftClient.getInstance().world != null;
        float gameTime = MinecraftClient.getInstance().world.getTime();

        Matrix4f matrix4f = matrices.peek().getPositionMatrix();

        TEST_SHADER_PROGRAM.bind();

        setUniform(TEST_SHADER_PROGRAM,"CameraPos",cameraPos);
        setUniform(TEST_SHADER_PROGRAM,"CameraRot",cameraRot);
        setUniform(TEST_SHADER_PROGRAM,"CameraFov",cameraFov);
        setUniform(TEST_SHADER_PROGRAM,"GameTime",gameTime);

        setSampler(TEST_SHADER_PROGRAM,0,Identifier.of(HiiDoctor4t.MOD_ID,"textures/spacemap/test.png"));
        setSampler(TEST_SHADER_PROGRAM,1,Identifier.of(HiiDoctor4t.MOD_ID,"textures/spacemap/earth_day.png"));

        VertexConsumer buffer = vertexConsumers.getBuffer(TEST_LAYER);
        this.renderSides(matrix4f, buffer, light, overlay);

        TEST_SHADER_PROGRAM.unbind();

        //if (MinecraftClient.getInstance().player != null) {
        //    MinecraftClient.getInstance().player.sendMessage(Text.literal(cameraRot.toString()));
        //}

        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
    }

    private void setUniform(ShaderProgram program, String name, Vec3d value) {
        if (program.getUniform(name) != null && value.toVector3f() != null) {
            Objects.requireNonNull(program.getUniform(name)).set(value.toVector3f());
        }
    }
    private void setUniform(ShaderProgram program, String name, Vector3f value) {
        if (program.getUniform(name) != null && value != null) {
            Objects.requireNonNull(program.getUniform(name)).set(value);
        }
    }
    private void setUniform(ShaderProgram program, String name, int value) {
        if (program.getUniform(name) != null) {
            Objects.requireNonNull(program.getUniform(name)).set(value);
        }
    }
    private void setUniform(ShaderProgram program, String name, float value) {
        if (program.getUniform(name) != null) {
            Objects.requireNonNull(program.getUniform(name)).set(value);
        }
    }
    private void setUniform(ShaderProgram program, String name, double value) {
        if (program.getUniform(name) != null) {
            Objects.requireNonNull(program.getUniform(name)).set((float) value);
        }
    }

    private void setSampler(ShaderProgram shader, int id, Identifier texture) {
        RenderSystem.setShaderTexture(id, texture);
        RenderSystem.bindTexture(id);
    }

    private void renderSides(Matrix4f matrix, VertexConsumer vertexConsumer, int light, int overlay) {
        float f = 0F;
        float g = 1F;
        this.renderSide(matrix, vertexConsumer, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, light, overlay);
        this.renderSide(matrix, vertexConsumer, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, light, overlay);
        this.renderSide(matrix, vertexConsumer, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, light, overlay);
        this.renderSide(matrix, vertexConsumer, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, light, overlay);
        this.renderSide(matrix, vertexConsumer, 0.0F, 1.0F, f, f, 0.0F, 0.0F, 1.0F, 1.0F, light, overlay);
        this.renderSide(matrix, vertexConsumer, 0.0F, 1.0F, g, g, 1.0F, 1.0F, 0.0F, 0.0F, light, overlay);
    }

    private void renderSide(Matrix4f model, VertexConsumer vertices, float x1, float x2, float y1, float y2, float z1, float z2, float z3, float z4, int light, int overlay) {
        vertices.vertex(model, x1, y1, z1);
        vertices.vertex(model, x2, y1, z2);
        vertices.vertex(model, x2, y2, z3);
        vertices.vertex(model, x1, y2, z4);
    }
}
