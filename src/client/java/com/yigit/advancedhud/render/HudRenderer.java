package com.yigit.advancedhud.render;

import com.yigit.advancedhud.config.ModConfig;
import com.yigit.advancedhud.util.TargetInfo;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.font.TextRenderer;

public class HudRenderer implements HudRenderCallback {
    private final TargetInfo targetInfo = new TargetInfo();
    private static final int BG_COLOR = 0x90000000;
    private static final int TEXT_COLOR = 0xFFFFFF;

    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.options.hudHidden || !ModConfig.get().enabled) return;

        targetInfo.update(client);
        if (!targetInfo.hasTarget()) return;

        TextRenderer textRenderer = client.textRenderer;
        int screenWidth = client.getWindow().getScaledWidth();
        
        String name = targetInfo.getName();
        String info = targetInfo.getExtraInfo();
        
        // Hide block ID if configured
        if (!targetInfo.isEntity() && !ModConfig.get().showBlockId) {
            info = "";
        }

        int nameWidth = textRenderer.getWidth(name);
        int infoWidth = textRenderer.getWidth(info);
        int maxWidth = Math.max(nameWidth, infoWidth);

        int padding = 5;
        int boxWidth = maxWidth + padding * 2;
        boolean showHealth = targetInfo.isEntity() && ModConfig.get().showEntityHealth && targetInfo.getHealth() >= 0;
        int boxHeight = (info.isEmpty() ? 15 : 25) + (showHealth ? 10 : 0);

        // Position: Top Center + Config Offset
        int x = (screenWidth - boxWidth) / 2 + ModConfig.get().xOffset;
        int y = ModConfig.get().yOffset;

        // Draw Background
        drawContext.fill(x, y, x + boxWidth, y + boxHeight, BG_COLOR);
        
        // Draw Name
        drawContext.drawText(textRenderer, name, x + padding, y + padding, TEXT_COLOR, true);
        
        // Draw Extra Info
        if (!info.isEmpty()) {
            drawContext.drawText(textRenderer, info, x + padding, y + padding + 12, 0xAAAAAA, true);
        }

        // Draw Health Bar if Entity
        if (showHealth) {
            int barWidth = maxWidth;
            int barHeight = 4;
            int barX = x + padding;
            int barY = y + boxHeight - padding - barHeight;
            
            float healthPercent = Math.max(0, Math.min(1, targetInfo.getHealth() / targetInfo.getMaxHealth()));
            int currentHealthWidth = (int) (barWidth * healthPercent);

            drawContext.fill(barX, barY, barX + barWidth, barY + barHeight, 0xFF440000);
            drawContext.fill(barX, barY, barX + currentHealthWidth, barY + barHeight, 0xFFFF0000);
        }
    }
}
