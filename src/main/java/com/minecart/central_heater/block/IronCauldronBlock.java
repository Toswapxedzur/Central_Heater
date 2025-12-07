package com.minecart.central_heater.block;

import com.minecart.central_heater.AllBlockEntity;
import com.minecart.central_heater.block_entity.pot.AbstractPotBlockEntity;
import com.minecart.central_heater.block_entity.pot.CauldronBlockEntity;
import com.minecart.central_heater.block_entity.pot.GoldenCauldronBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class IronCauldronBlock extends PotBlock{
    public static final MapCodec<IronCauldronBlock> CODEC = simpleCodec(IronCauldronBlock::new);

    private static final VoxelShape INSIDE = box((double)2.0F, (double)4.0F, (double)2.0F, (double)14.0F, (double)16.0F, (double)14.0F);
    private static final VoxelShape SHAPE = Shapes.join(Shapes.block(), Shapes.or(box((double)0.0F, (double)0.0F, (double)4.0F, (double)16.0F, (double)3.0F, (double)12.0F), new VoxelShape[]{box((double)4.0F, (double)0.0F, (double)0.0F, (double)12.0F, (double)3.0F, (double)16.0F), box((double)2.0F, (double)0.0F, (double)2.0F, (double)14.0F, (double)3.0F, (double)14.0F), INSIDE}), BooleanOp.ONLY_FIRST);

    public IronCauldronBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CauldronBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if(level.isClientSide){
            return createTickerHelper(blockEntityType, AllBlockEntity.cauldron.get(), AbstractPotBlockEntity::clientTick);
        }else{
            return createTickerHelper(blockEntityType, AllBlockEntity.cauldron.get(), AbstractPotBlockEntity::serverTick);
        }
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return SHAPE;
    }
}
