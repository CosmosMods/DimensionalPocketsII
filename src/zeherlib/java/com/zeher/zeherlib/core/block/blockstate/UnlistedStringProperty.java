package com.zeher.zeherlib.core.block.blockstate;

import net.minecraftforge.common.property.IUnlistedProperty;

public class UnlistedStringProperty implements IUnlistedProperty<String> {
	
	private String name;

	public UnlistedStringProperty(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isValid(String value) {
		return value != null;
	}

	@Override
	public Class<String> getType() {
		return String.class;
	}

	@Override
	public String valueToString(String value) {
		return value;
	}
}