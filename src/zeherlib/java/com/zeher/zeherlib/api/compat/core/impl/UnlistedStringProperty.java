package com.zeher.zeherlib.api.compat.core.impl;

import java.util.Collection;
import java.util.Optional;

import net.minecraft.state.IProperty;

public class UnlistedStringProperty implements IProperty<String> {
	
	private String name;

	public UnlistedStringProperty(String name) {
		this.name = name;
	}

	@Override
	public Collection<String> getAllowedValues() {
		return null;
	}

	@Override
	public Class<String> getValueClass() {
		return String.class;
	}

	@Override
	public Optional<String> parseValue(String value) {
		return Optional.of(value);
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getName(String value) {
		return this.name;
	}
}