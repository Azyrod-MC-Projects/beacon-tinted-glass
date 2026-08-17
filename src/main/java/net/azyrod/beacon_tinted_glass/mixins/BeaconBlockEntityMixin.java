package net.azyrod.beacon_tinted_glass.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.azyrod.beacon_tinted_glass.BeaconTintedGlass;
import net.azyrod.beacon_tinted_glass.BeaconTintedGlassConfig;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BeaconBeamOwner;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

import static net.azyrod.beacon_tinted_glass.BeaconTintedGlass.*;

@Mixin(BeaconBlockEntity.class)
public abstract class BeaconBlockEntityMixin {
    @Shadow
    private List<BeaconBeamOwner.Section> checkingBeamSections;

    @WrapOperation(
        method = "tick",
        at = @At(
            value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Ljava/lang/Object;)Z"
        )
    )
    private static boolean allowTintedGlass(BlockState instance, Object object, Operation<Boolean> original, @Local(argsOnly = true, name = "entity") BeaconBlockEntity baseEntity, @Local(name = "lastBeamSection") LocalRef<BeaconBeamOwner.Section> lastBeamSection) {
        if (!instance.is(Blocks.TINTED_GLASS)) {
            return original.call(instance, object);
        }
        int currentColor = lastBeamSection.get().getColor();
        int newColor = TINTED_GLASS_COLOR + (currentColor & COLOR_MASK); // Completely transparent color - but keeping original value so we can do stuff with it in the future
        BeaconBlockEntityMixin entity = (BeaconBlockEntityMixin)(Object)baseEntity;

        // TODO: Since we only override the Alpha Channel, we could keep the RGB value and restore it when exiting tinted glass
        if (config.tintedGlassMode == BeaconTintedGlassConfig.TintedGlassMode.TOGGLE) {
            if (BeaconTintedGlass.isColorInvisible(currentColor)) {
                newColor = COLOR_MASK; // HACK: Instead store the state on the mixin instance, so we can truly keep the original color value
            }

            BeaconBeamOwner.Section newSection = new BeaconBeamOwner.Section(newColor);
            lastBeamSection.set(newSection);
            entity.checkingBeamSections.add(newSection);
        } else {
            if (newColor == currentColor) {
                lastBeamSection.get().increaseHeight();
            } else {
                BeaconBeamOwner.Section newSection = new BeaconBeamOwner.Section(newColor);
                lastBeamSection.set(newSection);
                entity.checkingBeamSections.add(newSection);
            }
        }

        return true;
    }

    @WrapOperation(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/entity/BeaconBeamOwner$Section;increaseHeight()V",
            ordinal = 1
        )
    )
    private static void avoidDoubleIncreaseHeightForTintedGlass(BeaconBeamOwner.Section instance, Operation<Void> original, @Local(name = "state") BlockState state) {
        if (!state.is(Blocks.TINTED_GLASS)) {
            original.call(instance);
        }
    }

    @WrapOperation(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/ARGB;average(II)I"
        )
    )
    private static int tintedGlassOverrideColor(int lhs, int rhs, Operation<Integer> original) {
        if (BeaconTintedGlass.isColorInvisible(lhs) && lhs != COLOR_MASK) {
            if (config.tintedGlassMode == BeaconTintedGlassConfig.TintedGlassMode.BLOCK_ONCE) {
                return rhs; // Fully replace tinted glass with new color
            } else {
                return lhs; // In toggle mode or block_forever, keep tinted glass override
            }
        } else if (lhs == COLOR_MASK) {
            return rhs; // Fully replace tinted glass with new color
        } else {
            return original.call(lhs, rhs);
        }
    }
}
