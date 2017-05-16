package com.japaricraft.japaricraftmod.item;

import com.japaricraft.japaricraftmod.JapariCraftMod;
import com.japaricraft.japaricraftmod.mob.LuckyBeast;
import com.japaricraft.japaricraftmod.mob.SafeguardLuckyBeast;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class SummonGuardLucky extends Item {
    public SummonGuardLucky() {
        this.setCreativeTab(JapariCraftMod.tabJapariCraft);
        this.setUnlocalizedName("SummonGuardLuckyBeast");
        this.setMaxStackSize(1);
    }
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);

        if (!playerIn.capabilities.isCreativeMode) {
            itemstack.shrink(1);
        }

        world.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.BLOCK_NOTE_HAT, SoundCategory.PLAYERS, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
        playerIn.getCooldownTracker().setCooldown(this, 50);
        if (!world.isRemote) {
            SafeguardLuckyBeast beast = new SafeguardLuckyBeast(world);
            beast.setLocationAndAngles(playerIn.posX, playerIn.posY, playerIn.posZ, playerIn.rotationYaw, 0.0F);
            world.spawnEntity(beast);

        }
        playerIn.addStat(StatList.getObjectUseStats(this));
        return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
    }
    public boolean hasEffect(ItemStack stack) {
        return true;
    }
}