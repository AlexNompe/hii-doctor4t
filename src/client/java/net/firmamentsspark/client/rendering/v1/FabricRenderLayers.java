package net.firmamentsspark.client.rendering.v1;

import net.firmamentsspark.impl.client.rendering.RenderLayerAccessors;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexFormat;

// THIS CODE ISN'T MINE! GET IT HERE: https://gist.github.com/Daomephsta/e986c37932b42cd8d89066ceab412764#file-renderlayeraccessors-java

/**
 * Extend this to get access to protected members of RenderLayer, e.g. MultiPhaseParameters
 */
public abstract class FabricRenderLayers extends RenderLayer {
    protected FabricRenderLayers() {
        super(null, null, null, 0, false, false, null, null);
    }

    /**Makes a render layer**/
    protected static RenderLayer create(String name, VertexFormat vertexFormat,
                                        VertexFormat.DrawMode drawMode, int expectedBufferSize, RenderLayer.MultiPhaseParameters phaseParameters) {
        return RenderLayerAccessors.create(name, vertexFormat, drawMode, expectedBufferSize, phaseParameters);
    }
}