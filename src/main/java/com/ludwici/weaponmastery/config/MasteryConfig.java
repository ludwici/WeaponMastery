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
            .build();

    private boolean showImproveMasteryNotification = true;
    private int masteryRate = 1;

    public MasteryConfig() {}

    public boolean isShowImproveMasteryNotification() {
        return showImproveMasteryNotification;
    }

    public int getMasteryRate() {
        return masteryRate;
    }
}
