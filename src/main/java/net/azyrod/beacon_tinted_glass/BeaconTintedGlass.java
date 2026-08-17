package net.azyrod.beacon_tinted_glass;

import net.fabricmc.api.ModInitializer;

public class BeaconTintedGlass implements ModInitializer {
    public static String MOD_ID = "beacon_tinted_glass";
    public static int TINTED_GLASS_COLOR = 0x00000000;
    public static int ALPHA_MASK = 0xFF000000;
    public static int COLOR_MASK = 0x00FFFFFF;
    public static BeaconTintedGlassConfig config;

    @Override
    public void onInitialize() {
        BeaconTintedGlassConfig.HANDLER.load();
        config = BeaconTintedGlassConfig.HANDLER.instance();
    }

    public static boolean isColorInvisible(int color) {
        return (color & ALPHA_MASK) == 0x00000000;
    }
}
