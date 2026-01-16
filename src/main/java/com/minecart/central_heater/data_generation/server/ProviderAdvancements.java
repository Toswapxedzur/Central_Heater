package com.minecart.central_heater.data_generation.server;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.CentralHeater;
import com.minecart.central_heater.advancement.BurntObjectTrigger;
import com.minecart.central_heater.advancement.MinecartSpeedTrigger;
import com.minecart.central_heater.misc.Alltags;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ProviderAdvancements extends AdvancementProvider {
    public ProviderAdvancements(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
        super(output, registries, existingFileHelper, List.of(new GeneratorAdvancement()));
    }

    public static final class GeneratorAdvancement implements AdvancementGenerator{

        @Override
        public void generate(HolderLookup.Provider provider, Consumer<AdvancementHolder> consumer, ExistingFileHelper existingFileHelper) {
            AdvancementHolder root = Advancement.Builder.advancement()
                    .display(Items.FURNACE,
                            Component.translatable("advancements.central_heater.root.title"),
                            Component.translatable("advancements.central_heater.root.description"),
                            CentralHeater.modLoc("textures/gui/advancements/background/bricks.png"),
                            AdvancementType.TASK, true, true, false)
                    .addCriterion("has_furnace", InventoryChangeTrigger.TriggerInstance.hasItems(Items.FURNACE))
                    .save(consumer, CentralHeater.modLoc("main/root"), existingFileHelper);

            AdvancementHolder blasting = Advancement.Builder.advancement().parent(root)
                    .display(Items.BLAST_FURNACE,
                            Component.translatable("advancements.central_heater.blasting.title"),
                            Component.translatable("advancements.central_heater.blasting.description"),
                            null, AdvancementType.TASK, true, true, false)
                    .addCriterion("has_blast_furnace", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BLAST_FURNACE))
                    .save(consumer, CentralHeater.modLoc("main/processing_blasting"), existingFileHelper);

            AdvancementHolder campfire = Advancement.Builder.advancement().parent(root)
                    .display(Items.CAMPFIRE,
                            Component.translatable("advancements.central_heater.campfire.title"),
                            Component.translatable("advancements.central_heater.campfire.description"),
                            null, AdvancementType.TASK, true, true, false)
                    .addCriterion("has_campfire", InventoryChangeTrigger.TriggerInstance.hasItems(Items.CAMPFIRE))
                    .save(consumer, CentralHeater.modLoc("main/processing_campfire"), existingFileHelper);

            AdvancementHolder muddy = Advancement.Builder.advancement().parent(root)
                    .display(AllBlockItem.MUD_BRICK.asItem(),
                            Component.translatable("advancements.central_heater.muddy.title"),
                            Component.translatable("advancements.central_heater.muddy.description"),
                            null, AdvancementType.TASK, true, true, false)
                    .addCriterion("has_mud_brick", InventoryChangeTrigger.TriggerInstance.hasItems(AllBlockItem.MUD_BRICK.get()))
                    .save(consumer, CentralHeater.modLoc("materials/muddy"), existingFileHelper);

            AdvancementHolder muddyStove = Advancement.Builder.advancement().parent(muddy)
                    .display(AllBlockItem.MUD_BRICK_STOVE.asItem(),
                            Component.translatable("advancements.central_heater.muddy_stove.title"),
                            Component.translatable("advancements.central_heater.muddy_stove.description"),
                            null, AdvancementType.TASK, true, true, false)
                    .addCriterion("placed_mud_stove", placedBlock(AllBlockItem.MUD_BRICK_STOVE.get()))
                    .save(consumer, CentralHeater.modLoc("materials/muddy_stove"), existingFileHelper);

            AdvancementHolder muddyPot = Advancement.Builder.advancement().parent(muddy)
                    .display(AllBlockItem.MUD_BRICK_POT.asItem(),
                            Component.translatable("advancements.central_heater.muddy_pot.title"),
                            Component.translatable("advancements.central_heater.muddy_pot.description"),
                            null, AdvancementType.TASK, true, true, false)
                    .addCriterion("placed_mud_pot", placedBlock(AllBlockItem.MUD_BRICK_POT.get()))
                    .save(consumer, CentralHeater.modLoc("materials/muddy_pot"), existingFileHelper);

            AdvancementHolder bricky = Advancement.Builder.advancement().parent(muddy)
                    .display(Items.BRICK,
                            Component.translatable("advancements.central_heater.bricky.title"),
                            Component.translatable("advancements.central_heater.bricky.description"),
                            null, AdvancementType.TASK, true, true, false)
                    .addCriterion("has_brick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BRICK))
                    .save(consumer, CentralHeater.modLoc("materials/bricky"), existingFileHelper);

            AdvancementHolder brickyStove = Advancement.Builder.advancement().parent(bricky)
                    .display(AllBlockItem.BRICK_STOVE.asItem(),
                            Component.translatable("advancements.central_heater.bricky_stove.title"),
                            Component.translatable("advancements.central_heater.bricky_stove.description"),
                            null, AdvancementType.TASK, true, true, false)
                    .addCriterion("placed_brick_stove", placedBlock(AllBlockItem.BRICK_STOVE.get()))
                    .save(consumer, CentralHeater.modLoc("materials/bricky_stove"), existingFileHelper);

            AdvancementHolder brickyCauldron = Advancement.Builder.advancement().parent(bricky)
                    .display(AllBlockItem.BRICK_CAULDRON.asItem(),
                            Component.translatable("advancements.central_heater.bricky_cauldron.title"),
                            Component.translatable("advancements.central_heater.bricky_cauldron.description"),
                            null, AdvancementType.TASK, true, true, false)
                    .addCriterion("placed_brick_cauldron", placedBlock(AllBlockItem.BRICK_CAULDRON.get()))
                    .save(consumer, CentralHeater.modLoc("materials/bricky_cauldron"), existingFileHelper);

            AdvancementHolder stoney = Advancement.Builder.advancement().parent(bricky)
                    .display(AllBlockItem.STONE_BRICK.asItem(),
                            Component.translatable("advancements.central_heater.stoney.title"),
                            Component.translatable("advancements.central_heater.stoney.description"),
                            null, AdvancementType.TASK, true, true, false)
                    .addCriterion("has_stone_brick", InventoryChangeTrigger.TriggerInstance.hasItems(AllBlockItem.STONE_BRICK.get()))
                    .save(consumer, CentralHeater.modLoc("materials/stoney"), existingFileHelper);

            AdvancementHolder stoneyAppliances = Advancement.Builder.advancement().parent(stoney)
                    .display(AllBlockItem.STONE_STOVE.asItem(),
                            Component.translatable("advancements.central_heater.stoney_appliances.title"),
                            Component.translatable("advancements.central_heater.stoney_appliances.description"),
                            null, AdvancementType.TASK, true, true, false)
                    .addCriterion("placed_stone_stove", placedBlock(AllBlockItem.STONE_STOVE.get()))
                    .save(consumer, CentralHeater.modLoc("materials/stoney_appliances"), existingFileHelper);

            AdvancementHolder ironCauldron = Advancement.Builder.advancement().parent(root)
                    .display(AllBlockItem.IRON_CAULDRON.asItem(),
                            Component.translatable("advancements.central_heater.iron_cauldron.title"),
                            Component.translatable("advancements.central_heater.iron_cauldron.description"),
                            null, AdvancementType.TASK, true, true, false)
                    .addCriterion("has_iron_cauldron", InventoryChangeTrigger.TriggerInstance.hasItems(AllBlockItem.IRON_CAULDRON.get()))
                    .save(consumer, CentralHeater.modLoc("cauldron/iron"), existingFileHelper);

            AdvancementHolder goldenCauldron = Advancement.Builder.advancement().parent(ironCauldron)
                    .display(AllBlockItem.GOLDEN_CAULDRON.asItem(),
                            Component.translatable("advancements.central_heater.golden_cauldron.title"),
                            Component.translatable("advancements.central_heater.golden_cauldron.description"),
                            null, AdvancementType.TASK, true, true, false)
                    .addCriterion("has_golden_cauldron", InventoryChangeTrigger.TriggerInstance.hasItems(AllBlockItem.GOLDEN_CAULDRON.get()))
                    .save(consumer, CentralHeater.modLoc("cauldron/golden"), existingFileHelper);

            AdvancementHolder sturdyBrick = Advancement.Builder.advancement().parent(ironCauldron)
                    .display(AllBlockItem.STURDY_BRICK.asItem(),
                            Component.translatable("advancements.central_heater.sturdy_brick.title"),
                            Component.translatable("advancements.central_heater.sturdy_brick.description"),
                            null, AdvancementType.TASK, true, true, false)
                    .addCriterion("has_sturdy_brick", InventoryChangeTrigger.TriggerInstance.hasItems(AllBlockItem.STURDY_BRICK.get()))
                    .save(consumer, CentralHeater.modLoc("cauldron/sturdy_brick"), existingFileHelper);

            AdvancementHolder sturdyTank = Advancement.Builder.advancement().parent(sturdyBrick)
                    .display(AllBlockItem.STURDY_TANK_ITEM.asItem(),
                            Component.translatable("advancements.central_heater.sturdy_tank.title"),
                            Component.translatable("advancements.central_heater.sturdy_tank.description"),
                            null, AdvancementType.TASK, true, true, false)
                    .addCriterion("has_sturdy_tank", InventoryChangeTrigger.TriggerInstance.hasItems(AllBlockItem.STURDY_TANK_ITEM.get()))
                    .save(consumer, CentralHeater.modLoc("cauldron/sturdy_tank"), existingFileHelper);

            AdvancementHolder sturdyArmor = Advancement.Builder.advancement().parent(sturdyBrick)
                    .display(AllBlockItem.STURDY_CHESTPLATE.asItem(),
                            Component.translatable("advancements.central_heater.sturdy_armor.title"),
                            Component.translatable("advancements.central_heater.sturdy_armor.description"),
                            null, AdvancementType.GOAL, true, true, false)
                    .addCriterion("wearing_sturdy_armor", CriteriaTriggers.INVENTORY_CHANGED.createCriterion(new InventoryChangeTrigger.TriggerInstance(
                            Optional.of(EntityPredicate.wrap(
                                    EntityPredicate.Builder.entity().equipment(
                                            EntityEquipmentPredicate.Builder.equipment()
                                                    .head(ItemPredicate.Builder.item().of(AllBlockItem.STURDY_HELMET.get()))
                                                    .chest(ItemPredicate.Builder.item().of(AllBlockItem.STURDY_CHESTPLATE.get()))
                                                    .legs(ItemPredicate.Builder.item().of(AllBlockItem.STURDY_LEGGINGS.get()))
                                                    .feet(ItemPredicate.Builder.item().of(AllBlockItem.STURDY_BOOTS.get()))
                                                    .build()
                                    ).build()
                            )),
                            InventoryChangeTrigger.TriggerInstance.Slots.ANY,
                            java.util.List.of()
                    )))
                    .save(consumer, CentralHeater.modLoc("cauldron/sturdy_armor"), existingFileHelper);

            AdvancementHolder netherStove = Advancement.Builder.advancement().parent(root)
                    .display(AllBlockItem.RED_NETHER_BRICK_STOVE.asItem(),
                            Component.translatable("advancements.central_heater.nether_stove.title"),
                            Component.translatable("advancements.central_heater.nether_stove.description"),
                            null, AdvancementType.TASK, true, true, false)
                    .addCriterion("placed_any_nether_stove", placedAnyBlock(
                            AllBlockItem.NETHER_BRICK_STOVE.get(),
                            AllBlockItem.RED_NETHER_BRICK_STOVE.get(),
                            AllBlockItem.BLACKSTONE_STOVE.get()
                    ))
                    .save(consumer, CentralHeater.modLoc("nether/nether_stove"), existingFileHelper);

            AdvancementHolder scorchedCoal = Advancement.Builder.advancement().parent(root)
                    .display(AllBlockItem.SCORCHED_COAL.asItem(),
                            Component.translatable("advancements.central_heater.scorched_coal.title"),
                            Component.translatable("advancements.central_heater.scorched_coal.description"),
                            null, AdvancementType.TASK, true, true, false)
                    .addCriterion("has_scorched_coal", InventoryChangeTrigger.TriggerInstance.hasItems(AllBlockItem.SCORCHED_COAL.get()))
                    .save(consumer, CentralHeater.modLoc("nether/scorched_coal"), existingFileHelper);

            AdvancementHolder haunting = Advancement.Builder.advancement().parent(scorchedCoal)
                    .display(AllBlockItem.BLAZING_FURNACE.get(),
                            Component.translatable("advancements.central_heater.haunting.title"),
                            Component.translatable("advancements.central_heater.haunting.description"),
                            null, AdvancementType.TASK, true, true, false)
                    .addCriterion("has_blazing_furnace", InventoryChangeTrigger.TriggerInstance.hasItems(AllBlockItem.BLAZING_FURNACE.get()))
                    .save(consumer, CentralHeater.modLoc("main/processing_haunting"), existingFileHelper);

            AdvancementHolder diamondShard = Advancement.Builder.advancement().parent(netherStove)
                    .display(AllBlockItem.DIAMOND_SHARD.get(),
                            Component.translatable("advancements.central_heater.diamond_shard.title"),
                            Component.translatable("advancements.central_heater.diamond_shard.description"),
                            null, AdvancementType.TASK, true, true, false)
                    .addCriterion("crafted_diamond_shard", RecipeCraftedTrigger.TriggerInstance.craftedItem(
                            CentralHeater.modLoc("diamond_shard_from_block_smoldering")))
                    .save(consumer, CentralHeater.modLoc("nether/diamond_shard"), existingFileHelper);

            AdvancementHolder ancientDebris = Advancement.Builder.advancement().parent(netherStove)
                    .display(Items.ANCIENT_DEBRIS,
                            Component.translatable("advancements.central_heater.synthetic_debris.title"),
                            Component.translatable("advancements.central_heater.synthetic_debris.description"),
                            null, AdvancementType.CHALLENGE, true, true, false)
                    .addCriterion("crafted_ancient_debris", RecipeCraftedTrigger.TriggerInstance.craftedItem(
                            CentralHeater.modLoc("ancient_debris_from_block_smoldering")))
                    .save(consumer, CentralHeater.modLoc("nether/synthetic_debris"), existingFileHelper);

            AdvancementHolder extraCrispy = Advancement.Builder.advancement().parent(root)
                    .display(AllBlockItem.BURNT_BEEF.asItem(),
                            Component.translatable("advancements.central_heater.extra_crispy.title"),
                            Component.translatable("advancements.central_heater.extra_crispy.description"),
                            null, AdvancementType.TASK, true, true, true)
                    .addCriterion("has_overburnt", InventoryChangeTrigger.TriggerInstance.hasItems(
                            ItemPredicate.Builder.item().of(Alltags.Items.OVERBURNT).build()))
                    .save(consumer, CentralHeater.modLoc("misc/extra_crispy"), existingFileHelper);

            AdvancementHolder speedDemon = Advancement.Builder.advancement().parent(haunting)
                    .display(AllBlockItem.BLAZING_FURNACE_MINECART.asItem(),
                            Component.translatable("advancements.central_heater.speed_demon.title"),
                            Component.translatable("advancements.central_heater.speed_demon.description"),
                            null, AdvancementType.CHALLENGE, true, true, false)
                    .addCriterion("fast_minecart", MinecartSpeedTrigger.TriggerInstance.speeding(
                            MinMaxBounds.Doubles.atLeast(1.6)
                    ))
                    .save(consumer, CentralHeater.modLoc("challenges/speed_demon"), existingFileHelper);

            AdvancementHolder wastefulBurning = Advancement.Builder.advancement().parent(root)
                    .display(AllBlockItem.FIRE_ASH.asItem(),
                            Component.translatable("advancements.central_heater.wasteful_burning.title"),
                            Component.translatable("advancements.central_heater.wasteful_burning.description"),
                            null, AdvancementType.TASK, true, true, false)
                    .addCriterion("burnt_coal_block", BurntObjectTrigger.TriggerInstance.burnt(Blocks.COAL_BLOCK))
                    .save(consumer, CentralHeater.modLoc("challenges/wasteful_burning"), existingFileHelper);
        }

        private Criterion<ItemUsedOnLocationTrigger.TriggerInstance> placedBlock(Block block) {
            return ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(
                    LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
            );
        }

        private Criterion<ItemUsedOnLocationTrigger.TriggerInstance> placedAnyBlock(Block... blocks) {
            LootItemBlockStatePropertyCondition.Builder[] conditions = new LootItemBlockStatePropertyCondition.Builder[blocks.length];
            for (int i = 0; i < blocks.length; i++) {
                conditions[i] = LootItemBlockStatePropertyCondition.hasBlockStateProperties(blocks[i]);
            }
            return ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(AnyOfCondition.anyOf(conditions));
        }
    }
}
