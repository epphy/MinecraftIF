package com.mif.mif.core.feature.addon.creeperexplosiongrief;

import com.mif.mif.core.feature.FeatureId;
import com.mif.mif.core.feature.addon.Addon;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class CreeperAntiGriefAddon implements Addon {
    private static boolean active = false;

    @Override
    public void enable() {
        CreeperAntiGriefAddon.active = true;
    }

    @Override
    public void disable() {
        CreeperAntiGriefAddon.active = false;
    }

    public static boolean isActive() {
        return CreeperAntiGriefAddon.active;
    }

    @Override
    public FeatureId getFeatureId() {
        return FeatureId.CREEPER_ANTI_GRIEF_ADDON;
    }
}
