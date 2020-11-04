package com.unixlike.chunky.blocks;

import com.unixlike.chunky.storage.ChunkLoaderPosition;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;

public class ChunkLoaderBlock extends Block {
    public ChunkLoaderBlock() {
        super(Block.Properties
            .create(Material.IRON)
            .hardnessAndResistance(5.0f, 6.0f)
            .sound(SoundType.STONE)
            .harvestLevel(1)
            .harvestTool(ToolType.PICKAXE)
        );
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

        if (!worldIn.isRemote() && worldIn instanceof ServerWorld) {
            ServerWorld world = (ServerWorld) worldIn;
            int chunkX = pos.getX() >> 4;
            int chunkZ = pos.getZ() >> 4;
            world.forceChunk(chunkX, chunkZ, true);

            ChunkLoaderPosition.addChunk(chunkX, chunkZ);

            System.out.format("Now loading chunk x = %d and z = %d\n", chunkX, chunkZ);
        }
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBlockHarvested(worldIn, pos, state, player);

        if (!worldIn.isRemote() && worldIn instanceof ServerWorld) {
            ServerWorld world = (ServerWorld) worldIn;
            int chunkX = pos.getX() >> 4;
            int chunkZ = pos.getZ() >> 4;
            world.forceChunk(chunkX, chunkZ, false);

            ChunkLoaderPosition.removeChunk(chunkX, chunkZ);
    
            System.out.format("Now unloading chunk x = %d and z = %d\n", chunkX, chunkZ);
        }
    }
}
