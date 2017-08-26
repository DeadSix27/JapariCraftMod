package com.japaricraft.japaricraftmod.block.field;

import com.japaricraft.japaricraftmod.JapariFluidCore;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class BlockSandStarWater extends BlockFluidClassic {
    public BlockSandStarWater() {
        super(JapariFluidCore.SANDSTARWATER, Material.WATER);
        this.setUnlocalizedName("sandstarwater");
        this.setLightLevel(1f);
    }

    @SideOnly(Side.CLIENT)
    public Vec3d getFogColor(World world, BlockPos pos, IBlockState state, Entity entity, Vec3d originalColor, float partialTicks) {
        float f12 = 0.0F;
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase ent = ((EntityLivingBase) entity);
            f12 = (float) EnchantmentHelper.getRespirationModifier(ent) * 0.2F;
            if (ent.isPotionActive(MobEffects.WATER_BREATHING)) {
                f12 = f12 * 0.3F + 0.6F;
            }
        }
        return new Vec3d(0.1F + f12, 0.1F + f12, 0.1F + f12);
    }

    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if (entityIn instanceof EntityLivingBase && entityIn.ticksExisted % 20 == 0) {
            ((EntityLivingBase) entityIn).heal(0.30f);
            if (entityIn instanceof EntityPlayer && !worldIn.isRemote) {
                ((EntityPlayer) entityIn).addPotionEffect(new PotionEffect(MobEffects.LUCK, 300, 0));
            }
        }
    }

    public Boolean isEntityInsideMaterial(IBlockAccess world, BlockPos blockpos, IBlockState iblockstate, Entity entity, double yToTest, Material materialIn, boolean testingHead) {
        return materialIn == Material.WATER ? true : super.isEntityInsideMaterial(world, blockpos, iblockstate, entity, yToTest, materialIn, testingHead);
    }
}