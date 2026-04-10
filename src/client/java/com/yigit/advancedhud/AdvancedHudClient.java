package com.yigit.advancedhud;

import com.yigit.advancedhud.render.HudRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdvancedHudClient implements ClientModInitializer {
	public static final String MOD_ID = "advanced-hud";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
		LOGGER.info("Advanced HUD Initialized ");
		// Force config load and save to ensure it's up to date
		com.yigit.advancedhud.config.ModConfig.get();
		
		HudElementRegistry.addLast(
            Identifier.fromNamespaceAndPath(MOD_ID, "hud_renderer"), 
            new HudRenderer()
        );
	}
}
