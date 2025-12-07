package com.minecart.central_heater.util;

import com.google.gson.stream.MalformedJsonException;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemUtil {
    private ItemUtil(){

    }

    public static boolean isFlatItem(ItemStack stack){
        return !VirtualLevel.getItemRenderer().getItemModelShaper().getItemModel(stack).isGui3d();
    }

    public static boolean isFlatItem(Item item){
        return VirtualLevel.getItemRenderer().getItemModelShaper().getItemModel(item).isGui3d();
    }}
