package net.firmamentsspark.registry;

import net.firmamentsspark.HiiDoctor4t;
import net.firmamentsspark.block.*;
import net.firmamentsspark.block.entity.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static net.firmamentsspark.registry.BlockRegistry.*;

public class BlockEntityRegistry {

    public static final BlockEntityType<SpaceBlockEntity> SPACE_BLOCK_ENTITY = BlockEntityType.Builder.create(SpaceBlockEntity::new, SPACE_BLOCK).build();

    public static void registering() {
        HiiDoctor4t.LOGGER.info("BlockEntity registry...");

        Registry.register(Registries.BLOCK_ENTITY_TYPE,
                Identifier.of(HiiDoctor4t.MOD_ID, "space_block_entity"),
                SPACE_BLOCK_ENTITY);
    }
}
