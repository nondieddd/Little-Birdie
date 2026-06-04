package org.nondied.banda_de_trapo.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class DiscoFloorBlock extends Block implements BlockEntityProvider {

    public DiscoFloorBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new DiscoFloorBlockEntity(pos, state);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        if (!world.isClient) {
            autoCalculateCenter(world, pos);
        }
    }

    public void recalcularCentro(World world, BlockPos startPos) {
        autoCalculateCenter(world, startPos);
    }

    private void autoCalculateCenter(World world, BlockPos startPos) {
        Set<BlockPos> trackBlocks = new HashSet<>();
        Queue<BlockPos> queue = new LinkedList<>();
        queue.add(startPos);
        trackBlocks.add(startPos);

        int minX = startPos.getX(), maxX = startPos.getX();
        int minZ = startPos.getZ(), maxZ = startPos.getZ();
        while (!queue.isEmpty()) {
            BlockPos current = queue.poll();
            for (BlockPos offset : new BlockPos[]{
                    current.north(), current.south(),
                    current.east(),  current.west()}) {
                if (!trackBlocks.contains(offset) && world.getBlockState(offset).isOf(this)) {
                    trackBlocks.add(offset);
                    queue.add(offset);
                    minX = Math.min(minX, offset.getX());
                    maxX = Math.max(maxX, offset.getX());
                    minZ = Math.min(minZ, offset.getZ());
                    maxZ = Math.max(maxZ, offset.getZ());
                }
            }
        }

        BlockPos center = new BlockPos(
                (minX + maxX) / 2,
                startPos.getY(),
                (minZ + maxZ) / 2
        );

        for (BlockPos bp : trackBlocks) {
            if (world.getBlockEntity(bp) instanceof DiscoFloorBlockEntity be) {
                be.setCenterPos(center);
            }
        }
    }
}