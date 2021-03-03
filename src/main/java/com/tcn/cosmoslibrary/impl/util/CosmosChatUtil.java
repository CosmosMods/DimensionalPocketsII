package com.tcn.cosmoslibrary.impl.util;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.lwjgl.opengl.GL11;

import com.tcn.cosmoslibrary.client.impl.util.TextHelper;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.world.World;

public class CosmosChatUtil {
	
	public static EnumMap<Direction, Colour> FD_COLOURS = new EnumMap<>(Direction.class);
	private static Map<String, String> chatColours = new HashMap<>();
	
	static {
		// @formatter:off
		double alpha = 7.0;
		Colour purple = Colour.PURPLE.copy();
		purple.a = alpha;
		Colour orange = Colour.ORANGE.copy();
		orange.a = alpha;
		Colour yellow = Colour.YELLOW.copy();
		yellow.a = alpha;
		Colour blue = Colour.BLUE.copy();
		blue.a = alpha;
		Colour green = Colour.GREEN.copy();
		green.a = alpha;
		Colour red = Colour.RED.copy();
		red.a = alpha;

		FD_COLOURS.put(Direction.DOWN,  purple);
		FD_COLOURS.put(Direction.UP,    orange);
		FD_COLOURS.put(Direction.NORTH, yellow);
		FD_COLOURS.put(Direction.SOUTH, blue);
		FD_COLOURS.put(Direction.WEST,  green);
		FD_COLOURS.put(Direction.EAST,  red);
		// @formatter:on

		chatColours.put("BLACK", TextHelper.BLACK);
		chatColours.put("BLUE", TextHelper.BLUE);
		chatColours.put("GREEN", TextHelper.GREEN);
		chatColours.put("TEAL", TextHelper.TEAL);
		chatColours.put("RED", TextHelper.RED);
		chatColours.put("PURPLE", TextHelper.PURPLE);
		chatColours.put("ORANGE", TextHelper.ORANGE);
		chatColours.put("LIGHT_GRAY", TextHelper.LIGHT_GRAY);
		chatColours.put("GRAY", TextHelper.GRAY);
		chatColours.put("LIGHT_BLUE", TextHelper.LIGHT_BLUE);
		chatColours.put("BRIGHT_GREEN", TextHelper.BRIGHT_GREEN);
		chatColours.put("BRIGHT_BLUE", TextHelper.BRIGHT_BLUE);
		chatColours.put("RED", TextHelper.RED);
		chatColours.put("PINK", TextHelper.PINK);
		chatColours.put("YELLOW", TextHelper.YELLOW);
		chatColours.put("WHITE", TextHelper.WHITE);
	}
	
	public static String capitalizeString(String string) {
		if (string == null || string.isEmpty())
			return "";

		String firstLetter = string.substring(0, 1).toUpperCase();
		if (string.length() == 1)
			return firstLetter;

		String rest = string.substring(1).toLowerCase();
		return firstLetter + rest;
	}
	
	public static StringTextComponent createChatLink(String text, String url, boolean bold, boolean underline, boolean italic, Color color) {
		StringTextComponent link = new StringTextComponent(text);
		Style style = link.getStyle();
		style.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
		style.setBold(Boolean.valueOf(bold));
		style.setUnderlined(Boolean.valueOf(underline));
		style.setItalic(Boolean.valueOf(italic));
		style.setColor(color);
		return link;
	}
	
	public static String getColourByName(String colourName) {
		String colour = chatColours.get(colourName);
		return colour != null ? colour : TextHelper.WHITE;
	}

	public static final void sendPlayerMessage(PlayerEntity playerIn, boolean format, String message) {
		World world = playerIn.getEntityWorld();
		
		if (world.isRemote) {
			if (format) {
				TranslationTextComponent comp = new TranslationTextComponent(message);
				playerIn.sendMessage(comp, UUID.randomUUID());
			} else {
				StringTextComponent comp = new StringTextComponent(message);
				playerIn.sendMessage(comp, UUID.randomUUID());
			}
		}
	}
	
	public static class Colour {
		public static final Colour WHITE = new Colour(1.0, 1.0, 1.0, 1.0);

	    public static final Colour LIGHT_BLUE = new Colour(0.5, 0.5, 1.0, 1.0);
	    public static final Colour BLUE = new Colour(0.0, 0.0, 1.0, 1.0);
	    public static final Colour DARK_BLUE = new Colour(0.0, 0.0, 0.5, 1.0);

	    public static final Colour LIGHT_RED = new Colour(1.0, 0.5, 0.5, 1.0);
	    public static final Colour RED = new Colour(1.0, 0.2, 0.2, 1.0);
	    public static final Colour DARK_RED = new Colour(0.5, 0.0, 0.0, 1.0);

	    public static final Colour LIGHT_GREEN = new Colour(0.5, 1.0, 0.5, 1.0);
	    public static final Colour GREEN = new Colour(0.0, 1.0, 0.0, 1.0);

	    public static final Colour YELLOW = new Colour(1.0, 1.0, 0.0, 1.0);
	    public static final Colour CYAN = new Colour(0.0, 1.0, 1.0, 1.0);
	    public static final Colour PINK = new Colour(1.0, 0.0, 1.0, 1.0);

	    public static final Colour NEON_BLUE = new Colour(0.4, 0.8, 1.0, 1.0);
	    public static final Colour ORANGE = new Colour(0.9, 0.6, 0.2, 1.0);

	    public static final Colour PURPLE = new Colour(0.5, 0.0, 1.0, 1.0);

	    public static final Colour LIGHT_GREY = new Colour(0.8, 0.8, 0.8, 1.0);
	    public static final Colour GREY = new Colour(0.5, 0.5, 0.5, 1.0);
	    public static final Colour DARK_GREY = new Colour(0.2, 0.2, 0.2, 1.0);

	    public static final Colour BLACK = new Colour(0.0, 0.0, 0.0, 1.0);

	    public double r, g, b, a;

	    public Colour(double r, double g, double b, double a) {
	        this.r = r;
	        this.g = g;
	        this.b = b;
	        this.a = a;
	    }

	    public Colour(int colour) {
	        this.a = (colour >> 24 & 255) / 255.0F;
	        this.r = (colour >> 16 & 255) / 255.0F;
	        this.g = (colour >> 8 & 255) / 255.0F;
	        this.b = (colour & 255) / 255.0F;

	    }

	    public Colour add(Colour o) {
	        r += o.r;
	        g += o.g;
	        b += o.b;
	        a += o.a;
	        return this;
	    }

	    public Colour sub(Colour o) {
	        r -= o.r;
	        g -= o.g;
	        b -= o.b;
	        a -= o.a;
	        return this;
	    }

	    public Colour invert() {
	        r = 1.0 - r;
	        g = 1.0 - g;
	        b = 1.0 - b;
	        a = 1.0 - a;
	        return this;
	    }

	    public Colour multiply(Colour o) {
	        r *= o.r;
	        g *= o.g;
	        b *= o.b;
	        a *= o.a;
	        return this;
	    }

	    public Colour multiply(double num) {
	        r *= num;
	        g *= num;
	        b *= num;
	        a *= num;
	        return this;
	    }

	    public Colour copy() {
	        return clone();
	    }

	    @Override
	    protected final Colour clone() {
	        try {
	            return (Colour) super.clone();
	        } catch (CloneNotSupportedException ignored) {
	        }
	        return new Colour(r, g, b, a);
	    }

	    public int getInt() {
	        return getInt(r, g, b, a);
	    }

	    public void doGLColor4() {
	        GL11.glColor4d(r, g, b, a);
	    }

	    public boolean equals(Colour o) {
	        return o != null && r == o.r && g == o.g && b == o.b && a == o.a;
	    }

	    public static int getInt(double r, double g, double b, double a) {
	        int temp = 0;
	        temp = temp | ((int) (a * 255) << 24);
	        temp = temp | ((int) (r * 255) << 16);
	        temp = temp | ((int) (g * 255) << 8);
	        temp = temp | ((int) (b * 255));
	        return temp;
	    }
	}
}
