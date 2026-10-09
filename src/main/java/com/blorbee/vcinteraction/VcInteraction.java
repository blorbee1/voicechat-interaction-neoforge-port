package com.blorbee.vcinteraction;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.datamaps.DataMapsUpdatedEvent;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.neoforged.neoforge.registries.datamaps.builtin.VibrationFrequency;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;

import java.util.Map;

@Mod(VcInteraction.MOD_ID)
public class VcInteraction {
    public static final String MOD_ID = "vcinteraction";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<GameEvent> EVENTS = DeferredRegister.create(Registries.GAME_EVENT, MOD_ID);

    public static Holder.Reference<GameEvent> VOICE_GAME_EVENT = EVENTS.register("voice", () -> new GameEvent(16));

    public VcInteraction(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.LOCAL, ServerConfig.SPEC);

        EVENTS.register(modEventBus);
        NeoForge.EVENT_BUS.register(this);

        LOGGER.info("VcInteraction started");
    }
}
