package com.japaricraft.japaricraftmod.block;

import com.japaricraft.japaricraftmod.JapariCraftMod;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Random;


public class SandStarORE extends Block {

    public SandStarORE()
    {
        super(Material.ROCK);
        setCreativeTab(JapariCraftMod.tabJapariCraft);/*クリエイティブタブの選択*/
        setUnlocalizedName("SandStarOre");/*システム名の設定*/
        setHardness(3.5F);/*硬さ*/
        setResistance(17.0F);/*爆破耐性*/
        setSoundType(SoundType.STONE);
        setLightLevel(4);
        setHarvestLevel("pickaxe",1);
    }

    @Override
    public int getExpDrop(IBlockState state, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune)
    {
        Random rand = world instanceof World ? ((World)world).rand : new Random();
        if (this.getItemDropped(state, rand, fortune) != Item.getItemFromBlock(this))
        {
            int i = 0;

            if (this == JapariCraftMod.sandstarore)
            {
                i = MathHelper.getInt(rand, 4, 8);
            }

            return i;
        }
        return 0;
    }

    @Override
    public ArrayList getDrops(IBlockAccess world, BlockPos pos, IBlockState blockstate, int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<>();
        drops.add(new ItemStack(JapariCraftMod.sugarstar, RANDOM.nextInt(3) + 1));
        drops.add(new ItemStack(JapariCraftMod.sandstarfragment, RANDOM.nextInt(2) + 1));
        return drops;
    }

}