package com.yigit.advancedhud;

import com.mojang.blaze3d.platform.InputConstants;
import com.yigit.advancedhud.config.ConfigScreen;
import com.yigit.advancedhud.render.HudRenderer;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.AlertScreen;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(AdvancedHudClient.MOD_ID)
public class AdvancedHudClient {
    public static final String MOD_ID = "advanced_hud";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final KeyMapping CONFIG_KEY = new KeyMapping(
            "key.advanced_hud.config",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_H,
            "category.advanced_hud"
    );

    public AdvancedHudClient() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::doClientStuff);
        modEventBus.addListener(this::registerKeys);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        LOGGER.info("Advanced HUD Initialized ");
        com.yigit.advancedhud.config.ModConfig.get();
        MinecraftForge.EVENT_BUS.register(new HudRenderer());

        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory((mc, parent) -> {
                    if (ModList.get().isLoaded("cloth_config")) {
                        return com.yigit.advancedhud.config.ConfigScreen.create(parent);
                    } else {
                        return new AlertScreen(
                                () -> mc.setScreen(parent),
                                Component.literal("Cloth Config Missing"),
                                Component.literal("The in-game configuration menu requires the 'Cloth Config' mod. " +
                                        "Please install it or edit 'config/advanced_hud.json' manually.")
                        );
                    }
                }));
    }

    private void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(CONFIG_KEY);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.Key event) {
        if (CONFIG_KEY.consumeClick()) {
            Minecraft.getInstance().setScreen(ConfigScreen.create(null));
        }
    }
}
