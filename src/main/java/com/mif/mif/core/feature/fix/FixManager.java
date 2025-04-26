package com.mif.mif.core.feature.fix;

import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;

public final class FixManager {
    private final EnumMap<FixType, Fix> fixes = new EnumMap<>(FixType.class);

    public boolean addFix(@NotNull FixType fixType, @NotNull Fix fix) {
        final boolean absent = !hasFix(fixType);
        if (absent) fixes.put(fixType, fix);
        return absent;
    }

    public boolean removeFix(@NotNull FixType fixType) {
        final boolean present = hasFix(fixType);
        if (present) fixes.remove(fixType);
        return present;
    }

    public boolean hasFix(@NotNull FixType fixType) {
        return fixes.containsKey(fixType);
    }

}
