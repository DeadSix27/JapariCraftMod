package com.japaricraft.japaricraftmod.mob;

import com.japaricraft.japaricraftmod.JapariCraftMod;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;


public class Jariuma extends EntityTameable implements IRangedAttackMob{


    public Jariuma(World worldIn) {
        super(worldIn);
        this.setSize(0.7F, 0.9F);
        this.setTamed(false);
    }


    public EntityAgeable createChild(EntityAgeable ageable) {
        return null;
    }



    protected void initEntityAI() {
        super.initEntityAI();
        this.aiSit = new EntityAISit(this);

        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, this.aiSit);
        this.tasks.addTask(3, new EntityAIAttackRanged(this,1.05D, 20, 10.0F));
        this.tasks.addTask(5, new EntityAIFollowOwner(this, 1.0D, 10.0F, 2.0F));
        this.tasks.addTask(6, new EntityAIMate(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F));
        this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
        this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(24D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(30D);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
    }


    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.BLOCK_GRAVEL_BREAK;
    }

    @Override
    protected SoundEvent getHurtSound() {
        return SoundEvents.BLOCK_GRAVEL_BREAK;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.BLOCK_GRAVEL_STEP;
    }

    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEFINED;
    }

    public Item getDropItem() {

        return null;//なにも落とさない
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn)
    {
        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float)((int)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()));

        if (flag)
        {
            this.applyEnchantments(this, entityIn);
        }

        return flag;
    }


    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);

        if (this.isTamed())
        {
            if (this.isOwner(player) && !this.world.isRemote && !this.isBreedingItem(stack))
            {
                this.aiSit.setSitting(!this.isSitting());
                if (isSitting()){
                player.sendStatusMessage(new TextComponentTranslation("entity.active"), true);
            }else {
                    player.sendStatusMessage(new TextComponentTranslation("entity.weitting"), true);
                }

                return true;
            }
        }
        else if ( stack != null && stack.getItem() == Items.FLINT && player.getDistanceSqToEntity(this) < 22.0D)
        {
            if (!player.capabilities.isCreativeMode)
            {
                stack.setCount(stack.getCount()-1);
            }

            if (!this.world.isRemote)
            {
                if (this.rand.nextInt(3) == 0)
                {
                    player.addStat(JapariCraftMod.achievement_friend);
                    player.addStat(JapariCraftMod.achievement_jari);
                    this.setTamed(true);
                    this.setOwnerId(player.getUniqueID());
                    this.playTameEffect(true);
                    this.world.setEntityState(this, (byte)7);
                }
                else
                {
                    this.playTameEffect(false);
                    this.world.setEntityState(this, (byte)6);
                }


            }

            return true;
        }

        return super.processInteract(player, hand);
    }

    protected void updateAITasks()
    {
        if (this.ticksExisted % 5 == 0)
        {
            this.heal(0.1F);
        }
    }

    @Override
    public boolean canDespawn()
    {
        return false;
    }


    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor)
    {
        ThrowFlint entityflint = new ThrowFlint(this.world, this);
        double d3 = target.posX - this.posX;
        double d4 = target.posY - this.posY;
        double d5 = target.posZ - this.posZ;
        entityflint.setThrowableHeading(d3,d4,d5,1.6F, 12.0F);
        this.playSound(SoundEvents.ENTITY_SNOWMAN_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        this.world.spawnEntity(entityflint);
    }
}