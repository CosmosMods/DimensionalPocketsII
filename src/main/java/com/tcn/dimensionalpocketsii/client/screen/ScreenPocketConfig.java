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

public final class ScreenPocketConfig extends Screen {
	
	private final Screen parent;
	
	private final int TITLE_HEIGHT = 8;
	
    private final int OPTIONS_LIST_TOP_HEIGHT = 24;
    private final int OPTIONS_LIST_BOTTOM_OFFSET = 32;
    private final int OPTIONS_LIST_ITEM_HEIGHT = 25;

    private final int BUTTON_WIDTH = 200;
    private final int BUTTON_HEIGHT = 20;
    private final int DONE_BUTTON_TOP_OFFSET = 26;
    
    private OptionsRowList optionsRowList;

	public ScreenPocketConfig(Screen parent) {
		super(new TranslationTextComponent("gui.config.name"));
		
		this.parent = parent;
	}

	@Override
	protected void init() {
		this.optionsRowList = new OptionsRowList(
				this.minecraft, this.width, this.height,
				OPTIONS_LIST_TOP_HEIGHT,
				this.height - OPTIONS_LIST_BOTTOM_OFFSET,
				OPTIONS_LIST_ITEM_HEIGHT);
		
		this.optionsRowList.addOption(new BooleanOption(
				"Keep Chunks Loaded",
				unused -> CoreConfigurationManager.getInstance().getKeepChunksLoaded(),
				(unused, newValue) -> CoreConfigurationManager.getInstance().setKeepChunksLoaded(newValue)
				));
		this.optionsRowList.addOption(new BooleanOption(
				"Can Destroy Walls In Creative",
				unused -> CoreConfigurationManager.getInstance().getCanDestroyWalls(),
				(unused, newValue) -> CoreConfigurationManager.getInstance().setCanDestroyWalls(newValue)
				));
		this.optionsRowList.addOption(new BooleanOption(
				"Cancel Commands",
				unused -> CoreConfigurationManager.getInstance().getCancelCommands(),
				(unused, newValue) -> CoreConfigurationManager.getInstance().setCancelCommands(newValue)
				));
		this.optionsRowList.addOption(new SliderPercentageOption(
				"Internal Pocket Height",
				15, 253, 1.0F,
				unused -> (double) CoreConfigurationManager.getInstance().getInternalHeight(),
				(unused, newValue) -> CoreConfigurationManager.getInstance().setInternalHeight(newValue.intValue()),
				(gs, option) -> new StringTextComponent("Internal Pocket Height: [" + option.get(gs) + "]")
				));
		this.optionsRowList.addOption(new BooleanOption(
				"Replace Internal",
				unused -> CoreConfigurationManager.getInstance().getInternalReplace(),
				(unused, newValue) -> CoreConfigurationManager.getInstance().setInternalReplace(newValue)
				));
		
		//this.optionsRowList.addOption(null);
		
		this.optionsRowList.addOption(new BooleanOption(
				"Can Teleport into Pocket Dimension",
				unused -> CoreConfigurationManager.getInstance().getCanTeleport(),
				(unused, newValue) -> CoreConfigurationManager.getInstance().setCanTeleport(newValue)
				));
		this.optionsRowList.addOption(new BooleanOption(
				"Debug Messages",
				unused -> CoreConfigurationManager.getInstance().getDebugMessage(),
				(unused, newValue) -> CoreConfigurationManager.getInstance().setDebugMessage(newValue)
				));
		
		
		this.optionsRowList.addOption(new BooleanOption(
				"Connected Textures",
				unused -> CoreConfigurationManager.getInstance().getConnectedTexturesInsidePocket(),
				(unused, newValue) -> CoreConfigurationManager.getInstance().setConnectedTexturesInsidePocket(newValue)
				));
		
		
				
				
		this.children.add(this.optionsRowList);
		
		//Button button = new Button();
		
		this.addButton(new Button(
				(this.width - BUTTON_WIDTH) /2,
				this.height - DONE_BUTTON_TOP_OFFSET,
				BUTTON_WIDTH, BUTTON_HEIGHT,
				new TranslationTextComponent("gui.done"),
				button -> this.closeScreen()
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
        CoreConfigurationManager.save();
    }

    @Override
    public void closeScreen() {
        this.minecraft.displayGuiScreen(parent);
    }
}
