package com.minecart.central_heater.block.misc;

import com.minecart.central_heater.block_entity.AllBlockEntity;
import com.minecart.central_heater.block_entity.misc.BurnableCampfireBlockEntity;
import com.minecart.central_heater.heat.api.ThermalMaterial;
import com.minecart.central_heater.heat.api.ThermalMaterialBehavior;
import com.minecart.central_heater.heat.api.ThermalOverrideProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class BurnableCampfireBlock extends CampfireBlock implements ThermalOverrideProvider {

    public BurnableCampfireBlock(boolean spawnParticles, int fireDamage, Properties properties) {
        super(spawnParticles, fireDamage, properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(LIT, Boolean.valueOf(false))
                .setValue(SIGNAL_FIRE, Boolean.valueOf(false))
                .setValue(WATERLOGGED, Boolean.valueOf(false))
                .setValue(FACING, Direction.NORTH));
    }

    @Override
    public @Nullable ThermalMaterial getThermalMaterialOverride(BlockState state) {
        if (state.getValue(WATERLOGGED)) {
            return ThermalMaterial.WATER;
        }
        return state.getValue(LIT) ? ThermalMaterial.SCORCHED : ThermalMaterial.BURNT_WOOD;
    }

    @Override
    public @Nullable ThermalMaterialBehavior getThermalBehaviorOverride(BlockState state) {
        return null;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        if (state != null) {
            return state.setValue(LIT, false);
        }
        return null;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BurnableCampfireBlockEntity(pos, state, fireDamage == 1 ? 0 : 1);
    }

    // Handles your @Inject for useItemOn
    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof BurnableCampfireBlockEntity campfireblockentity) {
            ItemStack itemStack = player.getItemInHand(hand);

            if (itemStack.is(Items.FLINT_AND_STEEL)) {
                campfireblockentity.kindle();
                if(level instanceof ServerLevel serverLevel)
                    stack.hurtAndBreak(1, serverLevel, player, item -> {});
                return ItemInteractionResult.SUCCESS;
            }

            // 2. Adding Fuel
            if (hitResult.getDirection().equals(Direction.UP) && campfireblockentity.fuels.isItemValid(stack)) {
                campfireblockentity.addFuel(stack, false);
                return ItemInteractionResult.SUCCESS;
            }

            // 3. Adding Food
            Optional<RecipeHolder<CampfireCookingRecipe>> optional = campfireblockentity.getCookableRecipe(itemStack);
            if (optional.isPresent() && campfireblockentity.placeFood(player, itemStack, optional.get().value().getCookingTime())) {
                player.awardStat(Stats.INTERACT_WITH_CAMPFIRE);
                return ItemInteractionResult.SUCCESS;
            }
        }

        // If none of our custom logic fires, fall back to normal block interaction
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (blockEntityType == AllBlockEntity.BURNABLE_CAMPFIRE.get()) {
            if (level.isClientSide) {
                return state.getValue(LIT)
                        ? createTickerHelper(blockEntityType, AllBlockEntity.BURNABLE_CAMPFIRE.get(), BurnableCampfireBlockEntity::particleTick)
                        : null;
            } else {
                return state.getValue(LIT)
                        ? createTickerHelper(blockEntityType, AllBlockEntity.BURNABLE_CAMPFIRE.get(), BurnableCampfireBlockEntity::cookTick)
                        : createTickerHelper(blockEntityType, AllBlockEntity.BURNABLE_CAMPFIRE.get(), BurnableCampfireBlockEntity::cooldownTick);
            }
        }
        return null; // Fallback in case of mismatched block entity type
    }

    // Handles your @Inject for onRemove
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
