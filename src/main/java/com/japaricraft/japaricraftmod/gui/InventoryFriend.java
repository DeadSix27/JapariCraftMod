package com.japaricraft.japaricraftmod.gui;

import com.japaricraft.japaricraftmod.mob.ai.EntityFriend;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class InventoryFriend extends InventoryBasic
{

    private EntityFriend entityfriends;

    public InventoryFriend(EntityFriend entityfriends, int slotCount)
    {
        super(entityfriends.getName(), true, slotCount);

        this.entityfriends = entityfriends;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player)
    {
        if (this.entityfriends.isEntityAlive())
        {
            return (player.getDistanceSqToEntity(this.entityfriends) < 64.0D);
        }

        return false;
    }

    // TODO /* ======================================== MOD START =====================================*/

    public EntityFriend getContainerEntityChasts()
    {
        return this.entityfriends;
    }

    public void readInventoryFromNBT(NBTTagList nbtList)
    {
        this.clear();

        for (int slot = 0; slot < nbtList.tagCount(); ++slot)
        {
            NBTTagCompound nbt = nbtList.getCompoundTagAt(slot);
            slot = nbt.getByte("Slot") & 255;

            if (slot < this.getSizeInventory())
            {
                this.setInventorySlotContents(slot, new ItemStack(nbt));
            }
        }
    }

    public NBTTagList writeInventoryToNBT()
    {
        NBTTagList nbtList = new NBTTagList();

        for (int slot = 0; slot < this.getSizeInventory(); ++slot)
        {
            ItemStack stackSlot = this.getStackInSlot(slot);

            if (FriendMobHelper.isNotEmptyItemStack(stackSlot))
            {
                NBTTagCompound nbt = new NBTTagCompound();
                nbt.setByte("Slot", (byte) slot);
                stackSlot.writeToNBT(nbt);
                nbtList.appendTag(nbt);
            }
        }

        return nbtList;
    }

}