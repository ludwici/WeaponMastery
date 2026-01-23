package com.ludwici.weaponmastery.config;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public class MasteryConfig {

    public static final BuilderCodec<MasteryConfig> CODEC = BuilderCodec.<MasteryConfig>builder(MasteryConfig.class, MasteryConfig::new)
            .append(new KeyedCodec<>("ShowImproveMasteryNotification", Codec.BOOLEAN),
                    (masteryConfig, aBoolean, extraInfo) -> masteryConfig.showImproveMasteryNotification = aBoolean,
                    (masteryConfig, extraInfo) -> masteryConfig.showImproveMasteryNotification)
            .add()
            .append(new KeyedCodec<>("MasteryRate", Codec.INTEGER),
                    (masteryConfig, integer, extraInfo) -> masteryConfig.masteryRate = integer,
                    (masteryConfig, extraInfo) -> masteryConfig.masteryRate)
            .add()
            .append(new KeyedCodec<>("MasteryChance", Codec.FLOAT),
                    (masteryConfig, aFloat, extraInfo) -> masteryConfig.masteryChance = aFloat,
                    (masteryConfig, extraInfo) -> masteryConfig.masteryChance)
            .add()
            .append(new KeyedCodec<>("MasteryMode", Codec.STRING),
                    (masteryConfig, aString, extraInfo) -> masteryConfig.masteryMode = aString,
                    (masteryConfig, extraInfo) -> masteryConfig.masteryMode)
            .add()
            .build();

    private boolean showImproveMasteryNotification = true;
    private int masteryRate = 1;
    private float masteryChance = 1;
    private String masteryMode = "KILL";

    public MasteryConfig() {}

    public boolean isShowImproveMasteryNotification() {
        return showImproveMasteryNotification;
    }

    public int getMasteryRate() {
        return masteryRate;
    }

    public float getMasteryChance() {
        return masteryChance;
    }

    public String getMasteryMode() {
        return masteryMode;
    }
}
