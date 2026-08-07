package com.minecart.central_heater.mixin;

import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.advancement.AllTrigger;
import com.minecart.central_heater.misc.DataMapHook;
import com.minecart.central_heater.recipe.AllRecipe;
import com.minecart.central_heater.recipe.recipe_input.BlockSmolderingRecipeInput;
import com.minecart.central_heater.recipe.recipe_types.BlockSmolderingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Debug(export = true)
@Mixin(FireBlock.class)
public abstract class FireBlockMixin extends BaseFireBlock {
    public FireBlockMixin(Properties properties, float fireDamage) {
        super(properties, fireDamage);
    }

    @Inject(
            method = "checkBurnOut",
            at = {
                    @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;removeBlock(Lnet/minecraft/core/BlockPos;Z)Z"),
                    @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z")
            },
            cancellable = true
    )
    private void onBurnBlock(Level level, BlockPos pos, int chance, RandomSource random, int age, Direction face, CallbackInfo ci) {
        if (level.isClientSide)
            return;

        BlockState oldState = level.getBlockState(pos);

        BlockSmolderingRecipeInput recipeInput = new BlockSmolderingRecipeInput(oldState, 1, true);

        // 2. Query the RecipeManager for a matching smoldering recipe where fireBurn is true
        Optional<RecipeHolder<BlockSmolderingRecipe>> optionalRecipe = level.getRecipeManager()
                .getRecipesFor(AllRecipe.BLOCK_SMOLDERING_RECIPE.get(), recipeInput, level)
                .stream()
                .filter(holder -> holder.value().isfireBurn())
                .findFirst();

        // 3. If a recipe exists, apply the recipe outputs
        if (optionalRecipe.isPresent()) {
            BlockSmolderingRecipe recipe = optionalRecipe.get().value();
            BlockState newState = recipe.getResultBlock();

            // Attempt to transfer the axis/rotation from the old block to the new block smoothly
            if (oldState.hasProperty(RotatedPillarBlock.AXIS)) {
                Direction.Axis axis = oldState.getValue(RotatedPillarBlock.AXIS);

                if (newState.hasProperty(BlockStateProperties.FACING)) {
                    Direction facing = switch (axis) {
                        case X -> Direction.EAST;
                        case Z -> Direction.SOUTH;
                        default -> Direction.UP;
                    };
                    newState = newState.setValue(BlockStateProperties.FACING, facing);
                } else if (newState.hasProperty(BlockStateProperties.AXIS)) {
                    newState = newState.setValue(BlockStateProperties.AXIS, axis);
                }
            }

            // Set the new Recipe Block
            level.setBlock(pos, newState, 3);

            // Drop the Recipe Items
            for (ItemStack stack : recipe.getItemOutputs()) {
                if (!stack.isEmpty()) {
                    double d0 = (double) pos.getX() + 0.5D;
                    double d1 = (double) pos.getY() + 0.5D;
                    double d2 = (double) pos.getZ() + 0.5D;
                    ItemEntity itemEntity = new ItemEntity(level, d0, d1, d2, stack.copy());
                    itemEntity.setDefaultPickUpDelay();
                    level.addFreshEntity(itemEntity);
                }
            }

            // Trigger advancements (same as old logic)
            ItemStack oldItem = oldState.getBlock().asItem().getDefaultInstance();
            if (level instanceof ServerLevel serverLevel) {
                Player nearestPlayer = serverLevel.getNearestPlayer(pos.getX(), pos.getY(), pos.getZ(), 10.0, false);
                if (nearestPlayer instanceof ServerPlayer player) {
                    AllTrigger.BURNT_OBJECT.get().trigger(player, oldItem);
                }
            }

            ci.cancel();
            return;
        }

        // --- 4. FALLBACK: If no recipe matches, run the standard Ash drop logic ---

        ItemStack oldItem = oldState.getBlock().asItem().getDefaultInstance();
        float dropChance = DataMapHook.getFireAshDropChance(oldItem);

        if (dropChance > 0) {
            if (level instanceof ServerLevel serverLevel) {
                Player nearestPlayer = serverLevel.getNearestPlayer(pos.getX(), pos.getY(), pos.getZ(), 10.0, false);
                if (nearestPlayer instanceof ServerPlayer player) {
                    AllTrigger.BURNT_OBJECT.get().trigger(player, oldItem);
                }
            }

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

    @Expression("0.2 + ? * 0.03")
    @ModifyExpressionValue(method = "tick", at = @At(value = "MIXINEXTRAS:EXPRESSION"))
    float modify(float original){
        return original/5;
    }
}
