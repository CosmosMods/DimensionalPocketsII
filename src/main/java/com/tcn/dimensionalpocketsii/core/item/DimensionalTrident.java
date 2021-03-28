package com.tcn.dimensionalpocketsii.core.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;
import com.tcn.dimensionalpocketsii.core.entity.DimensionalTridentEntity;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.IVanishable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.item.UseAction;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

@SuppressWarnings("unused")
public class DimensionalTrident extends TridentItem implements IVanishable {
	private ImmutableMultimap<Attribute, AttributeModifier> defaultModifiers;

	public DimensionalTrident(Item.Properties properties) {
		super(properties);
		Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 12.0D, AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", (double) 0.8F, AttributeModifier.Operation.ADDITION));
		this.defaultModifiers = builder.build();
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public boolean canAttackBlock(BlockState stateIn, World worldIn, BlockPos posIn, PlayerEntity playerIn) {
		return !playerIn.isCreative();
	}

	@Override
	public UseAction getUseAnimation(ItemStack stackIn) {
		return UseAction.SPEAR;
	}

	@Override
	public void releaseUsing(ItemStack stackIn, World worldIn, LivingEntity livingEntityIn, int p_77615_4_) {
		if (livingEntityIn instanceof PlayerEntity) {
			PlayerEntity playerentity = (PlayerEntity) livingEntityIn;
			int i = this.getUseDuration(stackIn) - p_77615_4_;
			if (i >= 10) {
				int j = EnchantmentHelper.getRiptide(stackIn);
				if (j <= 0 || playerentity.isInWaterOrRain()) {
					if (!worldIn.isClientSide) {
						stackIn.hurtAndBreak(1, playerentity, (playerEntity) -> { playerEntity.broadcastBreakEvent(livingEntityIn.getUsedItemHand()); });
						
						if (j == 0) {
							DimensionalTridentEntity tridententity = new DimensionalTridentEntity(worldIn, playerentity, stackIn);
							tridententity.shootFromRotation(playerentity, playerentity.xRot, playerentity.yRot, 0.0F, 2.5F + (float) j * 0.5F, 1.0F);
							
							if (playerentity.abilities.instabuild) {
								tridententity.pickup = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
							}

							worldIn.addFreshEntity(tridententity);
							worldIn.playSound((PlayerEntity) null, tridententity, SoundEvents.TRIDENT_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F);
							if (!playerentity.abilities.instabuild) {
								playerentity.inventory.removeItem(stackIn);
							}
						}
					}

					playerentity.awardStat(Stats.ITEM_USED.get(this));
					if (j > 0) {
						float f7 = playerentity.yRot;
						float f = playerentity.xRot;
						float f1 = -MathHelper.sin(f7 * ((float) Math.PI / 180F))
								* MathHelper.cos(f * ((float) Math.PI / 180F));
						float f2 = -MathHelper.sin(f * ((float) Math.PI / 180F));
						float f3 = MathHelper.cos(f7 * ((float) Math.PI / 180F))
								* MathHelper.cos(f * ((float) Math.PI / 180F));
						float f4 = MathHelper.sqrt(f1 * f1 + f2 * f2 + f3 * f3);
						float f5 = 3.0F * ((1.0F + (float) j) / 4.0F);
						f1 = f1 * (f5 / f4);
						f2 = f2 * (f5 / f4);
						f3 = f3 * (f5 / f4);
						playerentity.push((double) f1, (double) f2, (double) f3);
						playerentity.startAutoSpinAttack(20);
						if (playerentity.isOnGround()) {
							float f6 = 1.1999999F;
							playerentity.move(MoverType.SELF, new Vector3d(0.0D, (double) 1.1999999F, 0.0D));
						}

						SoundEvent soundevent;
						if (j >= 3) {
							soundevent = SoundEvents.TRIDENT_RIPTIDE_3;
						} else if (j == 2) {
							soundevent = SoundEvents.TRIDENT_RIPTIDE_2;
						} else {
							soundevent = SoundEvents.TRIDENT_RIPTIDE_1;
						}

						worldIn.playSound((PlayerEntity) null, playerentity, soundevent, SoundCategory.PLAYERS, 1.0F, 1.0F);
					}

				}
			}
		}
	}

	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack itemstack = playerIn.getItemInHand(handIn);
		if (itemstack.getDamageValue() >= itemstack.getMaxDamage() - 1) {
			return ActionResult.fail(itemstack);
		} else if (EnchantmentHelper.getRiptide(itemstack) > 0 && !playerIn.isInWaterOrRain()) {
			return ActionResult.fail(itemstack);
		} else {
			playerIn.startUsingItem(handIn);
			return ActionResult.consume(itemstack);
		}
	}

	@Override
	public boolean hurtEnemy(ItemStack stackIn, LivingEntity livingEntityIn, LivingEntity livingEntityIn2) {
		stackIn.hurtAndBreak(1, livingEntityIn2, (livingEntity) -> { livingEntity.broadcastBreakEvent(EquipmentSlotType.MAINHAND); });
		return true;
	}

	@Override
	public boolean mineBlock(ItemStack stackIn, World worldIn, BlockState stateIn, BlockPos posIn, LivingEntity livingEntityIn) {
		if ((double) stateIn.getDestroySpeed(worldIn, posIn) != 0.0D) {
			stackIn.hurtAndBreak(2, livingEntityIn, (livingEntity) -> { livingEntity.broadcastBreakEvent(EquipmentSlotType.MAINHAND); });
		}

		return true;
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType slotTypeIn) {
		return slotTypeIn == EquipmentSlotType.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(slotTypeIn);
	}

	@Override
	public int getEnchantmentValue() {
		return 1;
	}
}
