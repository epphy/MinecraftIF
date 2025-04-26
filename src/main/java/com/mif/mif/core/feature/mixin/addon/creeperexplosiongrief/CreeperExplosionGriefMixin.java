package com.mif.mif.core.feature.mixin.addon.creeperexplosiongrief;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public abstract class CreeperExplosionGriefMixin {

    @Unique
    private static final ThreadLocal<Boolean> CUSTOM_EXPLOSION = ThreadLocal.withInitial(() -> false);

    @Shadow public abstract void createExplosion(@Nullable Entity entity, @Nullable DamageSource damageSource, @Nullable ExplosionBehavior behavior, double x, double y, double z, float power, boolean createFire, World.ExplosionSourceType explosionSourceType, ParticleEffect smallParticle, ParticleEffect largeParticle, RegistryEntry<SoundEvent> soundEvent);

    @Inject(method = "createExplosion", at = @At("HEAD"), cancellable = true)
    private void preventBlockDamage(Entity entity, DamageSource damageSource, ExplosionBehavior behavior, double x, double y, double z, float power, boolean createFire, World.ExplosionSourceType explosionSourceType, ParticleEffect smallParticle, ParticleEffect largeParticle, RegistryEntry<SoundEvent> soundEvent, CallbackInfo ci) {
        if (CUSTOM_EXPLOSION.get()) return;
        if () return; // Add a flag here
        if (entity.getType() != EntityType.CREEPER) return;

        CUSTOM_EXPLOSION.set(true);
        ci.cancel();
        this.createExplosion(entity, damageSource, behavior, x, y, z, power, createFire, World.ExplosionSourceType.NONE, smallParticle, largeParticle, soundEvent);
        CUSTOM_EXPLOSION.set(false);
    }

}
