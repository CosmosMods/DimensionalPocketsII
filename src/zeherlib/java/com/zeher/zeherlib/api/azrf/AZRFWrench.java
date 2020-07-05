package com.zeher.zeherlib.api.azrf;

import com.zeher.zeherlib.api.core.interfaces.IWrenchAdvanced;
import com.zeher.zeherlib.mod.item.ModItem;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class AZRFWrench extends ModItem implements IWrenchAdvanced {

	private static int maxDamage = 200;

	public AZRFWrench(String name, CreativeTabs tab) {
		super(name, tab);
		this.setMaxStackSize(1);
		this.setMaxDamage(maxDamage);
		this.setNoRepair();
	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		return EnumActionResult.FAIL;
	}

	public boolean doesSneakBypassUse(ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player) {
		return true;
	}

	@Override
	public boolean isActive(ItemStack paramstack) {
		return true;
	}

	@Override
	public boolean canDestroyBlockInCreative(World world, BlockPos pos, ItemStack stack, EntityPlayer player) {
		return false;
	}
}