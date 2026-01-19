package com.minecart.central_heater.mixin;

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
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(CampfireBlock.class)
public abstract class CampFireBlockMixin extends BaseEntityBlock {
    private CampFireBlockMixin(Properties properties) {
        super(properties);
    }

    // Forces the campfire to NOT be lit by default during construction
    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/lang/Boolean;valueOf(Z)Ljava/lang/Boolean;", ordinal = 0))
    private Boolean central_heater$defaultLit(boolean value){
        return Boolean.FALSE;
    }

    // Forces the campfire to NOT be lit when placed
    @Redirect(method = "getStateForPlacement", at = @At(value = "INVOKE", target = "Ljava/lang/Boolean;valueOf(Z)Ljava/lang/Boolean;", ordinal = 2))
    private Boolean central_heater$placementLit(boolean value){
        return Boolean.FALSE;
    }

    @Overwrite
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BurnableCampfireBlockEntity(pos, state);
    }

    /**
     * Ported from 1.21 useItemOn to 1.20.1 use.
     */
    @Overwrite
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof BurnableCampfireBlockEntity campfireblockentity) {
            ItemStack itemStack = player.getItemInHand(hand);

            // 1.20.1 Recipe lookup (no RecipeHolder)
            Optional<CampfireCookingRecipe> optional = campfireblockentity.getCookableRecipe(itemStack);

            if (!level.isClientSide) {
                // Handle Ignition
                if (itemStack.is(Items.FLINT_AND_STEEL)) {
                    campfireblockentity.kindle();
                    itemStack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(hand));
                    return InteractionResult.SUCCESS;
                }

                // Handle Fueling
                if (hitResult.getDirection().equals(Direction.UP) && campfireblockentity.fuels.isItemValid(itemStack)) {
                    campfireblockentity.addFuel(itemStack, false);
                    return InteractionResult.SUCCESS;
                }

                // Handle Cooking
                if (optional.isPresent()) {
                    if (campfireblockentity.placeFood(player, itemStack, optional.get().getCookingTime())) {
                        player.awardStat(Stats.INTERACT_WITH_CAMPFIRE);
                        return InteractionResult.SUCCESS;
                    }
                }
            }

            // If it's a valid recipe or fuel, prevent further interaction even on client
            if (optional.isPresent() || campfireblockentity.fuels.isItemValid(itemStack)) {
                return InteractionResult.CONSUME;
            }
        }

        return InteractionResult.PASS;
    }

    @Overwrite
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide) {
            return state.getValue(CampfireBlock.LIT) ?
                    createTickerHelper(blockEntityType, AllBlockEntity.burnable_campfire.get(), BurnableCampfireBlockEntity::particleTick) : null;
        } else {
            return state.getValue(CampfireBlock.LIT)
                    ? createTickerHelper(blockEntityType, AllBlockEntity.burnable_campfire.get(), BurnableCampfireBlockEntity::cookTick)
                    : createTickerHelper(blockEntityType, AllBlockEntity.burnable_campfire.get(), BurnableCampfireBlockEntity::cooldownTick);
        }
    }

    @Overwrite
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockentity = level.getBlockEntity(pos);
            if (blockentity instanceof BurnableCampfireBlockEntity burnable) {
                burnable.dropContents();
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }
}