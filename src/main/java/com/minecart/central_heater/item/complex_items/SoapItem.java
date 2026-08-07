package com.minecart.central_heater.item.complex_items;

import com.minecart.central_heater.recipe.AllRecipe;
import com.minecart.central_heater.recipe.recipe_input.BlockCleaningRecipeInput;
import com.minecart.central_heater.recipe.recipe_types.BlockCleaningRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 1.20.1 port of SoapItem. Uses NBT instead of 1.21+ data components, omits
 * NeoForge's ItemAbility (canPerformAction returns true for our own internal
 * check), and rewrites banner washing using 1.20.1's NBT pattern list.
 */
public class SoapItem extends Item {

    private static final String TAG_IS_BLOCK_TARGET = "central_heater_IsBlockTarget";
    private static final String TAG_TARGET_ID = "central_heater_TargetId";

    private static final int USE_DURATION = 60;

    private static final List<BlockWashingHandler> BLOCK_WASHERS = new ArrayList<>();
    private static final List<EntityWashingHandler> ENTITY_WASHERS = new ArrayList<>();

    static {
        prepareBlockWasher();
        prepareEntityWasher();
    }

    public SoapItem(Properties properties) {
        super(properties);
    }

    public static void prepareEntityWasher() {
        ENTITY_WASHERS.add(entity -> {
            if (entity instanceof Sheep sheep && !sheep.isSheared() && sheep.getColor() != DyeColor.WHITE) {
                Item dyeItem = DyeItem.byColor(sheep.getColor());
                sheep.setColor(DyeColor.WHITE);
                return EntityWashResult.successItem(new ItemStack(dyeItem));
            }
            return EntityWashResult.fail();
        });
    }

    public static void prepareBlockWasher() {
        // Banner: strip the last applied pattern layer and drop the matching dye
        BLOCK_WASHERS.add((level, pos, state) -> {
            if (level.getBlockEntity(pos) instanceof BannerBlockEntity banner) {
                CompoundTag beTag = banner.saveWithoutMetadata();
                if (!beTag.contains("Patterns", Tag.TAG_LIST)) {
                    return BlockWashResult.fail();
                }
                ListTag patterns = beTag.getList("Patterns", Tag.TAG_COMPOUND);
                if (patterns.isEmpty()) {
                    return BlockWashResult.fail();
                }
                CompoundTag last = patterns.getCompound(patterns.size() - 1);
                int colorIdx = last.getInt("Color");
                DyeColor color = DyeColor.byId(colorIdx);

                patterns.remove(patterns.size() - 1);
                if (patterns.isEmpty()) {
                    beTag.remove("Patterns");
                } else {
                    beTag.put("Patterns", patterns);
                }
                banner.load(beTag);
                banner.setChanged();
                level.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);

                Item dyeItem = DyeItem.byColor(color);
                return BlockWashResult.successItem(new ItemStack(dyeItem), 1.0f);
            }
            return BlockWashResult.fail();
        });
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
        return UseAnim.BRUSH;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
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

        int ticksUsed = this.getUseDuration(stack) - remainingUseDuration + 1;

        boolean currentIsBlock = hitresult.getType() == HitResult.Type.BLOCK;
        long currentTargetId = 0L;

        if (currentIsBlock) {
            currentTargetId = ((BlockHitResult) hitresult).getBlockPos().asLong();
        } else if (hitresult instanceof EntityHitResult ehr) {
            currentTargetId = ehr.getEntity().getId();
        }

        CompoundTag tag = stack.getOrCreateTag();

        if (ticksUsed <= 1) {
            tag.putBoolean(TAG_IS_BLOCK_TARGET, currentIsBlock);
            tag.putLong(TAG_TARGET_ID, currentTargetId);
        } else {
            boolean savedIsBlock = tag.getBoolean(TAG_IS_BLOCK_TARGET);
            long savedTargetId = tag.getLong(TAG_TARGET_ID);

            if (savedIsBlock != currentIsBlock || savedTargetId != currentTargetId) {
                livingEntity.releaseUsingItem();
                return;
            }
        }

        if (hitresult instanceof EntityHitResult entityHitResult) {
            Entity targetEntity = entityHitResult.getEntity();

            if (ticksUsed % 10 == 5) {
                level.playSound(player, targetEntity.blockPosition(), SoundEvents.BRUSH_GENERIC, SoundSource.PLAYERS);

                if (level.isClientSide()) {
                    for (int i = 0; i < 7; i++) {
                        level.addParticle(
                                ParticleTypes.SPLASH,
                                targetEntity.getRandomX(0.8),
                                targetEntity.getRandomY() + 0.2,
                                targetEntity.getRandomZ(0.8),
                                0.0, 0.1, 0.0
                        );
                    }
                }
            }

            if (ticksUsed >= USE_DURATION) {
                if (!level.isClientSide()) {
                    boolean washedSomething = false;
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

                    if (washedSomething) {
                        level.playSound(null, targetEntity.blockPosition(), SoundEvents.BUCKET_EMPTY, SoundSource.PLAYERS);
                        EquipmentSlot slot = stack.equals(player.getItemBySlot(EquipmentSlot.OFFHAND)) ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND;
                        stack.hurtAndBreak(1, livingEntity, e -> e.broadcastBreakEvent(slot));
                    }
                }
                livingEntity.releaseUsingItem();
            }
            return;
        }

        if (hitresult instanceof BlockHitResult blockhitresult) {
            BlockPos blockpos = blockhitresult.getBlockPos();
            BlockState blockstate = level.getBlockState(blockpos);

            if (ticksUsed % 10 == 5) {
                level.playSound(player, blockpos, SoundEvents.BRUSH_GENERIC, SoundSource.BLOCKS);
                if (level.isClientSide()) {
                    HumanoidArm humanoidarm = livingEntity.getUsedItemHand() == InteractionHand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite();
                    this.spawnWaterParticles(level, blockhitresult, livingEntity.getViewVector(0.0F), humanoidarm);
                }
            }

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
                        if (level.random.nextFloat() < result.dropChance()) {
                            Block.popResource(level, blockpos, result.byproduct());
                        }
                    }
                }

                if (washedSomething) {
                    livingEntity.releaseUsingItem();
                    level.playSound(null, blockpos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS);
                    EquipmentSlot slot = stack.equals(player.getItemBySlot(EquipmentSlot.OFFHAND)) ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND;
                    stack.hurtAndBreak(1, livingEntity, e -> e.broadcastBreakEvent(slot));
                }
            }
            return;
        }

        livingEntity.releaseUsingItem();
    }

    private HitResult calculateHitResult(Player player) {
        double range = player.isCreative() ? 5.0D : 4.5D;
        Vec3 eyePos = player.getEyePosition();
        Vec3 viewVector = player.getViewVector(1.0F);
        Vec3 endPos = eyePos.add(viewVector.x * range, viewVector.y * range, viewVector.z * range);
        AABB aabb = player.getBoundingBox().expandTowards(viewVector.scale(range)).inflate(1.0D, 1.0D, 1.0D);

        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(player, eyePos, endPos, aabb,
                (entity) -> !entity.isSpectator() && entity.isPickable(), range * range);
        if (entityHit != null) {
            return entityHit;
        }

        return player.level().clip(new ClipContext(
                eyePos,
                endPos,
                ClipContext.Block.OUTLINE,
                ClipContext.Fluid.NONE,
                player
        ));
    }

    private void spawnWaterParticles(Level level, BlockHitResult hitResult, Vec3 pos, HumanoidArm arm) {
        int armModifier = arm == HumanoidArm.RIGHT ? 1 : -1;
        int particleCount = level.getRandom().nextIntBetweenInclusive(7, 11);
        Direction direction = hitResult.getDirection();
        Vec3 hitLocation = hitResult.getLocation();

        double xOffset = direction == Direction.NORTH || direction == Direction.SOUTH ? 1.0 : 0.1;
        double zOffset = direction == Direction.EAST || direction == Direction.WEST ? 1.0 : 0.1;

        for (int k = 0; k < particleCount; ++k) {
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

    public Optional<CleanResult> getCleanResult(Level level, BlockState originalState) {
        BlockCleaningRecipeInput input = new BlockCleaningRecipeInput(originalState.getBlock());

        return level.getRecipeManager()
                .getRecipeFor(AllRecipe.BLOCK_CLEANING.get(), input, level)
                .map(recipe -> {
                    BlockState cleanedState = recipe.getOutputBlock().defaultBlockState();
                    for (Property<?> property : originalState.getProperties()) {
                        if (cleanedState.hasProperty(property)) {
                            cleanedState = copyProperty(originalState, cleanedState, property);
                        }
                    }
                    return new CleanResult(cleanedState, recipe.getItemOutput().copy(), recipe.getDropChance());
                });
    }

    private static <T extends Comparable<T>> BlockState copyProperty(BlockState from, BlockState to, Property<T> property) {
        return to.setValue(property, from.getValue(property));
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
        public static EntityWashResult fail() { return new EntityWashResult(false, ItemStack.EMPTY); }
        public static EntityWashResult successItem(ItemStack drop) { return new EntityWashResult(true, drop); }
        public static EntityWashResult successNone() { return new EntityWashResult(true, ItemStack.EMPTY); }
    }

    public interface EntityWashingHandler {
        EntityWashResult tryWash(Entity entity);
    }
}
