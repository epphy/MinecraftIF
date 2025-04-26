package com.mif.mif.core;

import com.mif.mif.core.feature.addon.Addon;
import com.mif.mif.core.feature.addon.AddonManager;
import com.mif.mif.core.feature.addon.AddonType;
import com.mif.mif.core.feature.fix.Fix;
import com.mif.mif.core.feature.fix.FixManager;
import com.mif.mif.core.feature.fix.FixType;
import com.mif.mif.util.MIFLogger;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FeatureManager {
    private static FeatureManager instance;
    private AddonManager addonManager;
    private FixManager fixManager;

    public static FeatureManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Not initialized yet");
        }
        return instance;
    }

    public static void init() {
        if (instance == null) {
            instance = new FeatureManager();
        }

        instance.addonManager = new AddonManager();
        instance.fixManager = new FixManager();

        // Here we register all features using the config
        MIFLogger.info(instance, "initialized");
    }

    public void refresh() {
        // Here we refresh all features using the updated config
    }

    public void unload() {
        instance = null;
    }

    public boolean addFix(@NotNull FixType fixType, @NotNull Fix fix) {
        return fixManager.addFix(fixType, fix);
    }

    public boolean addAddon(@NotNull AddonType addonType, @NotNull Addon addon) {
        return addonManager.addAddon(addonType, addon);
    }

    public boolean removeFix(@NotNull FixType fixType) {
        return fixManager.removeFix(fixType);
    }

    public boolean removeAddon(@NotNull AddonType addonType) {
        return addonManager.removeAddon(addonType);
    }

    public boolean isFixEnabled(@NotNull FixType fixType) {
        return fixManager.hasFix(fixType);
    }

    public boolean isAddonEnabled(@NotNull AddonType addonType) {
        return addonManager.hasAddon(addonType);
    }
}
