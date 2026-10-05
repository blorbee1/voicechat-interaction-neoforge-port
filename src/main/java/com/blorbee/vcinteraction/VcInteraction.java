package com.blorbee.vcinteraction;

import net.minecraft.resources.Identifier;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;

@Mod(VcInteraction.MOD_ID)
public class VcInteraction {
    public static final String MOD_ID = "vcinteraction";
    public static final Logger LOGGER = LogUtils.getLogger();

    public VcInteraction(IEventBus modEventBus, ModContainer modContainer) {

        modContainer.registerConfig(ModConfig.Type.LOCAL, Config.SPEC);

        LOGGER.info("VcInteraction started");
    }

    public static Identifier loc(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
