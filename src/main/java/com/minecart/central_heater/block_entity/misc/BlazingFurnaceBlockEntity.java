package com.minecart.central_heater.block_entity.misc;

import com.google.common.collect.Maps;
import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.recipe.AllRecipe;
import com.minecart.central_heater.block_entity.AllBlockEntity;
import com.minecart.central_heater.user_interface.menu.BlazingFurnaceMenu;
import com.minecart.central_heater.misc.DataMapHook;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public class BlazingFurnaceBlockEntity extends AbstractFurnaceBlockEntity {
    private static volatile Map<Item, Integer> hauntFuelCache;

    public static void invalidateCache() {
        hauntFuelCache = null;
    }

    public static Map<Item, Integer> getHauntFuel() {
        Map<Item, Integer> map = hauntFuelCache;
        if (map != null) {
            return map;
        } else {
            Map<Item, Integer> newMap = Maps.newLinkedHashMap();
            add(newMap, Items.SOUL_SAND, 100);
            add(newMap, Items.SOUL_SOIL, 100);
            add(newMap, AllBlockItem.SCORCHED_DUST.get(), 100);
            add(newMap, AllBlockItem.SOUL_MIXTURE.get(), 300);
            add(newMap, AllBlockItem.SCORCHED_COAL.get(), 600);
            //for the one people on earth that is making addon to this mod, remember write mixin here to add your own fuel to the recipe book
            hauntFuelCache = newMap;
            return newMap;
        }
    }

    private static void add(Map<Item, Integer> map, ItemLike item, int time) {
        map.put(item.asItem(), time);
    }

    private static void add(Map<Item, Integer> map, TagKey<Item> tag, int time) {
        for (var holder : BuiltInRegistries.ITEM.getTagOrEmpty(tag)) {
            map.put(holder.value(), time);
        }
    }

    public static void invalidateHauntCache() {
        hauntFuelCache = null;
    }

    public BlazingFurnaceBlockEntity(BlockPos pos, BlockState blockState) {
        super(AllBlockEntity.blazing_furnace.get(), pos, blockState, AllRecipe.HAUNTING.get());
    }

    protected Component getDefaultName() {
        return Component.translatable("container.blazing_furnace");
    }

    protected AbstractContainerMenu createMenu(int id, Inventory player) {
        return new BlazingFurnaceMenu(id, player, this, this.dataAccess);
    }

    @Override
    protected int getBurnDuration(ItemStack fuel) {
        return fuel.isEmpty() ? 0 : DataMapHook.getNetherFuelBurnTime(fuel);
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        if (index == 2) {
            return false;
        } else if (index != 1) {
            return true;
        } else {
            ItemStack itemstack = (ItemStack)this.items.get(1);
            return DataMapHook.getNetherFuelBurnTime(stack) > 0;
        }
    }
}
