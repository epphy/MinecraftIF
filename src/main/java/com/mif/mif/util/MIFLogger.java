package com.mif.mif.util;

import com.mif.mif.MinecraftIFMod;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

@Setter
@UtilityClass
public class MIFLogger {

    private static final Logger LOGGER = LoggerFactory.getLogger(MinecraftIFMod.getModId());
    private static final String TEMPLATE = "[{}] {}: {}";
    private static Level currentLevel = Level.WARN;

    public void announce(@NotNull String message) {
        LOGGER.info(TEMPLATE, "ANNOUNCEMENT", "MinecraftIFMod", message);
    }

    public void debug(@NotNull Object source, @NotNull String message) {
        if (isLevelDisabled(Level.DEBUG)) return;
        LOGGER.info(TEMPLATE, "DEBUG", getSourceName(source), message);
    }

    public void info(@NotNull Object source, @NotNull String message) {
        if (isLevelDisabled(Level.INFO)) return;
        LOGGER.info(TEMPLATE, "INFO", getSourceName(source), message);
    }

    public void warn(@NotNull Object source, @NotNull String message) {
        if (isLevelDisabled(Level.WARN)) return;
        LOGGER.warn(TEMPLATE, "WARN", getSourceName(source), message);
    }

    public void error(@NotNull Object source, @NotNull String message) {
        if (isLevelDisabled(Level.ERROR)) return;
        LOGGER.error(TEMPLATE, "ERROR", getSourceName(source), message);
    }

    public void error(@NotNull Object source, @NotNull String message, @NotNull Throwable throwable) {
        if (isLevelDisabled(Level.ERROR)) return;
        LOGGER.error(TEMPLATE, "ERROR", getSourceName(source), message, throwable);
    }

    private boolean isLevelDisabled(Level messageLevel) {
        return messageLevel.toInt() < currentLevel.toInt();
    }

    private String getSourceName(@NotNull Object source) {
        if (source instanceof String) return source.toString();
        return source.getClass().getSimpleName();
    }
}
