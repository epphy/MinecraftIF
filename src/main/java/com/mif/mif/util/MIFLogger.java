package com.mif.mif.util;

import com.mif.mif.MinecraftIFMod;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@UtilityClass
public class MIFLogger {
    private static final Logger GLOBAL_LOGGER = LoggerFactory.getLogger(MinecraftIFMod.getModId());

    public void announce(@NotNull String message) {
        GLOBAL_LOGGER.info(message);
    }

    public void debug(@NotNull Object source, @NotNull String message) {
        getLogger(getSourceClass(source)).debug(message);
    }

    public void info(@NotNull Object source, @NotNull String message) {
        getLogger(getSourceClass(source)).info(message);
    }

    public void warn(@NotNull Object source, @NotNull String message) {
        getLogger(getSourceClass(source)).warn(message);
    }

    public void error(@NotNull Object source, @NotNull String message) {
        getLogger(getSourceClass(source)).error(message);
    }

    public void error(@NotNull Object source, @NotNull String message, @NotNull Throwable throwable) {
        getLogger(getSourceClass(source)).error(message, throwable);
    }

    private Logger getLogger(@NotNull Class<?> sourceClass) {
        return LoggerFactory.getLogger(sourceClass);
    }

    private Class<?> getSourceClass(@NotNull Object source) {
        return source.getClass();
    }
}
