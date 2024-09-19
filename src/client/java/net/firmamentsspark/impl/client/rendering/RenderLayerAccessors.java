package net.firmamentsspark.impl.client.rendering;


import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexFormat;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

// THIS CODE ISN'T MINE! GET IT HERE: https://gist.github.com/Daomephsta/e986c37932b42cd8d89066ceab412764#file-renderlayeraccessors-java

public abstract class RenderLayerAccessors extends RenderLayer
{
    private RenderLayerAccessors() {
        super(null, null, null, 0, false, false, null, null);
    }

    private static final MethodHandle RENDER_LAYER__OF;
    static {
        try {
            var lookup = MethodHandles.privateLookupIn(RenderLayer.class, MethodHandles.lookup());
            MappingResolver mappings = FabricLoader.getInstance().getMappingResolver();
            {
                var methodName = mappings.mapMethodName("intermediary", "net.minecraft.class_1921", "method_24048",
                        "(Ljava/lang/String;Lnet/minecraft/class_293;Lnet/minecraft/class_293$class_5596;"
                                + "ILnet/minecraft/class_1921$class_4688;)Lnet/minecraft/class_1921$class_4687;");
                var renderLayer$Multiphase = Class.forName(mappings.mapClassName("intermediary",
                        "net.minecraft.class_1921$class_4687"));
                var methodDesc = MethodType.methodType(renderLayer$Multiphase, String.class, VertexFormat.class,
                        VertexFormat.DrawMode.class, int.class, MultiPhaseParameters.class);
                RENDER_LAYER__OF = lookup.findStatic(RenderLayer.class, methodName, methodDesc)
                        .asType(methodDesc.changeReturnType(RenderLayer.class));
            }
        }
        catch (IllegalAccessException | ClassNotFoundException | NoSuchMethodException e) {
            throw new RuntimeException("Failed to initialise RenderLayer method handles", e);
        }
    }

    public static RenderLayer create(String name, VertexFormat vertexFormat,
                                     VertexFormat.DrawMode drawMode, int expectedBufferSize, RenderLayer.MultiPhaseParameters phaseParameters) {
        try {
            return (RenderLayer) RENDER_LAYER__OF.invokeExact(name, vertexFormat,
                    drawMode, expectedBufferSize, phaseParameters);
        }
        catch (Throwable e) {
            throw new RuntimeException("Failed to invoke RenderLayer.of method handle", e);
        }
    }
}