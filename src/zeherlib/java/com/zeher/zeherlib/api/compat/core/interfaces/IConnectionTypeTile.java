package com.zeher.zeherlib.api.compat.core.interfaces;

public interface IConnectionTypeTile {

	public EnumConnectionType TYPE = EnumConnectionType.getStandardValue();

	public EnumConnectionType getType();
	
	public void setType(EnumConnectionType type);
	
	public void cycleType();

}