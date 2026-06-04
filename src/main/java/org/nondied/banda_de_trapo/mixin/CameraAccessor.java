package org.nondied.banda_de_trapo.mixin;

import net.minecraft.client.render.Camera;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Camera.class)
public interface CameraAccessor {

    @Invoker("setPos")
    void invokeSetPos(Vec3d pos);

    @Invoker("setRotation")
    void invokeSetRotation(float yaw, float pitch);
}