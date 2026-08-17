package net.azyrod.beacon_tinted_glass;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        BeaconTintedGlassConfig config = BeaconTintedGlass.config;

        return parent -> BeaconTintedGlassConfig.HANDLER.generateGui().generateScreen(parent);
//        return parent -> YetAnotherConfigLib.createBuilder()
//                .title(Component.literal("Beacon Tinted Glass Config"))
//                .category(ConfigCategory.createBuilder()
//                        .name(Component.literal("General"))
//                        .option(Option.<BeaconTintedGlassConfig.TintedGlassMode>createBuilder()
//                                .name(Component.literal("Tinted Glass Mode"))
//                                .binding(BeaconTintedGlassConfig.TintedGlassMode.BLOCK, () -> config.tintedGlassMode, newVal -> config.tintedGlassMode = newVal)
//                                .description(OptionDescription.of(Component.literal("Determines if the beacon beam is only disabled until the next colored block,\nor if another TintedGlass block is required to toggle it back on.")))
//                                .controller(EnumControllerBuilder::create)
//                                .build())
//                        .build())
//                .build()
//                .generateScreen(parent);
    }
}
