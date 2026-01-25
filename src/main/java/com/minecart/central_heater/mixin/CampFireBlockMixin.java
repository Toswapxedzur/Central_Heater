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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(CampfireBlock.class)
public abstract class CampFireBlockMixin extends BaseEntityBlock {

    private CampFireBlockMixin(Properties properties) {
        super(properties);
    }

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/lang/Boolean;valueOf(Z)Ljava/lang/Boolean;", ordinal = 0))
    private Boolean valueOf(boolean value){
        return Boolean.FALSE;
    }

    @Redirect(method = "getStateForPlacement", at = @At(value = "INVOKE", target = "Ljava/lang/Boolean;valueOf(Z)Ljava/lang/Boolean;", ordinal = 2))
    private Boolean valueOf2(boolean value){
        return Boolean.FALSE;
    }

    @Inject(method = "newBlockEntity", at = @At("HEAD"), cancellable = true)
    public void onNewBlockEntity(BlockPos pos, BlockState state, CallbackInfoReturnable<BlockEntity> cir) {
        cir.setReturnValue(new BurnableCampfireBlockEntity(pos, state));
    }

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    public void onUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        if (level.getBlockEntity(pos) instanceof BurnableCampfireBlockEntity campfire) {
            ItemStack stack = player.getItemInHand(hand);
            // 1.20.1: Recipe is just CampfireCookingRecipe, not RecipeHolder
            Optional<CampfireCookingRecipe> recipe = campfire.getCookableRecipe(stack);

            if (!level.isClientSide) {
                // 1. Flint and Steel
                if (stack.is(Items.FLINT_AND_STEEL)) {
                    campfire.kindle();
                    stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(hand));
                    cir.setReturnValue(InteractionResult.SUCCESS);
                    return;
                }

                // 2. Fuel
                if (hit.getDirection() == Direction.UP && campfire.fuels.isItemValid(stack)) {
                    campfire.addFuel(stack, false);
                    cir.setReturnValue(InteractionResult.SUCCESS);
                    return;
                }

                // 3. Food
                if (recipe.isPresent() && campfire.placeFood(player, stack, recipe.get().getCookingTime())) {
                    player.awardStat(Stats.INTERACT_WITH_CAMPFIRE);
                    cir.setReturnValue(InteractionResult.SUCCESS);
                    return;
                }
            }

            // Compatibility: Return PASS so we don't block other mods unless we acted
            cir.setReturnValue(InteractionResult.PASS);
        }
    }

    // REPLACED: getTicker -> @Inject
    @Inject(method = "getTicker", at = @At("HEAD"), cancellable = true)
    public <T extends BlockEntity> void onGetTicker(Level level, BlockState state, BlockEntityType<T> type, CallbackInfoReturnable<BlockEntityTicker<T>> cir) {
        if (type == AllBlockEntity.burnable_campfire.get()) {
            if (level.isClientSide) {
                cir.setReturnValue(state.getValue(CampfireBlock.LIT)
                        ? createTickerHelper(type, AllBlockEntity.burnable_campfire.get(), BurnableCampfireBlockEntity::particleTick)
                        : null);
            } else {
                cir.setReturnValue(state.getValue(CampfireBlock.LIT)
                        ? createTickerHelper(type, AllBlockEntity.burnable_campfire.get(), BurnableCampfireBlockEntity::cookTick)
                        : createTickerHelper(type, AllBlockEntity.burnable_campfire.get(), BurnableCampfireBlockEntity::cooldownTick));
            }
        }
    }

    // REPLACED: onRemove -> @Inject
    @Inject(method = "onRemove", at = @At("HEAD"))
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving, CallbackInfo ci) {
        if (!state.is(newState.getBlock())) {
            if (level.getBlockEntity(pos) instanceof BurnableCampfireBlockEntity burnable) {
                burnable.dropContents();
            }
        }
    }
}