package com.minecart.central_heater.heat.registry;

import com.minecart.central_heater.heat.api.ThermalMaterial;
import com.minecart.central_heater.heat.api.ThermalOverrideProvider;
import com.minecart.central_heater.misc.DataMapHook;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ThermalMaterialRegistry {
    private static final Map<Block, ThermalMaterial> EXACT_MATERIALS = new HashMap<>();
    private static final Map<TagKey<Block>, ThermalMaterial> TAG_MATERIALS = new LinkedHashMap<>();

    static {
        registerTag(BlockTags.LOGS, ThermalMaterial.WOOD);
        registerTag(BlockTags.PLANKS, ThermalMaterial.WOOD);
        registerTag(BlockTags.LEAVES, ThermalMaterial.WOOD);
        registerTag(BlockTags.WOOL, ThermalMaterial.INSULATOR);
        registerTag(BlockTags.STONE_BRICKS, ThermalMaterial.BRICK);
        registerTag(BlockTags.BASE_STONE_OVERWORLD, ThermalMaterial.STONE);
        registerTag(BlockTags.BASE_STONE_NETHER, ThermalMaterial.BLACKSTONE);
        registerExact(Blocks.WATER, ThermalMaterial.WATER);
        registerExact(Blocks.LAVA, ThermalMaterial.LAVA);
        registerExact(Blocks.MAGMA_BLOCK, ThermalMaterial.SCORCHED);
    }

    private ThermalMaterialRegistry() {
    }

    public static void registerExact(Block block, ThermalMaterial material) {
        EXACT_MATERIALS.put(block, material);
    }

    public static void registerTag(TagKey<Block> tag, ThermalMaterial material) {
        TAG_MATERIALS.put(tag, material);
    }

    public static ThermalMaterial resolve(BlockState state) {
        if (state.getBlock() instanceof ThermalOverrideProvider provider) {
            ThermalMaterial override = provider.getThermalMaterialOverride(state);
            if (override != null) {
                return override;
            }
        }

        ThermalMaterial mapped = DataMapHook.getMappedThermalMaterial(state);
        if (mapped != null) {
            return mapped;
        }

        ThermalMaterial exact = EXACT_MATERIALS.get(state.getBlock());
        if (exact != null) {
            return exact;
        }

        for (Map.Entry<TagKey<Block>, ThermalMaterial> entry : TAG_MATERIALS.entrySet()) {
            if (state.is(entry.getKey())) {
                return entry.getValue();
            }
        }

        return fallback(state);
    }

    public static @Nullable ThermalMaterial exactMaterial(Block block) {
        return EXACT_MATERIALS.get(block);
    }

    private static ThermalMaterial fallback(BlockState state) {
        if (state.isAir()) {
            return ThermalMaterial.AIR;
        }
        if (state.getFluidState().is(Fluids.WATER)) {
            return ThermalMaterial.WATER;
        }
        if (state.getFluidState().is(Fluids.LAVA) || state.is(Blocks.LAVA)) {
            return ThermalMaterial.LAVA;
        }

        ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(state.getBlock());
        String path = blockId.getPath();
        if (path.contains("sturdy")) {
            return ThermalMaterial.STURDY;
        }
        if (path.contains("scorched") || path.contains("ash")) {
            return ThermalMaterial.SCORCHED;
        }
        if (path.contains("burnt")) {
            return ThermalMaterial.BURNT_WOOD;
        }
        if (path.contains("copper")) {
            return ThermalMaterial.COPPER;
        }
        if (path.contains("gold")) {
            return ThermalMaterial.GOLD;
        }
        if (path.contains("iron") || path.contains("netherite") || path.contains("anvil") || path.contains("bars")) {
            return ThermalMaterial.METAL;
        }
        if (path.contains("mud_brick")) {
            return ThermalMaterial.MUD_BRICK;
        }
        if (path.contains("brick")) {
            return ThermalMaterial.BRICK;
        }
        if (path.contains("deepslate")) {
            return ThermalMaterial.DEEPSLATE;
        }
        if (path.contains("blackstone") || path.contains("basalt")) {
            return ThermalMaterial.BLACKSTONE;
        }
        if (path.contains("coal")) {
            return ThermalMaterial.COAL;
        }
        if (path.contains("stone")) {
            return ThermalMaterial.STONE;
        }
        return ThermalMaterial.STONE;
    }
}
