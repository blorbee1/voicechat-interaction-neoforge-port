package com.blorbee.vcinteraction;

import de.maxhenkel.voicechat.api.*;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.MicrophonePacketEvent;
import de.maxhenkel.voicechat.api.events.VoicechatServerStartedEvent;
import de.maxhenkel.voicechat.api.opus.OpusDecoder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@ForgeVoicechatPlugin
public class Plugin implements VoicechatPlugin {
    public static VoicechatApi voicechatApi;
    private static ConcurrentHashMap<UUID, Long> cooldowns;

    @Nullable
    public static VoicechatServerApi voicechatServerApi;

    @Nullable
    private OpusDecoder decoder;

    @Override
    public String getPluginId() {
        return VcInteraction.MOD_ID;
    }

    @Override
    public void initialize(VoicechatApi api) {
        voicechatApi = api;
        cooldowns = new ConcurrentHashMap<>();
    }

    @Override
    public void registerEvents(EventRegistration registration) {
        registration.registerEvent(VoicechatServerStartedEvent.class, this::onServerStarted);
        registration.registerEvent(MicrophonePacketEvent.class, this::onMicPacket);
    }

    private void onServerStarted(VoicechatServerStartedEvent event) {
        voicechatServerApi = event.getVoicechat();
    }

    private void onMicPacket(MicrophonePacketEvent event) {
        VoicechatConnection senderConnection = event.getSenderConnection();
        if (senderConnection == null)
            return;

        if (event.getPacket().getOpusEncodedData().length <= 0) {
            // dont trigger any events when stopping to talk
            return;
        }

        if (!ServerConfig.GROUP_INTERACTION.get()) {
            if (senderConnection.isInGroup())
                return;
        }

        if (!ServerConfig.WHISPER_INTERACTION.get()) {
            if (event.getPacket().isWhispering())
                return;
        }

        if (!(senderConnection.getPlayer().getPlayer() instanceof ServerPlayer player)) {
            VcInteraction.LOGGER.warn("Received microphone packets from non-player");
            return;
        }

        if (!ServerConfig.SNEAK_INTERACTION.get()) {
            if (player.isCrouching())
                return;
        }

        if (decoder == null) {
            decoder = event.getVoicechat().createDecoder();
        }

        decoder.resetState();
        short[] decoded = decoder.decode(event.getPacket().getOpusEncodedData());

        if (AudioUtils.calculateAudioLevel(decoded) < ServerConfig.MIN_ACTIVATION_THRESHOLD.get().doubleValue()) {
            return;
        }

        player.level().getServer().execute(() -> {
            if (activate(player)) {
                player.gameEvent(BuiltInRegistries.GAME_EVENT.wrapAsHolder(VcInteraction.VOICE_GAME_EVENT.get()));
            }
        });
    }

    private boolean activate(ServerPlayer player) {
        Long lastTimestamp = cooldowns.get(player.getUUID());
        long currentTime = player.level().getGameTime();
        if (lastTimestamp == null || currentTime - lastTimestamp > 20L) {
            cooldowns.put(player.getUUID(), currentTime);
            return true;
        }
        return false;
    }
}
