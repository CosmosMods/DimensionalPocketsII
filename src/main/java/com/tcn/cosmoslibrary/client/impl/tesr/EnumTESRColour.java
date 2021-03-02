package com.tcn.cosmoslibrary.client.impl.tesr;

import com.tcn.cosmoslibrary.client.impl.util.TextHelper;

import net.minecraft.util.IStringSerializable;

/**
 * Used for custom colour objects for {@link TileEntitySpecialRenderer}.
 * @param name [basic name of the colour]
 * @param colour [R-G-B] {% colour of each channel]
 */
public enum EnumTESRColour implements IStringSerializable {
	
	BLACK(0, "black", "Black", TextHelper.BLACK, 0.1F, 0.1F, 0.1F),
    GRAY(1, "gray", "Gray", TextHelper.GRAY, 0.5F, 0.5F, 0.5F),
    WHITE(2, "white", "White", TextHelper.WHITE, 0.9F, 0.9F, 0.9F),
	
	RED(3, "red", "Red", TextHelper.RED, 255F/255F, 43F/255F, 39F/255F),
	
	GREEN(4, "green", "Green", TextHelper.GREEN, 54F/255F, 75F/255F, 24F/255F),
	LIGHT_GREEN(5, "light_green", "Light Green", TextHelper.BRIGHT_GREEN, 54F/255F, 255F/255F, 24F/255F),
	DARK_GREEN(6, "dark_green", "Dark Green", TextHelper.GREEN, 16F/255F, 65F/255F, 53F/255F),
    
	BLUE(7, "blue", "Blue", TextHelper.BLUE, 37F/255F, 49F/255F, 147F/255F),
	LIGHT_BLUE(8, "light_blue", "Light Blue", TextHelper.BRIGHT_BLUE, 99F/255F, 135F/255F, 210F/255F),
    
	ORANGE(9, "orange", "Orange", TextHelper.ORANGE, 255F/255F, 102F/255F, 0F/255F),
	
    PURPLE(10, "purple", "Purple", TextHelper.PURPLE, 84/255F , 26/255F, 140/255F),
    YELLOW(11, "yellow", "Yellow", TextHelper.YELLOW, 209F/255F, 199F/255F, 0F/255F);
	
	private int index;
    private final String name;
    private final float[] colour;
    private final String text_colour;
    private final String display_name;

    EnumTESRColour(int index, String name, String display_name, String text_colour, float... conversionColorParticles){
    	this.index = index;
        this.name = name;
        this.colour = conversionColorParticles;
        this.text_colour = text_colour;
        this.display_name = display_name;
    }
    
    @Override
    public String getString() {
    	return this.name;
    }

	public float[] getColour() {
		return this.colour;
	}
	
	public String getTextColour() {
		return this.text_colour;
	}
	
	public String getDisplayName() {
		return this.display_name;
	}
	
	public int getIndex() {
		return this.index;
	}
}