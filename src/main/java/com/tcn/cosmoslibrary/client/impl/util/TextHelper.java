package com.tcn.cosmoslibrary.client.impl.util;

import java.util.List;

import org.lwjgl.glfw.GLFW;

import com.tcn.cosmoslibrary.CosmosReference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.InputMappings;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.text.ITextProperties;
import net.minecraftforge.fluids.FluidStack;

public final class TextHelper {
	
	public static final String BLACK = (char) 167 + "0";
	public static final String BLUE = (char) 167 + "1";
	public static final String GREEN = (char) 167 + "2";
	public static final String TEAL = (char) 167 + "3";
	public static final String RED = (char) 167 + "4";
	public static final String PURPLE = (char) 167 + "5";
	public static final String ORANGE = (char) 167 + "6";
	public static final String LIGHT_GRAY = (char) 167 + "7";
	public static final String GRAY = (char) 167 + "8";
	public static final String LIGHT_BLUE = (char) 167 + "9";
	public static final String BRIGHT_GREEN = (char) 167 + "a";
	public static final String BRIGHT_BLUE = (char) 167 + "b";
	public static final String LIGHT_RED = (char) 167 + "c";
	public static final String PINK = (char) 167 + "d";
	public static final String YELLOW = (char) 167 + "e";
	public static final String WHITE = (char) 167 + "f";

	public static final String OBFUSCATED = (char) 167 + "k";
	public static final String BOLD = (char) 167 + "l";
	public static final String STRIKETHROUGH = (char) 167 + "m";
	public static final String UNDERLINE = (char) 167 + "n";
	public static final String ITALIC = (char) 167 + "o";
	public static final String END = (char) 167 + "r";

	public static boolean isAltKeyDown(Minecraft mc) {
		return (InputMappings.isKeyDown(mc.getMainWindow().getHandle(), GLFW.GLFW_KEY_LEFT_ALT) || InputMappings.isKeyDown(mc.getMainWindow().getHandle(), GLFW.GLFW_KEY_RIGHT_ALT));
	}

	public static boolean isControlKeyDown(Minecraft mc) {
		return (InputMappings.isKeyDown(mc.getMainWindow().getHandle(), GLFW.GLFW_KEY_LEFT_CONTROL) || InputMappings.isKeyDown(mc.getMainWindow().getHandle(), GLFW.GLFW_KEY_RIGHT_CONTROL));
	}

	public static boolean isShiftKeyDown(Minecraft mc) {
		return (InputMappings.isKeyDown(mc.getMainWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT) || InputMappings.isKeyDown(mc.getMainWindow().getHandle(), GLFW.GLFW_KEY_RIGHT_SHIFT));
	}

	public static int getSplitStringHeight(FontRenderer fontRenderer, ITextProperties input, int width) {
		List<IReorderingProcessor> stringRows = fontRenderer.trimStringToWidth(input, width);
		return stringRows.size() * fontRenderer.FONT_HEIGHT;
	}

	public static String camelCase(String input) {
		return input.substring(0, 1).toLowerCase() + input.substring(1);
	}

	public static String titleCase(String input) {
		return input.substring(0, 1).toUpperCase() + input.substring(1);
	}

	public static String localize(String key) {
		return I18n.format(key);
	}

	public static String getFluidName(FluidStack fluid) {
		return getFluidName(fluid.getFluid());
	}

	public static String getFluidName(Fluid fluid) {
		String fluidName = "";
		if (fluid.getAttributes().getTemperature() > 1000) {
			fluidName = fluidName + RED;
		} else {
			fluidName = fluidName + BLUE;
		}
		fluidName = fluidName + localize(fluid.getRegistryName().toString()) + END;

		return fluidName;
	}

	public static String getScaledNumber(int number) {
		return getScaledNumber(number, 2);
	}

	public static String getScaledNumber(int number, int minDigits) {
		String numString = "";

		int numMod = 10 * minDigits;
		if (number > 100000 * numMod) {
			numString = numString + number / 1000000 + "M";
		} else if (number > 100 * numMod) {
			numString = numString + number / 1000 + "k";
		} else {
			numString = numString + number;
		}
		return numString;
	}

	public static String shiftForMoreDetails() {
		return WHITE + localize(CosmosReference.RESOURCE.BASE.TOOLTIP_HOLD) + " " + ORANGE + BOLD + localize(CosmosReference.RESOURCE.BASE.TOOLTIP_SHIFT) + " " + END + WHITE + localize(CosmosReference.RESOURCE.BASE.TOOLTIP_FOR_DETAILS) + END;
	}
	
	public static String shiftForLessDetails() {
		return WHITE + localize(CosmosReference.RESOURCE.BASE.TOOLTIP_RELEASE) + " " + ORANGE + BOLD + localize(CosmosReference.RESOURCE.BASE.TOOLTIP_SHIFT) + " " + END + WHITE + localize(CosmosReference.RESOURCE.BASE.TOOLTIP_LESS_DETAILS) + END;
	}
	
	public static String ctrlForMoreDetails() {
		return WHITE + localize(CosmosReference.RESOURCE.BASE.TOOLTIP_HOLD) + " " + PURPLE + BOLD + localize(CosmosReference.RESOURCE.BASE.TOOLTIP_CTRL) + " " + END + WHITE + localize(CosmosReference.RESOURCE.BASE.TOOLTIP_NBT) + END;
	}
	
	public static String ctrlForLessDetails() {
		return WHITE + localize(CosmosReference.RESOURCE.BASE.TOOLTIP_RELEASE) + " " + PURPLE + BOLD + localize(CosmosReference.RESOURCE.BASE.TOOLTIP_CTRL) + " " + END + WHITE + localize(CosmosReference.RESOURCE.BASE.TOOLTIP_NBT_LESS_DETAILS) + END;
	}

	public static String getActivationText(String key) {
		return BRIGHT_BLUE + localize(key) + END;
	}
	
	public static String getInfoText(String key) {
		return LIGHT_GRAY + localize(key) + END;
	}

	public static String getDescOneText(String key) {
		return TEAL + localize(key) + END;
	}

	public static String getDescTwoText(String key) {
		return GREEN + localize(key) + END;
	}
	
	public static String getDescThreeText(String key) {
		return LIGHT_BLUE + localize(key) + END;
	}
	
	public static String getLimitationText(String key) {
		return LIGHT_RED + localize(key) + END;
	}

	public static boolean displayShiftForDetail = true;
	public static boolean displayStackCount = false;
}
