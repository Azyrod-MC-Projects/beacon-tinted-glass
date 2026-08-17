package net.azyrod.beacon_tinted_glass;

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.autogen.AutoGen;
import dev.isxander.yacl3.config.v2.api.autogen.CustomDescription;
import dev.isxander.yacl3.config.v2.api.autogen.EnumCycler;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;

import static net.azyrod.beacon_tinted_glass.BeaconTintedGlass.MOD_ID;

public class BeaconTintedGlassConfig {
    @SerialEntry @AutoGen(category = "general") @EnumCycler
    @CustomDescription("""
    Determines if the beacon beam is only disabled until the next colored block, if another TintedGlass block is required to toggle it back on, or if it should stay blocked no matter what.
    
    - Block Once : Tinted Glass block only disables the beam until the next Colored Glass block
    - Toggle : Tinted Glass block disables the beam until the next Tinted Glass block
    - Block Forever : Tinted Glass block disable the beam and no block will enable it back
    """)
    public TintedGlassMode tintedGlassMode = TintedGlassMode.BLOCK_ONCE;

    public enum TintedGlassMode {
        BLOCK_ONCE,
        TOGGLE,
        BLOCK_FOREVER
    }

    public static ConfigClassHandler<BeaconTintedGlassConfig> HANDLER = ConfigClassHandler.createBuilder(BeaconTintedGlassConfig.class)
            .id(Identifier.fromNamespaceAndPath(MOD_ID, "config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve(MOD_ID + ".json5"))
                    .setJson5(true)
                    .build())
            .build();
}
