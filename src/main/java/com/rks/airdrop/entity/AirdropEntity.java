package com.rks.airdrop.entity;

import com.rks.airdrop.registry.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class AirdropEntity extends Entity implements GeoEntity {
    private static final RawAnimation IDLE_ANIMATION = RawAnimation.begin().thenLoop("idle");

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
        setDeltaMovement(Vec3.ZERO);
        if (!onGround()) {
            move(MoverType.SELF, Vec3.ZERO);
        }
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
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
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
