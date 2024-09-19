package net.firmamentsspark.block;

import com.mojang.serialization.MapCodec;
import net.firmamentsspark.block.entity.SpaceBlockEntity;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class SpaceBlock extends BlockWithEntity implements BlockEntityProvider {
    public SpaceBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SpaceBlockEntity(pos, state);
    }
}
