package com.japaricraft.japaricraftmod.item;

import com.japaricraft.japaricraftmod.JapariCraftMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class JapariBook extends Item {
    public JapariBook() {
        this.setCreativeTab(JapariCraftMod.tabJapariCraft);
        this.setUnlocalizedName("sandstarbook");
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        playerIn.openGui(JapariCraftMod.instance, 0, worldIn, ((int) playerIn.posX), ((int) playerIn.posY), ((int) playerIn.posZ));
        playerIn.addStat(StatList.getObjectUseStats(this));
        return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
    }

    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return true;
    }

    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.EPIC;
    }
}