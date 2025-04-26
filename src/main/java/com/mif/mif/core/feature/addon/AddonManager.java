package com.mif.mif.core.feature.addon;

import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;

public final class AddonManager {
    private final EnumMap<AddonType, Addon> addons = new EnumMap<>(AddonType.class);

    public boolean addAddon(@NotNull AddonType addonType, @NotNull Addon addon) {
        final boolean absent = !hasAddon(addonType);
        if (absent) addons.put(addonType, addon);
        return absent;
    }

    public boolean removeAddon(@NotNull AddonType addonType) {
        final boolean present = hasAddon(addonType);
        if (present) addons.remove(addonType);
        return present;
    }

    public boolean hasAddon(@NotNull AddonType addonType) {
        return addons.containsKey(addonType);
    }

}
