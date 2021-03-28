package com.tcn.dimensionalpocketsii.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tcn.dimensionalpocketsii.core.management.CoreConfigurationManager;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.OptionsRowList;
import net.minecraft.client.settings.BooleanOption;
import net.minecraft.client.settings.SliderPercentageOption;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class ScreenConfiguration extends Screen {
	
	private final Screen parent;
	
	private final int TITLE_HEIGHT = 8;
	
    private final int OPTIONS_LIST_TOP_HEIGHT = 24;
    private final int OPTIONS_LIST_BOTTOM_OFFSET = 32;
    private final int OPTIONS_LIST_ITEM_HEIGHT = 25;

    private final int BUTTON_WIDTH = 200;
    private final int BUTTON_HEIGHT = 20;
    private final int DONE_BUTTON_TOP_OFFSET = 26;
    
    private OptionsRowList optionsRowList;

	public ScreenConfiguration(Screen parent) {
		super(new TranslationTextComponent("dimensionalpocketsii.gui.config.name"));
		
		this.parent = parent;
	}

	@Override
	protected void init() {
		this.optionsRowList = new OptionsRowList(
				this.minecraft, this.width, this.height,
				OPTIONS_LIST_TOP_HEIGHT,
				this.height - OPTIONS_LIST_BOTTOM_OFFSET,
				OPTIONS_LIST_ITEM_HEIGHT);
		
		this.optionsRowList.addBig(new BooleanOption(
				"dimensionalpocketsii.gui.config.chunks",
				unused -> CoreConfigurationManager.getInstance().getKeepChunksLoaded(),
				(unused, newValue) -> CoreConfigurationManager.getInstance().setKeepChunksLoaded(newValue)
				));
		this.optionsRowList.addBig(new BooleanOption(
				"dimensionalpocketsii.gui.config.walls",
				unused -> CoreConfigurationManager.getInstance().getCanDestroyWalls(),
				(unused, newValue) -> CoreConfigurationManager.getInstance().setCanDestroyWalls(newValue)
				));
		this.optionsRowList.addBig(new BooleanOption(
				"dimensionalpocketsii.gui.config.commands",
				unused -> CoreConfigurationManager.getInstance().getCancelCommands(),
				(unused, newValue) -> CoreConfigurationManager.getInstance().setCancelCommands(newValue)
				));
		this.optionsRowList.addBig(new SliderPercentageOption(
				"dimensionalpocketsii.gui.config.height",
				15, 255, 1.0F,
				unused -> (double) CoreConfigurationManager.getInstance().getInternalHeight(),
				(unused, newValue) -> CoreConfigurationManager.getInstance().setInternalHeight(newValue.intValue()),
				(gs, option) -> new StringTextComponent("Internal Pocket Height: [" + option.get(gs) + "]")
				));
		this.optionsRowList.addBig(new BooleanOption(
				"dimensionalpocketsii.gui.config.replace",
				unused -> CoreConfigurationManager.getInstance().getInternalReplace(),
				(unused, newValue) -> CoreConfigurationManager.getInstance().setInternalReplace(newValue)
				));
		this.optionsRowList.addBig(new BooleanOption(
				"dimensionalpocketsii.gui.config.book",
				unused -> CoreConfigurationManager.getInstance().getShouldSpawnWithBook(),
				(unused, newValue) -> CoreConfigurationManager.getInstance().setShouldSpawnWithBook(newValue)
				));
		this.optionsRowList.addBig(new BooleanOption(
				"dimensionalpocketsii.gui.config.structures",
				unused -> CoreConfigurationManager.getInstance().getCanPlaceStructures(),
				(unused, newValue) -> CoreConfigurationManager.getInstance().setCanPlaceStructures(newValue)
				));
		this.optionsRowList.addBig(new BooleanOption(
				"dimensionalpocketsii.gui.config.use_items",
				unused -> CoreConfigurationManager.getInstance().getCanUseItems(),
				(unused, newValue) -> CoreConfigurationManager.getInstance().setCanUseItems(newValue)
				));
		
		this.optionsRowList.addBig(new BooleanOption(
				"dimensionalpocketsii.gui.config.messages",
				unused -> CoreConfigurationManager.getInstance().getDebugMessage(),
				(unused, newValue) -> CoreConfigurationManager.getInstance().setDebugMessage(newValue)
				));
		
		this.optionsRowList.addBig(new BooleanOption(
				"dimensionalpocketsii.gui.config.textures",
				gameSettings -> CoreConfigurationManager.getInstance().getConnectedTexturesInsidePocket(),
				(gameSettings, newValue) -> CoreConfigurationManager.getInstance().setConnectedTexturesInsidePocket(newValue)
				));
		this.children.add(this.optionsRowList);
		
		this.addButton(new Button(
				(this.width - BUTTON_WIDTH) /2,
				this.height - DONE_BUTTON_TOP_OFFSET,
				BUTTON_WIDTH, BUTTON_HEIGHT,
				new TranslationTextComponent("dimensionalpocketsii.gui.done"),
				button -> this.onClose()
			));
	}
	
	@Override
	public void render(MatrixStack stack, int mouse_x, int mouse_y, float ticks) {
		this.renderBackground(stack);
		
		this.optionsRowList.render(stack, mouse_x, mouse_y, ticks);
		drawCenteredString(stack, this.font, this.title, width / 2, TITLE_HEIGHT, 0xFFFFFF);
		
		super.render(stack, mouse_x, mouse_y, ticks);
	}
	
    @Override
    public void onClose() {
    	this.minecraft.setScreen(parent);
        CoreConfigurationManager.save();
    }

    //@Override
    public void closeScreen() {
        
    }
}
