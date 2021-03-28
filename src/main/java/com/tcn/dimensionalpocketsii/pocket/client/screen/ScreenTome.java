package com.tcn.dimensionalpocketsii.pocket.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tcn.cosmoslibrary.client.util.ScreenUtil;
import com.tcn.cosmoslibrary.client.util.ScreenUtil.FONT;
import com.tcn.cosmoslibrary.common.comp.CosmosCompHelper;
import com.tcn.cosmoslibrary.common.comp.CosmosColour;
import com.tcn.cosmoslibrary.common.comp.CosmosCompHelper.Value;
import com.tcn.dimensionalpocketsii.DimensionalPockets;
import com.tcn.dimensionalpocketsii.client.screen.widget.TomeButton;
import com.tcn.dimensionalpocketsii.client.screen.widget.TomeChangeButton;
import com.tcn.dimensionalpocketsii.core.management.CoreModBusManager;

import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenTome extends Screen {

	public static final ResourceLocation TEXTURE = new ResourceLocation(DimensionalPockets.MOD_ID, "textures/gui/tome/tome.png");
	
	private int currPage;
	private int pageCount = 27;
	private TomeChangeButton buttonNextPage;
	private TomeChangeButton buttonPreviousPage;
	private TomeButton buttonExit;
	private TomeButton buttonHome;
	
	private TomeButton tabIntroduction; //1,
	private TomeButton tabPockets; //2, 3, 4, 5
	private TomeButton tabModules; //6, 7, 8, 9
	private TomeButton tabItems; // 9, 10
	private TomeButton tabConfiguration; //11, 12
	private TomeButton tabArmourWeapons; // 13, 14, 15
	private TomeButton tabRecipes; //16, 17, 18, 19, 20, 21, 22, 23, 24
	private TomeButton tabCredits; // 25
	
	private final boolean pageTurnSounds;

	public ScreenTome(boolean pageTurnSoundsIn) {
		super(CosmosCompHelper.locComp("dimensionalpocketsii.tome_heading"));
		
		this.pageTurnSounds = pageTurnSoundsIn;
	}

	@Override
	protected void init() { }

	@OnlyIn(Dist.CLIENT)
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		int[] screen_coords = new int[] { ((this.width - 202) / 2), (this.height - 225) / 2};

		this.renderBackground(matrixStack);
		this.minecraft.getTextureManager().bind(TEXTURE);
		
		this.blit(matrixStack, (this.width - 202) / 2, (this.height - 225) / 2, 0, 0, 202, 225);
		
		FONT.drawString(matrixStack, font, screen_coords, 24, 10, CosmosCompHelper.locString("dimensionalpocketsii.tome_page") + this.currPage, false, true);
		FONT.drawString(matrixStack, font, screen_coords, 69, 10, CosmosCompHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_heading"), false, true, CosmosColour.POCKET_PURPLE_GUI.dec());
		
		this.buttons.clear();
		this.addButtons();
		
		if (this.currPage == 0) {
			drawWrappedString(matrixStack, 101, -4, 0, CosmosColour.BLACK.dec(), CosmosCompHelper.locString("dimensionalpocketsii.tome_one_body") + CosmosCompHelper.locString(Value.PURPLE + Value.UNDERLINE, "dimensionalpocketsii.tome_one_body_one")
					 + CosmosCompHelper.locString("dimensionalpocketsii.tome_one_body_two"));
			
			drawCenteredString(matrixStack, 101, 65, CosmosColour.BLACK.dec(), CosmosCompHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_one_body_heading"), false);
			
			FONT.drawString(matrixStack, font, screen_coords, 30, 110, CosmosCompHelper.locString("1: ", "dimensionalpocketsii.tome_two_heading"), CosmosColour.PURPLE.dec(), false);
			FONT.drawString(matrixStack, font, screen_coords, 172, 110, "1", CosmosColour.PURPLE.dec(), false);
			
			FONT.drawString(matrixStack, font, screen_coords, 30, 120, CosmosCompHelper.locString("2: ", "dimensionalpocketsii.tome_three_heading"), CosmosColour.CYAN.dec(), false);
			FONT.drawString(matrixStack, font, screen_coords, 160, 120, "2-5", CosmosColour.CYAN.dec(), false);
			
			FONT.drawString(matrixStack, font, screen_coords, 30, 130, CosmosCompHelper.locString("3: ", "dimensionalpocketsii.tome_four_heading"), CosmosColour.BLUE.dec(), false);
			FONT.drawString(matrixStack, font, screen_coords, 160, 130, "6-7", CosmosColour.BLUE.dec(), false);
			
			FONT.drawString(matrixStack, font, screen_coords, 30, 140, CosmosCompHelper.locString("4: ", "dimensionalpocketsii.tome_five_heading"), CosmosColour.LIGHT_BLUE.dec(), false);
			FONT.drawString(matrixStack, font, screen_coords, 160, 140, "8-9", CosmosColour.LIGHT_BLUE.dec(), false);
			
			FONT.drawString(matrixStack, font, screen_coords, 30, 150, CosmosCompHelper.locString("5: ", "dimensionalpocketsii.tome_six_heading"), CosmosColour.GREEN.dec(), false);
			FONT.drawString(matrixStack, font, screen_coords, 149, 150, "10-11", CosmosColour.GREEN.dec(), false);

			FONT.drawString(matrixStack, font, screen_coords, 30, 160, CosmosCompHelper.locString("6: ", "dimensionalpocketsii.tome_seven_heading"), CosmosColour.ORANGE.dec(), false);
			FONT.drawString(matrixStack, font, screen_coords, 149, 160, "12-14", CosmosColour.ORANGE.dec(), false);
			
			FONT.drawString(matrixStack, font, screen_coords, 30, 170, CosmosCompHelper.locString("7: ", "dimensionalpocketsii.tome_nine_heading"), CosmosColour.MAGENTA.dec(), false);
			FONT.drawString(matrixStack, font, screen_coords, 149, 170, "15-23", CosmosColour.MAGENTA.dec(), false);
			
			FONT.drawString(matrixStack, font, screen_coords, 30, 180, CosmosCompHelper.locString("8: ", "dimensionalpocketsii.tome_ten_heading"), CosmosColour.RED.dec(), false);
			FONT.drawString(matrixStack, font, screen_coords, 167, 180, "24", CosmosColour.RED.dec(), false);
			
			drawCenteredString(matrixStack, 101, 174, CosmosColour.GRAY.dec(), CosmosCompHelper.locString("[ ", "dimensionalpocketsii.tome_foot_one", " 1 ]"), false);
		} 
		
		else if (this.currPage == 1) {
			drawCenteredString(matrixStack, 101, -8, CosmosColour.PURPLE.dec(), CosmosCompHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_two_heading"), false);
			
			drawWrappedString(matrixStack, 101, 4, 0, CosmosColour.BLACK.dec(), CosmosCompHelper.locString("dimensionalpocketsii.tome_two_body_one"));
			drawCenteredString(matrixStack, 101, 65, CosmosColour.PURPLE.dec(), CosmosCompHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_two_body_two"), false);
			
			FONT.drawString(matrixStack, font, screen_coords, 25, 110, CosmosCompHelper.locString("dimensionalpocketsii.tome_two_sub_one"), CosmosColour.CYAN.dec(), false);
			FONT.drawString(matrixStack, font, screen_coords, 25, 120, CosmosCompHelper.locString("dimensionalpocketsii.tome_two_sub_two"), CosmosColour.BLUE.dec(), false);
			FONT.drawString(matrixStack, font, screen_coords, 25, 130, CosmosCompHelper.locString("dimensionalpocketsii.tome_two_sub_three"), CosmosColour.LIGHT_BLUE.dec(), false);
			FONT.drawString(matrixStack, font, screen_coords, 25, 140, CosmosCompHelper.locString("dimensionalpocketsii.tome_two_sub_four"), CosmosColour.GREEN.dec(), false);
			FONT.drawString(matrixStack, font, screen_coords, 25, 150, CosmosCompHelper.locString("dimensionalpocketsii.tome_two_sub_five"), CosmosColour.ORANGE.dec(), false);
			FONT.drawString(matrixStack, font, screen_coords, 25, 160, CosmosCompHelper.locString("dimensionalpocketsii.tome_two_sub_six"), CosmosColour.MAGENTA.dec(), false);
			
			drawWrappedString(matrixStack, 101, 140, 0, CosmosColour.BLACK.dec(), CosmosCompHelper.locString("dimensionalpocketsii.tome_two_body_three"));
			
			drawCenteredString(matrixStack, 101, 174, CosmosColour.GRAY.dec(), CosmosCompHelper.locString("[ ", "dimensionalpocketsii.tome_foot_two", " 1 ]"), false);
		}
		
		else if (this.currPage == 2) {
			drawCenteredString(matrixStack, 101, -8, CosmosColour.CYAN.dec(), CosmosCompHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_three_heading"), false);
			
			this.minecraft.getTextureManager().bind(new ResourceLocation(DimensionalPockets.MOD_ID, "textures/gui/tome/blocks.png"));
			ScreenUtil.DRAW.drawStaticElement(this, matrixStack, screen_coords, 75, 40, 0, 0, 60, 60);
			
			drawWrappedString(matrixStack, 101, 70, 0, CosmosColour.BLACK.dec(), CosmosCompHelper.locString("dimensionalpocketsii.tome_three_body_one"));
			
			drawCenteredString(matrixStack, 101, 174, CosmosColour.GRAY.dec(), CosmosCompHelper.locString("[ ", "dimensionalpocketsii.tome_foot_three", " 1 ]"), false);
		} 
		
		else if (this.currPage == 3) {
			
			drawCenteredString(matrixStack, 101, 174, CosmosColour.GRAY.dec(), CosmosCompHelper.locString("[ ", "dimensionalpocketsii.tome_foot_three", " 2 ]"), false);
		} 
		
		else if (this.currPage == 4) {
			
			drawCenteredString(matrixStack, 101, 174, CosmosColour.GRAY.dec(), CosmosCompHelper.locString("[ ", "dimensionalpocketsii.tome_foot_three", " 3 ]"), false);
		} 
		
		else if (this.currPage == 5) {
			
			drawCenteredString(matrixStack, 101, 174, CosmosColour.GRAY.dec(), CosmosCompHelper.locString("[ ", "dimensionalpocketsii.tome_foot_three", " 4 ]"), false);
		} 
		
		//Modules
		else if (this.currPage == 6) {
			drawCenteredString(matrixStack, 101, -8, CosmosColour.BLUE.dec(), CosmosCompHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_four_heading"), false);

			drawWrappedString(matrixStack, 101, 4, 0, CosmosColour.BLACK.dec(), CosmosCompHelper.locString("dimensionalpocketsii.tome_four_body"));
			
			drawCenteredString(matrixStack, 101, 174, CosmosColour.GRAY.dec(), CosmosCompHelper.locString("[ ", "dimensionalpocketsii.tome_foot_four", " 1 ]"), false);
		} 
		
		else if (this.currPage == 7) {
			drawCenteredString(matrixStack, 101, -8, CosmosColour.BLUE.dec(), CosmosCompHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_four_heading_one"), false);

			this.minecraft.getTextureManager().bind(new ResourceLocation(DimensionalPockets.MOD_ID, "textures/gui/tome/blocks.png"));
			ScreenUtil.DRAW.drawStaticElement(this, matrixStack, screen_coords, 75, 40, 60, 0, 60, 60);
			
			drawWrappedString(matrixStack, 101, 70, 0, CosmosColour.BLACK.dec(), CosmosCompHelper.locString("dimensionalpocketsii.tome_four_body_one"));
			
			drawCenteredString(matrixStack, 101, 174, CosmosColour.GRAY.dec(), CosmosCompHelper.locString("[ ", "dimensionalpocketsii.tome_foot_four", " 2 ]"), false);
		}
		
		else if (this.currPage == 8) {
			drawCenteredString(matrixStack, 101, -8, CosmosColour.BLUE.dec(), CosmosCompHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_four_heading_two"), false);

			this.minecraft.getTextureManager().bind(new ResourceLocation(DimensionalPockets.MOD_ID, "textures/gui/tome/blocks.png"));
			ScreenUtil.DRAW.drawStaticElement(this, matrixStack, screen_coords, 75, 40, 120, 0, 60, 60);
			
			drawWrappedString(matrixStack, 101, 70, 0, CosmosColour.BLACK.dec(), CosmosCompHelper.locString("dimensionalpocketsii.tome_four_body_two"));
			
			drawCenteredString(matrixStack, 101, 174, CosmosColour.GRAY.dec(), CosmosCompHelper.locString("[ ", "dimensionalpocketsii.tome_foot_four", " 3 ]"), false);
		}
		
		else if (this.currPage == 9) {
			drawCenteredString(matrixStack, 101, -8, CosmosColour.BLUE.dec(), CosmosCompHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_four_heading_three"), false);

			this.minecraft.getTextureManager().bind(new ResourceLocation(DimensionalPockets.MOD_ID, "textures/gui/tome/blocks.png"));
			ScreenUtil.DRAW.drawStaticElement(this, matrixStack, screen_coords, 75, 40, 180, 0, 61, 61);
			
			drawWrappedString(matrixStack, 101, 85, 0, CosmosColour.BLACK.dec(), CosmosCompHelper.locString("dimensionalpocketsii.tome_four_body_three"));
			
			drawCenteredString(matrixStack, 101, 174, CosmosColour.GRAY.dec(), CosmosCompHelper.locString("[ ", "dimensionalpocketsii.tome_foot_four", " 4 ]"), false);
		}
		
		else if (this.currPage == 10) {
			drawCenteredString(matrixStack, 101, -8, CosmosColour.LIGHT_BLUE.dec(), CosmosCompHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_five_heading"), false);
			
			drawCenteredString(matrixStack, 101, 174, CosmosColour.GRAY.dec(), CosmosCompHelper.locString("[ ", "dimensionalpocketsii.tome_foot_five", " 1 ]"), false);
		} 
		
		else if (this.currPage == 11) {
			
			drawCenteredString(matrixStack, 101, 174, CosmosColour.GRAY.dec(), CosmosCompHelper.locString("[ ", "dimensionalpocketsii.tome_foot_five", " 2 ]"), false);
		} 
		
		else if (this.currPage == 12) {
			drawCenteredString(matrixStack, 101, -8, CosmosColour.GREEN.dec(), CosmosCompHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_six_heading"), false);
			
			drawCenteredString(matrixStack, 101, 174, CosmosColour.GRAY.dec(), CosmosCompHelper.locString("[ ", "dimensionalpocketsii.tome_foot_six", " 1 ]"), false);
		}
		
		else if (this.currPage == 13) {
			
			drawCenteredString(matrixStack, 101, 174, CosmosColour.GRAY.dec(), CosmosCompHelper.locString("[ ", "dimensionalpocketsii.tome_foot_six", " 2 ]"), false);
		} 
		
		else if (this.currPage == 14) {
			drawCenteredString(matrixStack, 101, -8, CosmosColour.ORANGE.dec(), CosmosCompHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_seven_heading"), false);
			
			drawCenteredString(matrixStack, 101, 174, CosmosColour.GRAY.dec(), CosmosCompHelper.locString("[ ", "dimensionalpocketsii.tome_foot_seven", " 1 ]"), false);
		} 
		
		else if (this.currPage == 15) {

			drawCenteredString(matrixStack, 101, 174, CosmosColour.GRAY.dec(), CosmosCompHelper.locString("[ ", "dimensionalpocketsii.tome_foot_seven", " 2 ]"), false);
		} 
		
		else if (this.currPage == 16) {
			drawCenteredString(matrixStack, 101, -8, CosmosColour.ORANGE.dec(), CosmosCompHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_eight_heading"), false);
			
			drawCenteredString(matrixStack, 101, 174, CosmosColour.GRAY.dec(), CosmosCompHelper.locString("[ ", "dimensionalpocketsii.tome_foot_eight", " 1 ]"), false);
		} 
		
		else if (this.currPage >= 17 && this.currPage <= 25) {
			this.minecraft.getTextureManager().bind(TEXTURE);
			ScreenUtil.DRAW.drawStaticElement(this, matrixStack, screen_coords, 30, 35, 202, 0, 54, 74);
			ScreenUtil.DRAW.drawStaticElement(this, matrixStack, screen_coords, 30, 125, 202, 0, 54, 74);
			ScreenUtil.DRAW.drawStaticElement(this, matrixStack, screen_coords, 123, 35, 202, 0, 54, 74);
			ScreenUtil.DRAW.drawStaticElement(this, matrixStack, screen_coords, 123, 125, 202, 0, 54, 74);
			

			drawCenteredString(matrixStack, 101, 174, CosmosColour.GRAY.dec(), CosmosCompHelper.locString("[ ", "dimensionalpocketsii.tome_foot_nine", " " + (this.currPage - 16) + " ]"), false);
			
			if (this.currPage == 17) {
				drawCenteredString(matrixStack, 101, -8, CosmosColour.MAGENTA.dec(),CosmosCompHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_nine_heading"), false);
				font.draw(matrixStack, Value.BOLD + "2", screen_coords[0] + 67, screen_coords[1] + 102, CosmosColour.GRAY.dec());
				font.draw(matrixStack, Value.BOLD + "4", screen_coords[0] + 67, screen_coords[1] + 192, CosmosColour.GRAY.dec());
				font.draw(matrixStack, Value.BOLD + "4", screen_coords[0] + 160, screen_coords[1] + 192, CosmosColour.GRAY.dec());
			}
		} 
		
		else if (this.currPage == 26) {
			drawCenteredString(matrixStack, 101, -8, CosmosColour.RED.dec(), CosmosCompHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_ten_heading"), false);
			
			drawCenteredString(matrixStack, 101, 10, CosmosColour.BLACK.dec(), CosmosCompHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_ten_body_one"), false);
			drawCenteredString(matrixStack, 101, 22, CosmosColour.PURPLE.dec(), "TheCosmicNebula", false);
			drawCenteredString(matrixStack, 101, 32, CosmosColour.PURPLE.dec(), "(Head Programmer)", false);
			drawCenteredString(matrixStack, 101, 44, CosmosColour.BLUE.dec(), "NPException + Team", false);
			drawCenteredString(matrixStack, 101, 54, CosmosColour.BLUE.dec(), "(Original Mod)", false);

			drawCenteredString(matrixStack, 101, 68, CosmosColour.BLACK.dec(), CosmosCompHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_ten_body_two"), false);
			drawCenteredString(matrixStack, 101, 80, CosmosColour.CYAN.dec(), "Scarlet Spark", false);
			drawCenteredString(matrixStack, 101, 90, CosmosColour.CYAN.dec(), "(Lead Beta Tester)", false);
			drawCenteredString(matrixStack, 101, 102, CosmosColour.GREEN.dec(), "Apolybrium", false);
			drawCenteredString(matrixStack, 101, 112, CosmosColour.GREEN.dec(), "(Ideas & Textures)", false);

			drawCenteredString(matrixStack, 101, 126, CosmosColour.BLACK.dec(), CosmosCompHelper.locString(Value.UNDERLINE, "dimensionalpocketsii.tome_ten_body_three"), false);
			drawCenteredString(matrixStack, 101, 138, CosmosColour.ORANGE.dec(), "Rechalow", false);
			drawCenteredString(matrixStack, 101, 148, CosmosColour.ORANGE.dec(), "(Chinese)", false);

			drawCenteredString(matrixStack, 101, 174, CosmosColour.GRAY.dec(), CosmosCompHelper.locString("[ ", "dimensionalpocketsii.tome_foot_ten", " ]"), false);
		}

		this.renderComponentHoverEffect(matrixStack, Style.EMPTY, mouseX, mouseY);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void renderComponentHoverEffect(MatrixStack matrixStack, Style style, int mouseX, int mouseY) {
		int[] screen_coords = new int[] { ((this.width - 202) / 2), (this.height - 225) / 2 };
		
		if (this.buttonExit.isMouseOver(mouseX, mouseY)) {
			this.renderTooltip(matrixStack, CosmosCompHelper.locComp(CosmosColour.RED, false, "dimensionalpocketsii.tome_button_one"), mouseX, mouseY);
		}  else if (this.buttonHome.isMouseOver(mouseX, mouseY)) {
			this.renderTooltip(matrixStack, CosmosCompHelper.locComp(CosmosColour.GREEN, false, "dimensionalpocketsii.tome_button_two"), mouseX, mouseY);
		} else if (this.buttonNextPage.isMouseOver(mouseX, mouseY)) {
			this.renderTooltip(matrixStack, CosmosCompHelper.locComp(CosmosColour.PURPLE, false, "dimensionalpocketsii.tome_button_three"), mouseX, mouseY);
		} else if (this.buttonPreviousPage.isMouseOver(mouseX, mouseY)) {
			this.renderTooltip(matrixStack, CosmosCompHelper.locComp(CosmosColour.PURPLE, false, "dimensionalpocketsii.tome_button_four"), mouseX, mouseY);
		} 
		
		else if (this.tabIntroduction.isMouseOver(mouseX, mouseY)) {
			this.renderTooltip(matrixStack, CosmosCompHelper.locComp(CosmosColour.PURPLE, false, "dimensionalpocketsii.tome_two_heading"), mouseX, mouseY);
		} else if (this.tabPockets.isMouseOver(mouseX, mouseY)) {
			this.renderTooltip(matrixStack, CosmosCompHelper.locComp(CosmosColour.CYAN, false, "dimensionalpocketsii.tome_three_heading"), mouseX, mouseY);
		} else if (this.tabModules.isMouseOver(mouseX, mouseY)) {
			this.renderTooltip(matrixStack, CosmosCompHelper.locComp(CosmosColour.BLUE, false, "dimensionalpocketsii.tome_four_heading"), mouseX, mouseY);
		} else if (this.tabItems.isMouseOver(mouseX, mouseY)) {
			this.renderTooltip(matrixStack, CosmosCompHelper.locComp(CosmosColour.LIGHT_BLUE, false, "dimensionalpocketsii.tome_five_heading"), mouseX, mouseY);
		} else if (this.tabConfiguration.isMouseOver(mouseX, mouseY)) {
			this.renderTooltip(matrixStack, CosmosCompHelper.locComp(CosmosColour.GREEN, false, "dimensionalpocketsii.tome_six_heading"), mouseX, mouseY);
		} else if (this.tabArmourWeapons.isMouseOver(mouseX, mouseY)) {
			this.renderTooltip(matrixStack, CosmosCompHelper.locComp(CosmosColour.ORANGE, false, "dimensionalpocketsii.tome_seven_heading"), mouseX, mouseY);
		} else if (this.tabRecipes.isMouseOver(mouseX, mouseY)) {
			this.renderTooltip(matrixStack, CosmosCompHelper.locComp(CosmosColour.MAGENTA, false, "dimensionalpocketsii.tome_nine_heading"), mouseX, mouseY);
		} else if (this.tabCredits.isMouseOver(mouseX, mouseY)) {
			this.renderTooltip(matrixStack, CosmosCompHelper.locComp(CosmosColour.RED, false, "dimensionalpocketsii.tome_ten_heading"), mouseX, mouseY);
		}
		
		if (this.currPage == 17) {
			this.renderCraftingGrid(matrixStack, screen_coords, mouseX, mouseY, new int[] { -1, 2, -1, 2, 1, 2, -1, 2, -1, 3}, 0);
			this.renderCraftingGrid(matrixStack, screen_coords, mouseX, mouseY, new int[] { -1, -1, -1, -1, 5, -1, -1, -1, -1, 6 }, 1);
			
			this.renderCraftingGrid(matrixStack, screen_coords, mouseX, mouseY, new int[] { 2, 2, -1, 2, 2, -1, -1, -1, -1, 45 }, 2);
			this.renderCraftingGrid(matrixStack, screen_coords, mouseX, mouseY, new int[] { -1, 2, -1, 2, 52, 2, -1, 2, -1, 50 }, 3);
		}
		
		else if (this.currPage == 18) {
			this.renderCraftingGrid(matrixStack, screen_coords, mouseX, mouseY, new int[] { -1, 50, -1, 50, 53, 50, -1, 50, -1, 20 }, 0);			
			this.renderCraftingGrid(matrixStack, screen_coords, mouseX, mouseY, new int[] { -1, 50, -1, 50, 54, 50, -1, 50, -1, 18 }, 1);
			
			this.renderCraftingGrid(matrixStack, screen_coords, mouseX, mouseY, new int[] { -1, -1, -1, -1, 58, -1, -1, 2, -1, 59 }, 2);
			//this.renderCraftingGrid(matrixStack, screen_coords, mouseX, mouseY, new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }, 3);
		} 
		
		else if (this.currPage == 19) {
			this.renderCraftingGrid(matrixStack, screen_coords, mouseX, mouseY, new int[] { 43, 42, 43, 42, 6, 42, 43, 42, 43, 44 }, 0);
			this.renderCraftingGrid(matrixStack, screen_coords, mouseX, mouseY, new int[] { 3, 3, 3, 3, 3, 3, 3, 3, 3, 43 }, 1);
			
			this.renderCraftingGrid(matrixStack, screen_coords, mouseX, mouseY, new int[] { 45, 3, 45, 3, 44, 3, 45, 3, 45, 46 }, 2);
			this.renderCraftingGrid(matrixStack, screen_coords, mouseX, mouseY, new int[] { 3, -1, 3, 3, 3, 3, -1, 3, -1, 7 }, 3);
		} 
		
		else if (this.currPage == 20) {
			this.renderCraftingGrid(matrixStack, screen_coords, mouseX, mouseY, new int[] { 3, 4, 3, 3, 9, 3, 3, 44, 3, 10 }, 0);
			this.renderCraftingGrid(matrixStack, screen_coords, mouseX, mouseY, new int[] { 3, 7, 3, 11, 10, 11, 3, 47, 3, 12 }, 1);
			
			this.renderCraftingGrid(matrixStack, screen_coords, mouseX, mouseY, new int[] { -1, 3, -1, 11, 3, 11, -1, 22, -1, 27 }, 2);
			this.renderCraftingGrid(matrixStack, screen_coords, mouseX, mouseY, new int[] { 3, 3, 3, -1, 21, -1, -1, 11, -1, 26 }, 3);
		} 
		
		else if (this.currPage == 21) {
			this.renderCraftingGrid(matrixStack, screen_coords, mouseX, mouseY, new int[] { 3, 3, -1, 3, 23, -1, -1, 11, -1, 28 }, 0);
			this.renderCraftingGrid(matrixStack, screen_coords, mouseX, mouseY, new int[] { -1, 3, -1, -1, 24, -1, -1, 11, -1, 29 }, 1);
			
			this.renderCraftingGrid(matrixStack, screen_coords, mouseX, mouseY, new int[] { -1, 3, 3, -1, 25, -1, -1, 11, -1, 30 }, 2);
			this.renderCraftingGrid(matrixStack, screen_coords, mouseX, mouseY, new int[] { -1, 3, 20, 3, 19, 20, -1, 3, 20, 51 }, 3);
		} 
		
		else if (this.currPage == 22) {
			this.renderCraftingGrid(matrixStack, screen_coords, mouseX, mouseY, new int[] { 1, 3, 1, 3, 4, 3, 1, 3, 1, 8 }, 0);
			this.renderCraftingGrid(matrixStack, screen_coords, mouseX, mouseY, new int[] { 3, 18, 3, 4, 8, 4, 3, 49, 3, 17 }, 1);
			
			this.renderCraftingGrid(matrixStack, screen_coords, mouseX, mouseY, new int[] { 3, 55, 3, 18, 8, 18, 3, 56, 3, 57 }, 2);
			this.renderCraftingGrid(matrixStack, screen_coords, mouseX, mouseY, new int[] { -1, -1, -1, -1, 61, -1, -1, 8, -1, 60 }, 3);
		} 
		
		else if (this.currPage == 23) {
			this.renderCraftingGrid(matrixStack, screen_coords, mouseX, mouseY, new int[] { -1, -1, -1, 3, 3, 3, 11, 31, 11, 35 }, 0);
			this.renderCraftingGrid(matrixStack, screen_coords, mouseX, mouseY, new int[] { 11, 32, 11, 3, 3, 3, 3, 3, 3, 36 }, 1);
			
			this.renderCraftingGrid(matrixStack, screen_coords, mouseX, mouseY, new int[] { 3, 3, 3, 3, 33, 3, 11, -1, 11, 37 }, 2);
			this.renderCraftingGrid(matrixStack, screen_coords, mouseX, mouseY, new int[] { -1, -1, -1, 3, 34, 3, 11, -1, 11, 38 }, 3);
		} 
		
		else if (this.currPage == 24) {
			this.renderCraftingGrid(matrixStack, screen_coords, mouseX, mouseY, new int[] { 43, 36, 43, 48, 5, 48, 16, 13, 16, 39 }, 0);
			this.renderCraftingGrid(matrixStack, screen_coords, mouseX, mouseY, new int[] { 3, -1, 3, 17, 39, 17, 7, 9, 7, 40 }, 1);
			
			this.renderCraftingGrid(matrixStack, screen_coords, mouseX, mouseY, new int[] { 3, 7, 3, 6, 39, 6, 3, 12, 3, 41 }, 2);
			this.renderCraftingGrid(matrixStack, screen_coords, mouseX, mouseY, new int[] { 3, 14, 3, 13, 15, 13, -1, 3, -1, 16}, 3);
		}
		
		else if (this.currPage == 25) {
			this.renderCraftingGrid(matrixStack, screen_coords, mouseX, mouseY, new int[] { 3, -1, 3, 3, 62, 3, -1, 11, -1, 63 }, 0);
			this.renderCraftingGrid(matrixStack, screen_coords, mouseX, mouseY, new int[] { 3, 9, 3, 9, 8, 9, 3, 56, 3, 64 }, 1);

			//this.renderCraftingGrid(matrixStack, screen_coords, mouseX, mouseY, new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }, 2);
			//this.renderCraftingGrid(matrixStack, screen_coords, mouseX, mouseY, new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }, 3);
		}
		
		super.renderComponentHoverEffect(matrixStack, style, mouseX, mouseY);
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
		this.buttons.clear();
		this.buttonNextPage = this.addButton(new TomeChangeButton(this.width / 2 + 58, this.height / 2 + 92, true, (p_214159_1_) -> { this.nextPage(); }, this.pageTurnSounds, TEXTURE));
		this.buttonPreviousPage = this.addButton(new TomeChangeButton(this.width / 2 - 79, this.height / 2 + 92, false, (p_214158_1_) -> { this.previousPage(); }, this.pageTurnSounds, TEXTURE));
		this.buttonExit = this.addButton(new TomeButton(this.width / 2 + 70, this.height / 2 - 105, 13, 0, CosmosColour.WHITE.dec(), TEXTURE, (p_214161_1_) -> { this.minecraft.setScreen((Screen) null); }));
		this.buttonHome = this.addButton(new TomeButton(this.width / 2 + 54, this.height / 2 - 105, 13, 1, CosmosColour.WHITE.dec(), TEXTURE, (p_214161_1_) -> { this.showPage(0); }));
		
		this.tabIntroduction = this.addButton(new TomeButton(this.width / 2 + 85, this.height / 2 - 106, CosmosColour.POCKET_PURPLE_LIGHT.dec(), TEXTURE, (p_214161_1_) -> { this.showPage(1); }));
		this.tabPockets = this.addButton(new TomeButton(this.width / 2 + 85, this.height / 2 - 80, CosmosColour.CYAN.dec(), TEXTURE, (p_214161_1_) -> { this.showPage(2); }));
		this.tabModules = this.addButton(new TomeButton(this.width / 2 + 85, this.height / 2 - 54, CosmosColour.BLUE.dec(), TEXTURE, (p_214161_1_) -> { this.showPage(6); }));
		this.tabItems = this.addButton(new TomeButton(this.width / 2 + 85, this.height / 2 - 28, CosmosColour.LIGHT_BLUE.dec(), TEXTURE, (p_214161_1_) -> { this.showPage(10); }));
		this.tabConfiguration = this.addButton(new TomeButton(this.width / 2 + 85, this.height / 2 - 2, CosmosColour.LIME.dec(), TEXTURE, (p_214161_1_) -> { this.showPage(13); }));
		this.tabArmourWeapons = this.addButton(new TomeButton(this.width / 2 + 85, this.height / 2 + 24, CosmosColour.ORANGE.dec(), TEXTURE, (p_214161_1_) -> { this.showPage(15); }));
		this.tabRecipes = this.addButton(new TomeButton(this.width / 2 + 85, this.height / 2 + 50, CosmosColour.PURPLE.dec(), TEXTURE, (p_214161_1_) -> { this.showPage(17); }));
		this.tabCredits = this.addButton(new TomeButton(this.width / 2 + 85, this.height / 2 + 76, CosmosColour.RED.dec(), TEXTURE, (p_214161_1_) -> { this.showPage(27); }));
		
		this.updateButtons();
	}

	private void updateButtons() {
		this.buttonNextPage.visible = this.currPage < this.pageCount - 1;
		this.buttonPreviousPage.visible = this.currPage > 0;
	}

	public boolean showPage(int pageNum) {
		int i = MathHelper.clamp(pageNum, 0, this.pageCount - 1);
		if (i != this.currPage) {
			this.currPage = i;
			this.updateButtons();
			return true;
		} else {
			return false;
		}
		
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
	
	private void drawCenteredString(MatrixStack matrixStack, int xOffset, int yOffset, int colour, String string, boolean shadow) {
		int x = (width - 202) / 2 + 3;
		int y = height / 2 - 80;

		if (shadow) {
			font.drawShadow(matrixStack, string, ((float)(x - font.width(string) / 2) + xOffset), y + yOffset, colour);
		} else {
			font.draw(matrixStack, string, ((float)(x - font.width(string) / 2) + xOffset), y + yOffset, colour);
		}
	}
	
	private void drawWrappedString(MatrixStack matrixStack, int xOffset, int yOffset, int length, int colour, String string) {
		int prevLines = 0;
		for (String str : string.split("<br>")) {
			int x = (width - 202) / 2 + 3;
			int y = height / 2 - 80;

			//if (shouldDrawRecipe()) {
				font.draw(matrixStack, str, ((float)(x - font.width(str) / 2) + xOffset), y + yOffset + (font.lineHeight * prevLines), colour);
			//}
			prevLines += (int) Math.ceil((float) (str.length() * 7) / (float) 204);
		}
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
	
	public void renderCraftingGrid(MatrixStack matrixStack, int[] screen_coords, int mouseX, int mouseY, int[] ref, int grid_ref) {
		int[] LX = new int[] { 31, 49, 67 }; //left to right [L]
		int[] RX = new int[] { 124, 142, 160 }; //left to right [R]
		int[] TY = new int[] { 36, 54, 72, 92 }; //top to bottom [T]
		int[] BY = new int[] { 126, 144, 162, 182 }; // top to bottom [B]
		
		final ItemStack[] items = new ItemStack[] {
				ItemStack.EMPTY,
				
				new ItemStack(Items.IRON_INGOT), new ItemStack(CoreModBusManager.DIMENSIONAL_SHARD), 
				new ItemStack(CoreModBusManager.DIMENSIONAL_INGOT), new ItemStack(Items.DIAMOND), new ItemStack(Items.NETHER_STAR), 
				new ItemStack(CoreModBusManager.NETHER_STAR_SHARD), new ItemStack(CoreModBusManager.DIMENSIONAL_WRENCH),
				new ItemStack(CoreModBusManager.MODULE_BASE), new ItemStack(Items.REDSTONE), new ItemStack(CoreModBusManager.DIMENSIONAL_SHIFTER),
				new ItemStack(Items.NETHERITE_INGOT), new ItemStack(CoreModBusManager.DIMENSIONAL_SHIFTER_ENHANCED),
				new ItemStack(Items.BLAZE_POWDER), new ItemStack(Items.PHANTOM_MEMBRANE), new ItemStack(Items.ELYTRA),
				new ItemStack(CoreModBusManager.ELYTRA_WING), new ItemStack(CoreModBusManager.MODULE_CONNECTOR),
				new ItemStack(CoreModBusManager.DIMENSIONAL_PEARL), new ItemStack(Items.BOW), new ItemStack(CoreModBusManager.DIMENSIONAL_THREAD),
				
				new ItemStack(Items.NETHERITE_PICKAXE), new ItemStack(Items.NETHERITE_SWORD),
				new ItemStack(Items.NETHERITE_AXE), new ItemStack(Items.NETHERITE_SHOVEL),
				new ItemStack(Items.NETHERITE_HOE), 
				
				new ItemStack(CoreModBusManager.DIMENSIONAL_PICKAXE), new ItemStack(CoreModBusManager.DIMENSIONAL_SWORD),
				new ItemStack(CoreModBusManager.DIMENSIONAL_AXE), new ItemStack(CoreModBusManager.DIMENSIONAL_SHOVEL),
				new ItemStack(CoreModBusManager.DIMENSIONAL_HOE),
				
				new ItemStack(Items.NETHERITE_HELMET), new ItemStack(Items.NETHERITE_CHESTPLATE),
				new ItemStack(Items.NETHERITE_LEGGINGS), new ItemStack(Items.NETHERITE_BOOTS),
				
				new ItemStack(CoreModBusManager.DIMENSIONAL_HELMET), new ItemStack(CoreModBusManager.DIMENSIONAL_CHESTPLATE),
				new ItemStack(CoreModBusManager.DIMENSIONAL_LEGGINGS), new ItemStack(CoreModBusManager.DIMENSIONAL_BOOTS),
				new ItemStack(CoreModBusManager.DIMENSIONAL_ELYTRAPLATE), new ItemStack(CoreModBusManager.DIMENSIONAL_ELYTRAPLATE_SCREEN),
				new ItemStack(CoreModBusManager.DIMENSIONAL_ELYTRAPLATE_SHIFT),
				
				new ItemStack(Blocks.IRON_BLOCK), new ItemStack(CoreModBusManager.BLOCK_DIMENSIONAL_METAL),
				new ItemStack(CoreModBusManager.BLOCK_DIMENSIONAL_CORE), new ItemStack(CoreModBusManager.BLOCK_DIMENSIONAL),
				new ItemStack(CoreModBusManager.BLOCK_POCKET), new ItemStack(Blocks.DIAMOND_BLOCK), new ItemStack(Blocks.NETHERITE_BLOCK),
				new ItemStack(Blocks.CHEST), new ItemStack(CoreModBusManager.DIMENSIONAL_DUST), new ItemStack(CoreModBusManager.DIMENSIONAL_BOW),
				new ItemStack(Items.GLOWSTONE_DUST), new ItemStack(Items.STRING), new ItemStack(Items.ENDER_PEARL), new ItemStack(Items.EMERALD),
				new ItemStack(Blocks.REDSTONE_BLOCK), new ItemStack(CoreModBusManager.MODULE_CHARGER), new ItemStack(Items.BOOK), 
				new ItemStack(CoreModBusManager.DIMENSIONAL_TOME), new ItemStack(CoreModBusManager.MODULE_CRAFTER), new ItemStack(Blocks.CRAFTING_TABLE),
				new ItemStack(Items.TRIDENT), new ItemStack(CoreModBusManager.DIMENSIONAL_TRIDENT), new ItemStack(CoreModBusManager.MODULE_ENERGY_DISPLAY)
		};
		
		if (grid_ref == 0) {
			//Top Left
			
			if (ref[0] != -1) { this.renderItemStack(matrixStack, items[ref[0]], screen_coords, LX[0], TY[0], mouseX, mouseY, true); }// 0
			if (ref[1] != -1) { this.renderItemStack(matrixStack, items[ref[1]], screen_coords, LX[1], TY[0], mouseX, mouseY, true); }// 1
			if (ref[2] != -1) { this.renderItemStack(matrixStack, items[ref[2]], screen_coords, LX[2], TY[0], mouseX, mouseY, true); }// 2
			if (ref[3] != -1) { this.renderItemStack(matrixStack, items[ref[3]], screen_coords, LX[0], TY[1], mouseX, mouseY, true); }// 3
			if (ref[4] != -1) { this.renderItemStack(matrixStack, items[ref[4]], screen_coords, LX[1], TY[1], mouseX, mouseY, true); }// 4
			if (ref[5] != -1) { this.renderItemStack(matrixStack, items[ref[5]], screen_coords, LX[2], TY[1], mouseX, mouseY, true); }// 5
			if (ref[6] != -1) { this.renderItemStack(matrixStack, items[ref[6]], screen_coords, LX[0], TY[2], mouseX, mouseY, true); }// 6
			if (ref[7] != -1) { this.renderItemStack(matrixStack, items[ref[7]], screen_coords, LX[1], TY[2], mouseX, mouseY, true); }// 7
			if (ref[8] != -1) { this.renderItemStack(matrixStack, items[ref[8]], screen_coords, LX[2], TY[2], mouseX, mouseY, true); }// 8
			if (ref[9] != -1) { this.renderItemStack(matrixStack, items[ref[9]], screen_coords, LX[1], TY[3], mouseX, mouseY, true); }// Out
		} else if (grid_ref == 1) {
			
			//Bottom Left
			if (ref[0] != -1) { this.renderItemStack(matrixStack, items[ref[0]], screen_coords, LX[0], BY[0], mouseX, mouseY, true); }// 0
			if (ref[1] != -1) { this.renderItemStack(matrixStack, items[ref[1]], screen_coords, LX[1], BY[0], mouseX, mouseY, true); }// 1
			if (ref[2] != -1) { this.renderItemStack(matrixStack, items[ref[2]], screen_coords, LX[2], BY[0], mouseX, mouseY, true); }// 2
			if (ref[3] != -1) { this.renderItemStack(matrixStack, items[ref[3]], screen_coords, LX[0], BY[1], mouseX, mouseY, true); }// 3
			if (ref[4] != -1) { this.renderItemStack(matrixStack, items[ref[4]], screen_coords, LX[1], BY[1], mouseX, mouseY, true); }// 4
			if (ref[5] != -1) { this.renderItemStack(matrixStack, items[ref[5]], screen_coords, LX[2], BY[1], mouseX, mouseY, true); }// 5
			if (ref[6] != -1) { this.renderItemStack(matrixStack, items[ref[6]], screen_coords, LX[0], BY[2], mouseX, mouseY, true); }// 6
			if (ref[7] != -1) { this.renderItemStack(matrixStack, items[ref[7]], screen_coords, LX[1], BY[2], mouseX, mouseY, true); }// 7
			if (ref[8] != -1) { this.renderItemStack(matrixStack, items[ref[8]], screen_coords, LX[2], BY[2], mouseX, mouseY, true); }// 8
			if (ref[9] != -1) { this.renderItemStack(matrixStack, items[ref[9]], screen_coords, LX[1], BY[3], mouseX, mouseY, true); }// Out
		} else if (grid_ref == 2) {
			
			//Top Right
			if (ref[0] != -1) { this.renderItemStack(matrixStack, items[ref[0]], screen_coords, RX[0], TY[0], mouseX, mouseY, true); }// 0
			if (ref[1] != -1) { this.renderItemStack(matrixStack, items[ref[1]], screen_coords, RX[1], TY[0], mouseX, mouseY, true); }// 1
			if (ref[2] != -1) { this.renderItemStack(matrixStack, items[ref[2]], screen_coords, RX[2], TY[0], mouseX, mouseY, true); }// 2
			if (ref[3] != -1) { this.renderItemStack(matrixStack, items[ref[3]], screen_coords, RX[0], TY[1], mouseX, mouseY, true); }// 3
			if (ref[4] != -1) { this.renderItemStack(matrixStack, items[ref[4]], screen_coords, RX[1], TY[1], mouseX, mouseY, true); }// 4
			if (ref[5] != -1) { this.renderItemStack(matrixStack, items[ref[5]], screen_coords, RX[2], TY[1], mouseX, mouseY, true); }// 5
			if (ref[6] != -1) { this.renderItemStack(matrixStack, items[ref[6]], screen_coords, RX[0], TY[2], mouseX, mouseY, true); }// 6
			if (ref[7] != -1) { this.renderItemStack(matrixStack, items[ref[7]], screen_coords, RX[1], TY[2], mouseX, mouseY, true); }// 7
			if (ref[8] != -1) { this.renderItemStack(matrixStack, items[ref[8]], screen_coords, RX[2], TY[2], mouseX, mouseY, true); }// 8
			if (ref[9] != -1) { this.renderItemStack(matrixStack, items[ref[9]], screen_coords, RX[1], TY[3], mouseX, mouseY, true); }// Out
		} else if (grid_ref == 3) {
			
			//Bottom Right
			if (ref[0] != -1) { this.renderItemStack(matrixStack, items[ref[0]], screen_coords, RX[0], BY[0], mouseX, mouseY, true); }// 0
			if (ref[1] != -1) { this.renderItemStack(matrixStack, items[ref[1]], screen_coords, RX[1], BY[0], mouseX, mouseY, true); }// 1
			if (ref[2] != -1) { this.renderItemStack(matrixStack, items[ref[2]], screen_coords, RX[2], BY[0], mouseX, mouseY, true); }// 2
			if (ref[3] != -1) { this.renderItemStack(matrixStack, items[ref[3]], screen_coords, RX[0], BY[1], mouseX, mouseY, true); }// 3
			if (ref[4] != -1) { this.renderItemStack(matrixStack, items[ref[4]], screen_coords, RX[1], BY[1], mouseX, mouseY, true); }// 4
			if (ref[5] != -1) { this.renderItemStack(matrixStack, items[ref[5]], screen_coords, RX[2], BY[1], mouseX, mouseY, true); }// 5
			if (ref[6] != -1) { this.renderItemStack(matrixStack, items[ref[6]], screen_coords, RX[0], BY[2], mouseX, mouseY, true); }// 6
			if (ref[7] != -1) { this.renderItemStack(matrixStack, items[ref[7]], screen_coords, RX[1], BY[2], mouseX, mouseY, true); }// 7
			if (ref[8] != -1) { this.renderItemStack(matrixStack, items[ref[8]], screen_coords, RX[2], BY[2], mouseX, mouseY, true); }// 8
			if (ref[9] != -1) { this.renderItemStack(matrixStack, items[ref[9]], screen_coords, RX[1], BY[3], mouseX, mouseY, true); }// Out
		}
	}
	
	public void renderItemStack(MatrixStack matrixStack, ItemStack stack, int[] screen_coords, int x, int y, int mouseX, int mouseY, boolean withTooltip) {
		int renderX = screen_coords[0] + x;
		int renderY = screen_coords[1] + y;
		
		Minecraft.getInstance().getItemRenderer().renderGuiItem(stack, screen_coords[0] + x, screen_coords[1] + y);
		
		if (withTooltip) {
			if (mouseX > renderX && mouseX < renderX + 16) {
				if (mouseY > renderY && mouseY < renderY + 16) {
					renderTooltip(matrixStack, stack, mouseX, mouseY);
				}
			}
		}
	}
	
	public void renderItemStack(MatrixStack matrixStack, ItemStack stack, int count, int[] screen_coords, int x, int y, int mouseX, int mouseY, boolean withTooltip) {
		int renderX = screen_coords[0] + x;
		int renderY = screen_coords[1] + y;
		
		Minecraft.getInstance().getItemRenderer().renderGuiItem(stack, screen_coords[0] + x, screen_coords[1] + y);
		
		if (withTooltip) {
			if (mouseX > renderX && mouseX < renderX + 16) {
				if (mouseY > renderY && mouseY < renderY + 16) {
					renderTooltip(matrixStack, stack, mouseX, mouseY);
				}
			}
		}
	}
}