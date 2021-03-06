package com.japaricraft.japaricraftmod.mob;

import com.google.common.collect.Sets;
import com.japaricraft.japaricraftmod.JapariCraftMod;
import com.japaricraft.japaricraftmod.gui.FriendMobNBTs;
import com.japaricraft.japaricraftmod.gui.InventoryFriendEquipment;
import com.japaricraft.japaricraftmod.gui.InventoryFriendMain;
import com.japaricraft.japaricraftmod.hander.JapariItems;
import com.japaricraft.japaricraftmod.mob.ai.EntityFriend;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import java.util.Set;

public class Araisan extends EntityFriend {

    private InventoryFriendMain inventoryFriendMain;
    private InventoryFriendEquipment inventoryFriendEquipment;
    private static final Set<Item> TAME_ITEMS = Sets.newHashSet(JapariItems.japariman,JapariItems.japarimanapple,JapariItems.japarimancocoa,JapariItems.japarimanfruit);
    private static final DataParameter<Boolean> HAT = EntityDataManager.createKey(Araisan.class, DataSerializers.BOOLEAN);

    public Araisan(World worldIn) {
        super(worldIn);
        this.setSize(0.6F, 1.7F);
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
        this.tasks.addTask(3, new EntityAIAvoidEntity<>(this,Cerulean.class, 6.5F, 1.1D, 1.1D));
        this.tasks.addTask(4, new EntityAIFollowOwner(this, 1.0D, 10.0F, 2.0F));
        this.tasks.addTask(5, new EntityAIMate(this, 1.0D));
        this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 5.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(24D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(30D);

        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);

        compound.setTag(FriendMobNBTs.ENTITY_FRIEND_INVENTORY, this.getInventoryFriendMain().writeInventoryToNBT());

        compound.setTag(FriendMobNBTs.ENTITY_FRIEND_EQUIPMENT, this.getInventoryFriendEquipment().writeInventoryToNBT());

    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);

        this.getInventoryFriendMain().readInventoryFromNBT(compound.getTagList(FriendMobNBTs.ENTITY_FRIEND_INVENTORY, 10));

        this.getInventoryFriendEquipment().readInventoryFromNBT(compound.getTagList(FriendMobNBTs.ENTITY_FRIEND_EQUIPMENT, 10));

    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_PLAYER_DEATH;
    }


    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEFINED;
    }

    public Item getDropItem() {

        return null;//なにも落とさない
    }

    @Override
    public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn)
    {
        ItemStack itemStack;

        switch (slotIn)
        {
            case CHEST :

                itemStack = this.getInventoryFriendEquipment().getChestItem();
                break;
            case FEET:

                itemStack = this.getInventoryFriendEquipment().getbootItem();
                break;

            default :

                itemStack = ItemStack.EMPTY;
                break;
        }

        return itemStack;
    }

    @Override
    public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack)
    {
        switch (slotIn)
        {
            case CHEST :

                this.getInventoryFriendEquipment().setInventorySlotContents(0,stack);
                break;
            case FEET:

                this.getInventoryFriendEquipment().setInventorySlotContents(1,stack);
                break;

            default :

                // none
                break;
        }
    }
    @Override
    public void onDeath(DamageSource cause)
    {
        World world = this.getEntityWorld();

        if (!world.isRemote)
        {
            InventoryHelper.dropInventoryItems(world, this, this.getInventoryFriendMain());

            InventoryHelper.dropInventoryItems(world, this, this.getInventoryFriendEquipment());
        }

        super.onDeath(cause);
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);

        if (this.isTamed())
        {
            if(player.isSneaking()&&!this.isSitting()){
                player.openGui(JapariCraftMod.instance,JapariCraftMod.ID_JAPARI_INVENTORY,this.getEntityWorld(), this.getEntityId(), 0, 0);
            }
            if (!stack.isEmpty()) {
                if (this.isOwner(player) && TAME_ITEMS.contains(stack.getItem())) {
                    ItemFood itemfood = (ItemFood) stack.getItem();
                    if(this.getHealth()<this.getMaxHealth()) {
                        if (!player.capabilities.isCreativeMode) {
                            stack.shrink(1);
                        }

                        this.heal((float) itemfood.getHealAmount(stack));
                        for (int i = 0; i < 7; ++i) {
                            double d0 = this.rand.nextGaussian() * 0.02D;
                            double d1 = this.rand.nextGaussian() * 0.02D;
                            double d2 = this.rand.nextGaussian() * 0.02D;
                            this.world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + 0.5D + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d0, d1, d2);
                        }
                        return true;
                    }
                }
            }
            if (this.isOwner(player) && !this.world.isRemote && !this.isBreedingItem(stack))
            {
                this.aiSit.setSitting(!this.isSitting());
                return true;
            }
        }
        else if (!this.isTamed() && TAME_ITEMS.contains(stack.getItem()))
        {
            if (!player.capabilities.isCreativeMode)
            {
                stack.setCount(stack.getCount()-1);
            }

            if (!this.world.isRemote)
            {
                if (this.rand.nextInt(3) == 0)
                {
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
            this.heal(0.06F);
        }
    }

    public InventoryFriendMain getInventoryFriendMain()
    {
        if (this.inventoryFriendMain == null)
        {
            this.inventoryFriendMain = new InventoryFriendMain(this);
        }

        return this.inventoryFriendMain;
    }
    public InventoryFriendEquipment getInventoryFriendEquipment()
    {
        if (this.inventoryFriendEquipment == null)
        {
            this.inventoryFriendEquipment = new InventoryFriendEquipment(this);
        }

        return this.inventoryFriendEquipment;
    }

    @Override
    public boolean canDespawn()
    {
        return false;
    }


}
