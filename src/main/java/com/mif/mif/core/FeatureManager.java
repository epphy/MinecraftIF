package com.mif.mif.core;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FeatureManager {
    private static final EnumMap<FeatureType, IFeature> FEATURES = new EnumMap<>(FeatureType.class);

    public boolean addFeature(@NotNull FeatureType featureType, @NotNull IFeature feature) {
        final boolean absent = !isFeatureRegistered(featureType);
        if (absent) FEATURES.put(featureType, feature);
        return absent;
    }

    public boolean removeFeature(@NotNull FeatureType featureType) {
        final boolean present = isFeatureRegistered(featureType);
        if (present) FEATURES.remove(featureType);
        return present;
    }

    public boolean isFeatureRegistered(@NotNull FeatureType featureType) {
        return FEATURES.containsKey(featureType);
    }
}
