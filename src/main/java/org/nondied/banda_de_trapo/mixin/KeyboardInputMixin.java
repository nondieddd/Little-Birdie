package org.nondied.banda_de_trapo.mixin;

import net.minecraft.client.input.KeyboardInput;
import org.nondied.banda_de_trapo.client.camera.OrbitalCameraSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public class KeyboardInputMixin {

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void onTick(CallbackInfo ci) {
        if (OrbitalCameraSystem.isActive()) {
            ci.cancel();
        }
    }
}