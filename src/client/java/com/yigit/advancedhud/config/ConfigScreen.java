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
                .setTooltip(Text.literal("Horizontal offset of the HUD from the centre."))
                .setSaveConsumer(v -> config.xOffset = v)
                .build());

        general.addEntry(entry.startIntField(Text.literal("Y Offset"), config.yOffset)
                .setDefaultValue(10)
                .setTooltip(Text.literal("Vertical offset of the HUD from the top."))
                .setSaveConsumer(v -> config.yOffset = v)
                .build());

        general.addEntry(entry.startBooleanToggle(Text.literal("Show Mod Name"), config.showModName)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Shows the name of the mod that added the block/entity."))
                .setSaveConsumer(v -> config.showModName = v)
                .build());

        // ── Blocks ───────────────────────────────────────────────────────────
        ConfigCategory blocks = builder.getOrCreateCategory(Text.literal("Blocks"));

        blocks.addEntry(entry.startBooleanToggle(Text.literal("Show Block ID"), config.showBlockId)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Shows the internal ID of the targeted block (e.g. minecraft:stone)."))
                .setSaveConsumer(v -> config.showBlockId = v)
                .build());

        blocks.addEntry(entry.startBooleanToggle(Text.literal("Show Effective Tool"), config.showEffectiveTool)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Shows which tool type is most effective on the block."))
                .setSaveConsumer(v -> config.showEffectiveTool = v)
                .build());

        blocks.addEntry(entry.startBooleanToggle(Text.literal("Show Harvest Level"), config.showHarvestLevel)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Shows the required tool tier to harvest the block (Stone / Iron / Diamond)."))
                .setSaveConsumer(v -> config.showHarvestLevel = v)
                .build());

        blocks.addEntry(entry.startBooleanToggle(Text.literal("Show Crop Growth"), config.showCropGrowth)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Shows the growth percentage for crop blocks."))
                .setSaveConsumer(v -> config.showCropGrowth = v)
                .build());

        blocks.addEntry(entry.startBooleanToggle(Text.literal("Show Waterlogged"), config.showWaterlogged)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Shows a waterlogged indicator for blocks that support it."))
                .setSaveConsumer(v -> config.showWaterlogged = v)
                .build());

        blocks.addEntry(entry.startBooleanToggle(Text.literal("Show Breaking Progress"), config.showBreakingProgress)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Shows a progress bar while mining a block."))
                .setSaveConsumer(v -> config.showBreakingProgress = v)
                .build());

        blocks.addEntry(entry.startBooleanToggle(Text.literal("Show Bee Count"), config.showBeeCount)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Shows the number of bees inside a hive/nest."))
                .setSaveConsumer(v -> config.showBeeCount = v)
                .build());

        blocks.addEntry(entry.startBooleanToggle(Text.literal("Show Container Info"), config.showContainerInfo)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Shows item count for containers (requires server-side access)."))
                .setSaveConsumer(v -> config.showContainerInfo = v)
                .build());

        // ── Entities ──────────────────────────────────────────────────────────
        ConfigCategory entities = builder.getOrCreateCategory(Text.literal("Entities"));

        entities.addEntry(entry.startBooleanToggle(Text.literal("Show Entity Health"), config.showEntityHealth)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Shows a health bar and value for living entities."))
                .setSaveConsumer(v -> config.showEntityHealth = v)
                .build());

        entities.addEntry(entry.startBooleanToggle(Text.literal("Show Entity Armor"), config.showEntityArmor)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Shows an armor bar for entities that have armor."))
                .setSaveConsumer(v -> config.showEntityArmor = v)
                .build());

        entities.addEntry(entry.startBooleanToggle(Text.literal("Show Entity Model"), config.showEntityModel)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Renders a small 3-D model of the targeted entity inside the HUD."))
                .setSaveConsumer(v -> config.showEntityModel = v)
                .build());

        entities.addEntry(entry.startBooleanToggle(Text.literal("Show Entity ID"), config.showEntityId)
                .setDefaultValue(false)
                .setTooltip(Text.literal("Shows the internal entity type ID (e.g. minecraft:zombie)."))
                .setSaveConsumer(v -> config.showEntityId = v)
                .build());

        entities.addEntry(entry.startBooleanToggle(Text.literal("Show Entity Owner"), config.showEntityOwner)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Shows the owner name for tamed animals."))
                .setSaveConsumer(v -> config.showEntityOwner = v)
                .build());

        entities.addEntry(entry.startBooleanToggle(Text.literal("Show Horse Stats"), config.showHorseStats)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Shows speed and jump height for horses and similar mobs."))
                .setSaveConsumer(v -> config.showHorseStats = v)
                .build());

        entities.addEntry(entry.startBooleanToggle(Text.literal("Show Villager Info"), config.showVillagerInfo)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Shows profession and trade level for villagers."))
                .setSaveConsumer(v -> config.showVillagerInfo = v)
                .build());

        return builder.build();
    }
}
