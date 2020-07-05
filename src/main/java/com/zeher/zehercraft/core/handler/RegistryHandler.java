package com.zeher.zehercraft.core.handler;

import com.zeher.zehercraft.ZeherCraft;
import com.zeher.zeherlib.mod.fluid.ModBlockFluid;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Handler for registry.
 */
public class RegistryHandler {
	
	/**
	 * Used to register items and blocks into the ore dictionary. {@link OreDictionary}
	 */
	public static class ORE_DICTIONARY {
		
		public static void registerBlocks() {
			OreDictionary.registerOre("oreCopper", BlockHandler.BASE.BLOCK_ORE_COPPER);
			OreDictionary.registerOre("oreSilver", BlockHandler.BASE.BLOCK_ORE_SILVER);
			OreDictionary.registerOre("oreTin", BlockHandler.BASE.BLOCK_ORE_TIN);
			OreDictionary.registerOre("oreSilicon", BlockHandler.BASE.BLOCK_ORE_SILICON);
			
			OreDictionary.registerOre("blockCopper", BlockHandler.BASE.BLOCK_COPPER);
			OreDictionary.registerOre("blockSilver", BlockHandler.BASE.BLOCK_SILVER);
			OreDictionary.registerOre("blockTin", BlockHandler.BASE.BLOCK_TIN);
			OreDictionary.registerOre("blockSilicon", BlockHandler.BASE.BLOCK_SILICON);
		}
	}
	
	
	/**
	 * Used in Item & Block Registry.
	 */
	public static class REGISTRY_EVENT {
		/**
		 * Registers multiple blocks to the specified resource category.
		 * @param event {@link ModelRegistryEvent} [required event]
		 * @param blocks [list of blocks to be registered]
		 */
		public static void registerAllModels(final ModelRegistryEvent event, Block... blocks) {
			for (int i = 0; i < blocks.length; i++) {
				registerModel(blocks[i]);
			}
		}
		
		/**
		 * Registers a single block to the specified resource category.
		 * @param blocks [block to be registered]
		 */
		@SideOnly(Side.CLIENT)
		public static void registerModel(Block block){
			Item item = Item.getItemFromBlock(block);
			ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
		}
		
		
		/**
		 * Registers all blocks.
		 * @param event {@link RegistryEvent.Register<Block>}
		 * @param blocks [list of blocks to be registered]
		 */
		public static void registerAllBlocks(RegistryEvent.Register<Block> event, Block... blocks) {
			event.getRegistry().registerAll(blocks);
		}
		
		/**
		 * Creates an array of {@link ItemBlock}s to be registered later.
		 * @param event {@link RegistryEvent.Register<Block>}
		 * @param blocks [list of blocks to be added]
		 * @returns ItemBlock[] [array of itemblocks to be registered later]
		 */
		public static ItemBlock[] getItemBlocks(RegistryEvent.Register<Block> event, Block... blocks) {
			ItemBlock[] item_blocks = new ItemBlock[blocks.length];
			event.getRegistry().registerAll(blocks);
			for (int i = 0; i < blocks.length; i++) {
				item_blocks[i] = new ItemBlock(blocks[i]);
				item_blocks[i].setRegistryName(blocks[i].getRegistryName());
			}
			
			return item_blocks;
		}
		
		/**
		 * Registers {@link ItemBlock} for specified blocks. (By ItemBlock[])
		 * @param event {@link RegistryEvent.Register<Item>}
		 * @param block_items [array of ItemBlocks]
		 * 
		 * Requires {@link @SubscribeEvent}
		 */
		public static void registerItemBlockArray(RegistryEvent.Register<Item> event, ItemBlock[] block_items) {
			event.getRegistry().registerAll(block_items);
		}
		
		/**
		 * Registers {@link ItemBlock} for specified blocks. (By ItemBlock...)
		 * @param event {@link RegistryEvent.Register<Item>}
		 * @param block_items [array of ItemBlocks]
		 * 
		 * Requires {@link @SubscribeEvent}
		 */
		public static void registerAllItemBlocks(RegistryEvent.Register<Item> event, Item... block_items) {
			event.getRegistry().registerAll(block_items);
		}
		
		/**
		 * Registers multiple fluid blocks to the specified resource category.
		 * @param event {@link ModelRegistryEvent} [required event]
		 * @param blocks [list of blocks to be registered]
		 */
		public static void registerAllFluidModels(final ModelRegistryEvent event, ModBlockFluid... blocks) {
			for (int i = 0; i < blocks.length; i++) {
				registerFluidModel(blocks[i]);
			}
		}
		
		/**
		 * Registers a single fluid block to the specified resource category.
		 * @param blocks [block to be registered]
		 */
		@SideOnly(Side.CLIENT)
		public static void registerFluidModel(ModBlockFluid fluid_block){
			Item item = Item.getItemFromBlock(fluid_block);
			
			final ModelResourceLocation modelResourceLocation = new ModelResourceLocation(new ResourceLocation(ZeherCraft.MOD_ID, "fluid/fluid_" + fluid_block.getFluid().getName()), "normal");
			
			ModelLoader.setCustomMeshDefinition(item, stack -> modelResourceLocation);
		
			ModelLoader.setCustomStateMapper((Block) fluid_block, new StateMapperBase() {
				@Override
				protected ModelResourceLocation getModelResourceLocation(IBlockState p_178132_1_) {
					return modelResourceLocation;
				}
			});
		}
	}
}