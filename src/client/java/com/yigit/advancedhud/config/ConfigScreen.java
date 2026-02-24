package com.yigit.advancedhud.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ConfigScreen {

    public static Screen create(Screen parent) {
        ModConfig config = ModConfig.get();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.literal("Advanced HUD Configuration"))
                .setSavingRunnable(config::save);

        ConfigEntryBuilder entry = builder.entryBuilder();

        // ── General ──────────────────────────────────────────────────────────
        ConfigCategory general = builder.getOrCreateCategory(Text.literal("General"));

        general.addEntry(entry.startBooleanToggle(Text.literal("Enable HUD"), config.enabled)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Master toggle for the entire HUD."))
                .setSaveConsumer(v -> config.enabled = v)
                .build());

        general.addEntry(entry.startIntField(Text.literal("X Offset"), config.xOffset)
                .setDefaultValue(0)
                .setTooltip(Text.literal("Horizontal offset from the center."))
                .setSaveConsumer(v -> config.xOffset = v)
                .build());

        general.addEntry(entry.startIntField(Text.literal("Y Offset"), config.yOffset)
                .setDefaultValue(10)
                .setTooltip(Text.literal("Vertical offset from the top."))
                .setSaveConsumer(v -> config.yOffset = v)
                .build());

        general.addEntry(entry.startIntSlider(Text.literal("HUD Transparency"), config.hudTransparency, 0, 100)
                .setDefaultValue(80)
                .setTooltip(Text.literal("Adjust the background transparency of the HUD (0-100)."))
                .setSaveConsumer(v -> config.hudTransparency = v)
                .build());

        general.addEntry(entry.startBooleanToggle(Text.literal("Show Mod Name"), config.showModName)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Shows the mod that added the block/entity."))
                .setSaveConsumer(v -> config.showModName = v)
                .build());

        // ── Blocks ───────────────────────────────────────────────────────────
        ConfigCategory blocks = builder.getOrCreateCategory(Text.literal("Blocks"));

        blocks.addEntry(entry.startBooleanToggle(Text.literal("Show Block ID"), config.showBlockId)
                .setDefaultValue(false)
                .setTooltip(Text.literal("Shows the internal ID of the block (e.g. minecraft:stone)."))
                .setSaveConsumer(v -> config.showBlockId = v)
                .build());

        blocks.addEntry(entry.startBooleanToggle(Text.literal("Show Effective Tool"), config.showEffectiveTool)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Shows the most effective tool type."))
                .setSaveConsumer(v -> config.showEffectiveTool = v)
                .build());

        blocks.addEntry(entry.startBooleanToggle(Text.literal("Show Harvest Level"), config.showHarvestLevel)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Shows the required tool tier (Stone / Iron / Diamond)."))
                .setSaveConsumer(v -> config.showHarvestLevel = v)
                .build());

        blocks.addEntry(entry.startBooleanToggle(Text.literal("Show Crop Growth"), config.showCropGrowth)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Shows the growth percentage for crops."))
                .setSaveConsumer(v -> config.showCropGrowth = v)
                .build());

        blocks.addEntry(entry.startBooleanToggle(Text.literal("Show Waterlogged"), config.showWaterlogged)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Shows a 💧 indicator if the block is waterlogged."))
                .setSaveConsumer(v -> config.showWaterlogged = v)
                .build());

        blocks.addEntry(entry.startBooleanToggle(Text.literal("Show Breaking Progress"), config.showBreakingProgress)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Shows progress bar while mining."))
                .setSaveConsumer(v -> config.showBreakingProgress = v)
                .build());

        blocks.addEntry(entry.startBooleanToggle(Text.literal("Show Bee Count"), config.showBeeCount)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Shows bee count inside hives/nests."))
                .setSaveConsumer(v -> config.showBeeCount = v)
                .build());

        blocks.addEntry(entry.startBooleanToggle(Text.literal("Show Container Info"), config.showContainerInfo)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Shows item counts for synced containers (e.g. Furnaces, Brewing Stands)."))
                .setSaveConsumer(v -> config.showContainerInfo = v)
                .build());

        // ── Entities ──────────────────────────────────────────────────────────
        ConfigCategory entities = builder.getOrCreateCategory(Text.literal("Entities"));

        entities.addEntry(entry.startBooleanToggle(Text.literal("Show Entity Health"), config.showEntityHealth)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Shows health bar and numerical value."))
                .setSaveConsumer(v -> config.showEntityHealth = v)
                .build());

        entities.addEntry(entry.startBooleanToggle(Text.literal("Show Entity Armor"), config.showEntityArmor)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Shows armor bar based on protective gear."))
                .setSaveConsumer(v -> config.showEntityArmor = v)
                .build());

        entities.addEntry(entry.startBooleanToggle(Text.literal("Show Entity Model"), config.showEntityModel)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Renders a small 3-D model of the entity."))
                .setSaveConsumer(v -> config.showEntityModel = v)
                .build());

        entities.addEntry(entry.startBooleanToggle(Text.literal("Show Entity ID"), config.showEntityId)
                .setDefaultValue(false)
                .setTooltip(Text.literal("Shows the internal entity ID (e.g. minecraft:zombie)."))
                .setSaveConsumer(v -> config.showEntityId = v)
                .build());

        entities.addEntry(entry.startBooleanToggle(Text.literal("Show Entity Owner"), config.showEntityOwner)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Shows the owner name for tamed entities."))
                .setSaveConsumer(v -> config.showEntityOwner = v)
                .build());

        entities.addEntry(entry.startBooleanToggle(Text.literal("Show Horse Stats"), config.showHorseStats)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Shows speed (m/s) and jump height for horses."))
                .setSaveConsumer(v -> config.showHorseStats = v)
                .build());

        entities.addEntry(entry.startBooleanToggle(Text.literal("Show Villager Info"), config.showVillagerInfo)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Shows profession and trading level."))
                .setSaveConsumer(v -> config.showVillagerInfo = v)
                .build());

        return builder.build();
    }
}
