package com.rks.airdrop.entity;

import com.rks.airdrop.registry.ModItems;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class AirdropEntity extends Entity implements GeoEntity {
    private static final RawAnimation IDLE_ANIMATION = RawAnimation.begin().thenLoop("idle");
    private static final DustParticleOptions FLARE_PARTICLE =
            new DustParticleOptions(new Vector3f(1.0F, 0.15F, 0.15F), 1.35F);
    private static final EntityDimensions GROUNDED_DIMENSIONS = EntityDimensions.scalable(1.75F, 1.35F);
    private static final EntityDimensions DESCENDING_DIMENSIONS = EntityDimensions.scalable(2.4F, 4.5F);
    private static final EntityDataAccessor<Boolean> DESCENDING =
            SynchedEntityData.defineId(AirdropEntity.class, EntityDataSerializers.BOOLEAN);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public AirdropEntity(EntityType<? extends AirdropEntity> entityType, Level level) {
        super(entityType, level);
        this.blocksBuilding = true;
        setNoGravity(true);
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (level().isClientSide) {
            return InteractionResult.SUCCESS;
        }

        breakOpen();
        return InteractionResult.CONSUME;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (level().isClientSide) {
            return true;
        }

        if (source.getEntity() instanceof Player) {
            breakOpen();
            return true;
        }

        return false;
    }

    @Override
    public void tick() {
        super.tick();

        if (isDescending()) {
            double nextY = Math.max(getDeltaMovement().y - 0.008D, -0.15D);
            setDeltaMovement(0.0D, nextY, 0.0D);
            move(MoverType.SELF, getDeltaMovement());
            resetFallDistance();

            if (onGround()) {
                setDescending(false);
                setDeltaMovement(Vec3.ZERO);
            }
        } else {
            setDeltaMovement(Vec3.ZERO);
        }

        spawnFlareParticles();
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return isDescending() ? DESCENDING_DIMENSIONS : GROUNDED_DIMENSIONS;
    }

    @Override
    protected void doWaterSplashEffect() {
    }

    @Override
    public void push(double x, double y, double z) {
    }

    @Override
    public void push(Entity entity) {
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(DESCENDING, Boolean.FALSE);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        setDescending(tag.getBoolean("Descending"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putBoolean("Descending", isDescending());
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "main", 0, state -> {
            state.getController().setAnimation(IDLE_ANIMATION);
            return PlayState.CONTINUE;
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public void setDescending(boolean descending) {
        boolean previous = isDescending();
        entityData.set(DESCENDING, descending);
        if (previous != descending) {
            refreshDimensions();
        }
    }

    public boolean isDescending() {
        return entityData.get(DESCENDING);
    }

    private void spawnFlareParticles() {
        if (!(level() instanceof ServerLevel serverLevel) || tickCount % 3 != 0) {
            return;
        }

        double baseX = getX();
        double baseY = getY() + (isDescending() ? 1.0D : 0.8D);
        double baseZ = getZ();

        for (int i = 0; i < 6; i++) {
            double offsetX = (random.nextDouble() - 0.5D) * 0.8D;
            double offsetZ = (random.nextDouble() - 0.5D) * 0.8D;
            serverLevel.sendParticles(FLARE_PARTICLE, baseX + offsetX, baseY, baseZ + offsetZ, 1, 0.0D, 0.3D, 0.0D, 0.02D);
        }
    }

    private void breakOpen() {
        if (isRemoved()) {
            return;
        }

        spawnAtLocation(new ItemStack(ModItems.AIRDROP_BOX.get()));
        spawnAtLocation(new ItemStack(ModItems.MEDIC_CRATE.get(), 2));
        spawnAtLocation(new ItemStack(ModItems.WEAPON_CRATE.get()));
        spawnAtLocation(new ItemStack(Items.OAK_PLANKS, 4));
        discard();
    }
}
