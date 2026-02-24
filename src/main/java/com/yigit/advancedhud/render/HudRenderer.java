package com.yigit.advancedhud.render;

import com.yigit.advancedhud.config.ModConfig;
import com.yigit.advancedhud.util.TargetInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HudRenderer {
    private final TargetInfo targetInfo = new TargetInfo();
    
    private static final List<ItemStack> PICKAXES = Arrays.asList(
        new ItemStack(Items.NETHERITE_PICKAXE),
        new ItemStack(Items.DIAMOND_PICKAXE),
        new ItemStack(Items.IRON_PICKAXE),
        new ItemStack(Items.STONE_PICKAXE),
        new ItemStack(Items.GOLDEN_PICKAXE)
    );
    private static final List<ItemStack> AXES = Arrays.asList(
        new ItemStack(Items.NETHERITE_AXE),
        new ItemStack(Items.DIAMOND_AXE),
        new ItemStack(Items.IRON_AXE),
        new ItemStack(Items.STONE_AXE),
        new ItemStack(Items.GOLDEN_AXE)
    );
    private static final List<ItemStack> SHOVELS = Arrays.asList(
        new ItemStack(Items.NETHERITE_SHOVEL),
        new ItemStack(Items.DIAMOND_SHOVEL),
        new ItemStack(Items.IRON_SHOVEL),
        new ItemStack(Items.STONE_SHOVEL),
        new ItemStack(Items.GOLDEN_SHOVEL)
    );
    private static final List<ItemStack> HOES = Arrays.asList(
        new ItemStack(Items.NETHERITE_HOE),
        new ItemStack(Items.DIAMOND_HOE),
        new ItemStack(Items.IRON_HOE),
        new ItemStack(Items.STONE_HOE),
        new ItemStack(Items.GOLDEN_HOE)
    );
    private static final List<ItemStack> SHEARS = Arrays.asList(
        new ItemStack(Items.SHEARS)
    );

    @SubscribeEvent
    public void onRenderGuiLayer(RenderGuiLayerEvent.Post event) {
        // Only render on a specific layer, e.g., crosshair or after everything
        if (event.getName() != VanillaGuiLayers.CROSSHAIR) return;

        Minecraft client = Minecraft.getInstance();
        if (client == null || client.options.hideGui || !ModConfig.get().enabled) return;

        targetInfo.update(client);
        if (!targetInfo.hasTarget()) return;

        GuiGraphics guiGraphics = event.getGuiGraphics();
        Font font = client.font;
        int screenWidth = client.getWindow().getGuiScaledWidth();
        ModConfig config = ModConfig.get();
        
        // --- DATA COLLECTION ---
        List<StatInfo> stats = new ArrayList<>();
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
        
        ItemStack toolToRender = ItemStack.EMPTY;
        if (!targetInfo.isEntity() && config.showEffectiveTool && !targetInfo.getEffectiveTool().isEmpty()) {
            List<ItemStack> tools = Collections.emptyList();
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
        int wTitle = font.width(title);
        int wMod = modName.isEmpty() ? 0 : font.width(modName);
        
        int wStatsTotal = 0;
        for (StatInfo s : stats) wStatsTotal = Math.max(wStatsTotal, font.width(s.icon) + 12 + font.width(s.text));
        if (showHealth) {
            int wH = 11 + 60 + 4 + font.width(String.format("%.1f", targetInfo.getHealth()));
            wStatsTotal = Math.max(wStatsTotal, wH);
        }
        if (targetInfo.isEntity() && config.showEntityArmor && (targetInfo.getArmor() > 0 || targetInfo.getToughness() > 0)) {
            int wA = 11 + 60 + 4 + font.width(String.valueOf(targetInfo.getArmor()));
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

        renderTooltipBackground(guiGraphics, x, y, totalW, totalH);
        
        // --- DRAWING ---
        int curX = x + padding;
        int curY = y + padding;
        
        if (iconW > 0) {
            int iconY = y + (totalH - iconS) / 2;
            if (showBreaking) iconY -= 2; 
            guiGraphics.renderItem(targetInfo.getStack(), curX, iconY);
            curX += iconW;
        }

        if (drawEntityModel) {
            int modelX = curX + entityW / 2;
            int modelY = y + totalH - padding - 2;
            
            org.joml.Quaternionf q1 = new org.joml.Quaternionf().rotationXYZ(0.43633232f, 0.0f, (float)Math.PI);
            org.joml.Quaternionf q2 = new org.joml.Quaternionf().rotationXYZ(0.0f, -0.7853982f, 0.0f);
            
            InventoryScreen.renderEntityInInventory(guiGraphics, (float)modelX, (float)modelY, (float)entityScale, new org.joml.Vector3f(0, 0, 0), q1, q2, targetInfo.getTargetedEntity());
            curX += entityW + innerGap;
        }

        
        guiGraphics.drawString(font, title, curX, curY, 0xFFFFFF, true);
        if (wMod > 0) {
            guiGraphics.drawString(font, modName, x + totalW - padding - toolW - wMod, curY, 0x5555FF, true);
        }
        curY += titleH;
        
        for (StatInfo s : stats) {
            guiGraphics.drawString(font, s.icon, curX, curY, 0xFFFFFF, true);
            guiGraphics.drawString(font, s.text, curX + 12, curY, s.color, true);
            curY += lineH;
        }
        
        if (showHealth) {
            guiGraphics.drawString(font, "❤", curX, curY, 0xFF5555, true);
            int barX = curX + 11, barY = curY + 2, barW = 60, barH = 5;
            float pct = Math.max(0, Math.min(1, targetInfo.getHealth() / targetInfo.getMaxHealth()));
            
            guiGraphics.fill(barX - 1, barY - 1, barX + barW + 1, barY + barH + 1, 0x88000000);
            guiGraphics.fill(barX, barY, barX + barW, barY + barH, 0x44FF0000);
            int progressW = (int)(barW * pct);
            if (progressW > 0) {
                guiGraphics.fill(barX, barY, barX + progressW, barY + barH, 0xFFFF3333);
                guiGraphics.fill(barX, barY, barX + progressW, barY + 1, 0xFFFF7777);
            }
            String healthText = String.format("%.1f", targetInfo.getHealth());
            guiGraphics.drawString(font, healthText, barX + barW + 4, curY, 0xFFFFFF, true);
            curY += lineH;
        }

        if (targetInfo.isEntity() && config.showEntityArmor && (targetInfo.getArmor() > 0 || targetInfo.getToughness() > 0)) {
            boolean hasToughness = targetInfo.getToughness() > 0;
            guiGraphics.drawString(font, hasToughness ? "🛡+" : "🛡", curX, curY, hasToughness ? 0x55FFFF : 0xAAAAAA, true);
            int barX = curX + 11, barY = curY + 2, barW = 60, barH = 5;
            float pct = Math.min(1, targetInfo.getArmor() / 20.0f);
            
            guiGraphics.fill(barX - 1, barY - 1, barX + barW + 1, barY + barH + 1, 0x88000000);
            guiGraphics.fill(barX, barY, barX + barW, barY + barH, 0x44555555);
            int progressW = (int)(barW * pct);
            if (progressW > 0) {
                guiGraphics.fill(barX, barY, barX + progressW, barY + barH, hasToughness ? 0xFF55FFFF : 0xFFAAAAAA);
                guiGraphics.fill(barX, barY, barX + progressW, barY + 1, hasToughness ? 0xFFAFFFFF : 0xFFDDDDDD);
            }
            String armorText = String.valueOf(targetInfo.getArmor());
            if (hasToughness) armorText += " (+" + targetInfo.getToughness() + ")";
            guiGraphics.drawString(font, armorText, barX + barW + 4, curY, 0xFFFFFF, true);
            curY += lineH;
        }
        
        if (toolW > 0) {
            int tx = x + totalW - padding - iconS;
            int ty = y + (totalH - iconS) / 2;
            if (showBreaking) ty -= 2;
            guiGraphics.renderItem(toolToRender, tx, ty);
            String hIcon = targetInfo.canHarvest() ? "✔" : "✘";
            int hCol = targetInfo.canHarvest() ? 0x55FF55 : 0xFF5555;
            guiGraphics.drawString(font, hIcon, tx + 12, ty + 10, hCol, true);
        }
        
        if (showBreaking) {
            int bW = totalW - padding * 2, bH = 2, bY = y + totalH - padding - bH;
            float prog = Math.max(0, Math.min(1, targetInfo.getBreakingProgress()));
            guiGraphics.fill(x + padding - 1, bY - 1, x + padding + bW + 1, bY + bH + 1, 0xFF000000);
            guiGraphics.fill(x + padding, bY, x + padding + bW, bY + bH, 0xFF444400);
            guiGraphics.fill(x + padding, bY, x + padding + (int)(bW * prog), bY + bH, 0xFFFFFF55);
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

    private void renderTooltipBackground(GuiGraphics guiGraphics, int x, int y, int width, int height) {
        ModConfig config = ModConfig.get();
        int alpha = (int) (config.hudTransparency * 2.55f);
        
        int bgColor = (alpha << 24) | 0x101010;
        int borderColor = (Math.min(alpha + 40, 255) << 24) | 0xFFFFFF;
        
        guiGraphics.fill(x + 1, y, x + width - 1, y + height, bgColor);
        guiGraphics.fill(x, y + 1, x + width, y + height - 1, bgColor);
        
        guiGraphics.fill(x + 1, y, x + width - 1, y + 1, borderColor);
        guiGraphics.fill(x + 1, y + height - 1, x + width - 1, y + height, (alpha / 4 << 24) | 0xFFFFFF);
        guiGraphics.fillGradient(x, y + 1, x + 1, y + height - 1, borderColor, (alpha / 4 << 24) | 0xFFFFFF);
        guiGraphics.fillGradient(x + width - 1, y + 1, x + width, y + height - 1, borderColor, (alpha / 4 << 24) | 0xFFFFFF);
    }
}
