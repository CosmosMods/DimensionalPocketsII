package com.tcn.cosmoslibrary.impl.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tcn.cosmoslibrary.CosmosReference;
import com.tcn.cosmoslibrary.client.impl.container.GuiContainerElements;
import com.tcn.cosmoslibrary.client.impl.screen.GuiListElement;
import com.tcn.cosmoslibrary.client.impl.util.TextHelper;
import com.tcn.cosmoslibrary.client.impl.widget.FluidWidget;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fluids.IFluidTank;

/**
 * Heavily used to simplify the creation and usage of GUIs.
 */
public class CosmosGuiUtil {
	
	public static final int DEFAULT_COLOUR_BACKGROUND = 4210752;
	public static final int DEFAULT_COLOUR_FONT_LIST = 16777215;
	
	public static final int BLACK = 0x000000;
	public static final int WHITE = 0xFFFFFF;
	public static final int LIGHT_BLUE = 0x5882FA;
	public static final int BLUE = 0x0000FF;
	public static final int LIGHT_GREY = 0xA4A4A4;
	public static final int GREY = 0x424242;
	public static final int GREEN = 0x00FF00;
	public static final int DARK_GREEN = 0x0B610B;
	public static final int RED = 0xFF0000;
	public static final int YELLOW = 0xFFFF00;
	public static final int ORANGE = 0xFF8000;
	public static final int CYAN = 0x01A9DB;
	public static final int MAGENTA = 0xDF01D7;
	public static final int PURPLE = 0x8904B1;
	public static final int PINK = 0xFE2EC8;
	public static final int BROWN = 0x61210B;
	
	public static class DRAW {
		public static void drawPowerBar(Screen container, MatrixStack stack, int[] screen_coords, int draw_x, int draw_y, int scaled, int[] bar_location, boolean has_stored) {
			container.getMinecraft().getTextureManager().bindTexture(CosmosReference.RESOURCE.BASE.GUI_ENERGY_BAR_LOC);
			
			if (has_stored) {
				container.blit(stack, screen_coords[0] + draw_x, (screen_coords[1] + bar_location[2] + draw_y) - scaled, bar_location[0], bar_location[1] - scaled, bar_location[3], scaled);
			}
			
			container.getMinecraft().getTextureManager().deleteTexture(CosmosReference.RESOURCE.BASE.GUI_ENERGY_BAR_LOC);
		}
		
		public static void drawSlot(Screen container, MatrixStack stack, int[] screen_coords, int draw_x, int draw_y, int[] slot_location) {
			container.getMinecraft().getTextureManager().bindTexture(CosmosReference.RESOURCE.BASE.GUI_SLOT_LOC);
			
			container.blit(stack, screen_coords[0] + draw_x, screen_coords[1] + draw_y, slot_location[0], slot_location[1], slot_location[2], slot_location[3]);
			
			container.getMinecraft().getTextureManager().deleteTexture(CosmosReference.RESOURCE.BASE.GUI_SLOT_LOC);
		}
		
		public static void drawBackground(Screen container, MatrixStack stack, int[] screen_coords, ResourceLocation texture) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			container.getMinecraft().getTextureManager().bindTexture(texture);
			
			container.blit(stack, screen_coords[0], screen_coords[1], 0, 0, container.width, container.height);
		}
		
		public static void drawBackground(Screen container, MatrixStack stack, int[] screen_coords, int draw_x, int draw_y, ResourceLocation texture) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			container.getMinecraft().getTextureManager().bindTexture(texture);
			
			container.blit(stack, screen_coords[0], screen_coords[1], draw_x, draw_y, container.width, container.height);
		}
		
		public static void bindTexture(Screen container, MatrixStack stack, ResourceLocation texture) {
			container.getMinecraft().getTextureManager().bindTexture(texture);
		}
		
		public static void unBindTexture(Screen container, MatrixStack stack, ResourceLocation texture) {
			container.getMinecraft().getTextureManager().deleteTexture(texture);
		}
		
		@SuppressWarnings("static-access")
		public static void drawFluidTank(Screen container, MatrixStack stack, int[] screen_coords, int draw_x, int draw_y, IFluidTank tank, int scaled) {
			if (tank.getFluidAmount() > 0) {
				
				ResourceLocation fluidStill = tank.getFluid().getFluid().getAttributes().getStillTexture();
				TextureAtlasSprite fluid_texture = container.getMinecraft().getInstance().getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(fluidStill);//.getTexture(tank.getFluid().getFluid().getAttributes().getStillTexture());
				container.getMinecraft().getTextureManager().bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
			
				if (!(fluid_texture == null)) {
					if(scaled <= 30){
						container.blit(stack, screen_coords[0] + draw_x, screen_coords[1] + draw_y + 38 - scaled, 16, scaled, 0, fluid_texture);
					} else if (scaled > 30 && scaled <= 45){
						container.blit(stack, screen_coords[0] + draw_x, screen_coords[1] + draw_y + 38 - scaled, 16, scaled / 2 + 1, 0, fluid_texture);
						container.blit(stack, screen_coords[0] + draw_x, screen_coords[1] + draw_y + 38 - scaled/2, 16, scaled / 2, 0, fluid_texture);
					} else if (scaled > 45){
						container.blit(stack, screen_coords[0] + draw_x, screen_coords[1] + draw_y + 38 - scaled, 16, scaled / 2, 0, fluid_texture);
						container.blit(stack, screen_coords[0] + draw_x, screen_coords[1] + draw_y + 38 - scaled/2, 16, scaled / 2, 0, fluid_texture);
					}
				}
			}
		}
		
		public static void renderToolTipPowerProducer(Screen container, MatrixStack stack, int[] screen_coords, int draw_x, int draw_y, int mouse_x, int mouse_y, int stored, int generation_rate, boolean producing) {
			if (IS_HOVERING.isHoveringPower(mouse_x, mouse_y, screen_coords[0] + draw_x, screen_coords[1] + draw_y)) {
				if (producing) {
					container.func_243308_b(stack, TEXT_LIST.generationText(stored, generation_rate), mouse_x - screen_coords[0], mouse_y - screen_coords[1]);
				} else {
					container.func_243308_b(stack, TEXT_LIST.storedTextNo(stored), mouse_x - screen_coords[0], mouse_y - screen_coords[1]);
				}
			}
		}
		
		public static void renderToolTipFluidLarge(Screen container, MatrixStack stack, int[] screen_coords, int draw_x, int draw_y, int mouse_x, int mouse_y, IFluidTank tank) {
			if (IS_HOVERING.isHoveringFluidLarge(mouse_x, mouse_y, screen_coords[0] + draw_x, screen_coords[1] + draw_y)) {
				if (tank.getFluidAmount() > 0) {
					container.func_243308_b(stack, TEXT_LIST.fluidText(tank.getFluid().getDisplayName().toString(), tank.getFluidAmount(), tank.getCapacity()), mouse_x - screen_coords[0], mouse_y - screen_coords[1]);
				} else {
					container.func_243308_b(stack, TEXT_LIST.fluidTextEmpty(), mouse_x - screen_coords[0], mouse_y - screen_coords[1]);
				}
			}
		}
		
		public static void renderToolTipEmptyFluidButton(Screen container, MatrixStack stack, int[] screen_coords, int draw_x, int draw_y, int mouse_x, int mouse_y, boolean has_fluid) {
			if (IS_HOVERING.isHoveringButtonStandard(mouse_x, mouse_y, screen_coords[0] + draw_x, screen_coords[1] + draw_y)) {
				if (has_fluid) {
					if (TextHelper.isShiftKeyDown(container.getMinecraft())) {
						container.func_243308_b(stack, TEXT_LIST.emptyFluidTankDo(), mouse_x - screen_coords[0], mouse_y - screen_coords[1]);
					} else {
						container.func_243308_b(stack, TEXT_LIST.emptyFluidTank(), mouse_x - screen_coords[0], mouse_y - screen_coords[1]);
					}
				}
			}
		}
		
		public static void drawScaledElementUpNestled(Screen container, MatrixStack stack, int[] screen_coords, int draw_x, int draw_y, int texture_x, int texture_y, int width, int height, int scaled) {
			container.blit(stack, screen_coords[0] + draw_x, (screen_coords[1] + draw_y + height) - scaled, texture_x, (texture_y + height) - scaled, width, scaled);
		}
		
		public static void drawScaledElementDownNestled(Screen container, MatrixStack stack, int[] screen_coords, int draw_x, int draw_y, int texture_x, int texture_y, int width, int scaled) {
			container.blit(stack, screen_coords[0] + draw_x, screen_coords[1] + draw_y, texture_x, texture_y, width, scaled);
		}
		
		public static void drawScaledElementRightNestled(Screen container, MatrixStack stack, int[] screen_coords, int draw_x, int draw_y, int texture_x, int texture_y, int height, int scaled) {
			container.blit(stack, screen_coords[0] + draw_x, screen_coords[1] + draw_y, texture_x, texture_y, scaled + 1, height);
		}
		
		public static void drawScaledElementUpExternal(Screen container, MatrixStack stack, int[] screen_coords, int draw_x, int draw_y, int texture_x, int texture_y, int width, int height, int scaled, ResourceLocation texture) {
			container.getMinecraft().getTextureManager().bindTexture(texture);
			
			container.blit(stack, screen_coords[0] + draw_x, (screen_coords[1] + draw_y + height) - scaled, texture_x, (texture_y + height) - scaled, width, scaled);
			
			container.getMinecraft().getTextureManager().deleteTexture(texture);
		}
		
		public static void drawScaledElementDownExternal(Screen container, MatrixStack stack, int[] screen_coords, int draw_x, int draw_y, int texture_x, int texture_y, int width, int scaled, ResourceLocation texture) {
			container.getMinecraft().getTextureManager().bindTexture(texture);
			
			container.blit(stack, screen_coords[0] + draw_x, screen_coords[1] + draw_y, texture_x, texture_y + scaled, width, scaled);
			
			container.getMinecraft().getTextureManager().deleteTexture(texture);
		}
		
		public static FluidWidget createBucketButton(int id, int[] screen_coords, int draw_x, int draw_y, int size, boolean enabled) {
			FluidWidget button;
			if (enabled) {
				button = new FluidWidget(screen_coords[0] + draw_x, screen_coords[1] + draw_y, size, 1, false);
			} else {
				button = new FluidWidget(screen_coords[0] + draw_x, screen_coords[1] + draw_y, size, 4, true);
			}
			return button;
		}
		
		public static void drawStaticElement(Screen container, MatrixStack stack, int[] screen_coords, int draw_x, int draw_y, int texture_x, int texture_y, int width, int height) {
			container.blit(stack, screen_coords[0] + draw_x, screen_coords[1] + draw_y, texture_x, texture_y, width, height);
		}
		
		public static void drawStaticElementToggled(Screen container, MatrixStack stack, int[] screen_coords, int draw_x, int draw_y, int texture_x, int texture_y, int width, int height, boolean enabled) {
			if (enabled) {
				container.blit(stack, screen_coords[0] + draw_x, screen_coords[1] + draw_y, texture_x, texture_y, width, height);
			}
		}	
	}
	
	public static class ELEMENT {
		public static class SCROLL {
			public static final int[] SCROLL_TYPE_ONE = new int[] { 15, 0 };
			public static final int[] SCROLL_TYPE_TWO = new int[] { 15, 15 };
			
			public static class DRAW {
				public static void drawScrollElement(GuiContainerElements container, MatrixStack stack, int[] screen_coords, int draw_x, int draw_y, int current_scroll, boolean toggled, int type, boolean enabled) {
					container.getMinecraft().getTextureManager().bindTexture(CosmosReference.RESOURCE.BASE.GUI_ELEMENT_MISC_LOC);
					
					if (type == 1) {
						if (enabled) {
							CosmosGuiUtil.DRAW.drawStaticElementToggled(container, stack, screen_coords, draw_x, (draw_y + (current_scroll / 10)), SCROLL_TYPE_ONE[0], SCROLL_TYPE_ONE[1], 13, 15, toggled);
						} else {
							CosmosGuiUtil.DRAW.drawStaticElementToggled(container, stack, screen_coords, draw_x, draw_y, SCROLL_TYPE_ONE[0], SCROLL_TYPE_ONE[1], 13, 15, toggled);
						}
					} else if (type == 2) {
						if (enabled) {
							CosmosGuiUtil.DRAW.drawStaticElementToggled(container, stack, screen_coords, draw_x, (draw_y + (current_scroll / 10)), SCROLL_TYPE_TWO[0], SCROLL_TYPE_TWO[1], 13, 15, toggled);
						} else {
							CosmosGuiUtil.DRAW.drawStaticElementToggled(container, stack, screen_coords, draw_x, draw_y, SCROLL_TYPE_TWO[0], SCROLL_TYPE_TWO[1], 13, 15, toggled);
						}
					}
					
					container.getMinecraft().getTextureManager().deleteTexture(CosmosReference.RESOURCE.BASE.GUI_ELEMENT_MISC_LOC);
				}
			}
		
		}
		
		public static class LIST {
			public static class DRAW {
				public static void drawListWithElementsSmall(GuiContainerElements container, FontRenderer fontRenderer, int draw_x, int draw_y, int width, int index_from, @Nullable ResourceLocation skin, ArrayList<String> list) {
					int height = 14;
					int spacing_y = height + 2;
					//int index_from_clamped = MathHelper.clamp(index_from, 0, list.size());
					
					ArrayList<GuiListElement> new_list = new ArrayList<GuiListElement>();
					
					if (list.isEmpty()) {
						return;
					} 
					
					else {
						for (int j = 0; j < list.size(); j++) {
							String string = list.get(j);
							
							if (string != null) {
								for (int o = 0; o < container.getElementList().size(); o++) {
									String test_string = container.getElementList().get(o).displayString;
									if (string.equals(test_string)) {
										return;
									}
								}
							}
						}
					}
					
					for (int i = 0; i < list.size(); i++) {
						String display_string = list.get(i);
						GuiListElement element = new GuiListElement(i, draw_x, draw_y + (spacing_y * i), width, height, skin, display_string, DEFAULT_COLOUR_FONT_LIST);
						
						if (new_list.size() < 1) {
							new_list.add(element);
						} else {
							if (!(new_list.contains(element))) {
								new_list.add(element);
							}
						}
					}
					
					container.setElementList(new_list);
				}
				
				
				public static void drawListWithElementsLarge(GuiContainerElements container, FontRenderer fontRenderer, int[] screen_coords, int draw_x, int draw_y, int width, @Nullable ResourceLocation skin, ArrayList<String> list) {
					int height = 20;
					int spacing_y = height + 2;
					
					for (int i = 0; i < list.size(); i++) {
						String display_string = list.get(i);
						
						container.getElementList().add(new GuiListElement(i, screen_coords, draw_x, draw_y + (spacing_y * i), width, height, skin, display_string, DEFAULT_COLOUR_FONT_LIST));
					}
				}
			}
		}
	}

	@Deprecated //TODO: Remove in next release.
	public static class FONT_LIST {
		@Deprecated //TODO: Remove in next release.
		public static class DRAW {
			@Deprecated //TODO: Remove in next release.
			public static void drawList(Screen container, MatrixStack stack, FontRenderer fontRenderer, int[] screen_coords, int draw_x, int draw_y, ArrayList<String> list) {
				int spacing_x = 5;
				int spacing_y = 4;
				int spacing_y_text = 10;
				
				for (int i = 0; i < list.size(); i++) {
					String display_string = list.get(i);
					
					fontRenderer.drawString(stack, display_string, screen_coords[0] + draw_x + spacing_x, screen_coords[1] + draw_y + spacing_y + (spacing_y_text * i), DEFAULT_COLOUR_FONT_LIST);
				}
			}
			
			@Deprecated //TODO: Remove in next release.
			public static void drawList(Screen container, MatrixStack stack, FontRenderer fontRenderer, int[] screen_coords, int draw_x, int draw_y, int colour, ArrayList<String> list) {
				int spacing_x = 5;
				int spacing_y = 8;
				
				for (int i = 0; i < list.size(); i++) {
					String display_string = list.get(i);
					
					fontRenderer.drawString(stack, display_string, screen_coords[0] + draw_x + spacing_x, screen_coords[1] + draw_y + spacing_y + (spacing_y * i), colour);
				}
			}
		}
	}
	
	public static class FONT {
		public static class DRAW {
			public static void drawString(MatrixStack stack, FontRenderer font, int[] screen_coords, int draw_x, int draw_y, String draw_text, boolean formatted, boolean draw_from) {
				if (formatted) {
					if (draw_from) {
						font.drawString(stack, I18n.format(draw_text), screen_coords[0] + draw_x, screen_coords[1] + draw_y, DEFAULT_COLOUR_BACKGROUND);
					} else {
						font.drawString(stack, I18n.format(draw_text), draw_x, draw_y, DEFAULT_COLOUR_BACKGROUND);
					}
				} else {
					if (draw_from) {
						font.drawString(stack, draw_text, screen_coords[0] + draw_x, screen_coords[1] + draw_y, DEFAULT_COLOUR_BACKGROUND);
					} else {
						font.drawString(stack, draw_text, draw_x, draw_y, DEFAULT_COLOUR_BACKGROUND);
					}
				}
			}
			
			public static void drawString(MatrixStack stack, FontRenderer font, int[] screen_coords, int draw_x, int draw_y, String draw_text, boolean formatted, boolean draw_from, int colour) {
				if (formatted) {
					if (draw_from) {
						font.drawString(stack, I18n.format(draw_text), screen_coords[0] + draw_x, screen_coords[1] + draw_y, colour);
					} else {
						font.drawString(stack, I18n.format(draw_text), draw_x, draw_y, colour);
					}
				} else {
					if (draw_from) {
						font.drawString(stack, draw_text, screen_coords[0] + draw_x, screen_coords[1] + draw_y, colour);
					} else {
						font.drawString(stack, draw_text, draw_x, draw_y, colour);
					}
				}
			}
			
			@Deprecated //Use new drawString()
			public static void drawString(MatrixStack stack, FontRenderer font, int[] screen_coords, int draw_x, int draw_y, String draw_text) {	
				font.drawString(stack, draw_text, screen_coords[0] + draw_x, screen_coords[1] + draw_y, DEFAULT_COLOUR_BACKGROUND);
			}
			
			@Deprecated //Use new drawString()
			public static void drawString(MatrixStack stack, FontRenderer font, int[] screen_coords, int draw_x, int draw_y, String draw_text, int colour) {
				font.drawString(stack, draw_text, screen_coords[0] + draw_x, screen_coords[1] + draw_y, colour);	
			}
			
			@Deprecated //Use new drawString()
			public static void drawStringUnformatted(MatrixStack stack, FontRenderer font, int[] screen_coords, int draw_x, int draw_y, String draw_text) {
				drawString(stack, font, screen_coords, draw_y, draw_y, draw_text);
			}
			
			@Deprecated //Use new drawString()
			public static void drawStringUnformatted(MatrixStack stack, FontRenderer font, int[] screen_coords, int draw_x, int draw_y, String draw_text, int colour) {
				drawString(stack, font, screen_coords, colour, colour, draw_text, colour);
			}
			
			@Deprecated //Use new drawString()
			public static void drawStringFormatted(MatrixStack stack, FontRenderer font, int[] screen_coords, int draw_x, int draw_y, String draw_text) {
				font.drawString(stack, I18n.format(draw_text), screen_coords[0] + draw_x, screen_coords[1] + draw_y, DEFAULT_COLOUR_BACKGROUND);
			}
			
			@Deprecated //Use new drawString()
			public static void drawStringFormatted(MatrixStack stack, FontRenderer font, int[] screen_coords, int draw_x, int draw_y, String draw_text, int colour) {
				font.drawString(stack, I18n.format(draw_text), screen_coords[0] + draw_x, screen_coords[1] + draw_y, colour);
			}
			
			@Deprecated
			public static void drawInventoryString(MatrixStack stack, FontRenderer font, int[] screen_coords, int draw_x, int draw_y) {
				font.drawString(stack, I18n.format("container.inventory"), screen_coords[0] + draw_x, screen_coords[1] + draw_y, DEFAULT_COLOUR_BACKGROUND);
			}
			
			public static void drawInventoryString(MatrixStack stack, FontRenderer font, int[] screen_coords, int draw_x, int draw_y, boolean draw_from) {
				if (draw_from) {
					font.drawString(stack, I18n.format("container.inventory"), screen_coords[0] + draw_x, screen_coords[1] + draw_y, DEFAULT_COLOUR_BACKGROUND);
				} else {
					font.drawString(stack, I18n.format("container.inventory"), draw_x, draw_y, DEFAULT_COLOUR_BACKGROUND);
				}
			}
			
			public static void drawInventoryString(MatrixStack stack, FontRenderer font, int[] screen_coords, int draw_x, int draw_y, int colour) {
				font.drawString(stack, I18n.format("container.inventory"), screen_coords[0] + draw_x, screen_coords[1] + draw_y, colour);
			}
			
			@Deprecated //1.14.4
			public static void drawCustomString(TileEntity inventory, FontRenderer font, int[] screen_coords, int draw_x, int draw_y) {
				//String name = I18n.format(inventory.getType().getRegistryName().toString());
				//font.drawString(name, screen_coords[0] + draw_x, screen_coords[1] + draw_y, DEFAULT_COLOUR_BACKGROUND);
			}
			@Deprecated //1.14.4
			public static void drawCustomString(IInventory inventory, FontRenderer font, int[] screen_coords, int draw_x, int draw_y, int colour) {
				//String name = inventory.hasCustomName() ? inventory.getName() : I18n.format(inventory.getName());
				//font.drawString(name, screen_coords[0] + draw_x, screen_coords[1] + draw_y, colour);
			}
		}
	}
	
	@Deprecated
	public static class UTILITY {
		
	}
	
	public static class IS_HOVERING {
		public static boolean isHoveringPower(int mouse_x, int mouse_y, int x, int y) {
			if (mouse_x >= x && mouse_x <= x + 17) {
				if (mouse_y >= y && mouse_y <= y + 62) {
					return true;
				}
			}
			return false;
		}
		
		public static boolean isHoveringPowerSmall(int mouse_x, int mouse_y, int x, int y) {
			if (mouse_x >= x && mouse_x <= x + 17) {
				if (mouse_y >= y && mouse_y <= y + 40) {
					return true;
				}
			}
			return false;
		}
		
		public static boolean isHovering(int mouse_x, int mouse_y, int min_x, int max_x, int min_y, int max_y) {
			if (mouse_x >= min_x && mouse_x <= max_x) {
				if (mouse_y >= min_y && mouse_y <= max_y) {
					return true;
				}
			}
			return false;
		}
		
		public static boolean isHoveringFluid(int mouse_x, int mouse_y, int x, int y) {
			if (mouse_x >= x && mouse_x <= x + 16) {
				if (mouse_y >= y && mouse_y <= y + 38) {
					return true;
				}
			}
			return false;
		}
		
		public static boolean isHoveringFluidLarge(int mouse_x, int mouse_y, int x, int y) {
			if (mouse_x >= x && mouse_x <= x + 16) {
				if (mouse_y >= y && mouse_y <= y + 60) {
					return true;
				}
			}
			return false;
		}
		
		public static boolean isHoveringButtonStandard(int mouse_x, int mouse_y, int x, int y) {
			if (mouse_x >= x && mouse_x <= x + 18) {
				if (mouse_y >= y && mouse_y <= y + 18) {
					return true;
				}
			}
			return false;
		}
		
		public static boolean isHoveringButton(int mouse_x, int mouse_y, int x, int y, int x_size, int y_size) {
			if (mouse_x >= x && mouse_x <= x + x_size) {
				if (mouse_y >= y && mouse_y <= y + y_size) {
					return true;
				}
			}
			return false;
		}
	}
	
	public static class TEXT_LIST {
		public static List<ITextComponent> storedTextRF(int stored, int speed) {
			StringTextComponent[] description = { 
					new StringTextComponent(TextHelper.PURPLE + "Stored: " + TextHelper.ORANGE + stored), 
					new StringTextComponent(TextHelper.GREEN + "Using: " + TextHelper.TEAL + speed + TextHelper.GREEN + " RF/t.")};
			
			return Arrays.asList(description);
		}
		
		public static List<ITextComponent> storedTextNo(int stored) {
			StringTextComponent[] description = {
					new StringTextComponent(TextHelper.PURPLE + "Stored: " + TextHelper.ORANGE + stored)};
			
			return Arrays.asList(description);
		}
		
		public static List<ITextComponent> fluidText(String name, int amount, int capacity) {
			StringTextComponent[] description = {
					new StringTextComponent(TextHelper.TEAL + "Fluid: " + name), 
					new StringTextComponent(TextHelper.ORANGE + "Amount: " + amount + " / " + capacity + " mB")};
			
			return Arrays.asList(description);
		}
		
		public static List<ITextComponent> fluidTextEmpty() {
			StringTextComponent[] description = { 
					new StringTextComponent(TextHelper.TEAL + "Empty:"), 
					new StringTextComponent(TextHelper.ORANGE + "Amount: 0 mB")};
			
			return Arrays.asList(description);
		}
		
		public static List<ITextComponent> emptyFluidTankDo() {
			StringTextComponent[] description = { 
					new StringTextComponent(TextHelper.GREEN + "Empty tank."), 
					new StringTextComponent(TextHelper.RED + "Warning: " + TextHelper.ORANGE + "Cannot be undone!")};
			
			return Arrays.asList(description);
		}

		public static List<ITextComponent> emptyFluidTank() {
			StringTextComponent[] description = {
					new StringTextComponent(TextHelper.GREEN + "Shift click " + TextHelper.LIGHT_GRAY + "to empty tank.")};
			
			return Arrays.asList(description);
		}
		
		public static List<ITextComponent> modeChange(String colour, String mode) {
			StringTextComponent[] description = { 
					new StringTextComponent(TextHelper.GREEN + "Click to change mode."), 
					new StringTextComponent(TextHelper.LIGHT_GRAY + "Current mode: " + colour + mode + TextHelper.LIGHT_GRAY + ".")};
			
			return Arrays.asList(description);
		}
		
		public static List<ITextComponent> generationText(int stored, int generation_rate) {
			StringTextComponent[] description = { 
					new StringTextComponent(TextHelper.PURPLE + "Stored: " + TextHelper.ORANGE + stored), 
					new StringTextComponent(TextHelper.RED + "Producing: " + TextHelper.TEAL + generation_rate + TextHelper.RED + " RF/t.")};
			
			return Arrays.asList(description);
		}
	}
}