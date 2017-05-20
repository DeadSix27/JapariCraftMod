package com.japaricraft.japaricraftmod.world;

import com.japaricraft.japaricraftmod.JapariCraftMod;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;


public class StarOreGenerator implements IWorldGenerator {

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (world.provider instanceof WorldProviderSurface) {
            generateOre(world, random, chunkX << 4, chunkZ << 4);
        }
    }
    private void generateOre(World world, Random random, int x, int z) {
        for(int i = 0; i < 10; i++) {
            int genX = x + random.nextInt(16);
            int genY = 1 + random.nextInt(15);
            int genZ = z + random.nextInt(16);
            new WorldGenMinable(JapariCraftMod.sandstarore.getDefaultState(blockpos, this.oreBlock, 2));
        }
    }

}
