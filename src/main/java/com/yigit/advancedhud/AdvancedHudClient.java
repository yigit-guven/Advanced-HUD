package com.yigit.advancedhud;

import com.yigit.advancedhud.render.HudRenderer;
import com.yigit.advancedhud.config.ConfigScreen;
import com.yigit.advancedhud.config.ModConfig;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.Util;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(AdvancedHudClient.MOD_ID)
public class AdvancedHudClient {
    public static final String MOD_ID = "advanced_hud";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public AdvancedHudClient(IEventBus modEventBus) {
        LOGGER.info("Advanced HUD Initializing...");
        
        // Register client setup event
        modEventBus.addListener(this::onClientSetup);
        
        // Force config load
        ModConfig.get();

        // Register config screen factory for the "Config" button in mod list
        ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> (client, parent) -> {
            if (ModList.get().isLoaded("cloth_config")) {
                return ConfigScreen.create(parent);
            } else {
                Util.getPlatform().openFile(ModConfig.getConfigFile());
                return parent;
            }
        });
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        LOGGER.info("Advanced HUD Client Setup");
        // NeoForge uses its own event bus for HUD rendering
        NeoForge.EVENT_BUS.register(new HudRenderer());
    }
}

