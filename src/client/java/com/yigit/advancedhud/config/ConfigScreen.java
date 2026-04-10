package com.yigit.advancedhud.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ConfigScreen {

    public static Screen create(Screen parent) {
        ModConfig config = ModConfig.get();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.literal("Advanced HUD Configuration"))
                .setSavingRunnable(config::save);

        ConfigEntryBuilder entry = builder.entryBuilder();

        // ── General ──────────────────────────────────────────────────────────
        ConfigCategory general = builder.getOrCreateCategory(Component.literal("General"));

        general.addEntry(entry.startBooleanToggle(Component.literal("Enable HUD"), config.enabled)
                .setDefaultValue(true)
                .setTooltip(Component.literal("Master toggle for the entire HUD."))
                .setSaveConsumer(v -> config.enabled = v)
                .build());

        general.addEntry(entry.startIntField(Component.literal("X Offset"), config.xOffset)
                .setDefaultValue(0)
                .setTooltip(Component.literal("Horizontal offset from the center."))
                .setSaveConsumer(v -> config.xOffset = v)
                .build());

        general.addEntry(entry.startIntField(Component.literal("Y Offset"), config.yOffset)
                .setDefaultValue(10)
                .setTooltip(Component.literal("Vertical offset from the top."))
                .setSaveConsumer(v -> config.yOffset = v)
                .build());

        general.addEntry(entry.startIntSlider(Component.literal("HUD Transparency"), config.hudTransparency, 0, 100)
                .setDefaultValue(80)
                .setTooltip(Component.literal("Adjust the background transparency of the HUD (0-100)."))
                .setSaveConsumer(v -> config.hudTransparency = v)
                .build());

        general.addEntry(entry.startBooleanToggle(Component.literal("Show Mod Name"), config.showModName)
                .setDefaultValue(true)
                .setTooltip(Component.literal("Shows the mod that added the block/entity."))
                .setSaveConsumer(v -> config.showModName = v)
                .build());

        // ── Blocks ───────────────────────────────────────────────────────────
        ConfigCategory blocks = builder.getOrCreateCategory(Component.literal("Blocks"));

        blocks.addEntry(entry.startBooleanToggle(Component.literal("Show Block ID"), config.showBlockId)
                .setDefaultValue(false)
                .setTooltip(Component.literal("Shows the internal ID of the block (e.g. minecraft:stone)."))
                .setSaveConsumer(v -> config.showBlockId = v)
                .build());

        blocks.addEntry(entry.startBooleanToggle(Component.literal("Show Effective Tool"), config.showEffectiveTool)
                .setDefaultValue(true)
                .setTooltip(Component.literal("Shows the most effective tool type."))
                .setSaveConsumer(v -> config.showEffectiveTool = v)
                .build());

        blocks.addEntry(entry.startBooleanToggle(Component.literal("Show Harvest Level"), config.showHarvestLevel)
                .setDefaultValue(true)
                .setTooltip(Component.literal("Shows the required tool tier (Stone / Iron / Diamond)."))
                .setSaveConsumer(v -> config.showHarvestLevel = v)
                .build());

        blocks.addEntry(entry.startBooleanToggle(Component.literal("Show Crop Growth"), config.showCropGrowth)
                .setDefaultValue(true)
                .setTooltip(Component.literal("Shows the growth percentage for crops."))
                .setSaveConsumer(v -> config.showCropGrowth = v)
                .build());

        blocks.addEntry(entry.startBooleanToggle(Component.literal("Show Waterlogged"), config.showWaterlogged)
                .setDefaultValue(true)
                .setTooltip(Component.literal("Shows a 💧 indicator if the block is waterlogged."))
                .setSaveConsumer(v -> config.showWaterlogged = v)
                .build());

        blocks.addEntry(entry.startBooleanToggle(Component.literal("Show Breaking Progress"), config.showBreakingProgress)
                .setDefaultValue(true)
                .setTooltip(Component.literal("Shows progress bar while mining."))
                .setSaveConsumer(v -> config.showBreakingProgress = v)
                .build());

        blocks.addEntry(entry.startBooleanToggle(Component.literal("Show Bee Count"), config.showBeeCount)
                .setDefaultValue(true)
                .setTooltip(Component.literal("Shows bee count inside hives/nests."))
                .setSaveConsumer(v -> config.showBeeCount = v)
                .build());

        blocks.addEntry(entry.startBooleanToggle(Component.literal("Show Container Info"), config.showContainerInfo)
                .setDefaultValue(true)
                .setTooltip(Component.literal("Shows item counts for synced containers (e.g. Furnaces, Brewing Stands)."))
                .setSaveConsumer(v -> config.showContainerInfo = v)
                .build());

        // ── Entities ──────────────────────────────────────────────────────────
        ConfigCategory entities = builder.getOrCreateCategory(Component.literal("Entities"));

        entities.addEntry(entry.startBooleanToggle(Component.literal("Show Entity Health"), config.showEntityHealth)
                .setDefaultValue(true)
                .setTooltip(Component.literal("Shows health bar and numerical value."))
                .setSaveConsumer(v -> config.showEntityHealth = v)
                .build());

        entities.addEntry(entry.startBooleanToggle(Component.literal("Show Entity Armor"), config.showEntityArmor)
                .setDefaultValue(true)
                .setTooltip(Component.literal("Shows armor bar based on protective gear."))
                .setSaveConsumer(v -> config.showEntityArmor = v)
                .build());

        entities.addEntry(entry.startBooleanToggle(Component.literal("Show Entity Model"), config.showEntityModel)
                .setDefaultValue(true)
                .setTooltip(Component.literal("Renders a small 3-D model of the entity."))
                .setSaveConsumer(v -> config.showEntityModel = v)
                .build());

        entities.addEntry(entry.startBooleanToggle(Component.literal("Show Entity ID"), config.showEntityId)
                .setDefaultValue(false)
                .setTooltip(Component.literal("Shows the internal entity ID (e.g. minecraft:zombie)."))
                .setSaveConsumer(v -> config.showEntityId = v)
                .build());

        entities.addEntry(entry.startBooleanToggle(Component.literal("Show Entity Owner"), config.showEntityOwner)
                .setDefaultValue(true)
                .setTooltip(Component.literal("Shows the owner name for tamed entities."))
                .setSaveConsumer(v -> config.showEntityOwner = v)
                .build());

        entities.addEntry(entry.startBooleanToggle(Component.literal("Show Horse Stats"), config.showHorseStats)
                .setDefaultValue(true)
                .setTooltip(Component.literal("Shows speed (m/s) and jump height for horses."))
                .setSaveConsumer(v -> config.showHorseStats = v)
                .build());

        entities.addEntry(entry.startBooleanToggle(Component.literal("Show Villager Info"), config.showVillagerInfo)
                .setDefaultValue(true)
                .setTooltip(Component.literal("Shows profession and trading level."))
                .setSaveConsumer(v -> config.showVillagerInfo = v)
                .build());

        return builder.build();
    }
}
