package com.mif.mif.client.config;

import com.mif.mif.config.Config;
import com.mif.mif.core.feature.FeatureId;
import lombok.RequiredArgsConstructor;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.Map;

@RequiredArgsConstructor
public final class ConfigScreenBuilder {
    private final Config config;

    public Screen createConfigScreen(Screen parent) {
        final ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable("mif.config.title"));

        final ConfigCategory category = builder
                .getOrCreateCategory(Text.translatable("mif.config.category.features"));

        for (final Map.Entry<FeatureId, Boolean> entry : config.getEnabledFeatures().entrySet()) {
            final FeatureId featureId = entry.getKey();
            category.addEntry(builder.entryBuilder()
                    .startBooleanToggle(Text.of(featureId.name().toLowerCase()), config.isFeatureEnabled(featureId))
                    .setSaveConsumer(newValue -> {
                        config.setFeatureEnabled(entry.getKey(), newValue);
                        config.save();
                    })
                    .build());
        }

        return builder.build();
    }

}
