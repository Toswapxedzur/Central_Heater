package com.minecart.central_heater.data_generation.server;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.CentralHeater;
import com.minecart.central_heater.advancement.BurntObjectTrigger;
import com.minecart.central_heater.advancement.MinecartSpeedTrigger;
import com.minecart.central_heater.misc.Alltags;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.ItemUsedOnLocationTrigger;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.RecipeCraftedTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ProviderAdvancements extends ForgeAdvancementProvider {
    public ProviderAdvancements(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
        super(output, registries, existingFileHelper, List.of(new GeneratorAdvancement()));
    }

    public static final class GeneratorAdvancement implements AdvancementGenerator {

        @Override
        public void generate(HolderLookup.Provider provider, Consumer<Advancement> consumer, ExistingFileHelper existingFileHelper) {
            // --- ROOT ---
            Advancement root = Advancement.Builder.advancement()
                    .display(Items.FURNACE,
                            Component.translatable("advancements.central_heater.root.title"),
                            Component.translatable("advancements.central_heater.root.description"),
                            new ResourceLocation(CentralHeater.MODID, "textures/gui/advancements/background/bricks.png"),
                            FrameType.TASK, true, true, false)
                    .addCriterion("has_furnace", InventoryChangeTrigger.TriggerInstance.hasItems(Items.FURNACE))
                    .save(consumer, CentralHeater.MODID + ":main/root");

            // --- PROCESSING ---
            Advancement blasting = Advancement.Builder.advancement().parent(root)
                    .display(Items.BLAST_FURNACE,
                            Component.translatable("advancements.central_heater.blasting.title"),
                            Component.translatable("advancements.central_heater.blasting.description"),
                            null, FrameType.TASK, true, true, false)
                    .addCriterion("has_blast_furnace", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BLAST_FURNACE))
                    .save(consumer, CentralHeater.MODID + ":main/processing_blasting");

            Advancement campfire = Advancement.Builder.advancement().parent(root)
                    .display(Items.CAMPFIRE,
                            Component.translatable("advancements.central_heater.campfire.title"),
                            Component.translatable("advancements.central_heater.campfire.description"),
                            null, FrameType.TASK, true, true, false)
                    .addCriterion("has_campfire", InventoryChangeTrigger.TriggerInstance.hasItems(Items.CAMPFIRE))
                    .save(consumer, CentralHeater.MODID + ":main/processing_campfire");

            // --- MATERIALS ---
            Advancement muddy = Advancement.Builder.advancement().parent(root)
                    .display(AllBlockItem.MUD_BRICK.get(),
                            Component.translatable("advancements.central_heater.muddy.title"),
                            Component.translatable("advancements.central_heater.muddy.description"),
                            null, FrameType.TASK, true, true, false)
                    .addCriterion("has_mud_brick", InventoryChangeTrigger.TriggerInstance.hasItems(AllBlockItem.MUD_BRICK.get()))
                    .save(consumer, CentralHeater.MODID + ":materials/muddy");

            Advancement muddyStove = Advancement.Builder.advancement().parent(muddy)
                    .display(AllBlockItem.MUD_BRICK_STOVE.get(),
                            Component.translatable("advancements.central_heater.muddy_stove.title"),
                            Component.translatable("advancements.central_heater.muddy_stove.description"),
                            null, FrameType.TASK, true, true, false)
                    .addCriterion("placed_mud_stove", placedBlock(AllBlockItem.MUD_BRICK_STOVE.get()))
                    .save(consumer, CentralHeater.MODID + ":materials/muddy_stove");

            Advancement muddyPot = Advancement.Builder.advancement().parent(muddy)
                    .display(AllBlockItem.MUD_BRICK_POT.get(),
                            Component.translatable("advancements.central_heater.muddy_pot.title"),
                            Component.translatable("advancements.central_heater.muddy_pot.description"),
                            null, FrameType.TASK, true, true, false)
                    .addCriterion("placed_mud_pot", placedBlock(AllBlockItem.MUD_BRICK_POT.get()))
                    .save(consumer, CentralHeater.MODID + ":materials/muddy_pot");

            Advancement bricky = Advancement.Builder.advancement().parent(muddy)
                    .display(Items.BRICK,
                            Component.translatable("advancements.central_heater.bricky.title"),
                            Component.translatable("advancements.central_heater.bricky.description"),
                            null, FrameType.TASK, true, true, false)
                    .addCriterion("has_brick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BRICK))
                    .save(consumer, CentralHeater.MODID + ":materials/bricky");

            Advancement brickyStove = Advancement.Builder.advancement().parent(bricky)
                    .display(AllBlockItem.BRICK_STOVE.get(),
                            Component.translatable("advancements.central_heater.bricky_stove.title"),
                            Component.translatable("advancements.central_heater.bricky_stove.description"),
                            null, FrameType.TASK, true, true, false)
                    .addCriterion("placed_brick_stove", placedBlock(AllBlockItem.BRICK_STOVE.get()))
                    .save(consumer, CentralHeater.MODID + ":materials/bricky_stove");

            Advancement brickyCauldron = Advancement.Builder.advancement().parent(bricky)
                    .display(AllBlockItem.BRICK_CAULDRON.get(),
                            Component.translatable("advancements.central_heater.bricky_cauldron.title"),
                            Component.translatable("advancements.central_heater.bricky_cauldron.description"),
                            null, FrameType.TASK, true, true, false)
                    .addCriterion("placed_brick_cauldron", placedBlock(AllBlockItem.BRICK_CAULDRON.get()))
                    .save(consumer, CentralHeater.MODID + ":materials/bricky_cauldron");

            Advancement stoney = Advancement.Builder.advancement().parent(bricky)
                    .display(AllBlockItem.STONE_BRICK.get(),
                            Component.translatable("advancements.central_heater.stoney.title"),
                            Component.translatable("advancements.central_heater.stoney.description"),
                            null, FrameType.TASK, true, true, false)
                    .addCriterion("has_stone_brick", InventoryChangeTrigger.TriggerInstance.hasItems(AllBlockItem.STONE_BRICK.get()))
                    .save(consumer, CentralHeater.MODID + ":materials/stoney");

            Advancement stoneyAppliances = Advancement.Builder.advancement().parent(stoney)
                    .display(AllBlockItem.STONE_STOVE.get(),
                            Component.translatable("advancements.central_heater.stoney_appliances.title"),
                            Component.translatable("advancements.central_heater.stoney_appliances.description"),
                            null, FrameType.TASK, true, true, false)
                    .addCriterion("placed_stone_stove", placedBlock(AllBlockItem.STONE_STOVE.get()))
                    .save(consumer, CentralHeater.MODID + ":materials/stoney_appliances");

            // --- CAULDRONS ---
            Advancement ironCauldron = Advancement.Builder.advancement().parent(root)
                    .display(AllBlockItem.IRON_CAULDRON.get(),
                            Component.translatable("advancements.central_heater.iron_cauldron.title"),
                            Component.translatable("advancements.central_heater.iron_cauldron.description"),
                            null, FrameType.TASK, true, true, false)
                    .addCriterion("has_iron_cauldron", InventoryChangeTrigger.TriggerInstance.hasItems(AllBlockItem.IRON_CAULDRON.get()))
                    .save(consumer, CentralHeater.MODID + ":cauldron/iron");

            Advancement goldenCauldron = Advancement.Builder.advancement().parent(ironCauldron)
                    .display(AllBlockItem.GOLDEN_CAULDRON.get(),
                            Component.translatable("advancements.central_heater.golden_cauldron.title"),
                            Component.translatable("advancements.central_heater.golden_cauldron.description"),
                            null, FrameType.TASK, true, true, false)
                    .addCriterion("has_golden_cauldron", InventoryChangeTrigger.TriggerInstance.hasItems(AllBlockItem.GOLDEN_CAULDRON.get()))
                    .save(consumer, CentralHeater.MODID + ":cauldron/golden");

            Advancement sturdyBrick = Advancement.Builder.advancement().parent(ironCauldron)
                    .display(AllBlockItem.STURDY_BRICK.get(),
                            Component.translatable("advancements.central_heater.sturdy_brick.title"),
                            Component.translatable("advancements.central_heater.sturdy_brick.description"),
                            null, FrameType.TASK, true, true, false)
                    .addCriterion("has_sturdy_brick", InventoryChangeTrigger.TriggerInstance.hasItems(AllBlockItem.STURDY_BRICK.get()))
                    .save(consumer, CentralHeater.MODID + ":cauldron/sturdy_brick");

            Advancement sturdyTank = Advancement.Builder.advancement().parent(sturdyBrick)
                    .display(AllBlockItem.STURDY_TANK_ITEM.get(),
                            Component.translatable("advancements.central_heater.sturdy_tank.title"),
                            Component.translatable("advancements.central_heater.sturdy_tank.description"),
                            null, FrameType.TASK, true, true, false)
                    .addCriterion("has_sturdy_tank", InventoryChangeTrigger.TriggerInstance.hasItems(AllBlockItem.STURDY_TANK_ITEM.get()))
                    .save(consumer, CentralHeater.MODID + ":cauldron/sturdy_tank");

            Advancement sturdyArmor = Advancement.Builder.advancement().parent(sturdyBrick)
                    .display(AllBlockItem.STURDY_CHESTPLATE.get(),
                            Component.translatable("advancements.central_heater.sturdy_armor.title"),
                            Component.translatable("advancements.central_heater.sturdy_armor.description"),
                            null, FrameType.GOAL, true, true, false)
                    .addCriterion("wearing_sturdy_armor", InventoryChangeTrigger.TriggerInstance.hasItems(
                            ItemPredicate.Builder.item().of(AllBlockItem.STURDY_HELMET.get()).build(),
                            ItemPredicate.Builder.item().of(AllBlockItem.STURDY_CHESTPLATE.get()).build(),
                            ItemPredicate.Builder.item().of(AllBlockItem.STURDY_LEGGINGS.get()).build(),
                            ItemPredicate.Builder.item().of(AllBlockItem.STURDY_BOOTS.get()).build()
                    ))
                    .save(consumer, CentralHeater.MODID + ":cauldron/sturdy_armor");

            // --- NETHER ---
            Advancement netherStove = Advancement.Builder.advancement().parent(root)
                    .display(AllBlockItem.RED_NETHER_BRICK_STOVE.get(),
                            Component.translatable("advancements.central_heater.nether_stove.title"),
                            Component.translatable("advancements.central_heater.nether_stove.description"),
                            null, FrameType.TASK, true, true, false)
                    .addCriterion("placed_any_nether_stove", placedAnyBlock(
                            AllBlockItem.NETHER_BRICK_STOVE.get(),
                            AllBlockItem.RED_NETHER_BRICK_STOVE.get(),
                            AllBlockItem.BLACKSTONE_STOVE.get()
                    ))
                    .save(consumer, CentralHeater.MODID + ":nether/nether_stove");

            Advancement scorchedCoal = Advancement.Builder.advancement().parent(root)
                    .display(AllBlockItem.SCORCHED_COAL.get(),
                            Component.translatable("advancements.central_heater.scorched_coal.title"),
                            Component.translatable("advancements.central_heater.scorched_coal.description"),
                            null, FrameType.TASK, true, true, false)
                    .addCriterion("has_scorched_coal", InventoryChangeTrigger.TriggerInstance.hasItems(AllBlockItem.SCORCHED_COAL.get()))
                    .save(consumer, CentralHeater.MODID + ":nether/scorched_coal");

            Advancement haunting = Advancement.Builder.advancement().parent(scorchedCoal)
                    .display(AllBlockItem.BLAZING_FURNACE.get(),
                            Component.translatable("advancements.central_heater.haunting.title"),
                            Component.translatable("advancements.central_heater.haunting.description"),
                            null, FrameType.TASK, true, true, false)
                    .addCriterion("has_blazing_furnace", InventoryChangeTrigger.TriggerInstance.hasItems(AllBlockItem.BLAZING_FURNACE.get()))
                    .save(consumer, CentralHeater.MODID + ":main/processing_haunting");

            Advancement diamondShard = Advancement.Builder.advancement().parent(netherStove)
                    .display(AllBlockItem.DIAMOND_SHARD.get(),
                            Component.translatable("advancements.central_heater.diamond_shard.title"),
                            Component.translatable("advancements.central_heater.diamond_shard.description"),
                            null, FrameType.TASK, true, true, false)
                    .addCriterion("crafted_diamond_shard", RecipeCraftedTrigger.TriggerInstance.craftedItem(
                            new ResourceLocation(CentralHeater.MODID, "diamond_shard_from_block_smoldering")))
                    .save(consumer, CentralHeater.MODID + ":nether/diamond_shard");

            Advancement ancientDebris = Advancement.Builder.advancement().parent(netherStove)
                    .display(Items.ANCIENT_DEBRIS,
                            Component.translatable("advancements.central_heater.synthetic_debris.title"),
                            Component.translatable("advancements.central_heater.synthetic_debris.description"),
                            null, FrameType.CHALLENGE, true, true, false)
                    .addCriterion("crafted_ancient_debris", RecipeCraftedTrigger.TriggerInstance.craftedItem(
                            new ResourceLocation(CentralHeater.MODID, "ancient_debris_from_block_smoldering")))
                    .save(consumer, CentralHeater.MODID + ":nether/synthetic_debris");

            // --- MISC ---
            Advancement extraCrispy = Advancement.Builder.advancement().parent(root)
                    .display(AllBlockItem.BURNT_BEEF.get(),
                            Component.translatable("advancements.central_heater.extra_crispy.title"),
                            Component.translatable("advancements.central_heater.extra_crispy.description"),
                            null, FrameType.TASK, true, true, true)
                    .addCriterion("has_overburnt", InventoryChangeTrigger.TriggerInstance.hasItems(
                            ItemPredicate.Builder.item().of(Alltags.Items.OVERBURNT).build()))
                    .save(consumer, CentralHeater.MODID + ":misc/extra_crispy");

            // --- CHALLENGES ---
            Advancement speedDemon = Advancement.Builder.advancement().parent(haunting)
                    .display(AllBlockItem.BLAZING_FURNACE_MINECART.get(),
                            Component.translatable("advancements.central_heater.speed_demon.title"),
                            Component.translatable("advancements.central_heater.speed_demon.description"),
                            null, FrameType.CHALLENGE, true, true, false)
                    .addCriterion("fast_minecart", MinecartSpeedTrigger.TriggerInstance.speeding(
                            MinMaxBounds.Doubles.atLeast(1.6)
                    ))
                    .save(consumer, CentralHeater.MODID + ":challenges/speed_demon");

            Advancement wastefulBurning = Advancement.Builder.advancement().parent(root)
                    .display(AllBlockItem.FIRE_ASH.get(),
                            Component.translatable("advancements.central_heater.wasteful_burning.title"),
                            Component.translatable("advancements.central_heater.wasteful_burning.description"),
                            null, FrameType.TASK, true, true, false)
                    .addCriterion("burnt_coal_block", BurntObjectTrigger.TriggerInstance.burnt(Blocks.COAL_BLOCK))
                    .save(consumer, CentralHeater.MODID + ":challenges/wasteful_burning");
        }

        private CriterionTriggerInstance placedBlock(Block block) {
            return ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(block);
        }

        private CriterionTriggerInstance placedAnyBlock(Block... blocks) {
            LootItemBlockStatePropertyCondition.Builder[] conditions = new LootItemBlockStatePropertyCondition.Builder[blocks.length];
            for (int i = 0; i < blocks.length; i++) {
                conditions[i] = LootItemBlockStatePropertyCondition.hasBlockStateProperties(blocks[i]);
            }
            return ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(conditions);
        }
    }
}
