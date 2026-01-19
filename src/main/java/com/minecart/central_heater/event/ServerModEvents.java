package com.minecart.central_heater.event;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.CentralHeater;
import com.minecart.central_heater.advancement.AllTrigger;
import com.minecart.central_heater.misc.Alltags;
import com.minecart.central_heater.misc.DataMapHook;
import com.minecart.central_heater.misc.NewCauldronInteraction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;
import java.util.Set;

/**
 * Handles mod-specific lifecycle events on the Mod Bus.
 */
@Mod.EventBusSubscriber(modid = CentralHeater.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ServerModEvents {

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            registerCompostables();
            DataMapHook.init();
            NewCauldronInteraction.bootStrap();
            AllTrigger.register();
        });
    }

    private static void registerCompostables() {
        addCompost(Alltags.Items.OVERBURNT, 0.3f);
    }

    private static void addCompost(ItemLike item, float chance) {
        ComposterBlock.COMPOSTABLES.put(item.asItem(), chance);
    }

    private static void addCompost(TagKey<Item> tag, float chance) {
        for(Item item : ForgeRegistries.ITEMS.tags().getTag(tag)){
            addCompost(item, chance);
        }
    }
}