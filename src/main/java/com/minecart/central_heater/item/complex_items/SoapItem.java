package com.minecart.central_heater.item.complex_items;

import com.minecart.central_heater.recipe.AllRecipe;
import com.minecart.central_heater.recipe.recipe_input.BlockCleaningRecipeInput;
import com.minecart.central_heater.recipe.recipe_types.BlockCleaningRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.*;
import net.neoforged.neoforge.common.ItemAbility;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SoapItem extends Item {

    // 1. Register the custom ItemAbility
    public static final ItemAbility SOAP_BRUSHING = ItemAbility.get("soap_brushing");


    private static final List<BlockWashingHandler> BLOCK_WASHERS = new ArrayList<>();
    private static final List<EntityWashingHandler> ENTITY_WASHERS = new ArrayList<>();

    static {
        prepareBlockWasher();
        prepareEntityWasher();
    }

    public static void prepareEntityWasher(){
        ENTITY_WASHERS.add((entity) -> {
            if (entity instanceof Sheep sheep && !sheep.isSheared() && sheep.getColor() != DyeColor.WHITE) {
                Item dyeItem = DyeItem.byColor(sheep.getColor());
                sheep.setColor(DyeColor.WHITE);

                return EntityWashResult.successItem(new ItemStack(dyeItem));
            }
            return EntityWashResult.fail();
        });
    }

    public static void prepareBlockWasher() {
        // --- 1. SPECIAL CASE: BANNERS (Removes the last applied layer) ---
        BLOCK_WASHERS.add((level, pos, state) -> {
            if (level.getBlockEntity(pos) instanceof BannerBlockEntity banner) {
                BannerPatternLayers patterns = banner.getPatterns();

                if (!patterns.layers().isEmpty()) {
                    List<BannerPatternLayers.Layer> layers = new ArrayList<>(patterns.layers());
                    BannerPatternLayers.Layer lastLayer = layers.remove(layers.size() - 1);

                    ItemStack dummyBanner = new ItemStack(state.getBlock());
                    dummyBanner.set(DataComponents.BANNER_PATTERNS, new BannerPatternLayers(layers));

                    banner.applyComponentsFromItemStack(dummyBanner);
                    banner.setChanged();
                    level.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);

                    Item dyeItem = DyeItem.byColor(lastLayer.color());
                    return BlockWashResult.successItem(new ItemStack(dyeItem), 1.0f);
                }
            }
            return BlockWashResult.fail();
        });
    }

    private static final int USE_DURATION = 60;


    public SoapItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
        return itemAbility == SOAP_BRUSHING;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player != null && this.calculateHitResult(player).getType() == HitResult.Type.BLOCK) {
            player.startUsingItem(context.getHand());
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget, InteractionHand usedHand) {
        player.startUsingItem(usedHand);
        return InteractionResult.CONSUME;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BRUSH; // Reuses the brushing animation
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        if (!(livingEntity instanceof Player player)) {
            livingEntity.releaseUsingItem();
            return;
        }

        HitResult hitresult = this.calculateHitResult(player);

        if (hitresult.getType() == HitResult.Type.MISS) {
            livingEntity.releaseUsingItem();
            return;
        }

        int ticksUsed = this.getUseDuration(stack, livingEntity) - remainingUseDuration + 1;

        boolean currentIsBlock = hitresult.getType() == HitResult.Type.BLOCK;
        long currentTargetId = 0L;

        if (currentIsBlock) {
            currentTargetId = ((BlockHitResult) hitresult).getBlockPos().asLong();
        } else if (hitresult instanceof EntityHitResult ehr) {
            currentTargetId = ehr.getEntity().getId();
        }

        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag tag = customData.copyTag();

        if (ticksUsed <= 1) {
            // First tick: Save the primitive data of what we are looking at
            tag.putBoolean("IsBlockTarget", currentIsBlock);
            tag.putLong("TargetId", currentTargetId);
            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        } else {
            // Compare against saved primitive data
            boolean savedIsBlock = tag.getBoolean("IsBlockTarget");
            long savedTargetId = tag.getLong("TargetId");

            if (savedIsBlock != currentIsBlock || savedTargetId != currentTargetId) {
                // Target changed! Release the item to reset the animation and timer
                livingEntity.releaseUsingItem();
                return;
            }
        }

        if (hitresult instanceof EntityHitResult entityHitResult) {
            Entity targetEntity = entityHitResult.getEntity();

            // Play sounds and spawn water particles every few ticks
            if (ticksUsed % 10 == 5) {
                level.playSound(player, targetEntity.blockPosition(), SoundEvents.BRUSH_GENERIC, SoundSource.PLAYERS);

                // Safely spawn particles on the client side, scaled dynamically to the entity's hitbox!
                if (level.isClientSide()) {
                    for (int i = 0; i < 7; i++) {
                        level.addParticle(
                                ParticleTypes.SPLASH,
                                targetEntity.getRandomX(0.8),
                                targetEntity.getRandomY() + 0.2, // Biased slightly towards the upper body/head
                                targetEntity.getRandomZ(0.8),
                                0.0, 0.1, 0.0
                        );
                    }
                }
            }

            // If we reached the end of the 3 seconds, finish scrubbing the entity!
            if (ticksUsed >= USE_DURATION) {
                if (!level.isClientSide()) {
                    boolean washedSomething = false;

                    // Run through our list of handlers
                    for (EntityWashingHandler washer : ENTITY_WASHERS) {
                        EntityWashResult result = washer.tryWash(targetEntity);

                        if (result.success()) {
                            washedSomething = true;
                            if (!result.drop().isEmpty()) {
                                targetEntity.spawnAtLocation(result.drop());
                            }
                            break;
                        }
                    }

                    // Only consume durability and play the sound if we actually cleaned something
                    if (washedSomething) {
                        level.playSound(null, targetEntity.blockPosition(), SoundEvents.BUCKET_EMPTY, SoundSource.PLAYERS);
                        EquipmentSlot equipmentslot = stack.equals(player.getItemBySlot(EquipmentSlot.OFFHAND)) ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND;
                        stack.hurtAndBreak(1, livingEntity, equipmentslot);
                    }
                }

                // Force the item to stop being used so the animation resets perfectly
                livingEntity.releaseUsingItem();
            }
            return;
        }

        // --- 2. BLOCK SCRUBBING ---
        else if (hitresult instanceof BlockHitResult blockhitresult) {
            BlockPos blockpos = blockhitresult.getBlockPos();
            BlockState blockstate = level.getBlockState(blockpos);

            // Play sounds and spawn water particles every few ticks on ANY block
            if (ticksUsed % 10 == 5) {
                level.playSound(player, blockpos, SoundEvents.BRUSH_GENERIC, SoundSource.BLOCKS);
                if (level.isClientSide()) {
                    HumanoidArm humanoidarm = livingEntity.getUsedItemHand() == InteractionHand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite();
                    this.spawnWaterParticles(level, blockhitresult, livingEntity.getViewVector(0.0F), humanoidarm);
                }
            }

            // If we reached the end of the 3 seconds, check if we should actually clean it
            if (!level.isClientSide() && ticksUsed >= USE_DURATION) {
                boolean washedSomething = false;

                for (BlockWashingHandler washer : BLOCK_WASHERS) {
                    BlockWashResult blockSpecialResult = washer.tryWash(level, blockpos, blockstate);

                    if (blockSpecialResult.success()) {
                        washedSomething = true;

                        if (!blockSpecialResult.drop().isEmpty() && level.random.nextFloat() < blockSpecialResult.dropChance()) {
                            Block.popResource(level, blockpos, blockSpecialResult.drop());
                        }
                        break;
                    }
                }

                if (!washedSomething) {

                    Optional<CleanResult> cleanResultOpt = getCleanResult(level, blockstate);

                    if (cleanResultOpt.isPresent()) {
                        CleanResult result = cleanResultOpt.get();

                        washedSomething = true;

                        if (blockstate.getBlock() instanceof BedBlock) {
                            Direction facing = blockstate.getValue(BedBlock.FACING);
                            BedPart part = blockstate.getValue(BedBlock.PART);

                            BlockPos otherPos = (part == BedPart.HEAD)
                                    ? blockpos.relative(facing.getOpposite())
                                    : blockpos.relative(facing);

                            BlockState otherState = level.getBlockState(otherPos);

                            if (otherState.is(blockstate.getBlock())) {
                                Optional<CleanResult> otherCleanOpt = getCleanResult(level, otherState);
                                if (otherCleanOpt.isPresent()) {
                                    level.setBlock(otherPos, otherCleanOpt.get().state(), Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE);
                                }
                            }
                        }

                        level.setBlock(blockpos, result.state(), Block.UPDATE_ALL);

                        if (level.random.nextFloat() < result.dropChance)
                            Block.popResource(level, blockpos, result.byproduct());
                    }
                }

                if (washedSomething) {
                    livingEntity.releaseUsingItem();

                    level.playSound(null, blockpos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS);
                    EquipmentSlot equipmentslot = stack.equals(player.getItemBySlot(EquipmentSlot.OFFHAND)) ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND;
                    stack.hurtAndBreak(1, livingEntity, equipmentslot);
                }
            }
            return;
        }

        livingEntity.releaseUsingItem();
    }

    private HitResult calculateHitResult(Player player) {
        double range = player.blockInteractionRange();
        Vec3 eyePos = player.getEyePosition();
        Vec3 viewVector = player.getViewVector(1.0F);
        Vec3 endPos = eyePos.add(viewVector.x * range, viewVector.y * range, viewVector.z * range);
        AABB aabb = player.getBoundingBox().expandTowards(viewVector.scale(range)).inflate(1.0D, 1.0D, 1.0D);

        // 1. Check Entities First
        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(player, eyePos, endPos, aabb, (entity) -> !entity.isSpectator() && entity.isPickable(), range * range);
        if (entityHit != null) {
            return entityHit;
        }

        // 2. Check Blocks using OUTLINE (allows hitting non-solid blocks like Banners)
        return player.level().clip(new net.minecraft.world.level.ClipContext(
                eyePos,
                endPos,
                net.minecraft.world.level.ClipContext.Block.OUTLINE,
                net.minecraft.world.level.ClipContext.Fluid.NONE,
                player
        ));
    }

    private void spawnWaterParticles(Level level, BlockHitResult hitResult, Vec3 pos, HumanoidArm arm) {
        int armModifier = arm == HumanoidArm.RIGHT ? 1 : -1;
        int particleCount = level.getRandom().nextInt(7, 12);
        Direction direction = hitResult.getDirection();
        Vec3 hitLocation = hitResult.getLocation();

        // Calculate a rough offset based on the block face being scrubbed
        double xOffset = direction == Direction.NORTH || direction == Direction.SOUTH ? 1.0 : 0.1;
        double zOffset = direction == Direction.EAST || direction == Direction.WEST ? 1.0 : 0.1;

        for(int k = 0; k < particleCount; ++k) {
            level.addParticle(
                    ParticleTypes.SPLASH,
                    hitLocation.x - (direction == Direction.WEST ? 1.0E-6 : 0.0),
                    hitLocation.y,
                    hitLocation.z - (direction == Direction.NORTH ? 1.0E-6 : 0.0),
                    xOffset * armModifier * level.getRandom().nextDouble() * 0.5,
                    0.1,
                    zOffset * armModifier * level.getRandom().nextDouble() * 0.5
            );
        }
    }

    public record CleanResult(BlockState state, ItemStack byproduct, float dropChance) {}

    public record BlockWashResult(boolean success, ItemStack drop, float dropChance) {
        public static BlockWashResult fail() { return new BlockWashResult(false, ItemStack.EMPTY, 0f); }
        public static BlockWashResult successItem(ItemStack drop, float dropChance) { return new BlockWashResult(true, drop, dropChance); }
        public static BlockWashResult successNone() { return new BlockWashResult(true, ItemStack.EMPTY, 0f); }
    }

    public interface BlockWashingHandler {
        BlockWashResult tryWash(Level level, BlockPos pos, BlockState state);
    }

    public record EntityWashResult(boolean success, ItemStack drop) {
        public static EntityWashResult fail() {
            return new EntityWashResult(false, ItemStack.EMPTY);
        }

        public static EntityWashResult successItem(ItemStack drop) {
            return new EntityWashResult(true, drop);
        }
        public static EntityWashResult successNone() {
            return new EntityWashResult(true, ItemStack.EMPTY);
        }
    }

    public interface EntityWashingHandler {
        EntityWashResult tryWash(Entity entity);
    }

    public Optional<CleanResult> getCleanResult(Level level, BlockState originalState) {
        BlockCleaningRecipeInput input = new BlockCleaningRecipeInput(originalState.getBlock());

        return level.getRecipeManager()
                .getRecipeFor(AllRecipe.BLOCK_CLEANING.get(), input, level)
                .map(holder -> {
                    BlockCleaningRecipe recipe = holder.value();
                    BlockState cleanedState = recipe.getOutputBlock().defaultBlockState();

                    for (Property<?> property : originalState.getProperties()) {
                        if (cleanedState.hasProperty(property)) {
                            cleanedState = copyProperty(originalState, cleanedState, property);
                        }
                    }

                    // Return the perfectly preserved state and a copy of the dye!
                    return new CleanResult(cleanedState, recipe.getItemOutput().copy(), recipe.getDropChance());
                });
    }

    // Helper method required to bypass Java's strict generic wildcards when copying properties
    private static <T extends Comparable<T>> BlockState copyProperty(BlockState from, BlockState to, Property<T> property) {
        return to.setValue(property, from.getValue(property));
    }
}