package com.mif.mif.command;

import com.mif.mif.MinecraftIFMod;
import com.mif.mif.config.ConfigManager;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.literal;

public class ReloadCommand {

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(
                    literal("mfi")
                            .requires(source -> source.hasPermissionLevel(2))
                            .then(literal("reload")
                                    .executes(context -> {
                                        if (!MinecraftIFMod.isServer()) {
                                            context.getSource().sendFeedback(() -> Text.translatable("mif.command.config.reload.server_only"), false);
                                            return 0;
                                        }
                                        ConfigManager.getInstance().getServerConfig().init();
                                        context.getSource().sendFeedback(() -> Text.translatable("mif.command.config.reload"), false);
                                        return 1;
                                    })
                            )
            );
        });
    }

}
