package com.minecart.central_heater.data_generation;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.Central_heater;
import com.minecart.central_heater.util.Alltags;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ProviderAdvancements extends AdvancementProvider {
    public ProviderAdvancements(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
        super(output, registries, existingFileHelper, List.of(new GeneratorAdvancement()));
    }

    public static final class GeneratorAdvancement implements AdvancementGenerator{

        @Override
        public void generate(HolderLookup.Provider provider, Consumer<AdvancementHolder> consumer, ExistingFileHelper existingFileHelper) {
            AdvancementHolder firstHeater = Advancement.Builder.advancement().display(new ItemStack(AllBlockItem.brick_stove.asItem()), Component.translatable("advancements.heaters.first_heater.title"),
                    Component.translatable("advancements.heaters.first_heater.description"), Central_heater.modLoc("textures/gui/advancements/background/bricks.png"),
                    AdvancementType.TASK, true, true, false).addCriterion("placed_brick_stove",
                    ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(AnyOfCondition.anyOf(LootItemBlockStatePropertyCondition.hasBlockStateProperties(AllBlockItem.brick_stove.get()),
                            LootItemBlockStatePropertyCondition.hasBlockStateProperties(AllBlockItem.mud_brick_stove.get()))))
                    .save(consumer, Central_heater.modLoc("heater/first_heater"), existingFileHelper);

            AdvancementHolder secondHeater = Advancement.Builder.advancement().parent(firstHeater).display(new ItemStack(AllBlockItem.stone_stove.asItem()), Component.translatable("advancements.heaters.second_heater.title"),
                            Component.translatable("advancements.heaters.second_heater.description"), null,
                            AdvancementType.TASK, true, true, false).addCriterion("placed_stone_stove",
                            ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(AnyOfCondition.anyOf(LootItemBlockStatePropertyCondition.hasBlockStateProperties(AllBlockItem.stone_stove.get()),
                                    LootItemBlockStatePropertyCondition.hasBlockStateProperties(AllBlockItem.deepslate_stove.get()))))
                    .save(consumer, Central_heater.modLoc("heater/second_heater"), existingFileHelper);

            AdvancementHolder thirdHeater = Advancement.Builder.advancement().parent(secondHeater).display(new ItemStack(AllBlockItem.blackstone_stove.asItem()), Component.translatable("advancements.heaters.third_heater.title"),
                            Component.translatable("advancements.heaters.third_heater.description"), null,
                            AdvancementType.TASK, true, true, false).addCriterion("placed_nether_bricks_stove",
                            ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(AnyOfCondition.anyOf(LootItemBlockStatePropertyCondition.hasBlockStateProperties(AllBlockItem.nether_brick_stove.get()),
                                    LootItemBlockStatePropertyCondition.hasBlockStateProperties(AllBlockItem.red_nether_brick_stove.get()), LootItemBlockStatePropertyCondition.hasBlockStateProperties(AllBlockItem.blackstone_stove.get()))))
                    .save(consumer, Central_heater.modLoc("heater/third_heater"), existingFileHelper);

            AdvancementHolder firstPot = Advancement.Builder.advancement().parent(firstHeater).display(new ItemStack(AllBlockItem.mud_brick_pot.asItem()), Component.translatable("advancements.heaters.first_pot.title"),
                            Component.translatable("advancements.heaters.first_pot.description"), null,
                            AdvancementType.TASK, true, true, false).addCriterion("placed_mud_brick_pot",
                            ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(AllBlockItem.mud_brick_pot.get()))
                    .save(consumer, Central_heater.modLoc("heater/first_pot"), existingFileHelper);

            AdvancementHolder secondPot = Advancement.Builder.advancement().parent(firstPot).display(new ItemStack(AllBlockItem.deepslate_pot.asItem()), Component.translatable("advancements.heaters.second_pot.title"),
                            Component.translatable("advancements.heaters.second_pot.description"), null,
                            AdvancementType.TASK, true, true, false).addCriterion("placed_stone_pot",
                            ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(AnyOfCondition.anyOf(LootItemBlockStatePropertyCondition.hasBlockStateProperties(AllBlockItem.stone_pot.get()),
                                    LootItemBlockStatePropertyCondition.hasBlockStateProperties(AllBlockItem.deepslate_pot.get()))))
                    .save(consumer, Central_heater.modLoc("heater/second_pot"), existingFileHelper);

            AdvancementHolder firstCauldron = Advancement.Builder.advancement().parent(secondPot).display(new ItemStack(AllBlockItem.iron_cauldron.asItem()), Component.translatable("advancements.heaters.first_cauldron.title"),
                            Component.translatable("advancements.heaters.first_cauldron.description"), null,
                            AdvancementType.TASK, true, true, false).addCriterion("placed_cauldron_pot",
                            ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(AllBlockItem.iron_cauldron.get()))
                    .save(consumer, Central_heater.modLoc("heater/first_cauldron"), existingFileHelper);

            AdvancementHolder secondCauldron = Advancement.Builder.advancement().parent(firstCauldron).display(new ItemStack(AllBlockItem.golden_cauldron.asItem()), Component.translatable("advancements.heaters.second_cauldron.title"),
                            Component.translatable("advancements.heaters.second_cauldron.description"), null,
                            AdvancementType.TASK, true, true, false).addCriterion("placed_golden_cauldron_pot",
                            ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(AllBlockItem.golden_cauldron.get()))
                    .save(consumer, Central_heater.modLoc("heater/second_cauldron"), existingFileHelper);

            AdvancementHolder sturdyBrick = Advancement.Builder.advancement().parent(firstCauldron).display(new ItemStack(AllBlockItem.sturdy_brick.asItem()), Component.translatable("advancements.heaters.sturdy_brick.title"),
                            Component.translatable("advancements.heaters.sturdy_brick.description"), null,
                            AdvancementType.TASK, true, true, false).addCriterion("has_sturdy_brick",
                            InventoryChangeTrigger.TriggerInstance.hasItems(AllBlockItem.sturdy_brick))
                    .save(consumer, Central_heater.modLoc("heater/sturdy_brick"), existingFileHelper);

            AdvancementHolder sturdyTank = Advancement.Builder.advancement().parent(sturdyBrick).display(new ItemStack(AllBlockItem.sturdy_tank.asItem()), Component.translatable("advancements.heaters.sturdy_tank.title"),
                            Component.translatable("advancements.heaters.sturdy_tank.description"), null,
                            AdvancementType.TASK, true, true, false).addCriterion("has_sturdy_tank",
                            InventoryChangeTrigger.TriggerInstance.hasItems(AllBlockItem.sturdy_tank))
                    .save(consumer, Central_heater.modLoc("heater/sturdy_tank"), existingFileHelper);

            AdvancementHolder overBurnt = Advancement.Builder.advancement().parent(firstHeater).display(new ItemStack(AllBlockItem.burnt_beef.asItem()), Component.translatable("advancements.heaters.overburnt.title"),
                            Component.translatable("advancements.heaters.overburnt.description"), null,
                            AdvancementType.TASK, true, true, true).addCriterion("has_overburnt",
                            InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(Alltags.Items.OVERBURNT).build()))
                    .save(consumer, Central_heater.modLoc("heater/overburnt"), existingFileHelper);

            AdvancementHolder syntheticRocks = Advancement.Builder.advancement().parent(firstHeater)
                    .display(new ItemStack(AllBlockItem.stone_brick.asItem()), Component.translatable("advancements.heaters.synthetic_rocks.title"),
                            Component.translatable("advancements.heaters.synthetic_rocks.description"),
                            null, AdvancementType.TASK, true, true, false)
                    .requirements(AdvancementRequirements.Strategy.OR)
                    .addCriterion("has_stone_brick",
                            InventoryChangeTrigger.TriggerInstance.hasItems(AllBlockItem.stone_brick))
                    .addCriterion("has_deepslate_brick",
                            InventoryChangeTrigger.TriggerInstance.hasItems(AllBlockItem.deepslate_brick))
                    .save(consumer, Central_heater.modLoc("heater/synthetic_rocks"), existingFileHelper);

            AdvancementHolder syntheticDiamond = Advancement.Builder.advancement().parent(firstCauldron)
                    .display(new ItemStack(Items.DIAMOND), Component.translatable("advancements.heaters.synthetic_diamond.title"),
                            Component.translatable("advancements.heaters.synthetic_diamond.description"), null,
                            AdvancementType.TASK, true, true, false)
                    .addCriterion("crafted_diamond",
                            RecipeCraftedTrigger.TriggerInstance.craftedItem(ResourceLocation.withDefaultNamespace("diamond_shard_from_seething_coal_block")))
                    .save(consumer, Central_heater.modLoc("heater/synthetic_diamond"), existingFileHelper);

            AdvancementHolder syntheticDebris = Advancement.Builder.advancement().parent(thirdHeater)
                    .display(new ItemStack(Items.ANCIENT_DEBRIS), Component.translatable("advancements.heaters.synthetic_debris.title"),
                            Component.translatable("advancements.heaters.synthetic_debris.description"),
                            null, AdvancementType.CHALLENGE, true, true, false)
                    .addCriterion("crafted_ancient_debris",
                            RecipeCraftedTrigger.TriggerInstance.craftedItem(ResourceLocation.withDefaultNamespace("recipe_with_ancient_debris_lava_time_12000_tier_3_with_flame_level_2")))
                    .save(consumer, Central_heater.modLoc("heater/synthetic_debris"), existingFileHelper);
        }
    }
}
