package net.hrumer.music_frequency.mixins;

import net.hrumer.music_frequency.Config;
import net.minecraft.client.sounds.MusicManager;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MusicManager.class)
public class MusicManagerMixin {

    @Shadow
    private int nextSongDelay;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if (nextSongDelay > 1) {
            float modifiedDelay = (float) (nextSongDelay / Config.musicFrequencyMultiplier);
            nextSongDelay = Math.max(1, (int) modifiedDelay);
        }
    }
}