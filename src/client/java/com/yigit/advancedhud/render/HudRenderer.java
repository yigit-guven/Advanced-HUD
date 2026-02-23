package com.yigit.advancedhud.render;

import com.yigit.advancedhud.config.ModConfig;
import com.yigit.advancedhud.util.TargetInfo;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.font.TextRenderer;

public class HudRenderer implements HudRenderCallback {
    private final TargetInfo targetInfo = new TargetInfo();

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
        
        boolean showHealth = targetInfo.isEntity() && ModConfig.get().showEntityHealth && targetInfo.getHealth() >= 0;
        int healthBarWidth = 100;
        
        int contentWidth = Math.max(nameWidth, infoWidth);
        if (showHealth) contentWidth = Math.max(contentWidth, healthBarWidth + 12);

        boolean hasIcon = !targetInfo.getStack().isEmpty();
        int iconSize = hasIcon ? 18 : 0;
        int padding = 6;
        
        int boxWidth = contentWidth + padding * 2 + (hasIcon ? iconSize + 4 : 0);
        int boxHeight = 0;
        
        // Calculate height
        boxHeight += 12; // Name height
        if (!info.isEmpty()) boxHeight += 10;
        if (showHealth) boxHeight += 12;
        boxHeight += padding * 2;

        // Position: Top Center + Config Offset
        int x = (screenWidth - boxWidth) / 2 + ModConfig.get().xOffset;
        int y = ModConfig.get().yOffset;

        // Draw Background (Vanilla Tooltip Style)
        renderTooltipBackground(drawContext, x, y, boxWidth, boxHeight);
        
        int currentX = x + padding;
        int currentY = y + padding;

        // Draw Icon
        if (hasIcon) {
            drawContext.drawItem(targetInfo.getStack(), currentX, currentY + (boxHeight - padding * 2 - 16) / 2);
            currentX += iconSize + 4;
        }

        // Draw Name
        drawContext.drawText(textRenderer, name, currentX, currentY, 0xFFFFFF, true);
        currentY += 12;
        
        // Draw Extra Info
        if (!info.isEmpty()) {
            drawContext.drawText(textRenderer, info, currentX, currentY, 0xAAAAAA, true);
            currentY += 10;
        }

        // Draw Health Bar if Entity
        if (showHealth) {
            drawContext.drawText(textRenderer, "❤", currentX, currentY, 0xFF5555, true);
            
            int barX = currentX + 12;
            int barY = currentY + 2;
            int barHeight = 5;
            
            float healthPercent = Math.max(0, Math.min(1, targetInfo.getHealth() / targetInfo.getMaxHealth()));
            int currentHealthWidth = (int) (healthBarWidth * healthPercent);

            drawContext.fill(barX - 1, barY - 1, barX + healthBarWidth + 1, barY + barHeight + 1, 0xFF000000);
            drawContext.fill(barX, barY, barX + healthBarWidth, barY + barHeight, 0xFF440000);
            drawContext.fill(barX, barY, barX + currentHealthWidth, barY + barHeight, 0xFFFF5555);
        }
    }

    private void renderTooltipBackground(DrawContext context, int x, int y, int width, int height) {
        int bgColor = 0xF0100010;
        int borderColorStart = 0x505000FF;
        int borderColorEnd = (borderColorStart & 0xFEFEFE) >> 1 | borderColorStart & 0xFF000000;
        
        // Frame colors
        int frameOuter = 0xFF222222;
        int frameInner = 0xFF444444;

        // Draw Frame
        context.fill(x - 2, y - 2, x + width + 2, y + height + 2, frameOuter);
        context.fill(x - 1, y - 1, x + width + 1, y + height + 1, frameInner);

        // Main background
        context.fill(x + 1, y, x + width - 1, y + height, bgColor);
        context.fill(x, y + 1, x + width, y + height - 1, bgColor);

        // Border
        context.fill(x + 1, y + 1, x + width - 1, y + 2, borderColorStart);
        context.fill(x + 1, y + height - 2, x + width - 1, y + height - 1, borderColorEnd);
        context.fillGradient(x + 1, y + 2, x + 2, y + height - 2, borderColorStart, borderColorEnd);
        context.fillGradient(x + width - 2, y + 2, x + width - 1, y + height - 2, borderColorStart, borderColorEnd);
    }
}
