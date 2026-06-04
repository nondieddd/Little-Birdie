package org.nondied.banda_de_trapo.mixin;

import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;
import org.nondied.banda_de_trapo.client.camera.OrbitalCameraSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public class CameraMixin {

    @Inject(
            method = "update",
            at = @At("TAIL")
    )
    private void update(
            BlockView area,
            Entity focusedEntity,
            boolean thirdPerson,
            boolean inverseView,
            float tickDelta,
            CallbackInfo ci
    ) {
        if (OrbitalCameraSystem.isActive()) {
            OrbitalCameraSystem.update((Camera) (Object) this, tickDelta);
        }
    }
}