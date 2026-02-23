package com.yigit.advancedhud.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yigit.advancedhud.AdvancedHudClient;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ModConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "advanced-hud.json");

    // Config options
    public boolean enabled = true;
    public int xOffset = 0;
    public int yOffset = 10;
    public boolean showEntityHealth = true;
    public boolean showBlockId = true;
    public boolean showModName = true;
    public boolean showEffectiveTool = true;
    public boolean showHarvestLevel = true;
    public boolean showCropGrowth = true;
    public boolean showEntityArmor = true;
    public boolean showEntityOwner = true;
    public boolean showEntityId = false;
    public boolean showWaterlogged = true;
    public boolean showBreakingProgress = true;
    public boolean showHorseStats = true;
    public boolean showVillagerInfo = true;
    public boolean showBeeCount = true;
    public boolean showContainerInfo = true;
    public boolean showEntityModel = true;

    private static ModConfig INSTANCE;

    public static ModConfig get() {
        if (INSTANCE == null) {
            INSTANCE = load();
        }
        return INSTANCE;
    }

    private static ModConfig load() {
        if (!CONFIG_FILE.exists()) {
            ModConfig config = new ModConfig();
            config.save();
            return config;
        }

        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            ModConfig config = GSON.fromJson(reader, ModConfig.class);
            if (config == null) config = new ModConfig();
            config.save(); // Ensure any new fields are written back
            return config;
        } catch (IOException e) {
            AdvancedHudClient.LOGGER.error("Failed to load config", e);
            return new ModConfig();
        }
    }

    public void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            AdvancedHudClient.LOGGER.error("Failed to save config", e);
        }
    }
}
