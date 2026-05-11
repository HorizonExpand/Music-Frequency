package net.hrumer.music_frequency;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER =new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.DoubleValue MUSIC_FREQUENCY_MULTIPLIER = BUILDER.comment("1 — default, 10 — always").defineInRange("musicFrequencyMultiplier", 1, 0.1, 10);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static double musicFrequencyMultiplier;

    @SubscribeEvent
static void onLoad(final ModConfig.ModConfigEvent event) {
        musicFrequencyMultiplier = MUSIC_FREQUENCY_MULTIPLIER.get();
    }
}
