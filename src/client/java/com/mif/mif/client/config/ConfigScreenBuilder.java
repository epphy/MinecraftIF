package com.mif.mif.client.config;

import com.mif.mif.config.ClientConfig;
import com.mif.mif.core.feature.FeatureId;
import lombok.RequiredArgsConstructor;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.Map;

/*
Todo:
    1. Tooltip what feature does
    2. Information of whether it is client or server side
    3. Warning that server side features won't work on server for mod-menu
    4. Update design of mod-menu
 */

@RequiredArgsConstructor
public final class ConfigScreenBuilder {
    private final ClientConfig clientConfig;

    public Screen createConfigScreen(Screen parent) {
        final ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable("mif.config.title"));

        final ConfigCategory category = builder
                .getOrCreateCategory(Text.translatable("mif.config.category.features"));

        for (final Map.Entry<FeatureId, Boolean> entry : clientConfig.getEnabledFeatures().entrySet()) {
            final FeatureId featureId = entry.getKey();
            category.addEntry(builder.entryBuilder()
                    .startBooleanToggle(Text.of(featureId.name().toLowerCase()), clientConfig.isFeatureEnabled(featureId))
                    .setSaveConsumer(newValue -> {
                        clientConfig.setFeatureEnabled(entry.getKey(), newValue);
                        clientConfig.save();
                    })
                    .build());
        }

        return builder.build();
    }

}
