package com.yigit.advancedhud.render;

import com.yigit.advancedhud.config.ModConfig;
import com.yigit.advancedhud.util.TargetInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class HudRenderer {
    private final TargetInfo targetInfo = new TargetInfo();
    
    private static final java.util.List<net.minecraft.world.item.ItemStack> PICKAXES = java.util.Arrays.asList(
        new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.NETHERITE_PICKAXE),
        new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.DIAMOND_PICKAXE),
        new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.IRON_PICKAXE),
        new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.STONE_PICKAXE),
        new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.GOLDEN_PICKAXE)
    );
    private static final java.util.List<net.minecraft.world.item.ItemStack> AXES = java.util.Arrays.asList(
        new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.NETHERITE_AXE),
        new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.DIAMOND_AXE),
        new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.IRON_AXE),
        new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.STONE_AXE),
        new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.GOLDEN_AXE)
    );
    private static final java.util.List<net.minecraft.world.item.ItemStack> SHOVELS = java.util.Arrays.asList(
        new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.NETHERITE_SHOVEL),
        new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.DIAMOND_SHOVEL),
        new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.IRON_SHOVEL),
        new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.STONE_SHOVEL),
        new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.GOLDEN_SHOVEL)
    );
    private static final java.util.List<net.minecraft.world.item.ItemStack> HOES = java.util.Arrays.asList(
        new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.NETHERITE_HOE),
        new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.DIAMOND_HOE),
        new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.IRON_HOE),
        new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.STONE_HOE),
        new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.GOLDEN_HOE)
    );
    private static final java.util.List<net.minecraft.world.item.ItemStack> SHEARS = java.util.Arrays.asList(
        new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.SHEARS)
    );

    @SubscribeEvent
    public void onHudRender(RenderGuiOverlayEvent.Post event) {
        if (event.getOverlay() != VanillaGuiOverlay.HOTBAR.type()) return;

        Minecraft client = Minecraft.getInstance();
        if (client == null || client.options.hideGui || !ModConfig.get().enabled) return;

        targetInfo.update(client);
        if (!targetInfo.hasTarget()) return;

        GuiGraphics drawContext = event.getGuiGraphics();
        Font textRenderer = client.font;
        int screenWidth = event.getWindow().getGuiScaledWidth();
        ModConfig config = ModConfig.get();
        
        // --- DATA COLLECTION ---
        java.util.List<StatInfo> stats = new java.util.ArrayList<>();
        if (!targetInfo.isEntity()) {
            if (config.showCropGrowth && targetInfo.isCrops()) stats.add(new StatInfo("🌱", targetInfo.getGrowthProgress() + "%", 0x55FF55));
            if (config.showBeeCount && targetInfo.getBeeCount() >= 0) stats.add(new StatInfo("🐝", String.valueOf(targetInfo.getBeeCount()), 0xFFAA00));
            if (config.showContainerInfo && targetInfo.getItemCount() >= 0) stats.add(new StatInfo("📦", targetInfo.getItemCount() + "/" + targetInfo.getInventorySize(), 0xAAAAAA));
            if (config.showBlockId) stats.add(new StatInfo("🆔", targetInfo.getExtraInfo(), 0xAAAAAA));
            if (config.showWaterlogged && targetInfo.isWaterlogged()) stats.add(new StatInfo("💧", "Waterlogged", 0x5555FF));
            if (config.showHarvestLevel && !targetInfo.getHarvestLevel().isEmpty()) stats.add(new StatInfo("⛏", targetInfo.getHarvestLevel(), 0xAAAAAA));
        } else {
            if (config.showEntityId) stats.add(new StatInfo("🆔", targetInfo.getEntityId(), 0xAAAAAA));
            if (config.showEntityOwner && !targetInfo.getOwner().isEmpty()) stats.add(new StatInfo("👤", targetInfo.getOwner(), 0xFFAA00));
            if (config.showHorseStats && targetInfo.getHorseJump() >= 0) {
                stats.add(new StatInfo("⚡", String.format("%.1f", targetInfo.getHorseSpeed() * 42.15f), 0xFFAA00));
                stats.add(new StatInfo("⇮", String.format("%.1f", targetInfo.getHorseJump()), 0x55FFFF));
            }
            if (config.showVillagerInfo && !targetInfo.getVillagerProfession().isEmpty()) {
                stats.add(new StatInfo("💼", targetInfo.getVillagerProfession() + (targetInfo.getVillagerLevel() > 0 ? " Lvl " + targetInfo.getVillagerLevel() : ""), 0xFFFF55));
            }
        }

        boolean showHealth = targetInfo.isEntity() && config.showEntityHealth && targetInfo.getHealth() >= 0;
        boolean showBreaking = !targetInfo.isEntity() && config.showBreakingProgress && targetInfo.getBreakingProgress() > 0;
        
        net.minecraft.world.item.ItemStack toolToRender = net.minecraft.world.item.ItemStack.EMPTY;
        if (!targetInfo.isEntity() && config.showEffectiveTool && !targetInfo.getEffectiveTool().isEmpty()) {
            java.util.List<net.minecraft.world.item.ItemStack> tools = java.util.Collections.emptyList();
            switch (targetInfo.getEffectiveTool()) {
                case "Pickaxe" -> tools = PICKAXES;
                case "Axe" -> tools = AXES;
                case "Shovel" -> tools = SHOVELS;
                case "Hoe" -> tools = HOES;
                case "Shears" -> tools = SHEARS;
            }
            if (!tools.isEmpty()) toolToRender = tools.get((int) ((System.currentTimeMillis() / 2000) % tools.size()));
        }

        // --- CALCULATION ---
        int padding = 4;
        int innerGap = 4;
        int lineH = 10;
        int titleH = 12;
        int iconS = 16;
        
        String title = targetInfo.getName();
        String modName = config.showModName ? targetInfo.getModName() : "";
        int wTitle = textRenderer.width(title);
        int wMod = modName.isEmpty() ? 0 : textRenderer.width(modName);
        
        int wStatsTotal = 0;
        for (StatInfo s : stats) wStatsTotal = Math.max(wStatsTotal, textRenderer.width(s.icon) + 12 + textRenderer.width(s.text));
        if (showHealth) {
            int wH = 11 + 60 + 4 + textRenderer.width(String.format("%.1f", targetInfo.getHealth()));
            wStatsTotal = Math.max(wStatsTotal, wH);
        }
        if (targetInfo.isEntity() && config.showEntityArmor && (targetInfo.getArmor() > 0 || targetInfo.getToughness() > 0)) {
            int wA = 11 + 60 + 4 + textRenderer.width(String.valueOf(targetInfo.getArmor()));
            wStatsTotal = Math.max(wStatsTotal, wA);
        }
        
        int mainAreaW = Math.max(wTitle + (wMod > 0 ? 20 + wMod : 0), wStatsTotal);
        int iconW = targetInfo.getStack().isEmpty() ? 0 : iconS + innerGap;
        int toolW = toolToRender.isEmpty() ? 0 : iconS + innerGap;
        
        int totalW = padding * 2 + iconW + mainAreaW + toolW;
        int nLines = 1 + stats.size() + (showHealth ? 1 : 0) + (targetInfo.isEntity() && config.showEntityArmor && (targetInfo.getArmor() > 0 || targetInfo.getToughness() > 0) ? 1 : 0);
        int totalH = padding * 2 + titleH + (nLines > 1 ? (nLines - 1) * lineH : 0);
        if (showBreaking) totalH += 6;
        totalH = Math.max(totalH, padding * 2 + iconS);
        
        // Entity Model Reservation
        boolean drawEntityModel = config.showEntityModel && targetInfo.isEntity() && targetInfo.getTargetedEntity() != null;
        int entityW = drawEntityModel ? 20 : 0;
        int entityScale = 15;
        if (drawEntityModel) {
            LivingEntity target = targetInfo.getTargetedEntity();
            float maxDim = Math.max(target.getBbHeight(), target.getBbWidth());
            entityScale = (int) (18.0f / Math.max(0.5f, maxDim));
            entityScale = Math.max(5, Math.min(20, entityScale));
            
            totalW += entityW + innerGap;
            totalH = Math.max(totalH, padding * 2 + 22);
        }

        int x = (screenWidth - totalW) / 2 + config.xOffset;
        int y = config.yOffset;

        renderTooltipBackground(drawContext, x, y, totalW, totalH);
        
        // --- DRAWING ---
        int curX = x + padding;
        int curY = y + padding;
        
        if (iconW > 0) {
            int iconY = y + (totalH - iconS) / 2;
            if (showBreaking) iconY -= 2; 
            drawContext.renderItem(targetInfo.getStack(), curX, iconY);
            curX += iconW;
        }

        if (drawEntityModel) {
            int modelX = curX + entityW / 2;
            int modelY = y + totalH - padding - 2;
            InventoryScreen.renderEntityInInventoryFollowsMouse(drawContext, modelX, modelY, entityScale, (float)modelX - 45, (float)modelY - 50, targetInfo.getTargetedEntity());
            curX += entityW + innerGap;
        }
        
        drawContext.drawString(textRenderer, title, curX, curY, 0xFFFFFF, true);
        if (wMod > 0) {
            drawContext.drawString(textRenderer, modName, x + totalW - padding - toolW - wMod, curY, 0x5555FF, true);
        }
        curY += titleH;
        
        for (StatInfo s : stats) {
            drawContext.drawString(textRenderer, s.icon, curX, curY, 0xFFFFFF, true);
            drawContext.drawString(textRenderer, s.text, curX + 12, curY, s.color, true);
            curY += lineH;
        }
        
        if (showHealth) {
            drawContext.drawString(textRenderer, "❤", curX, curY, 0xFF5555, true);
            int barX = curX + 11, barY = curY + 2, barW = 60, barH = 5;
            float pct = Math.max(0, Math.min(1, targetInfo.getHealth() / targetInfo.getMaxHealth()));
            drawContext.fill(barX - 1, barY - 1, barX + barW + 1, barY + barH + 1, 0x88000000);
            drawContext.fill(barX, barY, barX + barW, barY + barH, 0x44FF0000);
            int progressW = (int)(barW * pct);
            if (progressW > 0) {
                drawContext.fill(barX, barY, barX + progressW, barY + barH, 0xFFFF3333);
                drawContext.fill(barX, barY, barX + progressW, barY + 1, 0xFFFF7777);
            }
            String healthText = String.format("%.1f", targetInfo.getHealth());
            drawContext.drawString(textRenderer, healthText, barX + barW + 4, curY, 0xFFFFFF, true);
            curY += lineH;
        }

        if (targetInfo.isEntity() && config.showEntityArmor && (targetInfo.getArmor() > 0 || targetInfo.getToughness() > 0)) {
            boolean hasToughness = targetInfo.getToughness() > 0;
            drawContext.drawString(textRenderer, hasToughness ? "🛡+" : "🛡", curX, curY, hasToughness ? 0x55FFFF : 0xAAAAAA, true);
            int barX = curX + 11, barY = curY + 2, barW = 60, barH = 5;
            float pct = Math.min(1, targetInfo.getArmor() / 20.0f);
            drawContext.fill(barX - 1, barY - 1, barX + barW + 1, barY + barH + 1, 0x88000000);
            drawContext.fill(barX, barY, barX + barW, barY + barH, 0x44555555);
            int progressW = (int)(barW * pct);
            if (progressW > 0) {
                drawContext.fill(barX, barY, barX + progressW, barY + barH, hasToughness ? 0xFF55FFFF : 0xFFAAAAAA);
                drawContext.fill(barX, barY, barX + progressW, barY + 1, hasToughness ? 0xFFAFFFFF : 0xFFDDDDDD);
            }
            String armorText = String.valueOf(targetInfo.getArmor());
            if (hasToughness) armorText += " (+" + targetInfo.getToughness() + ")";
            drawContext.drawString(textRenderer, armorText, barX + barW + 4, curY, 0xFFFFFF, true);
            curY += lineH;
        }
        
        if (toolW > 0) {
            int tx = x + totalW - padding - iconS;
            int ty = y + (totalH - iconS) / 2;
            if (showBreaking) ty -= 2;
            drawContext.renderItem(toolToRender, tx, ty);
            String hIcon = targetInfo.canHarvest() ? "✔" : "✘";
            int hCol = targetInfo.canHarvest() ? 0x55FF55 : 0xFF5555;
            drawContext.drawString(textRenderer, hIcon, tx + 12, ty + 10, hCol, true);
        }
        
        if (showBreaking) {
            int bW = totalW - padding * 2, bH = 2, bY = y + totalH - padding - bH;
            float prog = Math.max(0, Math.min(1, targetInfo.getBreakingProgress()));
            drawContext.fill(x + padding - 1, bY - 1, x + padding + bW + 1, bY + bH + 1, 0xFF000000);
            drawContext.fill(x + padding, bY, x + padding + bW, bY + bH, 0xFF444400);
            drawContext.fill(x + padding, bY, x + padding + (int)(bW * prog), bY + bH, 0xFFFFFF55);
        }
    }

    private static class StatInfo {
        final String icon;
        final String text;
        final int color;

        StatInfo(String icon, String text, int color) {
            this.icon = icon;
            this.text = text;
            this.color = color;
        }
    }

    private void renderTooltipBackground(GuiGraphics context, int x, int y, int width, int height) {
        ModConfig config = ModConfig.get();
        int alpha = (int) (config.hudTransparency * 2.55f);
        int bgColor = (alpha << 24) | 0x101010;
        int borderColor = (Math.min(alpha + 40, 255) << 24) | 0xFFFFFF;
        context.fill(x + 1, y, x + width - 1, y + height, bgColor);
        context.fill(x, y + 1, x + width, y + height - 1, bgColor);
        context.fill(x + 1, y, x + width - 1, y + 1, borderColor);
        context.fill(x + 1, y + height - 1, x + width - 1, y + height, (alpha / 4 << 24) | 0xFFFFFF);
        context.fillGradient(x, y + 1, x + 1, y + height - 1, borderColor, (alpha / 4 << 24) | 0xFFFFFF);
        context.fillGradient(x + width - 1, y + 1, x + width, y + height - 1, borderColor, (alpha / 4 << 24) | 0xFFFFFF);
    }
}
