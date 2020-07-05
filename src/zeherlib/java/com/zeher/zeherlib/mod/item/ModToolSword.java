package com.zeher.zeherlib.mod.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModToolSword extends ItemSword {
	
	public boolean has_effect;
	
	public ModToolSword(ToolMaterial par2EnumToolMaterial2, CreativeTabs tab, String name, boolean effect) {
		super(par2EnumToolMaterial2);
		this.setCreativeTab(tab);
		
		this.setRegistryName(name);
		this.setUnlocalizedName(name);
		
		this.has_effect = effect;
	}
	
	public ModToolSword(ToolMaterial par2EnumToolMaterial2, CreativeTabs tab, String name) {
		super(par2EnumToolMaterial2);
		this.setCreativeTab(tab);
		
		this.setRegistryName(name);
		this.setUnlocalizedName(name);
		
		this.has_effect = false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isFull3D() {
		return true;
	}
	
	@Override
	public boolean hasEffect(ItemStack itemStack) {
		return this.has_effect;
	}
}