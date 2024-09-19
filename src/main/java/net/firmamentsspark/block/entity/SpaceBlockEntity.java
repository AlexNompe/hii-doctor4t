package net.firmamentsspark.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

import static net.firmamentsspark.registry.BlockEntityRegistry.SPACE_BLOCK_ENTITY;

public class SpaceBlockEntity extends BlockEntity {
    public SpaceBlockEntity(BlockPos pos, BlockState state) {
        super(SPACE_BLOCK_ENTITY, pos, state);
    }
}
