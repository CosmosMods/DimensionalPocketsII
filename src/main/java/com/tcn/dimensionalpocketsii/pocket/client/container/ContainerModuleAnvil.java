package com.tcn.dimensionalpocketsii.pocket.client.container;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.tcn.cosmoslibrary.client.container.CosmosContainerMenuBlockEntity;
import com.tcn.dimensionalpocketsii.core.management.ObjectManager;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AnvilUpdateEvent;

@SuppressWarnings("unused")
public class ContainerModuleAnvil extends CosmosContainerMenuBlockEntity {
	public static final int INPUT_SLOT = 0;
	public static final int ADDITIONAL_SLOT = 1;
	public static final int RESULT_SLOT = 2;

	private static final Logger LOGGER = LogUtils.getLogger();
	private static final boolean DEBUG_COST = false;
	public static final int MAX_NAME_LENGTH = 50;
	public int repairItemCountCost;
	private String itemName;
	private final DataSlot cost = DataSlot.standalone();
	private static final int COST_FAIL = 0;
	private static final int COST_BASE = 1;
	private static final int COST_ADDED_BASE = 1;
	private static final int COST_REPAIR_MATERIAL = 1;
	private static final int COST_REPAIR_SACRIFICE = 2;
	private static final int COST_INCOMPATIBLE_PENALTY = 1;
	private static final int COST_RENAME = 1;

	protected final ResultContainer resultSlots = new ResultContainer();
	
	protected final Container inputSlots = new SimpleContainer(2) {
		
		@Override
		public void setChanged() {
			super.setChanged();
			ContainerModuleAnvil.this.slotsChanged(this);
		}
	};
	
	public ContainerModuleAnvil(int indexIn, Inventory playerInventoryIn, FriendlyByteBuf extraData) {
		this(indexIn, playerInventoryIn, ContainerLevelAccess.NULL, extraData.readBlockPos());
	}

	public ContainerModuleAnvil(int indexIn, Inventory playerInventoryIn, ContainerLevelAccess accessIn, BlockPos posIn) {
		super(ObjectManager.container_anvil, indexIn, playerInventoryIn, accessIn, posIn);
		
		this.addSlot(new Slot(inputSlots, 0, 30, 51));
		this.addSlot(new Slot(inputSlots, 1, 79, 51));
		this.addSlot(new Slot(resultSlots, 2, 138, 51) {
			@Override
			public boolean mayPlace(ItemStack stackIn) {
				return false;
			}

			@Override
			public boolean mayPickup(Player playerIn) {
				return ContainerModuleAnvil.this.mayPickup(playerIn, this.hasItem());
			}

			@Override
			public void onTake(Player playerIn, ItemStack stackIn) {
				ContainerModuleAnvil.this.onTake(playerIn, stackIn);
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
		
	    this.addDataSlot(this.cost);
	}

	protected boolean mayPickup(Player playerIn, boolean bool) {
		 return (playerIn.getAbilities().instabuild || playerIn.experienceLevel >= this.cost.get()) && this.cost.get() > 0;
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
		return stillValid(this.access, playerIn, ObjectManager.block_wall_anvil);
	}

	protected void onTake(Player playerIn, ItemStack stackIn) {
		if (!playerIn.getAbilities().instabuild) {
			playerIn.giveExperienceLevels(-this.cost.get());
		}

		float breakChance = ForgeHooks.onAnvilRepair(playerIn, stackIn, ContainerModuleAnvil.this.inputSlots.getItem(0), ContainerModuleAnvil.this.inputSlots.getItem(1));
		
		this.inputSlots.setItem(0, ItemStack.EMPTY);
		if (this.repairItemCountCost > 0) {
			ItemStack itemstack = this.inputSlots.getItem(1);
			if (!itemstack.isEmpty() && itemstack.getCount() > this.repairItemCountCost) {
				itemstack.shrink(this.repairItemCountCost);
				this.inputSlots.setItem(1, itemstack);
			} else {
				this.inputSlots.setItem(1, ItemStack.EMPTY);
			}
		} else {
			this.inputSlots.setItem(1, ItemStack.EMPTY);
		}

		this.cost.set(0);
		this.access.execute((level, pos) -> {
			BlockState blockstate = level.getBlockState(pos);
			if (!playerIn.getAbilities().instabuild && blockstate.is(BlockTags.ANVIL)
					&& playerIn.getRandom().nextFloat() < breakChance) {
				BlockState blockstate1 = AnvilBlock.damage(blockstate);
				if (blockstate1 == null) {
					level.removeBlock(pos, false);
					level.levelEvent(1029, pos, 0);
				} else {
					level.setBlock(pos, blockstate1, 2);
					level.levelEvent(1030, pos, 0);
				}
			} else {
				level.levelEvent(1030, pos, 0);
			}

		});
	}

	private void shrinkStackInSlot(int slotIndex) {
		ItemStack itemstack = this.inputSlots.getItem(slotIndex);
		itemstack.shrink(1);
		this.inputSlots.setItem(slotIndex, itemstack);
	}

	public void createResult() {
		ItemStack itemstack = this.inputSlots.getItem(0);
		this.cost.set(1);
		int i = 0;
		int j = 0;
		int k = 0;
		if (itemstack.isEmpty()) {
			this.resultSlots.setItem(0, ItemStack.EMPTY);
			this.cost.set(0);
		} else {
			ItemStack itemstack1 = itemstack.copy();
			ItemStack itemstack2 = this.inputSlots.getItem(1);
			Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(itemstack1);
			j += itemstack.getBaseRepairCost() + (itemstack2.isEmpty() ? 0 : itemstack2.getBaseRepairCost());
			this.repairItemCountCost = 0;
			boolean flag = false;

			if (!onAnvilChange(this, itemstack, itemstack2, resultSlots, itemName, j, this.player))
				return;
			if (!itemstack2.isEmpty()) {
				flag = itemstack2.getItem() == Items.ENCHANTED_BOOK
						&& !EnchantedBookItem.getEnchantments(itemstack2).isEmpty();
				if (itemstack1.isDamageableItem() && itemstack1.getItem().isValidRepairItem(itemstack, itemstack2)) {
					int l2 = Math.min(itemstack1.getDamageValue(), itemstack1.getMaxDamage() / 4);
					if (l2 <= 0) {
						this.resultSlots.setItem(0, ItemStack.EMPTY);
						this.cost.set(0);
						return;
					}

					int i3;
					for (i3 = 0; l2 > 0 && i3 < itemstack2.getCount(); ++i3) {
						int j3 = itemstack1.getDamageValue() - l2;
						itemstack1.setDamageValue(j3);
						++i;
						l2 = Math.min(itemstack1.getDamageValue(), itemstack1.getMaxDamage() / 4);
					}

					this.repairItemCountCost = i3;
				} else {
					if (!flag && (!itemstack1.is(itemstack2.getItem()) || !itemstack1.isDamageableItem())) {
						this.resultSlots.setItem(0, ItemStack.EMPTY);
						this.cost.set(0);
						return;
					}

					if (itemstack1.isDamageableItem() && !flag) {
						int l = itemstack.getMaxDamage() - itemstack.getDamageValue();
						int i1 = itemstack2.getMaxDamage() - itemstack2.getDamageValue();
						int j1 = i1 + itemstack1.getMaxDamage() * 12 / 100;
						int k1 = l + j1;
						int l1 = itemstack1.getMaxDamage() - k1;
						if (l1 < 0) {
							l1 = 0;
						}

						if (l1 < itemstack1.getDamageValue()) {
							itemstack1.setDamageValue(l1);
							i += 2;
						}
					}

					Map<Enchantment, Integer> map1 = EnchantmentHelper.getEnchantments(itemstack2);
					boolean flag2 = false;
					boolean flag3 = false;

					for (Enchantment enchantment1 : map1.keySet()) {
						if (enchantment1 != null) {
							int i2 = map.getOrDefault(enchantment1, 0);
							int j2 = map1.get(enchantment1);
							j2 = i2 == j2 ? j2 + 1 : Math.max(j2, i2);
							boolean flag1 = enchantment1.canEnchant(itemstack);
							if (this.player.getAbilities().instabuild || itemstack.is(Items.ENCHANTED_BOOK)) {
								flag1 = true;
							}

							for (Enchantment enchantment : map.keySet()) {
								if (enchantment != enchantment1 && !enchantment1.isCompatibleWith(enchantment)) {
									flag1 = false;
									++i;
								}
							}

							if (!flag1) {
								flag3 = true;
							} else {
								flag2 = true;
								if (j2 > enchantment1.getMaxLevel()) {
									j2 = enchantment1.getMaxLevel();
								}

								map.put(enchantment1, j2);
								int k3 = 0;
								switch (enchantment1.getRarity()) {
								case COMMON:
									k3 = 1;
									break;
								case UNCOMMON:
									k3 = 2;
									break;
								case RARE:
									k3 = 4;
									break;
								case VERY_RARE:
									k3 = 8;
								}

								if (flag) {
									k3 = Math.max(1, k3 / 2);
								}

								i += k3 * j2;
								if (itemstack.getCount() > 1) {
									i = 40;
								}
							}
						}
					}

					if (flag3 && !flag2) {
						this.resultSlots.setItem(0, ItemStack.EMPTY);
						this.cost.set(0);
						return;
					}
				}
			}

			if (StringUtils.isBlank(this.itemName)) {
				if (itemstack.hasCustomHoverName()) {
					k = 1;
					i += k;
					itemstack1.resetHoverName();
				}
			} else if (!this.itemName.equals(itemstack.getHoverName().getString())) {
				k = 1;
				i += k;
				itemstack1.setHoverName(Component.literal(this.itemName));
			}
			if (flag && !itemstack1.isBookEnchantable(itemstack2))
				itemstack1 = ItemStack.EMPTY;

			this.cost.set(j + i);
			if (i <= 0) {
				itemstack1 = ItemStack.EMPTY;
			}

			if (k == i && k > 0 && this.cost.get() >= 40) {
				this.cost.set(39);
			}

			if (this.cost.get() >= 40 && !this.player.getAbilities().instabuild) {
				itemstack1 = ItemStack.EMPTY;
			}

			if (!itemstack1.isEmpty()) {
				int k2 = itemstack1.getBaseRepairCost();
				if (!itemstack2.isEmpty() && k2 < itemstack2.getBaseRepairCost()) {
					k2 = itemstack2.getBaseRepairCost();
				}

				if (k != i || k == 0) {
					k2 = calculateIncreasedRepairCost(k2);
				}

				itemstack1.setRepairCost(k2);
				EnchantmentHelper.setEnchantments(map, itemstack1);
			}

			this.resultSlots.setItem(0, itemstack1);
			this.broadcastChanges();
		}
	}

	@Override
	public ItemStack quickMoveStack(Player playerIn, int indexIn) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(indexIn);
		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			if (indexIn == 2) {
				if (!this.moveItemStackTo(itemstack1, 3, 39, true)) {
					return ItemStack.EMPTY;
				}

				slot.onQuickCraft(itemstack1, itemstack);
			} else if (indexIn != 0 && indexIn != 1) {
				if (indexIn >= 3 && indexIn < 39) {
					int i = this.shouldQuickMoveToAdditionalSlot(itemstack) ? 1 : 0;
					if (!this.moveItemStackTo(itemstack1, i, 2, false)) {
						return ItemStack.EMPTY;
					}
				}
			} else if (!this.moveItemStackTo(itemstack1, 3, 39, false)) {
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

			slot.onTake(playerIn, itemstack1);
		}

		return itemstack;
	}

	@Override
	public boolean canTakeItemForPickAll(ItemStack stackIn, Slot slotIn) {
		return slotIn.container != this.resultSlots && super.canTakeItemForPickAll(stackIn, slotIn);
	}

	protected boolean shouldQuickMoveToAdditionalSlot(ItemStack stackIn) {
		return false;
	}

	public static int calculateIncreasedRepairCost(int costIn) {
		return costIn * 2 + 1;
	}

	public void setItemName(String nameIn) {
		this.itemName = nameIn;
		if (this.getSlot(2).hasItem()) {
			ItemStack itemstack = this.getSlot(2).getItem();
			if (StringUtils.isBlank(nameIn)) {
				itemstack.resetHoverName();
			} else {
				itemstack.setHoverName(Component.literal(this.itemName));
			}
		}

		this.createResult();
	}

	public int getCost() {
		return this.cost.get();
	}

	public void setMaximumCost(int value) {
		this.cost.set(value);
	}
	
    public static boolean onAnvilChange(ContainerModuleAnvil container, @NotNull ItemStack left, @NotNull ItemStack right, Container outputSlot, String name, int baseCost, Player player) {
        AnvilUpdateEvent e = new AnvilUpdateEvent(left, right, name, baseCost, player);
        
        if (MinecraftForge.EVENT_BUS.post(e)) {
        	return false;
        }
        
        if (e.getOutput().isEmpty()) {
        	return true;
        }

        outputSlot.setItem(0, e.getOutput());
        container.setMaximumCost(e.getCost());
        container.repairItemCountCost = e.getMaterialCost();
        return false;
    }

}