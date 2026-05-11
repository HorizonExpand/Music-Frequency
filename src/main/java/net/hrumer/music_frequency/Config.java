package net.hrumer.music_frequency;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = MusicFrequency.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    private static final ModConfigSpec.DoubleValue MUSIC_FREQUENCY_MULTIPLIER = BUILDER.comment("1 — default, 10 — always").defineInRange("musicFrequencyMultiplier", 1, 0.1, 10);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static double musicFrequencyMultiplier;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        musicFrequencyMultiplier = MUSIC_FREQUENCY_MULTIPLIER.get();
    }
}
