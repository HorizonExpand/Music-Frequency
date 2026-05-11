package net.hrumer.music_frequency;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.gui.toasts.ToastGui;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

public class NowPlayingToast implements IToast {
    private final ITextComponent title;
    private final ITextComponent subtitle;
    private final ItemStack icon;
    private long firstDrawTime;
    private final int backgroundColor;
    private final int borderColor;

    public NowPlayingToast(ITextComponent title, ITextComponent subtitle, ItemStack icon, int bgColor, int borderColor) {
        this.title = title;
        this.subtitle = subtitle;
        this.icon = icon;
        this.backgroundColor = bgColor;
        this.borderColor = borderColor;
    }

    @Override
    public Visibility render(MatrixStack matrixStack, ToastGui toastGui, long time) {
        if (firstDrawTime == 0L) {
            firstDrawTime = time;
        }

        Minecraft mc = toastGui.getMinecraft();
        int width = 160;
        int height = 32;
        RenderSystem.enableBlend();
        fill(matrixStack, 0, 0, width, height, backgroundColor);
        fill(matrixStack, 0, 0, width, 1, borderColor);
        fill(matrixStack, 0, height-1, width, height, borderColor);
        fill(matrixStack, 0, 0, 1, height, borderColor);
        fill(matrixStack, width-1, 0, width, height, borderColor);

        ItemRenderer itemRenderer = mc.getItemRenderer();
        itemRenderer.renderGuiItem(icon, 8, 8);

        mc.font.draw(matrixStack, title, 45, 7, 0xFFFFFF);
        if (subtitle != null) {
            mc.font.draw(matrixStack, subtitle, 45, 18, 0xAAAAAA);
        }

        RenderSystem.disableBlend();

        return (time - firstDrawTime) >= 5000L ? Visibility.HIDE : Visibility.SHOW;
    }

    private void fill(MatrixStack ms, int x1, int y1, int x2, int y2, int color) {
        net.minecraft.client.gui.AbstractGui.fill(ms, x1, y1, x2, y2, color);
    }
}