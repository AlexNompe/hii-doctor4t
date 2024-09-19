package net.firmamentsspark.shaderprograms;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.impl.client.rendering.FabricShaderProgram;
import net.firmamentsspark.mixin.client.ExampleClientMixin;
import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.util.Window;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;

import java.io.IOException;

public class PositionedShaderProgram extends ShaderProgram {
    private final GlUniform cameraPosUniformLocation;

    public PositionedShaderProgram(ResourceFactory factory, String name, VertexFormat format) throws IOException {
        super(factory, name, format);
        this.cameraPosUniformLocation = this.getUniform("cameraPos");
    }

    @Override
    public void initializeUniforms(VertexFormat.DrawMode drawMode, Matrix4f viewMatrix, Matrix4f projectionMatrix, Window window) {
        for(int i = 0; i < 12; ++i) {
            int j = RenderSystem.getShaderTexture(i);
            this.addSampler("Sampler" + i, j);
        }

        if (this.cameraPosUniformLocation != null) {
            this.cameraPosUniformLocation.set(RenderSystem.getShaderGameTime());
        }

        if (this.modelViewMat != null) {
            this.modelViewMat.set(viewMatrix);
        }

        if (this.projectionMat != null) {
            this.projectionMat.set(projectionMatrix);
        }

        if (this.colorModulator != null) {
            this.colorModulator.set(RenderSystem.getShaderColor());
        }

        if (this.glintAlpha != null) {
            this.glintAlpha.set(RenderSystem.getShaderGlintAlpha());
        }

        if (this.fogStart != null) {
            this.fogStart.set(RenderSystem.getShaderFogStart());
        }

        if (this.fogEnd != null) {
            this.fogEnd.set(RenderSystem.getShaderFogEnd());
        }

        if (this.fogColor != null) {
            this.fogColor.set(RenderSystem.getShaderFogColor());
        }

        if (this.fogShape != null) {
            this.fogShape.set(RenderSystem.getShaderFogShape().getId());
        }

        if (this.textureMat != null) {
            this.textureMat.set(RenderSystem.getTextureMatrix());
        }

        if (this.gameTime != null) {
            this.gameTime.set(RenderSystem.getShaderGameTime());
        }

        if (this.screenSize != null) {
            this.screenSize.set((float)window.getFramebufferWidth(), (float)window.getFramebufferHeight());
        }

        if (this.lineWidth != null && (drawMode == VertexFormat.DrawMode.LINES || drawMode == VertexFormat.DrawMode.LINE_STRIP)) {
            this.lineWidth.set(RenderSystem.getShaderLineWidth());
        }

        RenderSystem.setupShaderLights(this);
    }
}
