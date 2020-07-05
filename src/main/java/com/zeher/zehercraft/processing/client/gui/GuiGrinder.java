package com.zeher.zehercraft.processing.client.gui;

import com.zeher.zehercraft.ZCReference;
import com.zeher.zehercraft.processing.client.container.ContainerGrinder;
import com.zeher.zehercraft.processing.core.tile.TileEntityGrinder;
import com.zeher.zeherlib.ZLReference;
import com.zeher.zeherlib.mod.util.ModGuiUtil;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiGrinder extends GuiContainer {
	
	private TileEntityGrinder INVENTORY;

	public GuiGrinder(InventoryPlayer inv_player, TileEntityGrinder tile) {
		super(new ContainerGrinder(inv_player, tile));
		this.INVENTORY = tile;
		
		this.xSize = 176;
		this.ySize = 177;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouse_x, int mouse_y) {
		int[] screen_coords = new int[]{(this.width - this.xSize) / 2, (this.height - this.ySize) / 2};
		
		ModGuiUtil.FONT.DRAW.drawString(this.fontRenderer, screen_coords, 51, 4, this.INVENTORY.getName(), true, false, ModGuiUtil.DEFAULT_COLOUR_FONT_LIST);
		ModGuiUtil.FONT.DRAW.drawString(this.fontRenderer, screen_coords, 8, 85, "container.inventory", true, false, ModGuiUtil.DEFAULT_COLOUR_FONT_LIST);
		
		if (ModGuiUtil.IS_HOVERING.isHoveringPower(mouse_x, mouse_y, screen_coords[0] + 79, screen_coords[1] + 15)) {
			if (this.INVENTORY.getProcessTime(0) > 0 || this.INVENTORY.canProcess() && this.INVENTORY.hasEnergy()) {
				this.drawHoveringText(ModGuiUtil.TEXT_LIST.storedTextRF(this.INVENTORY.getEnergyStored(EnumFacing.DOWN), ZCReference.RESOURCE.PROCESSING.RF_TICK_RATE[this.INVENTORY.getStackInSlot(3).getCount()]), mouse_x - screen_coords[0], mouse_y - screen_coords[1]);
			} else {
				this.drawHoveringText(ModGuiUtil.TEXT_LIST.storedTextNo(this.INVENTORY.getEnergyStored(EnumFacing.DOWN)), mouse_x - screen_coords[0], mouse_y - screen_coords[1]);
			}
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float ticks, int mouse_x, int mouse_y) {
		int[] screen_coords = new int[]{(this.width - this.xSize) / 2, (this.height - this.ySize) / 2};
		
		ModGuiUtil.DRAW.drawBackground(this, screen_coords, ZCReference.RESOURCE.PROCESSING.GRINDER_LOC_GUI);
		ModGuiUtil.DRAW.drawScaledElementDownNestled(this, screen_coords, 104, 38, 176, 0, 16, this.INVENTORY.getProcessProgressScaled(16));
		ModGuiUtil.DRAW.drawPowerBar(this, screen_coords, 79, 14, this.INVENTORY.getEnergyScaled(ZLReference.RESOURCE.INFO.ENERGY_BAR[2]), ZLReference.RESOURCE.INFO.ENERGY_BAR, this.INVENTORY.hasEnergy());
	}

	@Override
	public void drawScreen(int mouse_x, int mouse_y, float ticks) {
        this.drawDefaultBackground();
        super.drawScreen(mouse_x, mouse_y, ticks);
        this.renderHoveredToolTip(mouse_x, mouse_y);
    }
}
