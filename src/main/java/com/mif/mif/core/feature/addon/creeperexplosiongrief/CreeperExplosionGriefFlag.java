package com.mif.mif.core.feature.addon.creeperexplosiongrief;

import com.mif.mif.core.feature.addon.Addon;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public final class CreeperExplosionGriefFlag implements Addon {
    private boolean active = false;

    public CreeperExplosionGriefFlag() {
        setActive(true);
    }
}
