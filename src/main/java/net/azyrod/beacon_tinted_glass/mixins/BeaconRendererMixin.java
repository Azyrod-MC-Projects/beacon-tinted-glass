package net.azyrod.beacon_tinted_glass.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import net.azyrod.beacon_tinted_glass.BeaconTintedGlass;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BeaconRenderer.class)
@Environment(EnvType.CLIENT)
public class BeaconRendererMixin {
    @Inject(
        method = "submitBeaconBeam(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/resources/Identifier;FFIIIFF)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void cancelRenderIfTintedGlass(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, Identifier beamLocation, float scale, float animationTime, int beamStart, int height, int color, float solidBeamRadius, float beamGlowRadius, CallbackInfo ci) {
        if (BeaconTintedGlass.isColorInvisible(color)) {
            ci.cancel();
        }
    }
}
