package com.zhr.dimensionalpocketsii.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.zhr.dimensionalpocketsii.core.management.ModConfigurationManager;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.OptionsRowList;
import net.minecraft.client.settings.BooleanOption;
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
				unused -> ModConfigurationManager.getInstance().getKeepChunksLoaded(),
				(unused, newValue) -> ModConfigurationManager.getInstance().setKeepChunksLoaded(newValue)
				));
		this.optionsRowList.addOption(new BooleanOption(
				"Can Destroy Walls In Creative",
				unused -> ModConfigurationManager.getInstance().getCanDestroyWalls(),
				(unused, newValue) -> ModConfigurationManager.getInstance().setCanDestroyWalls(newValue)
				));
		this.optionsRowList.addOption(new BooleanOption(
				"Can Teleport into Pocket Dimension",
				unused -> ModConfigurationManager.getInstance().getCanTeleport(),
				(unused, newValue) -> ModConfigurationManager.getInstance().setCanTeleport(newValue)
				));
		this.optionsRowList.addOption(new BooleanOption(
				"System Messages",
				unused -> ModConfigurationManager.getInstance().getSystemMessage(),
				(unused, newValue) -> ModConfigurationManager.getInstance().setSystemMessage(newValue)
				));
		this.optionsRowList.addOption(new BooleanOption(
				"Connected Textures",
				unused -> ModConfigurationManager.getInstance().getConnectedTexturesInsidePocket(),
				(unused, newValue) -> ModConfigurationManager.getInstance().setConnectedTexturesInsidePocket(newValue)
				));
		this.optionsRowList.addOption(new BooleanOption(
				"Cancel Commands",
				unused -> ModConfigurationManager.getInstance().getCancelCommands(),
				(unused, newValue) -> ModConfigurationManager.getInstance().setCancelCommands(newValue)
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
        ModConfigurationManager.save();
    }

    @Override
    public void closeScreen() {
        this.minecraft.displayGuiScreen(parent);
    }
}
