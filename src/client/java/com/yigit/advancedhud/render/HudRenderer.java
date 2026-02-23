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
        ModConfig config = ModConfig.get();
        
        java.util.List<LineInfo> lines = new java.util.ArrayList<>();
        
        // 1. Name (Title)
        lines.add(new LineInfo(targetInfo.getName(), 0xFFFFFF, true));
        
        // 2. Extra Info (Block ID or Health)
        if (!targetInfo.isEntity()) {
            if (config.showBlockId) {
                lines.add(new LineInfo(targetInfo.getExtraInfo(), 0xAAAAAA, false));
            }
        } else {
            if (config.showEntityId) {
                lines.add(new LineInfo(targetInfo.getEntityId(), 0x888888, false));
            }
        }

        // 3. Mod Name
        if (config.showModName && !targetInfo.getModName().isEmpty()) {
            lines.add(new LineInfo(targetInfo.getModName(), 0x5555FF, false));
        }

        // 4. Block Specifics
        if (!targetInfo.isEntity()) {
            if (config.showEffectiveTool && !targetInfo.getEffectiveTool().isEmpty()) {
                lines.add(new LineInfo("Tool: " + targetInfo.getEffectiveTool(), 0xFFFF55, false));
            }
            if (config.showCropGrowth && targetInfo.isCrops()) {
                lines.add(new LineInfo("Growth: " + targetInfo.getGrowthProgress() + "%", 0x55FF55, false));
            }
            if (config.showWaterlogged && targetInfo.isWaterlogged()) {
                lines.add(new LineInfo("Waterlogged", 0x55FFFF, false));
            }
            if (config.showBeeCount && targetInfo.getBeeCount() >= 0) {
                lines.add(new LineInfo("Bees: " + targetInfo.getBeeCount(), 0xFFAA00, false));
            }
            if (config.showContainerInfo && targetInfo.getItemCount() >= 0) {
                lines.add(new LineInfo("Items: " + targetInfo.getItemCount() + "/" + targetInfo.getInventorySize(), 0xAAAAAA, false));
            }
        } else {
            // 5. Entity Specifics
            if (config.showEntityArmor && targetInfo.getArmor() > 0) {
                lines.add(new LineInfo("Armor: " + targetInfo.getArmor(), 0xAAAAAA, false));
            }
            if (config.showEntityOwner && !targetInfo.getOwner().isEmpty()) {
                lines.add(new LineInfo("Owner: " + targetInfo.getOwner(), 0x55FFFF, false));
            }
            
            // Horse Stats
            if (config.showHorseStats && targetInfo.getHorseJump() >= 0) {
                // Formatting speed and jump to be more human-readable
                // Speed: internal * 42.15 (~ blocks/sec)
                // Jump: internal ~ 0.4 to 1.0 (internal units)
                lines.add(new LineInfo(String.format("Speed: %.2f b/s", targetInfo.getHorseSpeed() * 42.15f), 0xFFAA00, false));
                lines.add(new LineInfo(String.format("Jump: %.2f", targetInfo.getHorseJump()), 0xFFAA00, false));
            }
            
            // Villager Info
            if (config.showVillagerInfo && !targetInfo.getVillagerProfession().isEmpty()) {
                String profession = targetInfo.getVillagerProfession();
                profession = profession.substring(0, 1).toUpperCase() + profession.substring(1);
                lines.add(new LineInfo(profession + (targetInfo.getVillagerLevel() > 0 ? " (Level " + targetInfo.getVillagerLevel() + ")" : ""), 0xFFFF55, false));
            }
        }

        boolean showHealth = targetInfo.isEntity() && config.showEntityHealth && targetInfo.getHealth() >= 0;
        int healthBarWidth = 100;
        
        int maxLineWidth = 0;
        for (LineInfo line : lines) {
            maxLineWidth = Math.max(maxLineWidth, textRenderer.getWidth(line.text));
        }
        if (showHealth) maxLineWidth = Math.max(maxLineWidth, healthBarWidth + 12);

        boolean hasIcon = !targetInfo.getStack().isEmpty();
        int iconSize = hasIcon ? 18 : 0;
        int padding = 6;
        
        int boxWidth = maxLineWidth + padding * 2 + (hasIcon ? iconSize + 4 : 0);
        int boxHeight = 0;
        
        for (int i = 0; i < lines.size(); i++) {
            boxHeight += (i == 0) ? 12 : 10;
        }
        if (showHealth) boxHeight += 12;
        
        boolean showBreakingProgress = !targetInfo.isEntity() && config.showBreakingProgress && targetInfo.getBreakingProgress() > 0;
        if (showBreakingProgress) boxHeight += 12;

        boxHeight += padding * 2;

        int x = (screenWidth - boxWidth) / 2 + config.xOffset;
        int y = config.yOffset;

        renderTooltipBackground(drawContext, x, y, boxWidth, boxHeight);
        
        int currentX = x + padding;
        int currentY = y + padding;

        if (hasIcon) {
            drawContext.drawItem(targetInfo.getStack(), currentX, currentY + (boxHeight - padding * 2 - 16) / 2);
            currentX += iconSize + 4;
        }

        for (int i = 0; i < lines.size(); i++) {
            LineInfo line = lines.get(i);
            drawContext.drawText(textRenderer, line.text, currentX, currentY, line.color, true);
            currentY += (i == 0) ? 12 : 10;
        }

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
            currentY += 12;
        }

        if (showBreakingProgress) {
            int barWidth = maxLineWidth;
            int barHeight = 5;
            int barY = currentY + 4;
            
            float progress = Math.max(0, Math.min(1, targetInfo.getBreakingProgress()));
            int currentProgressWidth = (int) (barWidth * progress);

            drawContext.fill(currentX - 1, barY - 1, currentX + barWidth + 1, barY + barHeight + 1, 0xFF000000);
            drawContext.fill(currentX, barY, currentX + barWidth, barY + barHeight, 0xFF444400);
            drawContext.fill(currentX, barY, currentX + currentProgressWidth, barY + barHeight, 0xFFFFFF55);
        }
    }

    private static class LineInfo {
        final String text;
        final int color;
        final boolean isTitle;

        LineInfo(String text, int color, boolean isTitle) {
            this.text = text;
            this.color = color;
            this.isTitle = isTitle;
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
