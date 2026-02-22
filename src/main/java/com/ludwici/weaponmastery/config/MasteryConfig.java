package com.ludwici.weaponmastery.config;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
            .append(new KeyedCodec<>("IgnoredWeapons", Codec.STRING_ARRAY),
                    (masteryConfig, value, extraInfo) -> masteryConfig.ignoredWeapons = Arrays.asList(value),
                    (masteryConfig, extraInfo) -> masteryConfig.ignoredWeapons.toArray(String[]::new))
            .add()
            .append(new KeyedCodec<>("IgnoredRoles", Codec.STRING_ARRAY),
                    (masteryConfig, value, extraInfo) -> masteryConfig.ignoredRoles = Arrays.asList(value),
                    (masteryConfig, extraInfo) -> masteryConfig.ignoredRoles.toArray(String[]::new))
            .add()
            .build();

    private boolean showImproveMasteryNotification = true;
    private int masteryRate = 1;
    private float masteryChance = 1;
    private String masteryMode = "KILL";
    private List<String> ignoredWeapons = new ArrayList<>();
    private List<String> ignoredRoles = new ArrayList<>();

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

    public List<String> getIgnoredWeapons() {
        return ignoredWeapons;
    }

    public List<String> getIgnoredRoles() {
        return ignoredRoles;
    }
}
