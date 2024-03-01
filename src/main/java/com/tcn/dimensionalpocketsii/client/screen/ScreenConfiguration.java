package com.tcn.dimensionalpocketsii.client.screen;

import java.io.File;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.tcn.cosmoslibrary.client.ui.screen.option.CosmosOptionBoolean;
import com.tcn.cosmoslibrary.client.ui.screen.option.CosmosOptionBoolean.TYPE;
import com.tcn.cosmoslibrary.client.ui.screen.option.CosmosOptionInstance;
import com.tcn.cosmoslibrary.client.ui.screen.option.CosmosOptionListElement;
import com.tcn.cosmoslibrary.client.ui.screen.option.CosmosOptionListTextEntry;
import com.tcn.cosmoslibrary.client.ui.screen.option.CosmosOptionTitle;
import com.tcn.cosmoslibrary.client.ui.screen.option.CosmosOptions;
import com.tcn.cosmoslibrary.client.ui.screen.option.CosmosOptionsList;
import com.tcn.cosmoslibrary.common.lib.ComponentColour;
import com.tcn.cosmoslibrary.common.lib.ComponentHelper;
import com.tcn.dimensionalpocketsii.core.management.ConfigurationManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ConfigScreenHandler.ConfigScreenFactory;

@SuppressWarnings("unused")
@OnlyIn(Dist.CLIENT)
public final class ScreenConfiguration extends Screen {

	private final Screen PARENT_SCREEN;

	private final int TITLE_HEIGHT = 8;

	private final int OPTIONS_LIST_TOP_HEIGHT = 24;
	private final int OPTIONS_LIST_BOTTOM_OFFSET = 32;
	private final int OPTIONS_LIST_ITEM_HEIGHT = 25;
	private final int OPTIONS_LIST_BUTTON_HEIGHT = 20;
	private final int OPTIONS_LIST_WIDTH = 335;

	private final int BIG_WIDTH = 310;
	private final int SMALL_WIDTH = 150;
	
	private final int DONE_BUTTON_TOP_OFFSET = 26;
	
	private CosmosOptionsList OPTIONS_ROW_LIST;
	private String CURRENT_SCREEN = "home";

	private final ComponentColour DESC_COLOUR = ComponentColour.LIGHT_GRAY;
	
	private CosmosOptionListTextEntry EDIT_BOX_BLOCKS;
	private CosmosOptionListTextEntry EDIT_BOX_ITEMS;
	private CosmosOptionListTextEntry EDIT_BOX_COMMANDS;
	
	private Button closeButton;
	
	public ScreenConfiguration(Screen parentScreenIn) {
		super(ComponentHelper.style(ComponentColour.POCKET_PURPLE_GUI, "boldunderline", "dimensionalpocketsii.gui.config.name"));

		this.PARENT_SCREEN = parentScreenIn;
		
		this.EDIT_BOX_BLOCKS = new CosmosOptionListTextEntry(ComponentHelper.style(ComponentColour.LIGHT_GRAY, "", ""), true, ComponentHelper.style(ComponentColour.GREEN, "bold", "+"), ComponentHelper.style(ComponentColour.GREEN, "", "dimensionalpocketsii.gui.config.add"), (button) -> {  }, (button) -> { return button.get(); });
		this.EDIT_BOX_ITEMS = new CosmosOptionListTextEntry(ComponentHelper.style(ComponentColour.LIGHT_GRAY, "", ""), true, ComponentHelper.style(ComponentColour.GREEN, "bold", "+"), ComponentHelper.style(ComponentColour.GREEN, "", "dimensionalpocketsii.gui.config.add"), (button) -> {  }, (button) -> { return button.get(); });
		this.EDIT_BOX_COMMANDS = new CosmosOptionListTextEntry(ComponentHelper.style(ComponentColour.LIGHT_GRAY, "", ""), true, ComponentHelper.style(ComponentColour.GREEN, "bold", "+"), ComponentHelper.style(ComponentColour.GREEN, "", "dimensionalpocketsii.gui.config.add"), (button) -> {  }, (button) -> { return button.get(); });
	}
	
	@Override
	protected void init() {
		//this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
		
		if (this.CURRENT_SCREEN == "home") {
			this.initOptions();
	
			this.OPTIONS_ROW_LIST.addBig(
				new CosmosOptionTitle(ComponentHelper.style(ComponentColour.LIGHT_GRAY, "boldunderline", "dimensionalpocketsii.gui.config.general_title"))
			);
			
			this.OPTIONS_ROW_LIST.addBig(
				CosmosOptionInstance.createIntSlider(ComponentHelper.style(ComponentColour.ORANGE, "dimensionalpocketsii.gui.config.height"),
					CosmosOptionInstance.getTooltipSplitComponent(ComponentHelper.style(DESC_COLOUR, "dimensionalpocketsii.gui.config.height_info"), 
							ComponentHelper.style(ComponentColour.RED, "dimensionalpocketsii.gui.config.height_info_two")
					), 
					ConfigurationManager.getInstance().getInternalHeight(), 15, 255, 15,
					ComponentColour.WHITE, ComponentHelper.style(ComponentColour.GREEN, "Min"), ComponentHelper.style(ComponentColour.DARK_YELLOW, "Blocks"), ComponentHelper.style(ComponentColour.RED, "Max"), (intValue) -> {
					ConfigurationManager.getInstance().setInternalHeight(intValue);
				})
			);

			this.OPTIONS_ROW_LIST.addBig(
				CosmosOptionInstance.createIntSlider(ComponentHelper.style(ComponentColour.ORANGE, "dimensionalpocketsii.gui.config.height_enhanced"),
					CosmosOptionInstance.getTooltipSplitComponent(ComponentHelper.style(DESC_COLOUR, "dimensionalpocketsii.gui.config.height_enhanced_info"), 
							ComponentHelper.style(ComponentColour.RED, "dimensionalpocketsii.gui.config.height_enhanced_info_two")
					), 
					ConfigurationManager.getInstance().getInternalHeightEnhanced(), 31, 255, 31,
					ComponentColour.WHITE, ComponentHelper.style(ComponentColour.GREEN, "Min"), ComponentHelper.style(ComponentColour.DARK_YELLOW, "Blocks"), ComponentHelper.style(ComponentColour.RED, "Max"), (intValue) -> {
					ConfigurationManager.getInstance().setInternalHeightEnhanced(intValue);
				})
			);
			
			this.OPTIONS_ROW_LIST.addBig(
				CosmosOptionInstance.createIntSlider(ComponentHelper.style(ComponentColour.ORANGE, "dimensionalpocketsii.gui.config.jump_range"),
					CosmosOptionInstance.getTooltipSplitComponent(ComponentHelper.style(DESC_COLOUR, "dimensionalpocketsii.gui.config.jump_range_info"), 
							ComponentHelper.style(ComponentColour.RED, "dimensionalpocketsii.gui.config.jump_range_info_two")
					), 
					ConfigurationManager.getInstance().getFocusJumpRange(), 4, 32, 12,
					ComponentColour.WHITE, ComponentHelper.style(ComponentColour.GREEN, "Min"), ComponentHelper.style(ComponentColour.DARK_YELLOW, "Blocks"), ComponentHelper.style(ComponentColour.RED, "Max"), (intValue) -> {
					ConfigurationManager.getInstance().setFocusJumpRange(intValue);
				})
			);
			
			this.OPTIONS_ROW_LIST.addSmall(
				new CosmosOptionBoolean(
					ComponentColour.ORANGE, "", "dimensionalpocketsii.gui.config.use_structures", TYPE.YES_NO,
					CosmosOptionInstance.getTooltipSplitComponent(ComponentHelper.style(DESC_COLOUR, "dimensionalpocketsii.gui.config.use_structures_info"), ComponentHelper.style(ComponentColour.LIME, "dimensionalpocketsii.gui.config.use_structures_info_two")),
					ConfigurationManager.getInstance().getCanPlaceStructures(),
					(newValue) -> ConfigurationManager.getInstance().setCanPlaceStructures(newValue), ":"
				),
				new CosmosOptionBoolean(
					ComponentColour.ORANGE, "", "dimensionalpocketsii.gui.config.use_items", TYPE.YES_NO,
					CosmosOptionInstance.getTooltipSplitComponent(ComponentHelper.style(DESC_COLOUR, "dimensionalpocketsii.gui.config.use_items_info"), ComponentHelper.style(ComponentColour.LIME, "dimensionalpocketsii.gui.config.use_items_info_two")),
					ConfigurationManager.getInstance().getCanUseItems(),
					(newValue) -> ConfigurationManager.getInstance().setCanUseItems(newValue), ":"
				)
			);
			
			this.OPTIONS_ROW_LIST.addSmall(
				new CosmosOptionBoolean(
					ComponentColour.ORANGE, "", "dimensionalpocketsii.gui.config.use_commands", TYPE.YES_NO,
					CosmosOptionInstance.getTooltipSplitComponent(ComponentHelper.style(DESC_COLOUR, "dimensionalpocketsii.gui.config.use_commands_info"), ComponentHelper.style(ComponentColour.LIME, "dimensionalpocketsii.gui.config.use_commands_info_two")),
					ConfigurationManager.getInstance().getCanUseCommands(),
					(newValue) -> ConfigurationManager.getInstance().setCanUseCommands(newValue), ":"
				),
				new CosmosOptionBoolean(
					ComponentColour.ORANGE, "", "dimensionalpocketsii.gui.config.chunks", TYPE.ON_OFF,
					CosmosOptionInstance.getTooltipSplitComponent( ComponentHelper.style(DESC_COLOUR, "dimensionalpocketsii.gui.config.chunks_info")),
					ConfigurationManager.getInstance().getKeepChunksLoaded(), 
					(newValue) -> ConfigurationManager.getInstance().setKeepChunksLoaded(newValue), ":"
				)
			);
	
			this.OPTIONS_ROW_LIST.addSmall(
				new CosmosOptionBoolean(
					ComponentColour.ORANGE, "", "dimensionalpocketsii.gui.config.replace", TYPE.YES_NO, 
					CosmosOptionInstance.getTooltipSplitComponent(ComponentHelper.style(DESC_COLOUR, "dimensionalpocketsii.gui.config.replace_info")),
					ConfigurationManager.getInstance().getInternalReplace(),
					(newValue) -> ConfigurationManager.getInstance().setInternalReplace(newValue), ":"
				),
				new CosmosOptionBoolean(
					ComponentColour.ORANGE, "", "dimensionalpocketsii.gui.config.hostile", TYPE.YES_NO, 
					CosmosOptionInstance.getTooltipSplitComponent(ComponentHelper.style(DESC_COLOUR, "dimensionalpocketsii.gui.config.hostile_info")),
					ConfigurationManager.getInstance().getStopHostileSpawns(),
					(newValue) -> ConfigurationManager.getInstance().setStopHostileSpawns(newValue), ":"
				)
			);
			
			this.OPTIONS_ROW_LIST.addSmall(
				new CosmosOptionBoolean(
					ComponentColour.ORANGE, "", "dimensionalpocketsii.gui.config.walls", TYPE.YES_NO,
					CosmosOptionInstance.getTooltipSplitComponent(ComponentHelper.style(DESC_COLOUR, "dimensionalpocketsii.gui.config.walls_info")),
					ConfigurationManager.getInstance().getCanDestroyWalls(),
					(newValue) -> ConfigurationManager.getInstance().setCanDestroyWalls(newValue), ":"
				),
				new CosmosOptionBoolean(
					ComponentColour.ORANGE, "", "dimensionalpocketsii.gui.config.backups", TYPE.YES_NO,
					CosmosOptionInstance.getTooltipSplitComponent(ComponentHelper.style(DESC_COLOUR, "dimensionalpocketsii.gui.config.backups_info")),
					ConfigurationManager.getInstance().getCreateBackups(),
					(newValue) -> ConfigurationManager.getInstance().setCreateBackups(newValue), ":"
				)
			);
	
			this.OPTIONS_ROW_LIST.addBig(
				new CosmosOptionTitle(ComponentHelper.style(ComponentColour.LIGHT_GRAY, "boldunderline", "dimensionalpocketsii.gui.config.messages_title"))
			);
			
			this.OPTIONS_ROW_LIST.addSmall(
				new CosmosOptionBoolean(
					ComponentColour.CYAN, "", "dimensionalpocketsii.gui.config.message.info", TYPE.ON_OFF, 
					CosmosOptionInstance.getTooltipSplitComponent(ComponentHelper.style(DESC_COLOUR, "dimensionalpocketsii.gui.config.message.info_DESC_COLOUR"), ComponentHelper.style(ComponentColour.RED, "bold", "dimensionalpocketsii.gui.config.message.restart")),
					ConfigurationManager.getInstance().getInfoMessage(),
					(newValue) -> ConfigurationManager.getInstance().setInfoMessage(newValue), ":"
				),
				new CosmosOptionBoolean(
					ComponentColour.CYAN, "", "dimensionalpocketsii.gui.config.message.debug", TYPE.ON_OFF, 
					CosmosOptionInstance.getTooltipSplitComponent(ComponentHelper.style(DESC_COLOUR, "dimensionalpocketsii.gui.config.message.debug_DESC_COLOUR"), ComponentHelper.style(ComponentColour.RED, "bold", "dimensionalpocketsii.gui.config.message.restart")),
					ConfigurationManager.getInstance().getDebugMessage(),
					(newValue) -> ConfigurationManager.getInstance().setDebugMessage(newValue), ":"
				)
			);
	
			this.OPTIONS_ROW_LIST.addBig(
				new CosmosOptionTitle(ComponentHelper.style(ComponentColour.LIGHT_GRAY, "boldunderline", "dimensionalpocketsii.gui.config.visual_title"))
			);
			
			this.OPTIONS_ROW_LIST.addBig(
				new CosmosOptionBoolean(
					ComponentColour.MAGENTA, "", "dimensionalpocketsii.gui.config.textures", TYPE.ON_OFF, 
					CosmosOptionInstance.getTooltipSplitComponent(ComponentHelper.style(DESC_COLOUR, "dimensionalpocketsii.gui.config.textures_info")),
					ConfigurationManager.getInstance().getConnectedTexturesInsidePocket(),
					(newValue) -> ConfigurationManager.getInstance().setConnectedTexturesInsidePocket(newValue), ":"
				) 
			);
			
			this.OPTIONS_ROW_LIST.addBig(
				new CosmosOptionTitle(ComponentHelper.style(ComponentColour.LIGHT_GRAY, "boldunderline", "dimensionalpocketsii.gui.config.blocked_title"))
			);

			this.OPTIONS_ROW_LIST.addSmall(
				CosmosOptionInstance.createScreenSwitchOption(ComponentHelper.style(ComponentColour.LIGHT_RED, "", "dimensionalpocketsii.gui.config.blocked_structures"), (button) -> { 
					this.switchScreen("blocks");
					this.updateWidgets();
				}, ""),
				CosmosOptionInstance.createScreenSwitchOption(ComponentHelper.style(ComponentColour.LIGHT_RED, "", "dimensionalpocketsii.gui.config.blocked_items"), (button) -> { 
					this.switchScreen("items");
					this.updateWidgets();
				}, "")
			);

			this.OPTIONS_ROW_LIST.addSmall(
				CosmosOptionInstance.createScreenSwitchOption(ComponentHelper.style(ComponentColour.LIGHT_RED, "", "dimensionalpocketsii.gui.config.blocked_commands"), (button) -> { 
					this.switchScreen("commands");
					this.updateWidgets();
				}, ""),
				null
			); 
			
			this.closeButton = (Button.builder(
				ComponentHelper.style(ComponentColour.RED, "bold", "dimensionalpocketsii.gui.done"), 
				(button) -> { 
					this.onClose();
				}).pos((this.width - this.BIG_WIDTH) / 2, this.height - this.DONE_BUTTON_TOP_OFFSET).size(this.SMALL_WIDTH, this.OPTIONS_LIST_BUTTON_HEIGHT).build()
			);
			this.addRenderableWidget(this.closeButton);
		} 
		
		else if (this.CURRENT_SCREEN == "blocks") {
			this.initOptions();
			
			this.OPTIONS_ROW_LIST.addBig(
				new CosmosOptionTitle(ComponentHelper.style(ComponentColour.LIGHT_GRAY, "boldunderline", "dimensionalpocketsii.gui.config.blocked_structures"))
			);
			
			this.EDIT_BOX_BLOCKS.setOnPressFunction((button) -> { 
				ConfigurationManager.getInstance().addBlockedStructure(this.EDIT_BOX_BLOCKS.getEditBox().getValue());
				this.EDIT_BOX_BLOCKS.getEditBox().setValue("");
				this.updateWidgets();
			});
			
			this.OPTIONS_ROW_LIST.addBig(EDIT_BOX_BLOCKS);
			
			for (int i = 0; i < ConfigurationManager.getInstance().getBlockedStructures().size(); i++) {
				String object = ConfigurationManager.getInstance().getBlockedStructures().get(i);
				
				this.OPTIONS_ROW_LIST.addBig(
					new CosmosOptionListElement(ComponentHelper.style(ComponentColour.WHITE, "", object), true, 
					ComponentHelper.style(ComponentColour.RED, "bold", "-"), 
					ComponentHelper.style(ComponentColour.RED, "dimensionalpocketsii.gui.config.remove"),
					(button) -> { 
						ConfigurationManager.getInstance().removeBlockedStructure(object);
						this.updateWidgets();
					}, 
					(button) -> {
						return button.get();
					})
				);
			}
			
			this.addRenderableWidget(Button.builder(
				ComponentHelper.style(ComponentColour.GREEN, "bold", "dimensionalpocketsii.gui.done"), 
				(button) -> { 
					this.switchScreen("home");
				}).pos((this.width) /2, this.height - DONE_BUTTON_TOP_OFFSET).size(SMALL_WIDTH, OPTIONS_LIST_BUTTON_HEIGHT).build()
			);
		} 
		
		else if (this.CURRENT_SCREEN == "items") {
			this.initOptions();
			
			this.OPTIONS_ROW_LIST.addBig(
				new CosmosOptionTitle(ComponentHelper.style(ComponentColour.LIGHT_GRAY, "boldunderline", "dimensionalpocketsii.gui.config.blocked_items"))
			);

			this.EDIT_BOX_ITEMS.setOnPressFunction((button) -> { 
				ConfigurationManager.getInstance().addBlockedItem(this.EDIT_BOX_ITEMS.getEditBox().getValue());
				this.EDIT_BOX_ITEMS.getEditBox().setValue("");
				this.updateWidgets();
			});
			
			this.OPTIONS_ROW_LIST.addBig(EDIT_BOX_ITEMS);
			
			for (int i = 0; i < ConfigurationManager.getInstance().getBlockedItems().size(); i++) {
				String object = ConfigurationManager.getInstance().getBlockedItems().get(i);
				
				this.OPTIONS_ROW_LIST.addBig(
					new CosmosOptionListElement(ComponentHelper.style(ComponentColour.WHITE, "", object), true, 
					ComponentHelper.style(ComponentColour.RED, "bold", "-"), 
					ComponentHelper.style(ComponentColour.RED, "dimensionalpocketsii.gui.config.remove"), 
					(button) -> { 
						ConfigurationManager.getInstance().removeBlockedItem(object);
						this.updateWidgets();
					},
					(button) -> {
						return button.get();
					})
				);
			}
			
			this.addRenderableWidget(Button.builder(
				ComponentHelper.style(ComponentColour.GREEN, "bold", "dimensionalpocketsii.gui.done"),
				(button) -> { 
					this.switchScreen("home"); 
				}).pos((this.width) /2, this.height - DONE_BUTTON_TOP_OFFSET).size(SMALL_WIDTH, OPTIONS_LIST_BUTTON_HEIGHT).build()
			);
		} 
		
		else if (this.CURRENT_SCREEN == "commands") {
			this.initOptions();

			this.OPTIONS_ROW_LIST.addBig(
				new CosmosOptionTitle(ComponentHelper.style(ComponentColour.LIGHT_GRAY, "boldunderline", "dimensionalpocketsii.gui.config.blocked_commands"))
			);

			this.OPTIONS_ROW_LIST.addBig(
				CosmosOptionInstance.createIntSlider(ComponentHelper.style(ComponentColour.ORANGE, "dimensionalpocketsii.gui.config.op_level"),
				CosmosOptionInstance.getTooltipSplitComponent(ComponentHelper.style(DESC_COLOUR, "dimensionalpocketsii.gui.config.op_level_info"), 
				ComponentHelper.style(ComponentColour.RED, "dimensionalpocketsii.gui.config.op_level_info_two")), 
				ConfigurationManager.getInstance().getOPLevel(), 0, 4, 4, ComponentColour.WHITE, ComponentHelper.style(ComponentColour.GREEN, "Min"), 
				ComponentHelper.style(ComponentColour.DARK_YELLOW, "dimensionalpocketsii.gui.config.op_level_slide"), ComponentHelper.style(ComponentColour.RED, "Max"), 
				(intValue) -> {
					ConfigurationManager.getInstance().setOPLevel(intValue);
				})
			);

			this.EDIT_BOX_COMMANDS.setOnPressFunction((button) -> { 
				ConfigurationManager.getInstance().addBlockedCommand(this.EDIT_BOX_COMMANDS.getEditBox().getValue());
				this.EDIT_BOX_COMMANDS.getEditBox().setValue("");
				this.updateWidgets();
			});
			
			this.OPTIONS_ROW_LIST.addBig(EDIT_BOX_COMMANDS);
			
			for (int i = 0; i < ConfigurationManager.getInstance().getBlockedCommands().size(); i++) {
				String object = ConfigurationManager.getInstance().getBlockedCommands().get(i);
				
				this.OPTIONS_ROW_LIST.addBig(
					new CosmosOptionListElement(ComponentHelper.style(ComponentColour.WHITE, "", object), true, 
					ComponentHelper.style(ComponentColour.RED, "bold", "-"), 
					ComponentHelper.style(ComponentColour.RED, "dimensionalpocketsii.gui.config.remove"), 
					(button) -> { 
						ConfigurationManager.getInstance().removeBlockedCommand(object);
						this.updateWidgets();
					}, (button) -> {
						return button.get();
					})
				);
			}
			
			this.addRenderableWidget(Button.builder(
				ComponentHelper.style(ComponentColour.GREEN, "bold", "dimensionalpocketsii.gui.done"), (button) -> { 
					this.switchScreen("home"); 
				}).pos((this.width) /2, this.height - DONE_BUTTON_TOP_OFFSET).size(SMALL_WIDTH, OPTIONS_LIST_BUTTON_HEIGHT).build()
			);
		}
		
		this.addWidget(this.OPTIONS_ROW_LIST);
	}
	
	public void initOptions() {
		this.OPTIONS_ROW_LIST = new CosmosOptionsList( 
			this.minecraft, this.width, this.height, OPTIONS_LIST_TOP_HEIGHT, this.height - OPTIONS_LIST_BOTTOM_OFFSET, 
			OPTIONS_LIST_ITEM_HEIGHT, OPTIONS_LIST_BUTTON_HEIGHT, 310, new CosmosOptions(Minecraft.getInstance(), new File("."))
		);
	}
	
	@Override
	public void tick() {
		if (this.CURRENT_SCREEN == "blocks") {
			this.EDIT_BOX_BLOCKS.getEditBox().tick();
		} else if (this.CURRENT_SCREEN == "items") {
			this.EDIT_BOX_ITEMS.getEditBox().tick();
		} else if (this.CURRENT_SCREEN == "commands") {
			this.EDIT_BOX_COMMANDS.getEditBox().tick();
		}
	}
	
	@Override
	public void render(GuiGraphics graphicsIn, int mouseX, int mouseY, float ticks) {
		this.renderBackground(graphicsIn);
		
		this.OPTIONS_ROW_LIST.render(graphicsIn, mouseX, mouseY, ticks);
		
		graphicsIn.drawCenteredString(this.font, this.title, width / 2, TITLE_HEIGHT, 0xFFFFFF);
		//drawCenteredString(graphicsIn, this.font, ComponentHelper.style(ComponentColour.GREEN, "bold", (this.CURRENT_SCREEN.substring(0, 1).toUpperCase()) + this.CURRENT_SCREEN.substring(1)), width / 2 + 150, TITLE_HEIGHT, 0xFFFFFF);
		
		super.render(graphicsIn, mouseX, mouseY, ticks);
		//List<FormattedCharSequence> list = tooltipAt(this.OPTIONS_ROW_LIST, mouseX, mouseY);
		//graphicsIn.renderTooltip(this.font, list, mouseX, mouseY);
	}
	
	public void updateWidgets() {
		double scroll = this.OPTIONS_ROW_LIST.getScrollAmount();
		
		this.clearWidgets();
		
		this.init();
		
		this.OPTIONS_ROW_LIST.setScrollAmount(scroll);
	}

	@SuppressWarnings("unchecked")
	public static List<FormattedCharSequence> tooltipAt(CosmosOptionsList listIn, int mouseX, int mouseY) {
		Optional<AbstractWidget> optional = listIn.getMouseOver((double)  mouseX, (double) mouseY);
		return (List<FormattedCharSequence>) (optional.isPresent() && optional.get() instanceof AbstractWidget ? ((AbstractWidget) optional.get()).getTooltip() : ImmutableList.of());
	}

	@Override
	public boolean handleComponentClicked(@Nullable Style styleIn) {
		return super.handleComponentClicked(styleIn);
	}

	@Override
	public Optional<GuiEventListener> getChildAt(double mouseX, double mouseY) {
		return super.getChildAt(mouseX, mouseY);
	}

	@Override
	public boolean keyPressed(int mouseX, int mouseY, int ticks) {
		if (this.CURRENT_SCREEN == "blocks") {
			return this.EDIT_BOX_BLOCKS.getEditBox().keyPressed(mouseX, mouseY, ticks);
		} else if (this.CURRENT_SCREEN == "items") {
			return this.EDIT_BOX_ITEMS.getEditBox().keyPressed(mouseX, mouseY, ticks);
		} else if (this.CURRENT_SCREEN == "commands") {
			return this.EDIT_BOX_COMMANDS.getEditBox().keyPressed(mouseX, mouseY, ticks);
		}
		
		return super.keyPressed(mouseX, mouseY, ticks);
	}
	
	@Override
	public boolean charTyped(char charCode, int test) {
		return super.charTyped(charCode, test);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int p_94701_, double p_94702_, double p_94703_) {
		boolean dragged = super.mouseDragged(mouseX, mouseY, p_94701_, p_94702_, p_94703_);
		
		if (this.getChildAt(mouseX, mouseY).isPresent()) {
			for (GuiEventListener listener : this.OPTIONS_ROW_LIST.children()) {
				if (listener.isMouseOver(mouseX, mouseY)) {
					this.updateWidgets();
				}
			}
		}
		
		return dragged;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int ticks) {
		boolean clicked = super.mouseClicked(mouseX, mouseY, ticks);
		
		if (this.CURRENT_SCREEN == "blocks") {
			return this.EDIT_BOX_BLOCKS.getEditBox().mouseClicked(mouseX, mouseY, ticks);
		} else if (this.CURRENT_SCREEN == "items") {
			return this.EDIT_BOX_ITEMS.getEditBox().mouseClicked(mouseX, mouseY, ticks);
		} else if (this.CURRENT_SCREEN == "commands") {
			return this.EDIT_BOX_COMMANDS.getEditBox().mouseClicked(mouseX, mouseY, ticks);
		}
		
		if (this.getChildAt(mouseX, mouseY).isPresent()) {
			for (GuiEventListener listener : this.OPTIONS_ROW_LIST.children()) {
				if (!listener.equals(this.closeButton)) {
					if (listener.isMouseOver(mouseX, mouseY)) {
						this.updateWidgets();
					}
				}
			}
		}
		
		return clicked;
	}
	
	public void switchScreen(String screen) {
		this.CURRENT_SCREEN = screen;
		this.updateWidgets();
		this.init();
	}
	
    @Override
    public void onClose() {
    	if (this.CURRENT_SCREEN == "home") {
	    	this.minecraft.setScreen(this.PARENT_SCREEN);

	        ConfigurationManager.save();
	    	super.onClose();
    	} 
    	
        ConfigurationManager.save();
    	super.onClose();
    }
}