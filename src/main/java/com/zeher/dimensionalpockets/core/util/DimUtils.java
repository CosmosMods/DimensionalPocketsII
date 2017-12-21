package com.zeher.dimensionalpockets.core.util;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import com.zeher.dimensionalpockets.DimensionalPockets;
import com.zeher.trzlib.api.TRZTextUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;


public class DimUtils {

	public static EnumMap<EnumFacing, Colour> FD_COLOURS = new EnumMap<>(EnumFacing.class);

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

		FD_COLOURS.put(EnumFacing.DOWN,  purple);
		FD_COLOURS.put(EnumFacing.UP,    orange);
		FD_COLOURS.put(EnumFacing.NORTH, yellow);
		FD_COLOURS.put(EnumFacing.SOUTH, blue);
		FD_COLOURS.put(EnumFacing.WEST,  green);
		FD_COLOURS.put(EnumFacing.EAST,  red);
		// @formatter:on

		chatColours.put("BLACK", TRZTextUtil.BLACK);
		chatColours.put("BLUE", TRZTextUtil.BLUE);
		chatColours.put("GREEN", TRZTextUtil.GREEN);
		chatColours.put("TEAL", TRZTextUtil.TEAL);
		chatColours.put("RED", TRZTextUtil.RED);
		chatColours.put("PURPLE", TRZTextUtil.PURPLE);
		chatColours.put("ORANGE", TRZTextUtil.ORANGE);
		chatColours.put("LIGHT_GRAY", TRZTextUtil.LIGHT_GRAY);
		chatColours.put("GRAY", TRZTextUtil.GRAY);
		chatColours.put("LIGHT_BLUE", TRZTextUtil.LIGHT_BLUE);
		chatColours.put("BRIGHT_GREEN", TRZTextUtil.BRIGHT_GREEN);
		chatColours.put("BRIGHT_BLUE", TRZTextUtil.BRIGHT_BLUE);
		chatColours.put("RED", TRZTextUtil.RED);
		chatColours.put("PINK", TRZTextUtil.PINK);
		chatColours.put("YELLOW", TRZTextUtil.YELLOW);
		chatColours.put("WHITE", TRZTextUtil.WHITE);
	}

	public static EnumFacing getDirectionFromBitMask(int num) {
		switch (num) {
			case 0:
				return EnumFacing.SOUTH;
			case 1:
				return EnumFacing.WEST;
			case 2:
				return EnumFacing.NORTH;
			case 3:
				return EnumFacing.EAST;
			default:
				return null;
		}
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

	public static NBTTagCompound getPlayerPersistTag(EntityPlayer player) {
		NBTTagCompound tag = player.getEntityData();

		NBTTagCompound persistTag;
		if (tag.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
			persistTag = tag.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
		} else {
			persistTag = new NBTTagCompound();
			tag.setTag(EntityPlayer.PERSISTED_NBT_TAG, persistTag);
		}

		NBTTagCompound modTag;
		String modID = DimensionalPockets.mod_id;

		if (persistTag.hasKey(modID)) {
			modTag = persistTag.getCompoundTag(modID);
		} else {
			modTag = new NBTTagCompound();
			persistTag.setTag(modID, modTag);
		}

		return modTag;
	}

	/**
	 * This method will write the given name and lore to the itemStack's
	 * "display"-nbt tag. (Thanks to oku)
	 */
	public static ItemStack generateItem(ItemStack itemStack, String name, boolean forceCleanName, String... loreStrings) {
		NBTTagCompound nbt = itemStack.getTagCompound();
		NBTTagCompound display;
		if (nbt == null) {
			nbt = new NBTTagCompound();
			itemStack.setTagCompound(nbt);
		}
		if (!itemStack.getTagCompound().hasKey("display")) {
			itemStack.setTagInfo("display", new NBTTagCompound());
		}

		display = itemStack.getTagCompound().getCompoundTag("display");

		if (loreStrings != null && loreStrings.length > 0) {
			NBTTagList lore = new NBTTagList();
			for (String s : loreStrings) {
				if (s != null) {
					lore.appendTag(new NBTTagString(TRZTextUtil.GRAY + s));
				}
			}
			display.setTag("Lore", lore);
		}

		if (name != null) {
			StringBuilder sb = new StringBuilder();
			if (forceCleanName) {
				sb.append(TRZTextUtil.END);
			}
			sb.append(name);

			display.setString("Name", sb.toString());
		}

		return itemStack;
	}

	/**
	 * Spawns an itemStack in the world.
	 */
	public static void spawnItemStack(ItemStack itemStack, World world, double d, double e, double f, int delayBeforePickup) {
		EntityItem entityItem = new EntityItem(world, d, e, f, itemStack);
		entityItem.setPickupDelay(delayBeforePickup);

		world.spawnEntity(entityItem);
	}

	/**
	 * Tries to check if this is a server side call. If it is a remote call, this
	 * throws an exception.
	 */
	public static void enforceServer() {
			Minecraft mc = Minecraft.getMinecraft();
			if (!mc.isIntegratedServerRunning() && (mc.world != null && mc.world.isRemote))
				throw new RuntimeException("DONT YOU DARE CALL THIS METHOD ON A CLIENT!");
	}

	/**
	 * Tries to check if this is a client side call. If it is a non remote call,
	 * this throws an exception.
	 */
	public static void enforceClient() {
			Minecraft mc = Minecraft.getMinecraft();
			if (mc.world != null && !mc.world.isRemote)
				throw new RuntimeException("DONT YOU DARE CALL THIS METHOD ON A CLIENT!");
	}

	public static boolean isOreDictItem(ItemStack stack, String oreDictName) {
		int targetOreDictID = OreDictionary.getOreID(oreDictName);
		for (int stackOreDictID : OreDictionary.getOreIDs(stack)) {
			if (targetOreDictID == stackOreDictID)
				return true;
		}
		return false;
	}

	public static boolean isItemPocketWrench(ItemStack stack) {
		if (!DimUtils.isOreDictItem(stack, "stickWood"))
			return false;

		if (!stack.hasTagCompound())
			return false;

		NBTTagCompound itemCompound = stack.getTagCompound();
		if (!itemCompound.hasKey("display"))
			return false;

		String customName = itemCompound.getCompoundTag("display").getString("Name");
		return "Pocket Wrench".equalsIgnoreCase(customName);
	}

	public static TextComponentString createChatLink(String text, String url, boolean bold, boolean underline, boolean italic, TextFormatting color) {
		TextComponentString link = new TextComponentString(text);
		Style style = link.getStyle();
		style.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
		style.setBold(Boolean.valueOf(bold));
		style.setUnderlined(Boolean.valueOf(underline));
		style.setItalic(Boolean.valueOf(italic));
		style.setColor(color);
		return link;
	}

	/**
	 * Ensures that the given inventory is the full inventory, i.e. takes double
	 * chests into account.<br>
	 * <i>METHOD COPIED FROM BUILDCRAFT</i>
	 *
	 * @param inv
	 * @return Modified inventory if double chest, unmodified otherwise.
	 */
	public static IInventory getInventory(ILockableContainer inv) {
		if (inv instanceof TileEntityChest) {
			TileEntityChest chest = (TileEntityChest) inv;

			TileEntityChest adjacent = null;

			if (chest.adjacentChestXNeg != null) {
				adjacent = chest.adjacentChestXNeg;
			}

			if (chest.adjacentChestXPos != null) {
				adjacent = chest.adjacentChestXPos;
			}

			if (chest.adjacentChestZNeg != null) {
				adjacent = chest.adjacentChestZNeg;
			}

			if (chest.adjacentChestZPos != null) {
				adjacent = chest.adjacentChestZPos;
			}

			if (adjacent != null)
				return new InventoryLargeChest("", inv, adjacent);
			return inv;
		}
		return inv;
	}

	/**
	 * Gets the {@link TRZTextUtil} colour for the given name. (which must
	 * match the unobuscated name of the enum value exactly)
	 *
	 * @param colourName
	 * @return the ECF colour for the given name, or
	 *         {@link TRZTextUtil#WHITE} if not found
	 */
	public static String getColourByName(String colourName) {
		String colour = chatColours.get(colourName);
		return colour != null ? colour : TRZTextUtil.WHITE;
	}
	
}
