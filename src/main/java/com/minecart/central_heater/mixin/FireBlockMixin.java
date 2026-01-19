package com.minecart.central_heater.mixin;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.advancement.AllTrigger;
import com.minecart.central_heater.block.misc.BurntLogBlock;
import com.minecart.central_heater.misc.DataMapHook;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FireBlock.class)
public abstract class FireBlockMixin extends BaseFireBlock {
    public FireBlockMixin(Properties properties, float fireDamage) {
        super(properties, fireDamage);
    }

    /**
     * Injects into the burnout logic to replace logs with burnt logs
     * or drop ash when a block is consumed by fire.
     */
    @Inject(method = "tryCatchFire", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;removeBlock(Lnet/minecraft/core/BlockPos;Z)Z"), cancellable = true)
    private void central_heater$onBurnBlock(Level level, BlockPos pos, int chance, RandomSource random, int age, Direction face, CallbackInfo ci) {
        if (level.isClientSide) return;

        BlockState oldState = level.getBlockState(pos);

        // --- Log to Burnt Log Logic ---
        if (oldState.is(BlockTags.LOGS_THAT_BURN)) {
            // Determine orientation based on original log axis
            Direction.Axis axis = oldState.hasProperty(RotatedPillarBlock.AXIS) ? oldState.getValue(RotatedPillarBlock.AXIS) : Direction.Axis.Y;
            Direction facing = switch (axis) {
                case X -> Direction.EAST;
                case Z -> Direction.SOUTH;
                default -> Direction.UP;
            };

            // Set the block to our custom Burnt Log instead of air
            BlockState newState = AllBlockItem.BURNT_LOG.get().defaultBlockState()
                    .setValue(BurntLogBlock.FACING, facing)
                    .setValue(BurntLogBlock.LAYERS, 4);

            level.setBlock(pos, newState, 3);
            ci.cancel(); // Prevent the original code from removing the block
            return;
        }

        // --- Ash Dropping Logic ---
        ItemStack oldItem = oldState.getBlock().asItem().getDefaultInstance();
        // Ensure DataMapHook uses a 1.20.1 compatible lookup (Tags or Config)
        float dropChance = DataMapHook.getFireAshDropChance(oldItem);

        if (level instanceof ServerLevel serverLevel) {
            Player nearestPlayer = serverLevel.getNearestPlayer(pos.getX(), pos.getY(), pos.getZ(), 10.0, false);
            if (nearestPlayer instanceof ServerPlayer player) {
                // AllTrigger is now a direct instance in 1.20.1
                AllTrigger.BURNT_OBJECT.trigger(player, oldItem);
            }
        }

        // Handle Ash Spawn
        if (random.nextFloat() <= dropChance) {
            ItemStack ashStack = new ItemStack(AllBlockItem.FIRE_ASH.get());
            double d0 = (double) pos.getX() + 0.5D;
            double d1 = (double) pos.getY() + 0.5D;
            double d2 = (double) pos.getZ() + 0.5D;
            ItemEntity itemEntity = new ItemEntity(level, d0, d1, d2, ashStack);
            itemEntity.setDefaultPickUpDelay();
            level.addFreshEntity(itemEntity);
        }
    }
}