package net.hrumer.music_frequency.mixins;

import net.hrumer.music_frequency.Config;
import net.hrumer.music_frequency.NowPlayingToast;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.audio.Sound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.toasts.SystemToast;
import net.minecraft.item.Items;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MusicTicker.class)
public class MusicManagerMixin {

    @Shadow
    private int nextSongDelay;
    @Shadow
    private ISound currentMusic;

    @Final
    private Minecraft minecraft;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if (nextSongDelay > 1) {
            float modifiedDelay = (float) (nextSongDelay / Config.musicFrequencyMultiplier);
            nextSongDelay = Math.max(1, (int) modifiedDelay);
        }
    }

    @Inject(method = "startPlaying", at = @At("TAIL"))
    private void showToast(CallbackInfo ci) {
        if (currentMusic == null || currentMusic.getSound() == SoundHandler.EMPTY_SOUND) {
            return;
        }

        String currentMusicName = getCurrentMusicTranslationKey();

        if (currentMusicName != null) {
            LogManager.getLogger().info("Current Music Name: {}", currentMusicName);
            ITextComponent title = new TranslationTextComponent("gui.now_playing")
                    .withStyle(style -> style.withBold(true).withColor(Color.fromRgb(0xFFAA00)));
            ITextComponent subtitle = new TranslationTextComponent(currentMusicName.replace("/", "."))
                    .withStyle(style -> style.withItalic(true));

            NowPlayingToast toast = new NowPlayingToast(title, subtitle, Items.MUSIC_DISC_CAT.getDefaultInstance(), 0xFF151515, 0xFF050505);
            minecraft.getToasts().addToast(toast);
        }
    }

    public String getCurrentMusicTranslationKey() {
        if (currentMusic != null) {
            Sound sound = currentMusic.getSound();
            if (sound != null) {
                return sound.getLocation().toString().replace("minecraft:", "");
            }
        }

        return null;
    }
}