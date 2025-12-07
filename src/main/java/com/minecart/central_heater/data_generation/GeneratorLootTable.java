package com.minecart.central_heater.data_generation;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.AllDataComponents;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;

import java.util.Set;

public class GeneratorLootTable extends BlockLootSubProvider {
    public GeneratorLootTable(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    public void generate() {
        CopyComponentsFunction.Builder sturdyTankCopyFunction = CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY)
                .include(DataComponents.CUSTOM_NAME)
                .include(AllDataComponents.SIMPLE_FLUID_CONTENT.get());


        LootTable.Builder sturdyTank = createSingleItemTable(AllBlockItem.sturdy_tank_item).apply(sturdyTankCopyFunction);

        add(AllBlockItem.sturdy_tank.get(), sturdyTank);

        for(Block block : getKnownBlocks()){
            if(!this.map.containsKey(block.getLootTable())){
                if(block instanceof SlabBlock slabBlock)
                    add(slabBlock, b -> createSlabItemTable(slabBlock));
                else
                    dropSelf(block);
            }
        }
    }

    @Override
    public Iterable<Block> getKnownBlocks() {
        return AllBlockItem.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }

}
