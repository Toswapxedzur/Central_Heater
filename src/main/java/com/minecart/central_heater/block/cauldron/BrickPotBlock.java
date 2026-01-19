package com.minecart.central_heater.block.cauldron;

import com.minecart.central_heater.block_entity.AllBlockEntity;
import com.minecart.central_heater.block_entity.cauldron.AbstractCauldronBlockEntity;
import com.minecart.central_heater.block_entity.cauldron.MudBrickPotBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class BrickPotBlock extends CauldronBlock {

    public static final VoxelShape SHAPE = Shapes.join(Shapes.block(),
            Shapes.or(box(2f, 4f, 2f, 14f, 16f, 14f), box(4f, 0f, 0f, 16f, 2f, 12f), box(0f, 0f, 4f, 16f, 2f, 12f)),
            BooleanOp.ONLY_FIRST);

    public BrickPotBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MudBrickPotBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if(level.isClientSide){
            return createTickerHelper(blockEntityType, AllBlockEntity.pot.get(), AbstractCauldronBlockEntity::clientTick);
        }else{
            return createTickerHelper(blockEntityType, AllBlockEntity.pot.get(), AbstractCauldronBlockEntity::serverTick);
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return SHAPE;
    }
}