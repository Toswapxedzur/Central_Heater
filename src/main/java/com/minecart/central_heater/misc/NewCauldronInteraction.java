package com.minecart.central_heater.misc;

import com.minecart.central_heater.block_entity.cauldron.AbstractCauldronBlockEntity;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.HashMap;
import java.util.Map;

public interface NewCauldronInteraction {

    // Single map for all Item -> Action mappings
    Map<Item, NewCauldronInteraction> INTERACTIONS = new HashMap<>();

    int WATER_AMOUNT_PER_LEVEL = 250;

    // Functional Method
    ItemInteractionResult interact(Level level, AbstractCauldronBlockEntity entity, Player player, InteractionHand hand, ItemStack stack);

    // --- Interaction Logic ---

    // 1. Fill Cauldron (Water Potion -> Water Fluid)
    // Works if tank is empty OR has water with space remaining
    NewCauldronInteraction FILL_WATER = (level, entity, player, hand, stack) -> {
        PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);
        if (contents != null && contents.is(Potions.WATER)) {
            FluidStack current = entity.getFluidTank().getFluidInTank(0);

            // Allow if Empty OR (Is Water AND has space)
            boolean canFill = current.isEmpty() || (current.is(FluidTags.WATER) && current.getAmount() + WATER_AMOUNT_PER_LEVEL <= entity.getFluidTank().getTankCapacity(0));

            if (canFill) {
                if (!level.isClientSide) {
                    Item item = stack.getItem();
                    player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                    player.awardStat(Stats.USE_CAULDRON);
                    player.awardStat(Stats.ITEM_USED.get(item));

                    entity.getFluidTank().fill(new FluidStack(Fluids.WATER, WATER_AMOUNT_PER_LEVEL), IFluidHandler.FluidAction.EXECUTE);

                    level.playSound(null, entity.getBlockPos(), SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                    level.gameEvent(null, GameEvent.FLUID_PLACE, entity.getBlockPos());
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    };

    // 2. Fill Bottle (Water Fluid -> Water Potion)
    // Requires Water inside
    NewCauldronInteraction FILL_BOTTLE = (level, entity, player, hand, stack) -> {
        FluidStack current = entity.getFluidTank().getFluidInTank(0);

        if (current.is(FluidTags.WATER) && current.getAmount() >= WATER_AMOUNT_PER_LEVEL) {
            if (!level.isClientSide) {
                Item item = stack.getItem();
                player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, PotionContents.createItemStack(Items.POTION, Potions.WATER)));
                player.awardStat(Stats.USE_CAULDRON);
                player.awardStat(Stats.ITEM_USED.get(item));

                drainWater(level, entity);
                level.playSound(null, entity.getBlockPos(), SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    };

    // 3. Wash Dyed Items
    NewCauldronInteraction WASH_DYE = (level, entity, player, hand, stack) -> {
        if (!stack.is(ItemTags.DYEABLE) || !stack.has(DataComponents.DYED_COLOR)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        return tryWash(level, entity, player, () -> {
            stack.remove(DataComponents.DYED_COLOR);
            player.awardStat(Stats.CLEAN_ARMOR);
        });
    };

    // 4. Wash Banners
    NewCauldronInteraction WASH_BANNER = (level, entity, player, hand, stack) -> {
        BannerPatternLayers layers = stack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
        if (layers.layers().isEmpty()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        return tryWash(level, entity, player, () -> {
            ItemStack copy = stack.copyWithCount(1);
            copy.set(DataComponents.BANNER_PATTERNS, layers.removeLast());
            player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, copy, false));
            player.awardStat(Stats.CLEAN_BANNER);
        });
    };

    // 5. Wash Shulker Boxes
    NewCauldronInteraction WASH_SHULKER = (level, entity, player, hand, stack) -> {
        Block block = Block.byItem(stack.getItem());
        if (!(block instanceof ShulkerBoxBlock)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        return tryWash(level, entity, player, () -> {
            ItemStack cleanBox = stack.transmuteCopy(Blocks.SHULKER_BOX, 1);
            player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, cleanBox, false));
            player.awardStat(Stats.CLEAN_SHULKER_BOX);
        });
    };

    // --- Helpers ---

    static ItemInteractionResult tryWash(Level level, AbstractCauldronBlockEntity entity, Player player, Runnable action) {
        FluidStack current = entity.getFluidTank().getFluidInTank(0);
        if (current.is(FluidTags.WATER) && current.getAmount() >= WATER_AMOUNT_PER_LEVEL) {
            if (!level.isClientSide) {
                action.run();
                drainWater(level, entity);
                level.playSound(null, entity.getBlockPos(), SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
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
        INTERACTIONS.put(Items.WOLF_ARMOR, WASH_DYE);

        for (Item item : BuiltInRegistries.ITEM) {
            if (item instanceof BannerItem) {
                INTERACTIONS.put(item, WASH_BANNER);
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