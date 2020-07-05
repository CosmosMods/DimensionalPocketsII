package com.zeher.zehercraft.processing.client.container.slot;

import com.zeher.zeherlib.core.recipe.CompactorRecipes;

import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class SlotCompactor extends Slot {
	
	private EntityPlayer player;
	private int amount;

	public SlotCompactor(EntityPlayer entity_player, IInventory inventory, int index, int x_pos, int y_pos) {
		super(inventory, index, x_pos, y_pos);
		this.player = entity_player;
	}

	public boolean isItemValid(ItemStack par1ItemStack) {
		return false;
	}

	public ItemStack decrStackSize(int par1) {
		if (this.getHasStack()) {
			this.amount += Math.min(par1, this.getStack().getCount());
		}
		return super.decrStackSize(par1);
	}

	public ItemStack onTake(EntityPlayer par1EntityPlayer, ItemStack par2ItemStack) {
		this.onCrafting(par2ItemStack);
		super.onTake(par1EntityPlayer, par2ItemStack);
		return par2ItemStack;
	}

	protected void onCrafting(ItemStack par1ItemStack, int par2) {
		this.amount += par2;
		this.onCrafting(par1ItemStack);
	}

	protected void onCrafting(ItemStack stack) {
		stack.onCrafting(this.player.world, this.player, this.amount);
		if (!this.player.world.isRemote) {
			int i = this.amount;
			float f = CompactorRecipes.getInstance().getCompactingExperience(stack);
			if (f == 0.0F) {
				i = 0;
			} else if (f < 1.0F) {
				int j = MathHelper.floor(i * f);
				if ((j < MathHelper.ceil(i * f)) && ((float) Math.random() < i * f - j)) {
					j++;
				}
				i = j;
			}
			while (i > 0) {
				int j = EntityXPOrb.getXPSplit(i);
				i -= j;
				this.player.world.spawnEntity(new EntityXPOrb(this.player.world, this.player.posX, this.player.posY + 0.5D, this.player.posZ + 0.5D, j));
			}
		}
		this.amount = 0;
	}
}