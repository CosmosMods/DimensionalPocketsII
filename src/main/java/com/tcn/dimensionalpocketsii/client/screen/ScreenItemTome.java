package com.tcn.dimensionalpocketsii.client.screen;

import java.util.Arrays;
import java.util.UUID;

import com.mojang.blaze3d.vertex.PoseStack;
import com.tcn.cosmoslibrary.client.ui.lib.CosmosUISystem;
import com.tcn.cosmoslibrary.client.ui.lib.CosmosUISystem.FONT;
import com.tcn.cosmoslibrary.client.ui.screen.widget.CosmosButtonUIMode;
import com.tcn.cosmoslibrary.common.enums.EnumUIMode;
import com.tcn.cosmoslibrary.common.lib.ComponentColour;
import com.tcn.cosmoslibrary.common.lib.ComponentHelper;
import com.tcn.cosmoslibrary.common.lib.ComponentHelper.Value;
import com.tcn.dimensionalpocketsii.DimensionalPockets;
import com.tcn.dimensionalpocketsii.client.screen.button.TomeButton;
import com.tcn.dimensionalpocketsii.client.screen.button.TomeChangeButton;
import com.tcn.dimensionalpocketsii.core.item.DimensionalTome;
import com.tcn.dimensionalpocketsii.core.management.NetworkManager;
import com.tcn.dimensionalpocketsii.core.management.ObjectManager;
import com.tcn.dimensionalpocketsii.core.network.PacketTomeUpdate;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenItemTome extends Screen {
	public ItemStack stack;
	
	public final ResourceLocation[] TEXTURE = new ResourceLocation[] { new ResourceLocation(DimensionalPockets.MOD_ID, "textures/gui/tome/tome.png"), new ResourceLocation(DimensionalPockets.MOD_ID, "textures/gui/tome/tome_dark.png") };
	public static final ResourceLocation FLAT_TEXTURES = new ResourceLocation(DimensionalPockets.MOD_ID, "textures/gui/tome/textures_flat.png");
	public static final ResourceLocation FLAT_TEXTURES_0 = new ResourceLocation(DimensionalPockets.MOD_ID, "textures/gui/tome/textures_flat_0.png");
	public static final ResourceLocation BLOCK_TEXTURES = new ResourceLocation(DimensionalPockets.MOD_ID, "textures/gui/tome/blocks.png");
	
	private int flipTimer = 0;
	private int flipTimerMulti = 0;
	
	private int currPage;
	private int pageCount = 48;

	protected CosmosButtonUIMode uiModeButton;
	private UUID playerUUID;
	
	private TomeChangeButton buttonNextPage;
	private TomeChangeButton buttonPreviousPage;
	private TomeButton buttonExit;
	private TomeButton buttonHome;
	
	private TomeButton tabIntroduction;
	private TomeButton tabPockets;
	private TomeButton tabModules;
	private TomeButton tabItems;
	
	@SuppressWarnings("unused")
	private TomeButton tabConfiguration;
	private TomeButton tabArmourWeapons;
	private TomeButton tabRecipes;
	private TomeButton tabCredits;

	private TomeButton buttonMiss;
	
	private final boolean pageTurnSounds;

	public ScreenItemTome(boolean pageTurnSoundsIn, UUID playerUUIDIn, ItemStack stackIn) {
		super(ComponentHelper.title("dimensionalpocketsii.tome_heading"));
		
		this.pageTurnSounds = pageTurnSoundsIn;
		this.stack = stackIn;
		this.playerUUID = playerUUIDIn;
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}
	
	@Override
	protected void init() {
		this.addButtons();
		super.init();
		
		this.currPage = DimensionalTome.getPage(this.stack);
	}
	
	@Override
	public void renderBackground(PoseStack poseStack) {
		super.renderBackground(poseStack);
		int[] screen_coords = CosmosUISystem.getScreenCoords(this, 202, 225);
		
		CosmosUISystem.renderStaticElementWithUIMode(this, poseStack, screen_coords, 0, 0, 0, 0, 202, 225, this.getUIMode(), TEXTURE);
	}

	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(poseStack);
		super.render(poseStack, mouseX, mouseY, partialTicks);
		
		int[] screen_coords = CosmosUISystem.getScreenCoords(this, 202, 225);
		
		if (this.flipTimer < 2000) {
			this.flipTimer += 2;
		} else {
			this.flipTimer = 0;
		}

		if (this.flipTimerMulti < 8000) {
			this.flipTimerMulti += 2;
		} else {
			this.flipTimerMulti = 0;
		}

		this.addButtons();

		FONT.drawString(poseStack, font, screen_coords, 23, 10, true, ComponentHelper.style2(ComponentColour.BLACK, "dimensionalpocketsii.tome_page", Integer.toString(this.currPage)));
		FONT.drawString(poseStack, font, screen_coords, 69, 10, true, ComponentHelper.style(ComponentColour.POCKET_PURPLE_LIGHT, "underline", "dimensionalpocketsii.tome_heading"));
		
		if (this.currPage == 0) {
			FONT.drawWrappedStringBR(poseStack, font, screen_coords, 104, -9, 0, ComponentColour.BLACK.dec(), ComponentHelper.locString("dimensionalpocketsii.tome_one_body") 
					+ ComponentHelper.locString(Value.PURPLE + Value.UNDERLINE, "dimensionalpocketsii.tome_one_body_one") + ComponentHelper.locString("dimensionalpocketsii.tome_one_body_two"));
			
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 58, ComponentColour.POCKET_PURPLE.dec(), ComponentHelper.locString(Value.UNDERLINE + Value.BOLD, "dimensionalpocketsii.tome_one_body_heading"), false);
			
			FONT.drawString(poseStack, font, screen_coords, 30, 105, true, ComponentHelper.style(ComponentColour.POCKET_PURPLE_LIGHT, "dimensionalpocketsii.tome_two_heading"));
			FONT.drawString(poseStack, font, screen_coords, 173, 105, true, ComponentHelper.style(ComponentColour.POCKET_PURPLE_LIGHT, "1"));
			
			FONT.drawString(poseStack, font, screen_coords, 30, 115, true, ComponentHelper.style(ComponentColour.DARK_CYAN, "dimensionalpocketsii.tome_three_heading"));
			FONT.drawString(poseStack, font, screen_coords, 161, 115, true, ComponentHelper.style(ComponentColour.DARK_CYAN, "2-5"));
			
			FONT.drawString(poseStack, font, screen_coords, 30, 125, true, ComponentHelper.style(ComponentColour.BLUE, "dimensionalpocketsii.tome_four_heading"));
			FONT.drawString(poseStack, font, screen_coords, 155, 125, true, ComponentHelper.style(ComponentColour.BLUE, "6-15"));
			
			FONT.drawString(poseStack, font, screen_coords, 30, 135, true, ComponentHelper.style(ComponentColour.LIGHT_BLUE, "dimensionalpocketsii.tome_five_heading"));
			FONT.drawString(poseStack, font, screen_coords, 149, 135, true, ComponentHelper.style(ComponentColour.LIGHT_BLUE, "16-20"));
			
			FONT.drawString(poseStack, font, screen_coords, 30, 145, true, ComponentHelper.style(ComponentColour.GRAY, "dimensionalpocketsii.tome_seven_heading"));
			FONT.drawString(poseStack, font, screen_coords, 149, 145, true, ComponentHelper.style(ComponentColour.GRAY, "24-27"));
			
			FONT.drawString(poseStack, font, screen_coords, 30, 155, true, ComponentHelper.style(ComponentColour.DARK_GREEN, "dimensionalpocketsii.tome_nine_heading"));
			FONT.drawString(poseStack, font, screen_coords, 149, 155, true, ComponentHelper.style(ComponentColour.DARK_GREEN, "28-44"));
			
			FONT.drawString(poseStack, font, screen_coords, 30, 165, true, ComponentHelper.style(ComponentColour.RED, "dimensionalpocketsii.tome_ten_tab"));
			FONT.drawString(poseStack, font, screen_coords, 149, 165, true, ComponentHelper.style(ComponentColour.RED, "45-46"));

			FONT.drawWrappedStringBR(poseStack, font, screen_coords, 104, 142, 0, ComponentColour.BLACK.dec(), ComponentHelper.locString("dimensionalpocketsii.tome_two_body_three"));
			
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 174, ComponentColour.BLACK.dec(), ComponentHelper.locString("[ ", "dimensionalpocketsii.tome_foot_one", " ]"), false);
		}
		
		else if (this.currPage == 1) {
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, -8, ComponentColour.POCKET_PURPLE_LIGHT.dec(), ComponentHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_two_heading"), false);
			
			FONT.drawWrappedStringBR(poseStack, font, screen_coords, 104, 4, 0, ComponentColour.BLACK.dec(), ComponentHelper.locString("dimensionalpocketsii.tome_two_body_one"));
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 65, ComponentColour.POCKET_PURPLE_LIGHT.dec(), ComponentHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_two_body_two"), false);
			
			FONT.drawString(poseStack, font, screen_coords, 25, 110, true, ComponentHelper.style(ComponentColour.DARK_CYAN, "dimensionalpocketsii.tome_two_sub_one"));
			FONT.drawString(poseStack, font, screen_coords, 25, 120, true, ComponentHelper.style(ComponentColour.BLUE, "dimensionalpocketsii.tome_two_sub_two"));
			FONT.drawString(poseStack, font, screen_coords, 25, 130, true, ComponentHelper.style(ComponentColour.LIGHT_BLUE, "dimensionalpocketsii.tome_two_sub_three"));
			FONT.drawString(poseStack, font, screen_coords, 25, 140, true, ComponentHelper.style(ComponentColour.GREEN, "dimensionalpocketsii.tome_two_sub_four"));
			FONT.drawString(poseStack, font, screen_coords, 25, 150, true, ComponentHelper.style(ComponentColour.GRAY, "dimensionalpocketsii.tome_two_sub_five"));
			FONT.drawString(poseStack, font, screen_coords, 25, 160, true, ComponentHelper.style(ComponentColour.DARK_GREEN, "dimensionalpocketsii.tome_two_sub_six"));
			
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 174, ComponentColour.BLACK.dec(), ComponentHelper.locString("[ ", "dimensionalpocketsii.tome_foot_two", " ]"), false);
		}
		
		else if (this.currPage == 2) {
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, -8, ComponentColour.DARK_CYAN.dec(), ComponentHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_three_heading"), false);
			
			CosmosUISystem.setTextureWithColour(poseStack, BLOCK_TEXTURES, new float[] { 1.0F, 1.0F, 1.0F, 1.0F });
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, 75, 40, 0, 0, 60, 60);
			
			FONT.drawWrappedStringBR(poseStack, font, screen_coords, 104, 70, 0, ComponentColour.BLACK.dec(), ComponentHelper.locString("dimensionalpocketsii.tome_three_body_one"));
			
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 174, ComponentColour.BLACK.dec(), ComponentHelper.locString("[ ", "dimensionalpocketsii.tome_foot_three", " 1 ]"), false);
		} 
		
		else if (this.currPage == 3) {
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, -8, ComponentColour.DARK_CYAN.dec(), ComponentHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_three_heading_one"), false);
			
			FONT.drawWrappedStringBR(poseStack, font, screen_coords, 104, 6, 0, ComponentColour.BLACK.dec(), ComponentHelper.locString("dimensionalpocketsii.tome_three_body_two"));
			FONT.drawWrappedStringBR(poseStack, font, screen_coords, 104, 35, 0, ComponentColour.BLACK.dec(), ComponentHelper.locString("dimensionalpocketsii.tome_three_body_three"));
			
			FONT.drawString(poseStack, font, screen_coords, 42, 90, true, ComponentHelper.style(ComponentColour.BLUE, "bold", "dimensionalpocketsii.tome_three_sub_one"));
			FONT.drawString(poseStack, font, screen_coords, 84, 90, true, ComponentHelper.style(ComponentColour.DARK_GREEN, "bold", "dimensionalpocketsii.tome_three_sub_two"));
			FONT.drawString(poseStack, font, screen_coords, 126, 90, true, ComponentHelper.style(ComponentColour.GRAY, "bold", "dimensionalpocketsii.tome_three_sub_three"));

			CosmosUISystem.setTextureWithColour(poseStack, FLAT_TEXTURES_0, new float[] { 1.0F, 1.0F, 1.0F, 1.0F });
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, 101 - 60, 102, 32, 96, 32, 32);
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, 101 - 14, 102, 64, 96, 32, 32);
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, 101 + 32, 102, 96, 96, 32, 32);
			
			FONT.drawWrappedStringBR(poseStack, font, screen_coords, 104, 108, 0, ComponentColour.BLACK.dec(), ComponentHelper.locString("dimensionalpocketsii.tome_three_body_four"));

			CosmosUISystem.setTextureWithColour(poseStack, FLAT_TEXTURES, new float[] { 1.0F, 1.0F, 1.0F, 1.0F });
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, 47, 168, 128, 192, 32, 32);
			
			CosmosUISystem.setTextureWithColour(poseStack, this.getUIMode().equals(EnumUIMode.DARK) ? TEXTURE[0] : TEXTURE[1], new float[] { 1.0F, 1.0F, 1.0F, 1.0F });
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, 92, 178, 236, 242, 20, 14);
			
			CosmosUISystem.setTextureWithColour(poseStack, FLAT_TEXTURES_0, new float[] { 1.0F, 1.0F, 1.0F, 1.0F });
			if (this.flipTimerMulti < 2000) {
				CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, 127, 168, 0, 96, 32, 32);
			} else if (this.flipTimerMulti > 2000 && this.flipTimerMulti < 4000) {
				CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, 127, 168, 32, 96, 32, 32);
			} else if (this.flipTimerMulti > 4000 && this.flipTimerMulti < 6000) {
				CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, 127, 168, 64, 96, 32, 32);
			} else if (this.flipTimerMulti > 6000 && this.flipTimerMulti < 8000) {
				CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, 127, 168, 96, 96, 32, 32);
			}
			
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 174, ComponentColour.BLACK.dec(), ComponentHelper.locString("[ ", "dimensionalpocketsii.tome_foot_three", " 2 ]"), false);
		} 
		
		else if (this.currPage == 4) {
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, -8, ComponentColour.DARK_CYAN.dec(), ComponentHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_three_heading_two"), false);

			FONT.drawWrappedStringBR(poseStack, font, screen_coords, 104, 6, 0, ComponentColour.BLACK.dec(), ComponentHelper.locString("You can also colour your<br>Pocket to any of the 16<br>vanilla Minecraft colours,<br>or any Mod colours.<br> <br>To apply a colour, right<br>click your Pocket Block,<br>or Connector with a Dye<br>Item.<br> <br>To reset the colour back<br>to purple, use a Dimensional<br>Shard instead of a Dye."));
			//FONT.drawWrappedStringBR(poseStack, font, screen_coords, 104, 35, 0, ComponentColour.BLACK.dec(), ComponentHelper.locString("dimensionalpocketsii.tome_three_body_three"));

			CosmosUISystem.setTextureWithColour(poseStack, this.getUIMode().equals(EnumUIMode.DARK) ? TEXTURE[0] : TEXTURE[1], new float[] { 1.0F, 1.0F, 1.0F, 1.0F });
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, 92, 170, 236, 242, 20, 14);
			
			CosmosUISystem.setTextureWithColour(poseStack, FLAT_TEXTURES_0, new float[] { 1.0F, 1.0F, 1.0F, 1.0F });
			if (this.flipTimerMulti < 2000) {
				CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, 127, 160, 0, 64, 32, 32);
				CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, 47, 160, 0, 128, 32, 32);
			} else if (this.flipTimerMulti > 2000 && this.flipTimerMulti < 4000) {
				CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, 127, 160, 32, 64, 32, 32);
				CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, 47, 160, 32, 128, 32, 32);
			} else if (this.flipTimerMulti > 4000 && this.flipTimerMulti < 6000) {
				CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, 127, 160, 64, 64, 32, 32);
				CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, 47, 160, 64, 128, 32, 32);
			} else if (this.flipTimerMulti > 6000 && this.flipTimerMulti < 8000) {
				CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, 127, 160, 96, 64, 32, 32);
				CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, 47, 160, 96, 128, 32, 32);
			}
			
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 174, ComponentColour.BLACK.dec(), ComponentHelper.locString("[ ", "dimensionalpocketsii.tome_foot_three", " 3 ]"), false);
		} 
		
		else if (this.currPage == 5) {
			
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 174, ComponentColour.BLACK.dec(), ComponentHelper.locString("[ ", "dimensionalpocketsii.tome_foot_three", " 4 ]"), false);
		} 
		
		//Modules
		else if (this.currPage == 6) {
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, -8, ComponentColour.BLUE.dec(), ComponentHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_four_heading"), false);

			FONT.drawWrappedStringBR(poseStack, font, screen_coords, 104, 4, 0, ComponentColour.BLACK.dec(), ComponentHelper.locString("dimensionalpocketsii.tome_four_body"));
			
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 174, ComponentColour.BLACK.dec(), ComponentHelper.locString("[ ", "dimensionalpocketsii.tome_foot_four", " 1 ]"), false);
		} 
		
		else if (this.currPage == 7) {
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, -8, ComponentColour.BLUE.dec(), ComponentHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_four_heading_one"), false);

			CosmosUISystem.setTextureWithColour(poseStack, FLAT_TEXTURES, new float[] { 1.0F, 1.0F, 1.0F, 1.0F });
			
			if (this.flipTimerMulti < 1000) {
				CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, (202) / 2 - 14, 40, 0, 32, 32, 32);
			} else if (this.flipTimerMulti < 2000) {
				CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, (202) / 2 - 14, 40, 32, 32, 32, 32);
			} else if (this.flipTimerMulti < 3000) {
				CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, (202) / 2 - 14, 40, 64, 32, 32, 32);
			} else if (this.flipTimerMulti < 4000) {
				CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, (202) / 2 - 14, 40, 96, 32, 32, 32);
			} else if (this.flipTimerMulti < 5000) {
				CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, (202) / 2 - 14, 40, 128, 32, 32, 32);
			} else if (this.flipTimerMulti < 6000) {
				CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, (202) / 2 - 14, 40, 160, 32, 32, 32);
			} else if (this.flipTimerMulti < 7000) {
				CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, (202) / 2 - 14, 40, 192, 32, 32, 32);
			} else if (this.flipTimerMulti < 8000) {
				CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, (202) / 2 - 14, 40, 224, 32, 32, 32);
			}
			
			FONT.drawWrappedStringBR(poseStack, font, screen_coords, 104, 45, 0, ComponentColour.BLACK.dec(), ComponentHelper.locString("dimensionalpocketsii.tome_four_body_one"));
			
			FONT.drawWrappedStringBR(poseStack, font, screen_coords, 104, 140, 0, ComponentColour.BLACK.dec(), ComponentHelper.locString("dimensionalpocketsii.tome_four_body_one_"));
			
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 174, ComponentColour.BLACK.dec(), ComponentHelper.locString("[ ", "dimensionalpocketsii.tome_foot_four", " 2 ]"), false);
		}
		
		else if (this.currPage == 8) {
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, -8, ComponentColour.BLUE.dec(), ComponentHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_four_heading_two"), false);

			CosmosUISystem.setTextureWithColour(poseStack, FLAT_TEXTURES, new float[] { 1.0F, 1.0F, 1.0F, 1.0F });
			
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, (202) / 2 - 14, 40, 64, 0, 32, 32);
			
			FONT.drawWrappedStringBR(poseStack, font, screen_coords, 104, 50, 0, ComponentColour.BLACK.dec(), ComponentHelper.locString("dimensionalpocketsii.tome_four_body_two"));
			
			FONT.drawWrappedStringBR(poseStack, font, screen_coords, 104, 155, 0, ComponentColour.BLACK.dec(), ComponentHelper.locString("dimensionalpocketsii.tome_four_body_two_"));
			
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 174, ComponentColour.BLACK.dec(), ComponentHelper.locString("[ ", "dimensionalpocketsii.tome_foot_four", " 3 ]"), false);
		}
		
		else if (this.currPage == 9) {
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, -8, ComponentColour.BLUE.dec(), ComponentHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_four_heading_three"), false);

			CosmosUISystem.setTextureWithColour(poseStack, FLAT_TEXTURES, new float[] { 1.0F, 1.0F, 1.0F, 1.0F });
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, (202) / 2 - 14, 40, 32, 0, 32, 32);
			
			FONT.drawWrappedStringBR(poseStack, font, screen_coords, 104, 50, 0, ComponentColour.BLACK.dec(), ComponentHelper.locString("dimensionalpocketsii.tome_four_body_three"));
			
			FONT.drawWrappedStringBR(poseStack, font, screen_coords, 104, 115, 0, ComponentColour.BLACK.dec(), ComponentHelper.locString("dimensionalpocketsii.tome_four_body_three_"));
			
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 174, ComponentColour.BLACK.dec(), ComponentHelper.locString("[ ", "dimensionalpocketsii.tome_foot_four", " 4 ]"), false);
		}
		
		else if (this.currPage == 10) {
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, -8, ComponentColour.BLUE.dec(), ComponentHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_four_heading_four"), false);

			CosmosUISystem.setTextureWithColour(poseStack, FLAT_TEXTURES, new float[] { 1.0F, 1.0F, 1.0F, 1.0F });
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, (202) / 2 - 14, 40, 0, 0, 32, 32);
			
			FONT.drawWrappedStringBR(poseStack, font, screen_coords, 104, 50, 0, ComponentColour.BLACK.dec(), ComponentHelper.locString("dimensionalpocketsii.tome_four_body_four"));
			
			FONT.drawWrappedStringBR(poseStack, font, screen_coords, 104, 115, 0, ComponentColour.BLACK.dec(), ComponentHelper.locString("dimensionalpocketsii.tome_four_body_four_"));
			
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 174, ComponentColour.BLACK.dec(), ComponentHelper.locString("[ ", "dimensionalpocketsii.tome_foot_four", " 5 ]"), false);
		}
		
		else if (this.currPage == 11) {
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, -8, ComponentColour.BLUE.dec(), ComponentHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_four_heading_five"), false);

			CosmosUISystem.setTextureWithColour(poseStack, FLAT_TEXTURES, new float[] { 1.0F, 1.0F, 1.0F, 1.0F });
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, (202) / 2 - 14, 40, 128, 0, 32, 32);
			
			FONT.drawWrappedStringBR(poseStack, font, screen_coords, 104, 60, 0, ComponentColour.BLACK.dec(), ComponentHelper.locString("dimensionalpocketsii.tome_four_body_five"));
			
			//FONT.drawWrappedStringBR(poseStack, font, screen_coords, 104, 110, 0, CosmosColour.BLACK.dec(), CosmosCompHelper.locString("dimensionalpocketsii.tome_four_body_five_"));
			
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 174, ComponentColour.BLACK.dec(), ComponentHelper.locString("[ ", "dimensionalpocketsii.tome_foot_four", " 6 ]"), false);
		}
		
		else if (this.currPage == 12) {
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, -8, ComponentColour.BLUE.dec(), ComponentHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_four_heading_six"), false);

			CosmosUISystem.setTextureWithColour(poseStack, FLAT_TEXTURES, new float[] { 1.0F, 1.0F, 1.0F, 1.0F });
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, (202) / 2 - 14, 40, 160, 0, 32, 32);
			
			FONT.drawWrappedStringBR(poseStack, font, screen_coords, 104, 60, 0, ComponentColour.BLACK.dec(), ComponentHelper.locString("dimensionalpocketsii.tome_four_body_six"));
			
			//FONT.drawWrappedStringBR(poseStack, font, screen_coords, 104, 110, 0, CosmosColour.BLACK.dec(), CosmosCompHelper.locString("dimensionalpocketsii.tome_four_body_six_"));
			
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 174, ComponentColour.BLACK.dec(), ComponentHelper.locString("[ ", "dimensionalpocketsii.tome_foot_four", " 7 ]"), false);
		}
		
		else if (this.currPage == 13) {
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, -8, ComponentColour.BLUE.dec(), ComponentHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_four_heading_seven"), false);

			CosmosUISystem.setTextureWithColour(poseStack, FLAT_TEXTURES, new float[] { 1.0F, 1.0F, 1.0F, 1.0F });
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, (202) / 2 - 14, 40, 96, 0, 32, 32);
			
			FONT.drawWrappedStringBR(poseStack, font, screen_coords, 104, 50, 0, ComponentColour.BLACK.dec(), ComponentHelper.locString("dimensionalpocketsii.tome_four_body_seven"));
			
			FONT.drawWrappedStringBR(poseStack, font, screen_coords, 104, 125, 0, ComponentColour.BLACK.dec(), ComponentHelper.locString("dimensionalpocketsii.tome_four_body_seven_"));
			
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 174, ComponentColour.BLACK.dec(), ComponentHelper.locString("[ ", "dimensionalpocketsii.tome_foot_four", " 8 ]"), false);
		}
		
		else if (this.currPage == 14) {
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, -8, ComponentColour.BLUE.dec(), ComponentHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_four_heading_eight"), false);

			CosmosUISystem.setTextureWithColour(poseStack, FLAT_TEXTURES, new float[] { 1.0F, 1.0F, 1.0F, 1.0F });
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, (202) / 2 - 14, 40, 192, 0, 32, 32);
			
			FONT.drawWrappedStringBR(poseStack, font, screen_coords, 104, 50, 0, ComponentColour.BLACK.dec(), ComponentHelper.locString("dimensionalpocketsii.tome_four_body_eight"));
			
			FONT.drawWrappedStringBR(poseStack, font, screen_coords, 104, 150, 0, ComponentColour.BLACK.dec(), ComponentHelper.locString("dimensionalpocketsii.tome_four_body_eight_"));
			
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 174, ComponentColour.BLACK.dec(), ComponentHelper.locString("[ ", "dimensionalpocketsii.tome_foot_four", " 9 ]"), false);
		}

		else if (this.currPage == 15) {
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, -8, ComponentColour.BLUE.dec(), ComponentHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_four_heading_nine"), false);

			CosmosUISystem.setTextureWithColour(poseStack, FLAT_TEXTURES, new float[] { 1.0F, 1.0F, 1.0F, 1.0F });
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, (202) / 2 - 14, 40, 224, 0, 32, 32);
			
			FONT.drawWrappedStringBR(poseStack, font, screen_coords, 104, 50, 0, ComponentColour.BLACK.dec(), ComponentHelper.locString("dimensionalpocketsii.tome_four_body_nine"));
			
			FONT.drawWrappedStringBR(poseStack, font, screen_coords, 104, 150, 0, ComponentColour.BLACK.dec(), ComponentHelper.locString("dimensionalpocketsii.tome_four_body_nine_"));
			
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 174, ComponentColour.BLACK.dec(), ComponentHelper.locString("[ ", "dimensionalpocketsii.tome_foot_four", " 9 ]"), false);
		}

		else if (this.currPage == 16) {
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, -8, ComponentColour.BLUE.dec(), ComponentHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_four_heading_ten"), false);

			CosmosUISystem.setTextureWithColour(poseStack, FLAT_TEXTURES, new float[] { 1.0F, 1.0F, 1.0F, 1.0F });
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, (202) / 2 - 14, 40, 224, 64, 32, 32);
			
			FONT.drawWrappedStringBR(poseStack, font, screen_coords, 104, 50, 0, ComponentColour.BLACK.dec(), ComponentHelper.locString("dimensionalpocketsii.tome_four_body_ten"));
			
			FONT.drawWrappedStringBR(poseStack, font, screen_coords, 104, 150, 0, ComponentColour.BLACK.dec(), ComponentHelper.locString("dimensionalpocketsii.tome_four_body_ten_"));
			
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 174, ComponentColour.BLACK.dec(), ComponentHelper.locString("[ ", "dimensionalpocketsii.tome_foot_four", " 10 ]"), false);
		}
		
		else if (this.currPage == 17) {
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, -8, ComponentColour.LIGHT_BLUE.dec(), ComponentHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_five_heading"), false);
			CosmosUISystem.setTextureWithColour(poseStack, FLAT_TEXTURES, new float[] { 1.0F, 1.0F, 1.0F, 1.0F });
			
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, (202) / 2 - 32 - 40, 40, 128, 192, 32, 32);
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, (202) / 2 - 32, 40, 192, 224, 32, 32);
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, (202) / 2 + 4, 40, 192, 192, 32, 32);
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, (202) / 2 + 40, 40, 160, 192, 32, 32);
			
			FONT.drawWrappedStringBR(poseStack, font, screen_coords, 104, 50, 0, ComponentColour.BLACK.dec(), ComponentHelper.locString("dimensionalpocketsii.tome_five_body"));
			
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 174, ComponentColour.BLACK.dec(), ComponentHelper.locString("[ ", "dimensionalpocketsii.tome_foot_five", " 1 ]"), false);
		} 

		else if (this.currPage == 18) {
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, -8, ComponentColour.LIGHT_BLUE.dec(), ComponentHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_five_heading_one"), false);

			CosmosUISystem.setTextureWithColour(poseStack, FLAT_TEXTURES, new float[] { 1.0F, 1.0F, 1.0F, 1.0F });
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, (202) / 2 - 14, 40, 128, 192, 32, 32);

			FONT.drawWrappedStringBR(poseStack, font, screen_coords, 104, 45, 0, ComponentColour.BLACK.dec(), ComponentHelper.locString("dimensionalpocketsii.tome_five_body_one"));
			
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 174, ComponentColour.BLACK.dec(), ComponentHelper.locString("[ ", "dimensionalpocketsii.tome_foot_five", " 2 ]"), false);
		} 
		
		else if (this.currPage == 19) {
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, -8, ComponentColour.LIGHT_BLUE.dec(), ComponentHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_five_heading_two"), false);

			CosmosUISystem.setTextureWithColour(poseStack, FLAT_TEXTURES, new float[] { 1.0F, 1.0F, 1.0F, 1.0F });
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, (202) / 2 - 32, 40, 224, 224, 32, 32);
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, (202) / 2 + 4, 40, 192, 224, 32, 32);

			FONT.drawWrappedStringBR(poseStack, font, screen_coords, 104, 40, 0, ComponentColour.BLACK.dec(), ComponentHelper.locString("dimensionalpocketsii.tome_five_body_two"));
			
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 174, ComponentColour.BLACK.dec(), ComponentHelper.locString("[ ", "dimensionalpocketsii.tome_foot_five", " 3 ]"), false);
		} 

		else if (this.currPage == 20) {
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, -8, ComponentColour.LIGHT_BLUE.dec(), ComponentHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_five_heading_three"), false);

			CosmosUISystem.setTextureWithColour(poseStack, FLAT_TEXTURES, new float[] { 1.0F, 1.0F, 1.0F, 1.0F });
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, (202) / 2 - 32, 40, 224, 192, 32, 32);
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, (202) / 2 + 4, 40, 192, 192, 32, 32);

			FONT.drawWrappedStringBR(poseStack, font, screen_coords, 104, 40, 0, ComponentColour.BLACK.dec(), ComponentHelper.locString("dimensionalpocketsii.tome_five_body_three"));
			
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 174, ComponentColour.BLACK.dec(), ComponentHelper.locString("[ ", "dimensionalpocketsii.tome_foot_five", " 4 ]"), false);
		} 

		else if (this.currPage == 21) {
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, -8, ComponentColour.LIGHT_BLUE.dec(), ComponentHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_five_heading_four"), false);

			CosmosUISystem.setTextureWithColour(poseStack, FLAT_TEXTURES, new float[] { 1.0F, 1.0F, 1.0F, 1.0F });
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, (202) / 2 - 14, 40, 160, 192, 32, 32);

			FONT.drawWrappedStringBR(poseStack, font, screen_coords, 104, 40, 0, ComponentColour.BLACK.dec(), ComponentHelper.locString("dimensionalpocketsii.tome_five_body_four"));
			
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 174, ComponentColour.BLACK.dec(), ComponentHelper.locString("[ ", "dimensionalpocketsii.tome_foot_five", " 5 ]"), false);
		} 
		
		else if (this.currPage == 21) {
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, -8, ComponentColour.GREEN.dec(), ComponentHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_six_heading"), false);
			
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 174, ComponentColour.BLACK.dec(), ComponentHelper.locString("[ ", "dimensionalpocketsii.tome_foot_six", " 1 ]"), false);
		}
		
		else if (this.currPage == 22) {
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, -8, ComponentColour.GRAY.dec(), ComponentHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_seven_heading"), false);
			
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 174, ComponentColour.BLACK.dec(), ComponentHelper.locString("[ ", "dimensionalpocketsii.tome_foot_seven", " 1 ]"), false);
		} 
		
		else if (this.currPage == 23) {
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, -8, ComponentColour.GRAY.dec(), ComponentHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_seven_heading_one"), false);
			
			CosmosUISystem.setTextureWithColour(poseStack, FLAT_TEXTURES, new float[] { 1.0F, 1.0F, 1.0F, 1.0F });
			
			if (this.flipTimer < 1000) {
				CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, (202) / 2 - 72,  40,  0,  160, 32, 32);
				CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, (202) / 2 - 32,  40, 32,  160, 32, 32);
				CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, (202) / 2 + 4,   40, 64,  160, 32, 32);
				CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, (202) / 2 + 40,  40, 96,  160, 32, 32);
			} else if (this.flipTimer < 2000) {
				CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, (202) / 2 - 72,  40, 128, 160, 32, 32);
				CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, (202) / 2 - 32,  40, 160, 160, 32, 32);
				CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, (202) / 2 + 4,   40, 192, 160, 32, 32);
				CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, (202) / 2 + 40,  40, 224, 160, 32, 32);
			}
			
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 174, ComponentColour.BLACK.dec(), ComponentHelper.locString("[ ", "dimensionalpocketsii.tome_foot_seven", " 2 ]"), false);
		} 

		else if (this.currPage == 24) {
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, -8, ComponentColour.GRAY.dec(), ComponentHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_seven_heading_two"), false);
			
			CosmosUISystem.setTextureWithColour(poseStack, FLAT_TEXTURES, new float[] { 1.0F, 1.0F, 1.0F, 1.0F });
			//CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, (202) / 2 - 54,  40, 32, 128, 32, 32);
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, (202) / 2 - 14,  40, 0, 128, 32, 32);
			//CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, (202) / 2 + 26,  40, 64, 128, 32, 32);
			
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 174, ComponentColour.BLACK.dec(), ComponentHelper.locString("[ ", "dimensionalpocketsii.tome_foot_seven", " 3 ]"), false);
		} 

		else if (this.currPage == 25) {
			
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 174, ComponentColour.BLACK.dec(), ComponentHelper.locString("[ ", "dimensionalpocketsii.tome_foot_seven", " 4 ]"), false);
		} 

		else if (this.currPage == 26) {
			
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 174, ComponentColour.BLACK.dec(), ComponentHelper.locString("[ ", "dimensionalpocketsii.tome_foot_seven", " 5 ]"), false);
		} 
		
		else if (this.currPage == 27) {
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, -8, ComponentColour.GRAY.dec(), ComponentHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_eight_heading"), false);
			
			CosmosUISystem.setTextureWithColour(poseStack, FLAT_TEXTURES, new float[] { 1.0F, 1.0F, 1.0F, 1.0F });
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, (202) / 2 - 72,  40,  0,  224, 32, 32);
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, (202) / 2 - 32,  40, 32,  224, 32, 32);
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, (202) / 2 + 4,   40, 64, 224, 32, 32);
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, (202) / 2 + 40,  40, 96, 224, 32, 32);

			FONT.drawWrappedStringBR(poseStack, font, screen_coords, 104, 45, 0, ComponentColour.BLACK.dec(), ComponentHelper.locString("dimensionalpocketsii.tome_eight_body"));
			
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 174, ComponentColour.BLACK.dec(), ComponentHelper.locString("[ ", "dimensionalpocketsii.tome_foot_eight", " 1 ]"), false);
		} 
		
		else if (this.currPage == 28) {
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, -10, ComponentColour.DARK_GREEN.dec(),ComponentHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_nine_heading"), false);
			
			CosmosUISystem.setTextureWithColour(poseStack, FLAT_TEXTURES, new float[] { 1.0F, 1.0F, 1.0F, 1.0F });
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, (202) / 2 - 54, 40, 192, 64, 32, 32);
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, (202) / 2 - 14, 40, 192, 96, 32, 32);
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, (202) / 2 + 26, 40, 224, 0, 32, 32);

			FONT.drawWrappedStringBR(poseStack, font, screen_coords, 104, 45, 0, ComponentColour.BLACK.dec(), ComponentHelper.locString("dimensionalpocketsii.tome_nine_body"));
			
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 174, ComponentColour.BLACK.dec(), ComponentHelper.locString("[ ", "dimensionalpocketsii.tome_foot_nine", " Intro ]"), false);
		}
		
		else if (this.currPage >= 29 && this.currPage <= 38) {
			CosmosUISystem.setTextureWithColourAlpha(poseStack, FLAT_TEXTURES, new float[] { 1.0F, 1.0F, 1.0F, 1.0F });
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, 202 / 2 - 30, 225 / 2 - 30, 0, 64, 64, 64);

			CosmosUISystem.setTextureWithColour(poseStack, this.getTexture(), new float[] { 1.0F, 1.0F, 1.0F, 1.0F });
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, 30, 35, 202, 0, 54, 74);
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, 30, 125, 202, 0, 54, 74);
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, 123, 35, 202, 0, 54, 74);
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, 123, 125, 202, 0, 54, 74);

			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 174, ComponentColour.BLACK.dec(), ComponentHelper.locString("[ ", "dimensionalpocketsii.tome_foot_nine", " " + (this.currPage - 27) + " ]"), false);
			
			if (this.currPage == 29) {
				FONT.drawCenteredString(poseStack, font, screen_coords, 104, -10, ComponentColour.DARK_GREEN.dec(),ComponentHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_nine_heading_one"), false);
				
				font.draw(poseStack, Value.BOLD + "4", screen_coords[0] + 67, screen_coords[1] + 102, ComponentColour.BLACK.dec());
				font.draw(poseStack, Value.BOLD + "2", screen_coords[0] + 160, screen_coords[1] + 102, ComponentColour.BLACK.dec());
			}
			
			if (this.currPage == 30) {
				font.draw(poseStack, Value.BOLD + "4", screen_coords[0] + 67, screen_coords[1] + 192, ComponentColour.BLACK.dec());
				font.draw(poseStack, Value.BOLD + "2", screen_coords[0] + 67, screen_coords[1] + 102, ComponentColour.BLACK.dec());
			}
			
			if (this.currPage == 31) {
				//font.draw(poseStack, Value.BOLD + "4", screen_coords[0] + 67, screen_coords[1] + 192, CosmosColour.BLACK.dec());
			}
			
			if (this.currPage == 34) {
				font.draw(poseStack, Value.BOLD + "2", screen_coords[0] + 67, screen_coords[1] + 102, ComponentColour.BLACK.dec());
			}
		}
		
		else if (this.currPage >= 39 && this.currPage <= 42) {
			CosmosUISystem.setTextureWithColourAlpha(poseStack, FLAT_TEXTURES, new float[] { 1.0F, 1.0F, 1.0F, 1.0F });
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, 202 / 2 - 30, 225 / 2 - 30, 128, 64, 64, 64);

			CosmosUISystem.setTextureWithColour(poseStack, this.getTexture(), new float[] { 1.0F, 1.0F, 1.0F, 1.0F });
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, 30, 35, 202, 116, 54, 74);
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, 30, 125, 202, 116, 54, 74);
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, 123, 35, 202, 116, 54, 74);
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, 123, 125, 202, 116, 54, 74);
			
			if (this.currPage == 39) {
				FONT.drawCenteredString(poseStack, font, screen_coords, 104, -10, ComponentColour.DARK_GREEN.dec(),ComponentHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_nine_heading_two"), false);
			}

			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 174, ComponentColour.BLACK.dec(), ComponentHelper.locString("[ ", "dimensionalpocketsii.tome_foot_nine", " " + (this.currPage - 27) + " ]"), false);
		}
		
		else if (this.currPage >= 43 && this.currPage <= 44) {
			CosmosUISystem.setTextureWithColourAlpha(poseStack, FLAT_TEXTURES, new float[] { 1.0F, 1.0F, 1.0F, 1.0F });
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, 202 / 2 - 30, 225 / 2 - 30, 64, 64, 64, 64);

			CosmosUISystem.setTextureWithColour(poseStack, this.getTexture(), new float[] { 1.0F, 1.0F, 1.0F, 1.0F });
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, 30, 35, 202, 76, 54, 38);
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, 30, 77, 202, 76, 54, 38);
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, 30, 119, 202, 76, 54, 38);
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, 30, 161, 202, 76, 54, 38);
			
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, 123, 35, 202, 76, 54, 38);
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, 123, 77, 202, 76, 54, 38);
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, 123, 119, 202, 76, 54, 38);
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, 123, 161, 202, 76, 54, 38);

			if (this.currPage == 43) {
				FONT.drawCenteredString(poseStack, font, screen_coords, 104, -10, ComponentColour.DARK_GREEN.dec(),ComponentHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_nine_heading_three"), false);
				
			}

			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 174, ComponentColour.BLACK.dec(), ComponentHelper.locString("[ ", "dimensionalpocketsii.tome_foot_nine", " " + (this.currPage - 27) + " ]"), false);
		}
		
		else if (this.currPage == 45) {
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, -8, ComponentColour.RED.dec(), ComponentHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_ten_heading"), false);
			
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 5, ComponentColour.POCKET_PURPLE_LIGHT.dec(), "TheCosmicNebula", false);
			FONT.drawCenteredComponent(poseStack, font, screen_coords, 104, 15, ComponentHelper.style(ComponentColour.POCKET_PURPLE_LIGHT, "dimensionalpocketsii.tome_ten"), false);
			
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 30, ComponentColour.DARK_GREEN.dec(), "Apolybrium", false);
			FONT.drawCenteredComponent(poseStack, font, screen_coords, 104, 40, ComponentHelper.style(ComponentColour.DARK_GREEN, "dimensionalpocketsii.tome_ten_one"), false);
			FONT.drawCenteredComponent(poseStack, font, screen_coords, 104, 50, ComponentHelper.style(ComponentColour.DARK_GREEN, "dimensionalpocketsii.tome_ten_one_"), false);

			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 65, ComponentColour.BLUE.dec(), "Scarlet Spark", false);
			FONT.drawCenteredComponent(poseStack, font, screen_coords, 104, 75, ComponentHelper.style(ComponentColour.BLUE, "dimensionalpocketsii.tome_ten_two"), false);
			FONT.drawCenteredComponent(poseStack, font, screen_coords, 104, 85, ComponentHelper.style(ComponentColour.BLUE, "dimensionalpocketsii.tome_ten_two_"), false);
			
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 100, ComponentColour.PURPLE.dec(), "Rechalow", false);
			FONT.drawCenteredComponent(poseStack, font, screen_coords, 104, 110, ComponentHelper.style(ComponentColour.PURPLE, "dimensionalpocketsii.tome_ten_three"), false);

			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 125, ComponentColour.BLUE.dec(), "NPException + Team", false);
			FONT.drawCenteredComponent(poseStack, font, screen_coords, 104, 135, ComponentHelper.style(ComponentColour.BLUE, "dimensionalpocketsii.tome_ten_four"), false);

			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 150, ComponentColour.RED.dec(), "VsnGamer", false);
			FONT.drawCenteredComponent(poseStack, font, screen_coords, 104, 160, ComponentHelper.style(ComponentColour.RED, "dimensionalpocketsii.tome_ten_five"), false);
			
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 174, ComponentColour.BLACK.dec(), ComponentHelper.locString("[ ", "dimensionalpocketsii.tome_foot_ten", " 1 ]"), false);
		}
		
		else if (this.currPage == 46) {
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, -8, ComponentColour.BLURPLE_LIGHT.dec(), ComponentHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_eleven_heading"), false);
			
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 10, ComponentColour.BLACK.dec(), ComponentHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_eleven_body_one"), false);
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 22, ComponentColour.ORANGE.dec(), "Azrael", false);
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 35, ComponentColour.ORANGE.dec(), "Jiale556276", false);
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 48, ComponentColour.ORANGE.dec(), "Imjustleo", false);
			
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 66, ComponentColour.BLACK.dec(), ComponentHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_eleven_body_two"), false);
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 78, ComponentColour.LIGHT_RED.dec(), "Tahlavos17", false);
			
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 98, ComponentColour.BLACK.dec(), ComponentHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_eleven_body_three"), false);
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 111, ComponentColour.RED.dec(), "Pl�r1’", false);

			FONT.drawWrappedStringBR(poseStack, font, screen_coords, 104, 130, 0, ComponentColour.BLURPLE_LIGHT.dec(), ComponentHelper.locString("dimensionalpocketsii.tome_eleven_body_four"));
			
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 174, ComponentColour.BLACK.dec(), ComponentHelper.locString("[ ", "dimensionalpocketsii.tome_foot_ten", " 2 ]"), false);
		}
		
		this.renderSecret(poseStack, screen_coords);
		
		//CosmosUISystem.setTextureWithColour(poseStack, TEXTURE, new float[] { 1.0F, 1.0F, 1.0F, 1.0F });
		this.renderComponentHoverEffect(poseStack, Style.EMPTY, mouseX, mouseY);
	}
	
	@Override
	public void renderComponentHoverEffect(PoseStack poseStack, Style style, int mouseX, int mouseY) {
		int[] screen_coords = CosmosUISystem.getScreenCoords(this, 202, 225);
		
		if (this.buttonExit.isMouseOver(mouseX, mouseY)) {
			this.renderTooltip(poseStack, ComponentHelper.style(ComponentColour.RED, "dimensionalpocketsii.tome_button_one"), mouseX, mouseY);
		} else if (this.buttonHome.isMouseOver(mouseX, mouseY)) {
			this.renderTooltip(poseStack, ComponentHelper.style(ComponentColour.GREEN, "dimensionalpocketsii.tome_button_two"), mouseX, mouseY);
		} 
		
		else if (this.buttonNextPage.isMouseOver(mouseX, mouseY)) {
			this.renderTooltip(poseStack, ComponentHelper.style(ComponentColour.POCKET_PURPLE_LIGHT, "dimensionalpocketsii.tome_button_three"), mouseX, mouseY);
		} else if (this.buttonPreviousPage.isMouseOver(mouseX, mouseY)) {
			this.renderTooltip(poseStack, ComponentHelper.style(ComponentColour.POCKET_PURPLE_LIGHT, "dimensionalpocketsii.tome_button_four"), mouseX, mouseY);
		} 
		
		else if (this.tabIntroduction.isMouseOver(mouseX, mouseY)) {
			this.renderTooltip(poseStack, ComponentHelper.style(ComponentColour.POCKET_PURPLE_LIGHT, "bold", "dimensionalpocketsii.tome_two_heading"), mouseX, mouseY);
		} else if (this.tabPockets.isMouseOver(mouseX, mouseY)) {
			this.renderTooltip(poseStack, ComponentHelper.style(ComponentColour.CYAN, "bold", "dimensionalpocketsii.tome_three_heading"), mouseX, mouseY);
		} else if (this.tabModules.isMouseOver(mouseX, mouseY)) {
			this.renderTooltip(poseStack, ComponentHelper.style(ComponentColour.BLUE, "bold", "dimensionalpocketsii.tome_four_heading"), mouseX, mouseY);
		} else if (this.tabItems.isMouseOver(mouseX, mouseY)) {
			this.renderTooltip(poseStack, ComponentHelper.style(ComponentColour.LIGHT_BLUE, "bold", "dimensionalpocketsii.tome_five_heading"), mouseX, mouseY);
		} else if (this.tabArmourWeapons.isMouseOver(mouseX, mouseY)) {
			this.renderTooltip(poseStack, ComponentHelper.style(ComponentColour.GRAY, "bold", "dimensionalpocketsii.tome_seven_heading"), mouseX, mouseY);
		} else if (this.tabRecipes.isMouseOver(mouseX, mouseY)) {
			this.renderTooltip(poseStack, ComponentHelper.style(ComponentColour.DARK_GREEN, "bold", "dimensionalpocketsii.tome_nine_heading"), mouseX, mouseY);
		} else if (this.tabCredits.isMouseOver(mouseX, mouseY)) {
			this.renderTooltip(poseStack, ComponentHelper.style(ComponentColour.RED, "bold", "dimensionalpocketsii.tome_ten_tab"), mouseX, mouseY);
		} else if (this.buttonMiss.isMouseOver(mouseX, mouseY)) {
			this.renderTooltip(poseStack, ComponentHelper.empty(), mouseX, mouseY);
		}
		
		else if (this.uiModeButton.isMouseOver(mouseX, mouseY)) {
			Component[] comp = new Component[] { 
				ComponentHelper.style(ComponentColour.WHITE, "cosmoslibrary.gui.ui_mode.info"),
				ComponentHelper.style(ComponentColour.GRAY, "cosmoslibrary.gui.ui_mode.value").append(this.getUIMode().getColouredComp())
			};
			
			this.renderComponentTooltip(poseStack, Arrays.asList(comp), mouseX, mouseY);
		}
		
		if (this.currPage == 29) {
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { -1, 2, -1, 2, 69, 2, -1, 2, -1, 50 }, 0);
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { -1, 2, -1, 2, 1, 2, -1, 2, -1, 3}, 1);

			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { -1, 50, -1, 50, 4, 50, -1, 50, -1, 87 }, 2);
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { -1, 50, -1, 50, 54, 50, -1, 50, -1, 18 }, 3);
		}
		
		else if (this.currPage == 30) {
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { -1, 50, -1, 50, 53, 50, -1, 50, -1, 20 }, 0);
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 11, 88, 11, 88, 3, 88, 11, 88, 11, 86 }, 1);
			
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { -1, -1, -1, -1, 5, -1, -1, -1, -1, 6 }, 2);
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 6, 6, -1, 6, 6, -1, -1, -1, -1, 5 }, 3);
		} 

		else if (this.currPage == 31) {
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 3, -1, 3, 3, 3, 3, -1, 3, -1, 7 }, 0);
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 3, 3, 3, 3, 3, 3, 3, 3, 3, 43 }, 1);
			
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 87, 87, 87, 87, 87, 87, 87, 87, 87, 88 }, 2);
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 2, 2, -1, 2, 2, -1, -1, -1, -1, 45 }, 3);
		} 
		
		else if (this.currPage == 32) {
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 43, 42, 43, 42, 6, 42, 43, 42, 43, 44 }, 0);
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 45, 3, 45, 3, 44, 3, 45, 3, 45, 46 }, 1);
			
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 45, 52, 45, 52, 87, 52, 45, 52, 45, 98 }, 2);
			
			if (this.flipTimer > 0 && this.flipTimer <= 1000) {
				this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { -1, -1, -1, -1, 98, -1, -1, -1, -1, 99 }, 3);
			} else {
				this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { -1, -1, -1, -1, 99, -1, -1, -1, -1, 98 }, 3);
			}
		} 
		
		else if (this.currPage == 33) {
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 3, 87, 3, 3, 90, 3, 3, 18, 3, 72 }, 0);
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 3, 69, 3, 3, 72, 3, 3, 18, 3, 73 }, 1);
			
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 3, 87, 3, 3, 72, 3, 3, 44, 3, 10 }, 2);
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 3, 64, 3, 89, 72, 89, 3, 89, 3, 74 }, 3);
		} 
		
		else if (this.currPage == 34) {
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 1, 3, 1, 3, 87, 3, 1, 3, 1, 8 }, 0);
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 3, 87, 3, 1, 8, 1, 3, 17, 3, 40 }, 1);
			
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 3, 87, 3, 18, 8, 18, 3, 12, 3, 41 }, 2);
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 3, 90, 3, 87, 8, 87, 3, 93, 3, 92 }, 3);
		} 

		else if (this.currPage == 35) {
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 3, 94, 3, 11, 8, 11, 3, 89, 3, 95 }, 0);
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 3, 11, 3, 89, 8, 89, 3, 12, 3, 96 }, 1);
			
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 3, 11, 3, 18, 8, 18, 3, 108, 3, 109 }, 2);
			//this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 3, 11, 3, 89, 8, 89, 3, 12, 3, 96 }, 3);
		}

		else if (this.currPage == 36) {
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 3, 18, 3, 87, 8, 87, 3, 49, 3, 17 }, 0);
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 3, 55, 3, 18, 8, 18, 3, 89, 3, 57 }, 1);
			
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { -1, -1, -1, -1, 61, -1, -1, 8, -1, 60 }, 2);
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { -1, -1, -1, -1, 65, -1, -1, 8, -1, 66 }, 3);
		} 
		
		else if (this.currPage == 37) {
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { -1, -1, -1, -1, 101, -1, -1, 8, -1, 100 }, 0);
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { -1, -1, -1, -1, 103, -1, -1, 8, -1, 102 }, 1);

			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 3, 9, 3, 9, 8, 9, 3, 56, 3, 64 }, 2);
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 3, 71, 3, 71, 8, 71, 3, 42, 3, 70 }, 3);
		}

		else if (this.currPage == 38) {
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 3, 67, 3, 7, 8, 7, 3, 45, 3, 68 }, 0);
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 3, 87, 3, 67, 8, 67, 3, 45, 3, 97 }, 1);
			
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 3, 65, 3, 71, 8, 71, 3, 89, 3, 91 }, 2);
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 3, 14, 3, 13, 15, 13, -1, 3, -1, 16 }, 3);
		}
		
		else if (this.currPage == 39) {
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 3, 87, 3, 11, 22, 11, 3, 87, 3, 27 }, 20);
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 3, 87, 3, 11, 23, 11, 3, 87, 3, 28 }, 21);

			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 3, 87, 3, 11, 21, 11, 3, 87, 3, 26 }, 22);
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 3, 87, 3, 11, 24, 11, 3, 87, 3, 29 }, 23);
		} 

		else if (this.currPage == 40) {
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 3, 87, 3, 11, 25, 11, 3, 87, 3, 30 }, 20);
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { -1, 11, 20, 3, 19, 20, -1, 11, 20, 51 }, 21);

			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 3, 87, 3, 11, 62, 11, 3, 87, 3, 63 }, 22);
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 48, 88, 48, 16, 83, 16, 13, 3, 13, 39 }, 23);
		}

		else if (this.currPage == 41) {
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 3, 11, 3, 87, 31, 87, 11, 3, 11, 35 }, 20);
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 3, 11, 3, 87, 32, 87, 11, 3, 11, 36 }, 21);

			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 3, 11, 3, 87, 33, 87, 11, 3, 11, 37 }, 22);
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 3, 11, 3, 87, 34, 87, 11, 3, 11, 38 }, 23);
		}

		else if (this.currPage == 42) {
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 3, 87, 3, 11, 104, 11, 3, 87, 3, 105 }, 20);
			//this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 87, 11, 87, -1, 32, -1, 11, 87, 11, 36 }, 21);
			
			//this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 87, 11, 87, -1, 33, -1, 11, 87, 11, 37 }, 22);
			//this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 87, 11, 87, -1, 34, -1, 11, 87, 11, 38 }, 23);
		}

		else if (this.currPage == 43) {
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 27, 86, 76 }, 10);
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 26, 86, 77 }, 11);
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 28, 86, 78 }, 12);
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 29, 86, 79 }, 13);
			
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 30, 86, 80 }, 14);
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 63, 86, 81 }, 15);
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 10, 86, 12 }, 16);
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 74, 86, 75 }, 17);
		}
		
		else if (this.currPage == 44) {
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 35, 86, 82 }, 10);
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 36, 86, 83 }, 11);
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 37, 86, 84 }, 12);
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 38, 86, 85 }, 13);

			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 51, 86, 107 }, 14);
			this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 105, 86, 106 }, 15);
			//this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 10, 86, 12 }, 16);
			//this.renderCraftingGrid(poseStack, screen_coords, mouseX, mouseY, new int[] { 74, 86, 75 }, 17);
		}
		
		super.renderComponentHoverEffect(poseStack, style, mouseX, mouseY);
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (super.keyPressed(keyCode, scanCode, modifiers)) {
			return true;
		} else {
			switch (keyCode) {
			case 266:
				this.buttonPreviousPage.onPress();
				return true;
			case 267:
				this.buttonNextPage.onPress();
				return true;
			default:
				return false;
			}
		}
	}
	
	@Override
	public void resize(Minecraft mc, int intOne, int intTwo) {
		int page = this.currPage;
		super.resize(mc, intOne, intTwo);
		this.currPage = page;
	}
	
	@Override
	public boolean mouseScrolled(double mouseX, double MouseY, double direction) {
		
		this.showPage(this.currPage -= (int) direction);
		
		return super.mouseScrolled(mouseX, MouseY, direction);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public boolean handleComponentClicked(Style style) {
		ClickEvent clickevent = style.getClickEvent();
		if (clickevent == null) {
			return false;
		} else if (clickevent.getAction() == ClickEvent.Action.CHANGE_PAGE) {
			String s = clickevent.getValue();

			try {
				int i = Integer.parseInt(s) - 1;
				return this.showPage(i);
			} catch (Exception exception) {
				return false;
			}
		} else {
			boolean flag = super.handleComponentClicked(style);
			if (flag && clickevent.getAction() == ClickEvent.Action.RUN_COMMAND) {
				this.minecraft.setScreen((Screen) null);
			}

			return flag;
		}
	}

	protected void addButtons() {
		this.clearWidgets();
		
		this.uiModeButton = this.addRenderableWidget(new CosmosButtonUIMode(this.getUIMode(), this.width / 2 + 71, this.height / 2 - 90, true, true, ComponentHelper.empty(), (button) -> { this.changeUIMode(); } ));
		
		this.buttonNextPage = this.addRenderableWidget(new TomeChangeButton(this.width / 2 + 58, this.height / 2 + 92, true, this.pageTurnSounds, this.getTexture(), (button) -> { this.nextPage(); }));
		this.buttonPreviousPage = this.addRenderableWidget(new TomeChangeButton(this.width / 2 - 79, this.height / 2 + 92, false, this.pageTurnSounds, this.getTexture(), (button) -> { this.previousPage(); }));
		
		this.buttonExit = this.addRenderableWidget(new TomeButton(this.width / 2 + 70, this.height / 2 - 105, 13, 1, ComponentColour.WHITE.dec(), this.getTexture(), (button) -> { this.onClose(); }));
		this.buttonHome = this.addRenderableWidget(new TomeButton(this.width / 2 + 54, this.height / 2 - 105, 13, 0, ComponentColour.WHITE.dec(), this.getTexture(), (button) -> { this.showPage(0); }));
		
		this.tabIntroduction = this.addRenderableWidget(new TomeButton(this.width / 2 + 85, this.height / 2 - 106, ComponentColour.POCKET_PURPLE_LIGHT.dec(), this.getTexture(), (button) -> { this.showPage(1); }));
		this.tabPockets = this.addRenderableWidget(new TomeButton(this.width / 2 + 85, this.height / 2 - 80, ComponentColour.CYAN.dec(), this.getTexture(), (button) -> { this.showPage(2); }));
		this.tabModules = this.addRenderableWidget(new TomeButton(this.width / 2 + 85, this.height / 2 - 54, ComponentColour.BLUE.dec(), this.getTexture(), (button) -> { this.showPage(6); }));
		this.tabItems = this.addRenderableWidget(new TomeButton(this.width / 2 + 85, this.height / 2 - 28, ComponentColour.LIGHT_BLUE.dec(), this.getTexture(), (button) -> { this.showPage(17); }));
		this.tabArmourWeapons = this.addRenderableWidget(new TomeButton(this.width / 2 + 85, this.height / 2 - 2, ComponentColour.GRAY.dec(), this.getTexture(), (button) -> { this.showPage(22); }));
		this.tabRecipes = this.addRenderableWidget(new TomeButton(this.width / 2 + 85, this.height / 2 + 24, ComponentColour.DARK_GREEN.dec(), this.getTexture(), (button) -> { this.showPage(28); }));
		this.tabCredits = this.addRenderableWidget(new TomeButton(this.width / 2 + 85, this.height / 2 + 50, ComponentColour.RED.dec(), this.getTexture(), (button) -> { this.showPage(45); }));
		
		this.tabConfiguration = this.addRenderableWidget(new TomeButton(this.width / 2 + 85, this.height / 2 +76,  ComponentColour.BLACK.dec(), this.getTexture(), (button) -> {  }));
		
		this.buttonMiss = this.addRenderableWidget(new TomeButton(0, 0, 10, 10, ComponentColour.WHITE.dec(), this.getTexture(), (button) -> { this.currPage = this.pageCount - 1; }));
		
		this.updateButtons();
	}

	public ResourceLocation getTexture() {
		EnumUIMode mode = this.getUIMode();
		
		if (mode.equals(EnumUIMode.DARK)) {
			return this.TEXTURE[1];
		} else {
			return this.TEXTURE[0];
		}
	}
	
	public EnumUIMode getUIMode() {
		if (this.stack != null) {
			return DimensionalTome.getUIMode(this.stack);
		}
		
		return EnumUIMode.DARK;
	}

	private void changeUIMode() {
		NetworkManager.sendToServer(new PacketTomeUpdate(this.playerUUID, this.currPage, this.getUIMode().getNextState()));
		DimensionalTome.setUIMode(this.stack, this.getUIMode().getNextState());
	}

	private void updateButtons() {
		this.buttonNextPage.visible = this.currPage < this.pageCount - 2;
		this.buttonPreviousPage.visible = this.currPage > 0;
	}

	public boolean showPage(int pageNum) {
		int i = Mth.clamp(pageNum, 0, this.pageCount - 2);
		if (i != this.currPage) {
			this.currPage = i;
			this.updateButtons();
			return true;
		} else {
			return false;
		}
		
	}
	
	@Override
	public void onClose() {
		NetworkManager.sendToServer(new PacketTomeUpdate(this.playerUUID, this.currPage == 46 ? 45 : this.currPage, null));
		super.onClose();
	}

	protected void previousPage() {
		if (this.currPage > 0) {
			--this.currPage;
		}
		this.updateButtons();
	}
	
	protected void nextPage() {
		if (this.currPage < this.pageCount - 1) {
			++this.currPage;
		}
		this.updateButtons();
	}
	
	@SuppressWarnings("unused")
	private boolean shouldDrawRecipe() {
		return getRecipeType() >= 0;
	}

	private int getRecipeType() {
		int type = -1;

		if (currPage == 0) {
			type = 0;
		} else if (currPage == 5) {
			type = 1;
		} else if (currPage == 6) {
			type = 2;
		}

		return type;
	}
	
	public void renderCraftingGrid(PoseStack poseStack, int[] screen_coords, int mouseX, int mouseY, int[] ref, int grid_ref) {
		int[] LX = new int[] { 31, 49, 67 }; //left to right [L]
		int[] RX = new int[] { 124, 142, 160 }; //left to right [R]
		int[] TY = new int[] { 36, 54, 72, 92 }; //top to bottom [T]
		int[] BY = new int[] { 126, 144, 162, 182 }; // top to bottom [B]
		
		int[] SLX = new int[] { 31, 67, 49,  124, 160, 142 };
		
		int[] STY = new int[] { 36, 56, 78, 98,  120, 140, 162, 182 };
		
		final ItemStack[] items = new ItemStack[] {
				ItemStack.EMPTY, // 0

				new ItemStack(Items.IRON_INGOT), // 1
				new ItemStack(ObjectManager.dimensional_shard), // 2
				new ItemStack(ObjectManager.dimensional_ingot), // 3
				new ItemStack(Items.DIAMOND), // 4
				new ItemStack(Items.NETHER_STAR), // 5
				new ItemStack(ObjectManager.nether_star_shard), // 6
				new ItemStack(ObjectManager.dimensional_wrench), // 7
				new ItemStack(ObjectManager.module_base), // 8
				new ItemStack(Items.REDSTONE), // 9
				new ItemStack(ObjectManager.dimensional_shifter), // 10
				new ItemStack(Items.NETHERITE_INGOT), // 11
				new ItemStack(ObjectManager.dimensional_shifter_enhanced), // 12
				new ItemStack(Items.BLAZE_POWDER), // 13
				new ItemStack(Items.PHANTOM_MEMBRANE), // 14
				new ItemStack(Items.ELYTRA), // 15
				new ItemStack(ObjectManager.elytra_wing), // 16
				new ItemStack(ObjectManager.module_connector), // 17
				new ItemStack(ObjectManager.dimensional_pearl), // 18
				new ItemStack(Items.BOW), // 19
				new ItemStack(ObjectManager.dimensional_thread), // 20

				new ItemStack(Items.NETHERITE_PICKAXE), // 21
				new ItemStack(Items.NETHERITE_SWORD), // 22
				new ItemStack(Items.NETHERITE_AXE), // 23
				new ItemStack(Items.NETHERITE_SHOVEL), // 24
				new ItemStack(Items.NETHERITE_HOE), // 25

				new ItemStack(ObjectManager.dimensional_pickaxe), // 26
				new ItemStack(ObjectManager.dimensional_sword), // 27
				new ItemStack(ObjectManager.dimensional_axe), // 28
				new ItemStack(ObjectManager.dimensional_shovel), // 29
				new ItemStack(ObjectManager.dimensional_hoe), // 30

				new ItemStack(Items.NETHERITE_HELMET), // 31
				new ItemStack(Items.NETHERITE_CHESTPLATE), // 32
				new ItemStack(Items.NETHERITE_LEGGINGS), // 33
				new ItemStack(Items.NETHERITE_BOOTS), // 34

				new ItemStack(ObjectManager.dimensional_helmet), // 35
				new ItemStack(ObjectManager.dimensional_chestplate), // 36
				new ItemStack(ObjectManager.dimensional_leggings), // 37
				new ItemStack(ObjectManager.dimensional_boots), // 38
				new ItemStack(ObjectManager.dimensional_elytraplate), // 39
				new ItemStack(ObjectManager.armour_module_screen), // 40
				new ItemStack(ObjectManager.armour_module_shifter), // 41

				new ItemStack(Blocks.IRON_BLOCK), // 42
				new ItemStack(ObjectManager.block_dimensional_metal), // 43
				new ItemStack(ObjectManager.block_dimensional_core), // 44
				new ItemStack(ObjectManager.block_dimensional), // 45
				new ItemStack(ObjectManager.block_pocket), // 46
				new ItemStack(Blocks.DIAMOND_BLOCK), // 47
				new ItemStack(Blocks.NETHERITE_BLOCK), // 48
				new ItemStack(Blocks.CHEST), // 49
				new ItemStack(ObjectManager.dimensional_dust), // 50
				new ItemStack(ObjectManager.dimensional_bow), // 51
				new ItemStack(Items.GLOWSTONE_DUST), // 52
				new ItemStack(Items.STRING), // 53
				new ItemStack(Items.ENDER_PEARL), // 54
				new ItemStack(Items.EMERALD), // 55
				new ItemStack(Blocks.REDSTONE_BLOCK), // 56
				new ItemStack(ObjectManager.module_charger), // 57
				new ItemStack(Items.BOOK), // 58
				new ItemStack(ObjectManager.dimensional_tome), // 59
				new ItemStack(ObjectManager.module_crafter), // 60
				new ItemStack(Blocks.CRAFTING_TABLE), // 61
				new ItemStack(Items.TRIDENT), // 62
				new ItemStack(ObjectManager.dimensional_trident), // 63
				new ItemStack(ObjectManager.module_energy_display), // 64
				new ItemStack(Blocks.FURNACE), // 65
				new ItemStack(ObjectManager.module_furnace), // 66
				new ItemStack(Blocks.SMITHING_TABLE), // 67
				new ItemStack(ObjectManager.module_armour_workbench), // 68
				new ItemStack(Blocks.GLOWSTONE), // 69
				new ItemStack(ObjectManager.module_fluid_display), // 70
				new ItemStack(Items.BUCKET), // 71

				new ItemStack(ObjectManager.dimensional_device_base), // 72
				new ItemStack(ObjectManager.dimensional_ejector), // 73
				new ItemStack(ObjectManager.dimensional_energy_cell), // 74
				new ItemStack(ObjectManager.dimensional_energy_cell_enhanced), // 75

				new ItemStack(ObjectManager.dimensional_sword_enhanced), // 76
				new ItemStack(ObjectManager.dimensional_pickaxe_enhanced), // 77
				new ItemStack(ObjectManager.dimensional_axe_enhanced), // 78
				new ItemStack(ObjectManager.dimensional_shovel_enhanced), // 79
				new ItemStack(ObjectManager.dimensional_hoe_enhanced), // 80
				new ItemStack(ObjectManager.dimensional_trident_enhanced), // 81

				new ItemStack(ObjectManager.dimensional_helmet_enhanced), // 82
				new ItemStack(ObjectManager.dimensional_chestplate_enhanced), // 83
				new ItemStack(ObjectManager.dimensional_leggings_enhanced), // 84
				new ItemStack(ObjectManager.dimensional_boots_enhanced), // 85
				new ItemStack(ObjectManager.dimensional_ingot_enhanced), // 86

				new ItemStack(ObjectManager.dimensional_gem), // 87
				new ItemStack(ObjectManager.block_dimensional_gem), // 88

				new ItemStack(Blocks.COPPER_BLOCK), // 89
				new ItemStack(Items.COPPER_INGOT), // 90

				new ItemStack(ObjectManager.module_generator), // 91
				new ItemStack(ObjectManager.armour_module_visor), // 92
				new ItemStack(Items.CLOCK), // 93
				new ItemStack(Items.DAYLIGHT_DETECTOR), // 94
				new ItemStack(ObjectManager.armour_module_solar), // 95
				new ItemStack(ObjectManager.armour_module_battery), // 96

				new ItemStack(ObjectManager.module_upgrade_station), // 97
				new ItemStack(ObjectManager.block_dimensional_focus), // 98
				new ItemStack(ObjectManager.module_focus), // 99

				new ItemStack(ObjectManager.module_blast_furnace), // 100
				new ItemStack(Blocks.BLAST_FURNACE), // 101
				new ItemStack(ObjectManager.module_smithing_table), // 102
				new ItemStack(Blocks.SMITHING_TABLE), // 103

				new ItemStack(Items.SHIELD), // 104
				new ItemStack(ObjectManager.dimensional_shield), // 105
				new ItemStack(ObjectManager.dimensional_shield_enhanced), // 106

				new ItemStack(ObjectManager.dimensional_bow_enhanced), // 107

				new ItemStack(Blocks.ENDER_CHEST), // 108
				new ItemStack(ObjectManager.armour_module_ender_chest), // 109
				// TODO:
		};
		
		//Vanilla Crafting
		if (grid_ref == 0) {
			//Top Left
			
			if (ref[0] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[0]], screen_coords, LX[0], TY[0], mouseX, mouseY, true); }// 0
			if (ref[1] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[1]], screen_coords, LX[1], TY[0], mouseX, mouseY, true); }// 1
			if (ref[2] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[2]], screen_coords, LX[2], TY[0], mouseX, mouseY, true); }// 2
			if (ref[3] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[3]], screen_coords, LX[0], TY[1], mouseX, mouseY, true); }// 3
			if (ref[4] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[4]], screen_coords, LX[1], TY[1], mouseX, mouseY, true); }// 4
			if (ref[5] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[5]], screen_coords, LX[2], TY[1], mouseX, mouseY, true); }// 5
			if (ref[6] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[6]], screen_coords, LX[0], TY[2], mouseX, mouseY, true); }// 6
			if (ref[7] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[7]], screen_coords, LX[1], TY[2], mouseX, mouseY, true); }// 7
			if (ref[8] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[8]], screen_coords, LX[2], TY[2], mouseX, mouseY, true); }// 8
			if (ref[9] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[9]], screen_coords, LX[1], TY[3], mouseX, mouseY, true); }// Out
		} else if (grid_ref == 2) {
			
			//Bottom Left
			if (ref[0] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[0]], screen_coords, LX[0], BY[0], mouseX, mouseY, true); }// 0
			if (ref[1] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[1]], screen_coords, LX[1], BY[0], mouseX, mouseY, true); }// 1
			if (ref[2] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[2]], screen_coords, LX[2], BY[0], mouseX, mouseY, true); }// 2
			if (ref[3] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[3]], screen_coords, LX[0], BY[1], mouseX, mouseY, true); }// 3
			if (ref[4] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[4]], screen_coords, LX[1], BY[1], mouseX, mouseY, true); }// 4
			if (ref[5] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[5]], screen_coords, LX[2], BY[1], mouseX, mouseY, true); }// 5
			if (ref[6] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[6]], screen_coords, LX[0], BY[2], mouseX, mouseY, true); }// 6
			if (ref[7] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[7]], screen_coords, LX[1], BY[2], mouseX, mouseY, true); }// 7
			if (ref[8] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[8]], screen_coords, LX[2], BY[2], mouseX, mouseY, true); }// 8
			if (ref[9] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[9]], screen_coords, LX[1], BY[3], mouseX, mouseY, true); }// Out
		} else if (grid_ref == 1) {
			
			//Top Right
			if (ref[0] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[0]], screen_coords, RX[0], TY[0], mouseX, mouseY, true); }// 0
			if (ref[1] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[1]], screen_coords, RX[1], TY[0], mouseX, mouseY, true); }// 1
			if (ref[2] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[2]], screen_coords, RX[2], TY[0], mouseX, mouseY, true); }// 2
			if (ref[3] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[3]], screen_coords, RX[0], TY[1], mouseX, mouseY, true); }// 3
			if (ref[4] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[4]], screen_coords, RX[1], TY[1], mouseX, mouseY, true); }// 4
			if (ref[5] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[5]], screen_coords, RX[2], TY[1], mouseX, mouseY, true); }// 5
			if (ref[6] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[6]], screen_coords, RX[0], TY[2], mouseX, mouseY, true); }// 6
			if (ref[7] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[7]], screen_coords, RX[1], TY[2], mouseX, mouseY, true); }// 7
			if (ref[8] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[8]], screen_coords, RX[2], TY[2], mouseX, mouseY, true); }// 8
			if (ref[9] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[9]], screen_coords, RX[1], TY[3], mouseX, mouseY, true); }// Out
		} else if (grid_ref == 3) {
			
			//Bottom Right
			if (ref[0] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[0]], screen_coords, RX[0], BY[0], mouseX, mouseY, true); }// 0
			if (ref[1] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[1]], screen_coords, RX[1], BY[0], mouseX, mouseY, true); }// 1
			if (ref[2] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[2]], screen_coords, RX[2], BY[0], mouseX, mouseY, true); }// 2
			if (ref[3] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[3]], screen_coords, RX[0], BY[1], mouseX, mouseY, true); }// 3
			if (ref[4] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[4]], screen_coords, RX[1], BY[1], mouseX, mouseY, true); }// 4
			if (ref[5] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[5]], screen_coords, RX[2], BY[1], mouseX, mouseY, true); }// 5
			if (ref[6] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[6]], screen_coords, RX[0], BY[2], mouseX, mouseY, true); }// 6
			if (ref[7] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[7]], screen_coords, RX[1], BY[2], mouseX, mouseY, true); }// 7
			if (ref[8] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[8]], screen_coords, RX[2], BY[2], mouseX, mouseY, true); }// 8
			if (ref[9] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[9]], screen_coords, RX[1], BY[3], mouseX, mouseY, true); }// Out
		} 
		
		//Smithing Table
		else if (grid_ref == 10) {
			
			if (ref[0] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[0]], screen_coords, SLX[0], STY[0], mouseX, mouseY, true); }// 0
			if (ref[1] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[1]], screen_coords, SLX[1], STY[0], mouseX, mouseY, true); }// 1
			if (ref[2] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[2]], screen_coords, SLX[2], STY[1], mouseX, mouseY, true); }// Out
		} else if (grid_ref == 11) {
			
			if (ref[0] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[0]], screen_coords, SLX[0], STY[2], mouseX, mouseY, true); }// 0
			if (ref[1] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[1]], screen_coords, SLX[1], STY[2], mouseX, mouseY, true); }// 1
			if (ref[2] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[2]], screen_coords, SLX[2], STY[3], mouseX, mouseY, true); }// Out
		} else if (grid_ref == 12) {
			
			if (ref[0] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[0]], screen_coords, SLX[0], STY[4], mouseX, mouseY, true); }// 0
			if (ref[1] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[1]], screen_coords, SLX[1], STY[4], mouseX, mouseY, true); }// 1
			if (ref[2] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[2]], screen_coords, SLX[2], STY[5], mouseX, mouseY, true); }// Out
		} else if (grid_ref == 13) {
			
			if (ref[0] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[0]], screen_coords, SLX[0], STY[6], mouseX, mouseY, true); }// 0
			if (ref[1] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[1]], screen_coords, SLX[1], STY[6], mouseX, mouseY, true); }// 1
			if (ref[2] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[2]], screen_coords, SLX[2], STY[7], mouseX, mouseY, true); }// Out
		} else if (grid_ref == 14) {
			
			if (ref[0] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[0]], screen_coords, SLX[3], STY[0], mouseX, mouseY, true); }// 0
			if (ref[1] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[1]], screen_coords, SLX[4], STY[0], mouseX, mouseY, true); }// 1
			if (ref[2] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[2]], screen_coords, SLX[5], STY[1], mouseX, mouseY, true); }// Out
		} else if (grid_ref == 15) {
			
			if (ref[0] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[0]], screen_coords, SLX[3], STY[2], mouseX, mouseY, true); }// 0
			if (ref[1] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[1]], screen_coords, SLX[4], STY[2], mouseX, mouseY, true); }// 1
			if (ref[2] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[2]], screen_coords, SLX[5], STY[3], mouseX, mouseY, true); }// Out
		} else if (grid_ref == 16) {
			
			if (ref[0] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[0]], screen_coords, SLX[3], STY[4], mouseX, mouseY, true); }// 0
			if (ref[1] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[1]], screen_coords, SLX[4], STY[4], mouseX, mouseY, true); }// 1
			if (ref[2] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[2]], screen_coords, SLX[5], STY[5], mouseX, mouseY, true); }// Out
		} else if (grid_ref == 17) {
			
			if (ref[0] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[0]], screen_coords, SLX[3], STY[6], mouseX, mouseY, true); }// 0
			if (ref[1] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[1]], screen_coords, SLX[4], STY[6], mouseX, mouseY, true); }// 1
			if (ref[2] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[2]], screen_coords, SLX[5], STY[7], mouseX, mouseY, true); }// Out
		} 
		
		//Upgrade Station
		else if (grid_ref == 20) {
			//Top Left
			
			if (ref[0] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[0]], screen_coords, LX[0], TY[0], mouseX, mouseY, true); }// 0
			if (ref[1] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[1]], screen_coords, LX[1], TY[0], mouseX, mouseY, true); }// 1
			if (ref[2] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[2]], screen_coords, LX[2], TY[0], mouseX, mouseY, true); }// 2
			if (ref[3] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[3]], screen_coords, LX[0], TY[1], mouseX, mouseY, true); }// 3
			if (ref[4] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[4]], screen_coords, LX[1], TY[1], mouseX, mouseY, true); }// Focus
			if (ref[5] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[5]], screen_coords, LX[2], TY[1], mouseX, mouseY, true); }// 5
			if (ref[6] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[6]], screen_coords, LX[0], TY[2], mouseX, mouseY, true); }// 6
			if (ref[7] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[7]], screen_coords, LX[1], TY[2], mouseX, mouseY, true); }// 7
			if (ref[8] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[8]], screen_coords, LX[2], TY[2], mouseX, mouseY, true); }// 8
			if (ref[9] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[9]], screen_coords, LX[1], TY[3], mouseX, mouseY, true); }// Out
		} else if (grid_ref == 21) {
			
			//Bottom Left
			if (ref[0] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[0]], screen_coords, LX[0], BY[0], mouseX, mouseY, true); }// 0
			if (ref[1] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[1]], screen_coords, LX[1], BY[0], mouseX, mouseY, true); }// 1
			if (ref[2] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[2]], screen_coords, LX[2], BY[0], mouseX, mouseY, true); }// 2
			if (ref[3] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[3]], screen_coords, LX[0], BY[1], mouseX, mouseY, true); }// 3
			if (ref[4] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[4]], screen_coords, LX[1], BY[1], mouseX, mouseY, true); }// Focus
			if (ref[5] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[5]], screen_coords, LX[2], BY[1], mouseX, mouseY, true); }// 5
			if (ref[6] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[6]], screen_coords, LX[0], BY[2], mouseX, mouseY, true); }// 6
			if (ref[7] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[7]], screen_coords, LX[1], BY[2], mouseX, mouseY, true); }// 7
			if (ref[8] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[8]], screen_coords, LX[2], BY[2], mouseX, mouseY, true); }// 8
			if (ref[9] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[9]], screen_coords, LX[1], BY[3], mouseX, mouseY, true); }// Out
		} else if (grid_ref == 22) {
			
			//Top Right
			if (ref[0] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[0]], screen_coords, RX[0], TY[0], mouseX, mouseY, true); }// 0
			if (ref[1] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[1]], screen_coords, RX[1], TY[0], mouseX, mouseY, true); }// 1
			if (ref[2] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[2]], screen_coords, RX[2], TY[0], mouseX, mouseY, true); }// 2
			if (ref[3] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[3]], screen_coords, RX[0], TY[1], mouseX, mouseY, true); }// 3
			if (ref[4] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[4]], screen_coords, RX[1], TY[1], mouseX, mouseY, true); }// Focus
			if (ref[5] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[5]], screen_coords, RX[2], TY[1], mouseX, mouseY, true); }// 5
			if (ref[6] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[6]], screen_coords, RX[0], TY[2], mouseX, mouseY, true); }// 6
			if (ref[7] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[7]], screen_coords, RX[1], TY[2], mouseX, mouseY, true); }// 7
			if (ref[8] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[8]], screen_coords, RX[2], TY[2], mouseX, mouseY, true); }// 8
			if (ref[9] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[9]], screen_coords, RX[1], TY[3], mouseX, mouseY, true); }// Out
		} else if (grid_ref == 23) {
			
			//Bottom Right
			if (ref[0] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[0]], screen_coords, RX[0], BY[0], mouseX, mouseY, true); }// 0
			if (ref[1] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[1]], screen_coords, RX[1], BY[0], mouseX, mouseY, true); }// 1
			if (ref[2] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[2]], screen_coords, RX[2], BY[0], mouseX, mouseY, true); }// 2
			if (ref[3] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[3]], screen_coords, RX[0], BY[1], mouseX, mouseY, true); }// 3
			if (ref[4] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[4]], screen_coords, RX[1], BY[1], mouseX, mouseY, true); }// Focus
			if (ref[5] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[5]], screen_coords, RX[2], BY[1], mouseX, mouseY, true); }// 5
			if (ref[6] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[6]], screen_coords, RX[0], BY[2], mouseX, mouseY, true); }// 6
			if (ref[7] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[7]], screen_coords, RX[1], BY[2], mouseX, mouseY, true); }// 7
			if (ref[8] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[8]], screen_coords, RX[2], BY[2], mouseX, mouseY, true); }// 8
			if (ref[9] != -1) { CosmosUISystem.renderItemStack(this, font, poseStack, items[ref[9]], screen_coords, RX[1], BY[3], mouseX, mouseY, true); }// Out
		} 
	}
	
	public void renderSecret(PoseStack poseStack, int[] screen_coords) {
		if (this.currPage == 47) {
			CosmosUISystem.setTextureWithColour(poseStack, FLAT_TEXTURES_0, new float[] { 1.0F, 1.0F, 1.0F, 1.0F });
			CosmosUISystem.renderStaticElement(this, poseStack, screen_coords, 202 / 2 - 16, 225 / 2 - 26, 223, 223, 32, 32);
			
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 40, ComponentColour.RED.dec(), ComponentHelper.locString("Adventuregell,"), false);
			
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 90, ComponentColour.RED.dec(), "I miss who you used to be..", false);
			
			FONT.drawCenteredString(poseStack, font, screen_coords, 104, 174, ComponentColour.BLACK.dec(), ComponentHelper.locString("[ ", "Tribute", " ]"), false);
		}
	}
}