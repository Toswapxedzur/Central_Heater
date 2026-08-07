package com.minecart.central_heater.block.misc;

import com.minecart.central_heater.block_entity.AllBlockEntity;
import com.minecart.central_heater.block_entity.misc.BurnableCampfireBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class BurnableCampfireBlock extends CampfireBlock {

    private final int tier;

    public BurnableCampfireBlock(boolean spawnParticles, int fireDamage, Properties properties) {
        super(spawnParticles, fireDamage, properties);
        this.tier = fireDamage == 1 ? 0 : 1;
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(LIT, Boolean.FALSE)
                .setValue(SIGNAL_FIRE, Boolean.FALSE)
                .setValue(WATERLOGGED, Boolean.FALSE)
                .setValue(FACING, Direction.NORTH));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        return state == null ? null : state.setValue(LIT, false);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BurnableCampfireBlockEntity(pos, state, tier);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.getBlockEntity(pos) instanceof BurnableCampfireBlockEntity campfire) {
            ItemStack stack = player.getItemInHand(hand);

            if (!level.isClientSide) {
                if (stack.is(Items.FLINT_AND_STEEL)) {
                    campfire.kindle();
                    stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
                    return InteractionResult.SUCCESS;
                }

                if (hit.getDirection() == Direction.UP && campfire.fuels.isItemValid(stack)) {
                    campfire.addFuel(stack, false);
                    return InteractionResult.SUCCESS;
                }

                Optional<CampfireCookingRecipe> recipe = campfire.getCookableRecipe(stack);
                if (recipe.isPresent() && campfire.placeFood(player, stack, recipe.get().getCookingTime())) {
                    player.awardStat(Stats.INTERACT_WITH_CAMPFIRE);
                    return InteractionResult.SUCCESS;
                }
            } else {
                if (stack.is(Items.FLINT_AND_STEEL)
                        || (hit.getDirection() == Direction.UP && campfire.fuels.isItemValid(stack))
                        || campfire.getCookableRecipe(stack).isPresent()) {
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return super.use(state, level, pos, player, hand, hit);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (blockEntityType == AllBlockEntity.burnable_campfire.get()) {
            if (level.isClientSide) {
                return state.getValue(LIT)
                        ? createTickerHelper(blockEntityType, AllBlockEntity.burnable_campfire.get(), BurnableCampfireBlockEntity::particleTick)
                        : null;
            } else {
                return state.getValue(LIT)
                        ? createTickerHelper(blockEntityType, AllBlockEntity.burnable_campfire.get(), BurnableCampfireBlockEntity::cookTick)
                        : createTickerHelper(blockEntityType, AllBlockEntity.burnable_campfire.get(), BurnableCampfireBlockEntity::cooldownTick);
            }
        }
        return null;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            if (level.getBlockEntity(pos) instanceof BurnableCampfireBlockEntity blockentity) {
                blockentity.dropContents();
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }
}
