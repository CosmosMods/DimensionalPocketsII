package com.tcn.dimensionalpocketsii.pocket.client.container;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import com.tcn.cosmoslibrary.client.container.CosmosContainerMenuBlockEntity;
import com.tcn.dimensionalpocketsii.core.management.ObjectManager;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmithingRecipe;

public class ContainerModuleSmithingTable extends CosmosContainerMenuBlockEntity {
	public static final int TEMPLATE_SLOT = 0;
	public static final int BASE_SLOT = 1;
	public static final int ADDITIONAL_SLOT = 2;
	public static final int RESULT_SLOT = 3;

	private final List<Integer> inputSlotIndexes;
	protected final ResultContainer resultSlots = new ResultContainer();
	private final int resultSlotIndex = 3;
	
	protected final Container inputSlots = new SimpleContainer(3) {
		
		@Override
		public void setChanged() {
			super.setChanged();
			ContainerModuleSmithingTable.this.slotsChanged(this);
		}
	};
	
	@Nullable
	private SmithingRecipe selectedRecipe;
	private final List<SmithingRecipe> recipes;

	public ContainerModuleSmithingTable(int indexIn, Inventory playerInventoryIn, FriendlyByteBuf extraData) {
		this(indexIn, playerInventoryIn, ContainerLevelAccess.NULL, extraData.readBlockPos());
	}

	public ContainerModuleSmithingTable(int indexIn, Inventory playerInventoryIn, ContainerLevelAccess accessIn, BlockPos posIn) {
		super(ObjectManager.container_smithing_table, indexIn, playerInventoryIn, accessIn, posIn);
		ItemCombinerMenuSlotDefinition itemcombinermenuslotdefinition = this.createInputSlotDefinitions();
	    this.inputSlotIndexes = itemcombinermenuslotdefinition.getInputSlotIndexes();
		
		this.recipes = this.getLevel().getRecipeManager().getAllRecipesFor(RecipeType.SMITHING);
		this.addSlot(new Slot(inputSlots, 0, 30, 51));
		this.addSlot(new Slot(inputSlots, 1, 48, 51));
		this.addSlot(new Slot(inputSlots, 2, 66, 51));
		
		this.addSlot(new Slot(resultSlots, 3, 138, 51) {
			@Override
			public boolean mayPlace(ItemStack stackIn) {
				return false;
			}

			@Override
			public boolean mayPickup(Player playerIn) {
				return ContainerModuleSmithingTable.this.mayPickup(playerIn, this.hasItem());
			}

			@Override
			public void onTake(Player playerIn, ItemStack stackIn) {
				ContainerModuleSmithingTable.this.onTake(playerIn, stackIn);
			}
		});
		
		for (int k = 0; k < 3; ++k) {
			for (int i1 = 0; i1 < 9; ++i1) {
				this.addSlot(new Slot(playerInventoryIn, i1 + k * 9 + 9, 12 + i1 * 18, 88 + k * 18));
			}
		}

		for (int l = 0; l < 9; ++l) {
			this.addSlot(new Slot(playerInventoryIn, l, 12 + l * 18, 146));
		}

	}

	protected ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {
		return ItemCombinerMenuSlotDefinition.create().withSlot(0, 8, 48, (stack) -> {
			return this.recipes.stream().anyMatch((recipe) -> {
				return recipe.isTemplateIngredient(stack);
			});
		}).withSlot(1, 26, 48, (stack) -> {
			return this.recipes.stream().anyMatch((recipe) -> {
				return recipe.isBaseIngredient(stack);
			});
		}).withSlot(2, 44, 48, (stack) -> {
			return this.recipes.stream().anyMatch((recipe) -> {
				return recipe.isAdditionIngredient(stack);
			});
		}).withResultSlot(3, 98, 48).build();
	}

	@Override
	public void slotsChanged(Container containerIn) {
		super.slotsChanged(containerIn);
		
		if (containerIn == this.inputSlots) {
			this.createResult();
		}
	}

	@Override
	public void removed(Player playerIn) {
		super.removed(playerIn);
		this.access.execute((p_39796_, p_39797_) -> {
			this.clearContainer(playerIn, this.inputSlots);
		});
	}

	@Override
	public boolean stillValid(Player playerIn) {
		return stillValid(this.access, playerIn, ObjectManager.block_wall_smithing_table);
	}

	protected boolean mayPickup(Player p_40268_, boolean p_40269_) {
		return this.selectedRecipe != null && this.selectedRecipe.matches(this.inputSlots, this.getLevel());
	}

	protected void onTake(Player playerIn, ItemStack stackIn) {
		stackIn.onCraftedBy(playerIn.level(), playerIn, stackIn.getCount());
		this.resultSlots.awardUsedRecipes(playerIn, this.getRelevantItems());
		this.shrinkStackInSlot(0);
		this.shrinkStackInSlot(1);
		this.shrinkStackInSlot(2);
		this.access.execute((level, pos) -> {
			level.levelEvent(1044, pos, 0);
		});
	}

	private List<ItemStack> getRelevantItems() {
		return List.of(this.inputSlots.getItem(0), this.inputSlots.getItem(1), this.inputSlots.getItem(2));
	}

	private void shrinkStackInSlot(int p_40271_) {
		ItemStack itemstack = this.inputSlots.getItem(p_40271_);
		if (!itemstack.isEmpty()) {
			itemstack.shrink(1);
			this.inputSlots.setItem(p_40271_, itemstack);
		}

	}

	public void createResult() {
		List<SmithingRecipe> list = this.getLevel().getRecipeManager().getRecipesFor(RecipeType.SMITHING, this.inputSlots, this.getLevel());
		if (list.isEmpty()) {
			this.resultSlots.setItem(0, ItemStack.EMPTY);
		} else {
			SmithingRecipe smithingrecipe = list.get(0);
			ItemStack itemstack = smithingrecipe.assemble(this.inputSlots, this.getLevel().registryAccess());
			if (itemstack.isItemEnabled(this.getLevel().enabledFeatures())) {
				this.selectedRecipe = smithingrecipe;
				this.resultSlots.setRecipeUsed(smithingrecipe);
				this.resultSlots.setItem(0, itemstack);
			}
		}
	}

	public ItemStack quickMoveStack(Player p_39792_, int p_39793_) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(p_39793_);
		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			int i = this.getInventorySlotStart();
			int j = this.getUseRowEnd();
			if (p_39793_ == this.getResultSlot()) {
				if (!this.moveItemStackTo(itemstack1, i, j, true)) {
					return ItemStack.EMPTY;
				}

				slot.onQuickCraft(itemstack1, itemstack);
			} else if (this.inputSlotIndexes.contains(p_39793_)) {
				if (!this.moveItemStackTo(itemstack1, i, j, false)) {
					return ItemStack.EMPTY;
				}
			} else if (this.canMoveIntoInputSlots(itemstack1) && p_39793_ >= this.getInventorySlotStart()
					&& p_39793_ < this.getUseRowEnd()) {
				int k = this.getSlotToQuickMoveTo(itemstack);
				if (!this.moveItemStackTo(itemstack1, k, this.getResultSlot(), false)) {
					return ItemStack.EMPTY;
				}
			} else if (p_39793_ >= this.getInventorySlotStart() && p_39793_ < this.getInventorySlotEnd()) {
				if (!this.moveItemStackTo(itemstack1, this.getUseRowStart(), this.getUseRowEnd(), false)) {
					return ItemStack.EMPTY;
				}
			} else if (p_39793_ >= this.getUseRowStart() && p_39793_ < this.getUseRowEnd() && !this
					.moveItemStackTo(itemstack1, this.getInventorySlotStart(), this.getInventorySlotEnd(), false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.setByPlayer(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(p_39792_, itemstack1);
		}

		return itemstack;
	}

	private static Optional<Integer> findSlotMatchingIngredient(SmithingRecipe recipeIn, ItemStack stackIn) {
		if (recipeIn.isTemplateIngredient(stackIn)) {
			return Optional.of(0);
		} else if (recipeIn.isBaseIngredient(stackIn)) {
			return Optional.of(1);
		} else {
			return recipeIn.isAdditionIngredient(stackIn) ? Optional.of(2) : Optional.empty();
		}
	}

	public boolean canTakeItemForPickAll(ItemStack stackIn, Slot slotIn) {
		return slotIn.container != this.resultSlots && super.canTakeItemForPickAll(stackIn, slotIn);
	}

	public boolean canMoveIntoInputSlots(ItemStack stackIn) {
		return this.recipes.stream().map((recipe) -> {
			return findSlotMatchingIngredient(recipe, stackIn);
		}).anyMatch(Optional::isPresent);
	}

	public int getSlotToQuickMoveTo(ItemStack p_267159_) {
		return this.inputSlots.isEmpty() ? 0 : this.inputSlotIndexes.get(0);
	}

	public int getResultSlot() {
		return this.resultSlotIndex;
	}

	private int getInventorySlotStart() {
		return this.getResultSlot() + 1;
	}

	private int getInventorySlotEnd() {
		return this.getInventorySlotStart() + 27;
	}

	private int getUseRowStart() {
		return this.getInventorySlotEnd();
	}

	private int getUseRowEnd() {
		return this.getUseRowStart() + 9;
	}
}