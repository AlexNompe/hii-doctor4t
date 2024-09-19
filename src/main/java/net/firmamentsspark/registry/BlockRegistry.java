package net.firmamentsspark.registry;

import net.firmamentsspark.HiiDoctor4t;
import net.firmamentsspark.block.*;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class BlockRegistry {

    public static final Block SPACE_BLOCK = new SpaceBlock(Block.Settings.create().strength(1.0f));

    public static void registering() {
        HiiDoctor4t.LOGGER.info("Block registry...");

        RegisterBlockItem(Identifier.of(HiiDoctor4t.MOD_ID, "space_block"), SPACE_BLOCK);
    }

    public static void RegisterBlockItem(Identifier path, Block block) {
        Registry.register(Registries.BLOCK, path, block);
        Registry.register(Registries.ITEM, path, new BlockItem(block, new Item.Settings()));
    }
}
