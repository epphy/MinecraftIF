package com.mif.mif.core.feature.mixin.fix;

import com.mif.mif.core.FeatureManager;
import com.mif.mif.core.feature.fix.Fix;
import net.minecraft.entity.mob.CreeperEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreeperEntity.class)
public final class DisableCreeperGriefMixin implements Fix {

    @Inject(method = "explode", at = @At("HEAD"), cancellable = true)
    private void disableCreeperExplosion(CallbackInfo ci) {

        if (FeatureManager.getInstance().isFeatureRegistered())
        ci.cancel();

    }

}
