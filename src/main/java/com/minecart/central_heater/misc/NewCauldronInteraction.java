package com.minecart.central_heater.misc;

import com.minecart.central_heater.block_entity.cauldron.AbstractCauldronBlockEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public interface NewCauldronInteraction {

    // Single map for all Item -> Action mappings
    Map<Item, NewCauldronInteraction> INTERACTIONS = new HashMap<>();

    int WATER_AMOUNT_PER_LEVEL = 250;

    // Functional Method
    InteractionResult interact(Level level, AbstractCauldronBlockEntity entity, Player player, InteractionHand hand, ItemStack stack);

    // --- Interaction Logic ---

    // 1. Fill Cauldron (Water Potion -> Water Fluid)
    NewCauldronInteraction FILL_WATER = (level, entity, player, hand, stack) -> {
        // 1.20.1: Use PotionUtils to check potion type
        if (PotionUtils.getPotion(stack) == Potions.WATER) {
            FluidStack current = entity.getFluidTank().getFluidInTank(0);

            // Allow if Empty OR (Is Water AND has space)
            // Note: Use getFluid().is(FluidTags.WATER) for tag compatibility or equality check
            boolean isWater = current.isEmpty() || current.getFluid() == Fluids.WATER;
            boolean hasSpace = current.getAmount() + WATER_AMOUNT_PER_LEVEL <= entity.getFluidTank().getTankCapacity(0);

            if (isWater && hasSpace) {
                if (!level.isClientSide) {
                    Item item = stack.getItem();
                    player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                    player.awardStat(Stats.USE_CAULDRON);
                    player.awardStat(Stats.ITEM_USED.get(item));

                    entity.getFluidTank().fill(new FluidStack(Fluids.WATER, WATER_AMOUNT_PER_LEVEL), IFluidHandler.FluidAction.EXECUTE);

                    level.playSound(null, entity.getBlockPos(), SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                    level.gameEvent(null, GameEvent.FLUID_PLACE, entity.getBlockPos());
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return InteractionResult.PASS;
    };

    // 2. Fill Bottle (Water Fluid -> Water Potion)
    NewCauldronInteraction FILL_BOTTLE = (level, entity, player, hand, stack) -> {
        FluidStack current = entity.getFluidTank().getFluidInTank(0);

        if (current.getFluid() == Fluids.WATER && current.getAmount() >= WATER_AMOUNT_PER_LEVEL) {
            if (!level.isClientSide) {
                Item item = stack.getItem();
                ItemStack potion = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER);
                player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, potion));
                player.awardStat(Stats.USE_CAULDRON);
                player.awardStat(Stats.ITEM_USED.get(item));

                drainWater(level, entity);
                level.playSound(null, entity.getBlockPos(), SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    };

    // 3. Wash Dyed Items
    NewCauldronInteraction WASH_DYE = (level, entity, player, hand, stack) -> {
        if (!(stack.getItem() instanceof DyeableLeatherItem item)) {
            return InteractionResult.PASS;
        }
        if (!item.hasCustomColor(stack)) {
            return InteractionResult.PASS;
        }
        return tryWash(level, entity, player, () -> {
            item.clearColor(stack);
            player.awardStat(Stats.CLEAN_ARMOR);
        });
    };

    // 4. Wash Banners
    NewCauldronInteraction WASH_BANNER = (level, entity, player, hand, stack) -> {
        if (BannerBlockEntity.getPatternCount(stack) <= 0) {
            return InteractionResult.PASS;
        }
        return tryWash(level, entity, player, () -> {
            ItemStack copy = stack.copyWithCount(1);
            BannerBlockEntity.removeLastPattern(copy);
            player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, copy, false));
            player.awardStat(Stats.CLEAN_BANNER);
        });
    };

    // 5. Wash Shulker Boxes
    NewCauldronInteraction WASH_SHULKER = (level, entity, player, hand, stack) -> {
        Block block = Block.byItem(stack.getItem());
        if (!(block instanceof ShulkerBoxBlock)) {
            return InteractionResult.PASS;
        }
        return tryWash(level, entity, player, () -> {
            ItemStack cleanBox = new ItemStack(Blocks.SHULKER_BOX);
            if (stack.hasTag()) {
                cleanBox.setTag(stack.getTag().copy());
            }
            player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, cleanBox, false));
            player.awardStat(Stats.CLEAN_SHULKER_BOX);
        });
    };

    // --- Helpers ---

    static InteractionResult tryWash(Level level, AbstractCauldronBlockEntity entity, Player player, Runnable action) {
        FluidStack current = entity.getFluidTank().getFluidInTank(0);
        // Only wash if water
        if (current.getFluid() == Fluids.WATER && current.getAmount() >= WATER_AMOUNT_PER_LEVEL) {
            if (!level.isClientSide) {
                action.run();
                drainWater(level, entity);
                level.playSound(null, entity.getBlockPos(), SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    static void drainWater(Level level, AbstractCauldronBlockEntity entity) {
        entity.getFluidTank().drain(WATER_AMOUNT_PER_LEVEL, IFluidHandler.FluidAction.EXECUTE);
        level.gameEvent(null, GameEvent.FLUID_PICKUP, entity.getBlockPos());
    }

    // --- Bootstrapping ---

    static void bootStrap() {
        INTERACTIONS.put(Items.POTION, FILL_WATER);
        INTERACTIONS.put(Items.GLASS_BOTTLE, FILL_BOTTLE);

        INTERACTIONS.put(Items.LEATHER_BOOTS, WASH_DYE);
        INTERACTIONS.put(Items.LEATHER_LEGGINGS, WASH_DYE);
        INTERACTIONS.put(Items.LEATHER_CHESTPLATE, WASH_DYE);
        INTERACTIONS.put(Items.LEATHER_HELMET, WASH_DYE);
        INTERACTIONS.put(Items.LEATHER_HORSE_ARMOR, WASH_DYE);
        // Wolf Armor removed (1.20.5+)

        for (Item item : ForgeRegistries.ITEMS) {
            if (item instanceof BannerItem) {
                INTERACTIONS.put(item, WASH_BANNER);
            }
            else if (item instanceof DyeableLeatherItem) {
                INTERACTIONS.put(item, WASH_DYE);
            }

            Block block = Block.byItem(item);
            if (block instanceof ShulkerBoxBlock shulkerBlock) {
                if (shulkerBlock.getColor() != null) {
                    INTERACTIONS.put(item, WASH_SHULKER);
                }
            }
        }
    }
}