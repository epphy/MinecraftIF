package com.mif.mif.core.feature.addon.creeperexplosiongrief;

import com.mif.mif.core.feature.FeatureContext;
import com.mif.mif.core.feature.FeatureId;
import com.mif.mif.core.feature.addon.Addon;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class CreeperAntiGriefAddon implements Addon {
    private static boolean active = false;

    private static void setActive(boolean active) {
        CreeperAntiGriefAddon.active = active;
    }

    public static boolean isActive() {
        return CreeperAntiGriefAddon.active;
    }

    @Override
    public void enable(FeatureContext featureContext) {
        setActive(true);
    }

    @Override
    public void disable() {
        setActive(false);
    }

    @Override
    public FeatureId getFeatureId() {
        return FeatureId.CREEPER_ANTI_GRIEF_ADDON;
    }
}
