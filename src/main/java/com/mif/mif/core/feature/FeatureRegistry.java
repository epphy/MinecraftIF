package com.mif.mif.core.feature;

import com.mif.mif.config.MIFConfig;
import com.mif.mif.core.feature.addon.Addon;
import com.mif.mif.core.feature.addon.AddonType;
import com.mif.mif.core.feature.fix.Fix;
import com.mif.mif.core.feature.fix.FixType;
import com.mif.mif.util.MIFLogger;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FeatureRegistry {
    private static final EnumMap<FixType, FeatureFactory<Fix>> FIX_REGISTRY = new EnumMap<>(FixType.class);
    private static final EnumMap<AddonType, FeatureFactory<Addon>> ADDON_REGISTRY = new EnumMap<>(AddonType.class);
    private static FeatureRegistry instance;

    public static FeatureRegistry getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Not initialized yet");
        }
        return instance;
    }

    public static void init() {
        if (instance == null) {
            instance = new FeatureRegistry();
        }

        instance.registerAllFeatures();
        MIFLogger.info(instance, "initialized");
    }

    public void unload() {
        instance = null;
    }

    private void registerAllFeatures() {
        // Fixes


        // Addons
    }

    // Suppressing warning due to type erasure.
    // We are sure to accept Fixes here only.
    @SuppressWarnings("unchecked")
    public <T extends Fix> boolean registerFix(@NotNull FixType fixType, @NotNull FeatureFactory<T> featureFactory) {
        final boolean absent = !hasRegistered(FIX_REGISTRY, fixType);
        if (absent) FIX_REGISTRY.put(fixType, (FeatureFactory<Fix>) featureFactory);
        return absent;
    }

    public boolean unregisterFix(@NotNull FixType fixType) {
        final boolean present = hasRegistered(FIX_REGISTRY, fixType);
        if (present) FIX_REGISTRY.remove(fixType);
        return present;
    }

    public Fix getFix(@NotNull FixType fixType, @NotNull FeatureContext featureContext) {
        return FIX_REGISTRY.get(fixType).create(featureContext);
    }

    // Suppressing warning due to type erasure.
    // We are sure to accept Addons here only.
    @SuppressWarnings("unchecked")
    public <T extends Addon> boolean registerAddon(@NotNull AddonType addonType, @NotNull FeatureFactory<T> featureFactory) {
        final boolean absent = !hasRegistered(ADDON_REGISTRY, addonType);
        if (absent) ADDON_REGISTRY.put(addonType, (FeatureFactory<Addon>) featureFactory);
        return absent;
    }

    public boolean unregisterAddon(@NotNull AddonType addonType) {
        final boolean present = hasRegistered(ADDON_REGISTRY, addonType);
        if (present) ADDON_REGISTRY.remove(addonType);
        return present;
    }

    public Addon getAddon(@NotNull AddonType addonType, @NotNull FeatureContext featureContext) {
        return ADDON_REGISTRY.get(addonType).create(featureContext);
    }

    public <K extends Enum<K>, V> boolean hasRegistered(EnumMap<K, V> map, K key) {
        return map.containsKey(key);
    }

}
