package com.tcn.cosmoslibrary.impl.interfaces.tile;

import com.tcn.cosmoslibrary.impl.enums.EnumConnectionType;

public interface IConnectionTypeTile {

	public EnumConnectionType CONNECTION_TYPE = EnumConnectionType.getStandardValue();

	public EnumConnectionType getConnectionType();
	
	public void setConnectionType(EnumConnectionType type, boolean update);
	
	public void cycleConnectionType(boolean update);

}