package com.zeher.zeherlib.mod.block;

import com.zeher.zeherlib.api.compat.core.interfaces.ISidedTile;
import com.zeher.zeherlib.api.compat.util.CompatUtil;
import com.zeher.zeherlib.mod.util.ModUtil;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ModBlockContainer extends BlockContainer {

	public ModBlockContainer(String name, Material material, String tool, int harvest, int hardness, int resistance, CreativeTabs tab) {
		super(material);
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(tab);
		setHarvestLevel(tool, harvest);
		setHardness(hardness);
		setResistance(resistance);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return null;
	}
}