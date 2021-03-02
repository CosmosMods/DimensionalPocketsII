package com.tcn.cosmoslibrary.impl.registry.object;

import com.tcn.cosmoslibrary.impl.enums.EnumConnectionType;
import com.tcn.cosmoslibrary.impl.nbt.UtilNBT;
import com.tcn.cosmoslibrary.impl.nbt.UtilNBT.Const;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;

public class ObjectConnectionType {

	private static final String NBT_TYPE_KEY = "type";
	
	private BlockPos pos;
	private EnumConnectionType type;
	
	public ObjectConnectionType(BlockPos posIn, EnumConnectionType typeIn) {
		this.setPos(posIn);
		this.setType(typeIn);
	}

	public BlockPos getPos() {
		return this.pos;
	}

	public void setPos(BlockPos pos) {
		this.pos = pos;
	}

	public EnumConnectionType getType() {
		return this.type;
	}

	public void setType(EnumConnectionType type) {
		this.type = type;
	}
	
	public static ObjectConnectionType readFromNBT(CompoundNBT compound) {
		CompoundNBT block_pos = compound.getCompound(Const.NBT_POS_KEY);
		
		int x = block_pos.getInt(UtilNBT.Const.NBT_POS_X_KEY);
		int y = block_pos.getInt(UtilNBT.Const.NBT_POS_Y_KEY);
		int z = block_pos.getInt(UtilNBT.Const.NBT_POS_Z_KEY);

		BlockPos pos = new BlockPos(x, y, z);
		
		String s = compound.getString(NBT_TYPE_KEY);
		
		EnumConnectionType type = EnumConnectionType.getStateFromName(s);
		
		return new ObjectConnectionType(pos, type);
	}
	
	public void writeToNBT(CompoundNBT compound) {
		String name = this.getType().getName();
		
		compound.putString(NBT_TYPE_KEY, name);
		
		int x = this.getPos().getX();
		int y = this.getPos().getY();
		int z = this.getPos().getZ();
		
		CompoundNBT block_pos = new CompoundNBT();
		
		block_pos.putInt(UtilNBT.Const.NBT_POS_X_KEY, x);
		block_pos.putInt(UtilNBT.Const.NBT_POS_Y_KEY, y);
		block_pos.putInt(UtilNBT.Const.NBT_POS_Z_KEY, z);
		
		compound.put(Const.NBT_POS_KEY, block_pos);
	}
}
