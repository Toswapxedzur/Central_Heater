package com.minecart.central_heater.block.cauldron;

import com.minecart.central_heater.block_entity.cauldron.AbstractCauldronBlockEntity;
import com.minecart.central_heater.misc.NewCauldronInteraction;
import com.minecart.central_heater.recipe.AllRecipe;
import com.minecart.central_heater.recipe.recipe_types.SmolderingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
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
import net.minecraftforge.fluids.FluidUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public abstract class CauldronBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL;

    protected CauldronBlock(Properties properties) {
        super(properties.noOcclusion().lightLevel(state -> state.getValue(BlockStateProperties.LEVEL)));
        this.registerDefaultState(getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(LEVEL, Integer.valueOf(0)));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public boolean isPathfindable(BlockState p_60475_, BlockGetter p_60476_, BlockPos p_60477_, PathComputationType p_60478_) {
        return false;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
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
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(level.isClientSide)
            return InteractionResult.SUCCESS;

        if(level.getBlockEntity(pos) instanceof AbstractCauldronBlockEntity entity && !entity.isRemoved()){
            ItemStack stack = player.getItemInHand(hand);

            // Logic from useWithoutItem (Empty Hand)
            if (stack.isEmpty()) {
                ItemStack extract = entity.getContainer().extractItem(false);

                if (extract.isEmpty()) {
                    return InteractionResult.PASS;
                }

                for (SmolderingRecipe recipe : level.getRecipeManager().getAllRecipesFor(AllRecipe.SMOLDERING.get())) {
                    if (recipe.getResults().stream().anyMatch(i -> ItemStack.isSameItem(i, extract))) {
                        player.awardRecipes(Collections.singleton(recipe));
                        break;
                    }
                }

                player.setItemInHand(hand, extract);
                return InteractionResult.SUCCESS;
            }
            // Logic from useItemOn (Item in Hand)
            else {
                if (FluidUtil.interactWithFluidHandler(player, hand, entity.getFluidTank())) {
                    return InteractionResult.SUCCESS;
                }

                NewCauldronInteraction interaction = NewCauldronInteraction.INTERACTIONS.get(stack.getItem());
                if (interaction != null) {
                    InteractionResult result = interaction.interact(level, entity, player, hand, stack);
                    if (result.consumesAction()) {
                        return result;
                    }
                }

                ItemStack remainder = entity.getContainer().insertItem(stack, true);
                if (remainder.getCount() < stack.getCount()) {
                    ItemStack actualRemainder = entity.getContainer().insertItem(stack, false);
                    player.setItemInHand(hand, actualRemainder);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if(!state.is(newState.getBlock())) {
            if(level.isClientSide)
                return;
            if (level.getBlockEntity(pos) instanceof AbstractCauldronBlockEntity entity)
                entity.drop();
            super.onRemove(state, level, pos, newState, movedByPiston);
            level.updateNeighbourForOutputSignal(pos, this);
        }
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
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