package com.tcn.dimensionalpocketsii.pocket.client.container;

import java.util.Optional;

import com.tcn.dimensionalpocketsii.core.management.CoreModBusManager;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeBookCategory;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ContainerCrafter extends RecipeBookContainer<CraftingInventory> {
	private final CraftingInventory craftSlots = new CraftingInventory(this, 3, 3);
	private final CraftResultInventory resultSlots = new CraftResultInventory();
	private final IWorldPosCallable access;
	private final PlayerEntity player;

	private final World world;
	private final BlockPos pos;

	public ContainerCrafter(int p_i50089_1_, PlayerInventory p_i50089_2_, PacketBuffer extraData) {
		this(p_i50089_1_, p_i50089_2_, IWorldPosCallable.NULL, extraData.readBlockPos());
	}

	public ContainerCrafter(int p_i50090_1_, PlayerInventory p_i50090_2_, IWorldPosCallable p_i50090_3_, BlockPos pos) {
		super(CoreModBusManager.CRAFTER_CONTAINER_TYPE, p_i50090_1_);
		this.pos = pos;
		this.world = p_i50090_2_.player.level;
		
		this.access = p_i50090_3_;
		this.player = p_i50090_2_.player;
		this.addSlot(new CraftingResultSlot(p_i50090_2_.player, this.craftSlots, this.resultSlots, 0, 128, 35));

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				this.addSlot(new Slot(this.craftSlots, j + i * 3, 34 + j * 18, 17 + i * 18));
			}
		}

		for (int k = 0; k < 3; ++k) {
			for (int i1 = 0; i1 < 9; ++i1) {
				this.addSlot(new Slot(p_i50090_2_, i1 + k * 9 + 9, 12 + i1 * 18, 88 + k * 18));
			}
		}

		for (int l = 0; l < 9; ++l) {
			this.addSlot(new Slot(p_i50090_2_, l, 12 + l * 18, 146));
		}

	}

	protected static void slotChangedCraftingGrid(int p_217066_0_, World p_217066_1_, PlayerEntity p_217066_2_,
			CraftingInventory p_217066_3_, CraftResultInventory p_217066_4_) {
		if (!p_217066_1_.isClientSide) {
			ServerPlayerEntity serverplayerentity = (ServerPlayerEntity) p_217066_2_;
			ItemStack itemstack = ItemStack.EMPTY;
			Optional<ICraftingRecipe> optional = p_217066_1_.getServer().getRecipeManager()
					.getRecipeFor(IRecipeType.CRAFTING, p_217066_3_, p_217066_1_);
			if (optional.isPresent()) {
				ICraftingRecipe icraftingrecipe = optional.get();
				if (p_217066_4_.setRecipeUsed(p_217066_1_, serverplayerentity, icraftingrecipe)) {
					itemstack = icraftingrecipe.assemble(p_217066_3_);
				}
			}

			p_217066_4_.setItem(0, itemstack);
			serverplayerentity.connection.send(new SSetSlotPacket(p_217066_0_, 0, itemstack));
		}
	}

	public void slotsChanged(IInventory p_75130_1_) {
		this.access.execute((p_217069_1_, p_217069_2_) -> {
			slotChangedCraftingGrid(this.containerId, p_217069_1_, this.player, this.craftSlots, this.resultSlots);
		});
	}

	public void fillCraftSlotsStackedContents(RecipeItemHelper p_201771_1_) {
		this.craftSlots.fillStackedContents(p_201771_1_);
	}

	public void clearCraftingContent() {
		this.craftSlots.clearContent();
		this.resultSlots.clearContent();
	}

	public boolean recipeMatches(IRecipe<? super CraftingInventory> p_201769_1_) {
		return p_201769_1_.matches(this.craftSlots, this.player.level);
	}

	public void removed(PlayerEntity p_75134_1_) {
		super.removed(p_75134_1_);
		this.access.execute((p_217068_2_, p_217068_3_) -> {
			this.clearContainer(p_75134_1_, p_217068_2_, this.craftSlots);
		});
	}

	public boolean stillValid(PlayerEntity p_75145_1_) {
		return stillValid(this.access, p_75145_1_, CoreModBusManager.BLOCK_WALL_CRAFTER);
	}

	public ItemStack quickMoveStack(PlayerEntity p_82846_1_, int p_82846_2_) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(p_82846_2_);
		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			if (p_82846_2_ == 0) {
				this.access.execute((p_217067_2_, p_217067_3_) -> {
					itemstack1.getItem().onCraftedBy(itemstack1, p_217067_2_, p_82846_1_);
				});
				if (!this.moveItemStackTo(itemstack1, 10, 46, true)) {
					return ItemStack.EMPTY;
				}

				slot.onQuickCraft(itemstack1, itemstack);
			} else if (p_82846_2_ >= 10 && p_82846_2_ < 46) {
				if (!this.moveItemStackTo(itemstack1, 1, 10, false)) {
					if (p_82846_2_ < 37) {
						if (!this.moveItemStackTo(itemstack1, 37, 46, false)) {
							return ItemStack.EMPTY;
						}
					} else if (!this.moveItemStackTo(itemstack1, 10, 37, false)) {
						return ItemStack.EMPTY;
					}
				}
			} else if (!this.moveItemStackTo(itemstack1, 10, 46, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			ItemStack itemstack2 = slot.onTake(p_82846_1_, itemstack1);
			if (p_82846_2_ == 0) {
				p_82846_1_.drop(itemstack2, false);
			}
		}

		return itemstack;
	}

	public boolean canTakeItemForPickAll(ItemStack p_94530_1_, Slot p_94530_2_) {
		return p_94530_2_.container != this.resultSlots && super.canTakeItemForPickAll(p_94530_1_, p_94530_2_);
	}

	public int getResultSlotIndex() {
		return 0;
	}

	public int getGridWidth() {
		return this.craftSlots.getWidth();
	}

	public int getGridHeight() {
		return this.craftSlots.getHeight();
	}

	@OnlyIn(Dist.CLIENT)
	public int getSize() {
		return 10;
	}

	@OnlyIn(Dist.CLIENT)
	public RecipeBookCategory getRecipeBookType() {
		return RecipeBookCategory.CRAFTING;
	}

	public World getWorld() {
		return world;
	}

	public BlockPos getBlockPos() {
		return pos;
	}
}
