package com.blorbee.vcinteraction;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ServerConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue GROUP_INTERACTION = BUILDER
        .comment("If talking in groups should trigger vibrations")
        .define("group_interaction", false);

    public static final ModConfigSpec.BooleanValue WHISPER_INTERACTION = BUILDER
        .comment("If whispering should trigger vibrations")
        .define("whisper_interaction", false);

    public static final ModConfigSpec.BooleanValue SNEAK_INTERACTION = BUILDER
        .comment("If talking while sneaking should trigger vibrations")
        .define("sneak_interaction", false);

    public static final ModConfigSpec.IntValue VOICE_SCULK_FREQUENCY = BUILDER
        .comment("The frequency of the voice vibration")
        .defineInRange("voice_sculk_frequency", 7, 1, 15);

    public static final ModConfigSpec.IntValue MIN_ACTIVATION_THRESHOLD = BUILDER
        .comment("The audio level threshold to activate the skulk sensor in dB")
        .defineInRange("min_activation_threshold", -50, -127, 0);

    static final ModConfigSpec SPEC = BUILDER.build();
}
