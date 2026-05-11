package net.hrumer.music_frequency.mixins;

import com.mojang.logging.LogUtils;
import net.hrumer.music_frequency.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MusicManager.class)
public class MusicManagerMixin {

    @Shadow
    private int nextSongDelay;
    @Shadow
    private SoundInstance currentMusic;

    @Final
    Minecraft minecraft;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if (nextSongDelay > 1) {
            float modifiedDelay = (float) (nextSongDelay / Config.musicFrequencyMultiplier);
            nextSongDelay = Math.max(1, (int) modifiedDelay);
        }
    }

    @Inject(method = "startPlaying", at = @At("TAIL"))
    private void showToast(CallbackInfo ci) {
        if (currentMusic == null || currentMusic.getSound() == SoundManager.EMPTY_SOUND) {
            return;
        }

        String currentMusicName = getCurrentMusicTranslationKey();

        if (currentMusicName != null) {
            LogUtils.getLogger().info("Current Music Name: {}", currentMusicName);
            Component title = Component.translatable("gui.now_playing")
                    .withStyle(style -> style.withBold(true).withColor(0xFFAA00));

            Component subtitle = Component.translatable(currentMusicName.replace("/", "."))
                    .withStyle(style -> style.withItalic(true));

            SystemToast.add(minecraft.getToasts(), SystemToast.SystemToastId.PERIODIC_NOTIFICATION, title, subtitle);
        }
    }

    public String getCurrentMusicTranslationKey() {
        if (currentMusic != null) {
            Sound sound = currentMusic.getSound();
            if (sound != null) {
                return sound.getLocation().toShortLanguageKey();
            }
        }

        return null;
    }
}