package com.minecart.central_heater.block.cauldron;

import com.minecart.central_heater.block_entity.cauldron.AbstractCauldronBlockEntity;
import com.minecart.central_heater.misc.NewCauldronInteraction;
import com.minecart.central_heater.recipe.AllRecipe;
import com.minecart.central_heater.recipe.recipe_types.SmolderingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class CauldronBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL;

    protected CauldronBlock(Properties properties) {
        super(properties.noOcclusion().lightLevel(state -> state.getValue(BlockStateProperties.LEVEL)));
        this.registerDefaultState(getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(LEVEL, Integer.valueOf(0)));
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LEVEL);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(level.isClientSide)
            return ItemInteractionResult.SUCCESS;
        if(level.getBlockEntity(pos) instanceof AbstractCauldronBlockEntity entity && !entity.isRemoved()){
            if (FluidUtil.interactWithFluidHandler(player, hand, entity.getFluidTank())) {
                return ItemInteractionResult.SUCCESS;
            }
            NewCauldronInteraction interaction = NewCauldronInteraction.INTERACTIONS.get(stack.getItem());
            if (interaction != null) {
                ItemInteractionResult result = interaction.interact(level, entity, player, hand, stack);
                if (result.consumesAction()) {
                    return result;
                }
            }
            if (!stack.isEmpty()) {
                ItemStack remainder = entity.getContainer().insertItem(stack, true);
                if (remainder.getCount() < stack.getCount()) {
                    ItemStack actualRemainder = entity.getContainer().insertItem(stack, false);
                    player.setItemInHand(hand, actualRemainder);
                    return ItemInteractionResult.SUCCESS;
                }
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide) return InteractionResult.SUCCESS;

        if (!player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
            return InteractionResult.PASS;
        }

        if (level.getBlockEntity(pos) instanceof AbstractCauldronBlockEntity entity && !entity.isRemoved()) {
            ItemStack extract = entity.getContainer().extractItem(false);

            if (extract.isEmpty()) {
                return InteractionResult.PASS;
            }

            for (RecipeHolder<SmolderingRecipe> recipe : level.getRecipeManager().getAllRecipesFor(AllRecipe.SMOLDERING.get())) {
                if (recipe.value().getResults().stream().anyMatch(i -> ItemStack.isSameItem(i, extract))) {
                    player.triggerRecipeCrafted(recipe, List.of(extract));
                    break;
                }
            }

            player.setItemInHand(InteractionHand.MAIN_HAND, extract);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if(!oldState.is(this)) level.invalidateCapabilities(pos);
        super.onPlace(state, level, pos, oldState, movedByPiston);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if(!state.is(newState.getBlock())) {
            level.invalidateCapabilities(pos);
            if(level.isClientSide)
                return;
            if (level.getBlockEntity(pos) instanceof AbstractCauldronBlockEntity entity)
                entity.drop();
            super.onRemove(state, level, pos, newState, movedByPiston);
            level.updateNeighbourForOutputSignal(pos, this);
        }
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide && entity instanceof net.minecraft.world.entity.item.ItemEntity itemEntity) {
            if (level.getBlockEntity(pos) instanceof AbstractCauldronBlockEntity cauldron && !cauldron.isRemoved()) {

                ItemStack stack = itemEntity.getItem();

                ItemStack remainder = cauldron.getContainer().insertItem(stack, false);

                if (remainder.isEmpty()) {
                    itemEntity.discard();
                } else if (remainder.getCount() < stack.getCount()) {
                    itemEntity.setItem(remainder);
                }
            }
        }
        super.entityInside(state, level, pos, entity);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide && placer instanceof Player player) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof AbstractCauldronBlockEntity cauldron) {
                cauldron.setPlacer(player.getUUID());
            }
        }
    }
}
