package org.nondied.banda_de_trapo.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class PeriquitoEntity extends PathAwareEntity implements GeoEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final String[] EMOTES = {
            "animation.sc4_periquito.emote5",
            "animation.sc4_periquito.emote6",
            "animation.sc4_periquito.emote7",
            "animation.sc4_periquito.emote8",
            "animation.sc4_periquito.emote9",
            "animation.sc4_periquito.emote10",
            "animation.sc4_periquito.emote11",
            "animation.sc4_periquito.emote1",
            "animation.sc4_periquito.emote2",
            "animation.sc4_periquito.emote3",
            "animation.sc4_periquito.emote4",
    };

    private String currentEmote = "IDLE";

    public PeriquitoEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints = 0;
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
    }

    @Override
    public void checkDespawn() {
    }

    @Override
    public boolean canImmediatelyDespawn(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        return false;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::animController));
    }

    private PlayState animController(AnimationState<PeriquitoEntity> state) {
        if (this.currentEmote.equals("IDLE")) {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("animation.sc4_periquito.idle"));
            this.currentEmote = "";
            return PlayState.CONTINUE;
        }

        if (!this.currentEmote.isEmpty()) {
            state.getController().setAnimation(RawAnimation.begin().thenPlay(this.currentEmote));
            this.currentEmote = "";
            return PlayState.CONTINUE;
        }

        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public void playEmote(int squareId) {
        if (squareId >= 0 && squareId < EMOTES.length) {
            this.currentEmote = EMOTES[squareId];
        }
    }

    public void stopAnimation() {
        this.currentEmote = "animation.sc4_periquito.idle";
    }
}