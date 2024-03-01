package com.tcn.dimensionalpocketsii.core.crafting;

import java.util.List;
import java.util.stream.Stream;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonObject;
import com.tcn.dimensionalpocketsii.DimensionalPockets;
import com.tcn.dimensionalpocketsii.core.management.ObjectManager;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

public class UpgradeStationRecipe implements Recipe<Container> {
	
	public final Ingredient focusIngredient;
	public final Ingredient[] topIngredients;
	public final Ingredient[] middleIngredients;
	public final Ingredient[] bottomIngredients;
	public final ItemStack resultIngredient;
	public ResourceLocation ident;

	public UpgradeStationRecipe(ResourceLocation identIn, Ingredient focusIngredientIn, Ingredient[] topIngredientsIn, Ingredient[] middleIngredientsIn, Ingredient[] bottomIngredientsIn, ItemStack resultIngredientIn) {
		this.ident = identIn;
		this.focusIngredient = focusIngredientIn;
		this.topIngredients = topIngredientsIn;
		this.middleIngredients = middleIngredientsIn;
		this.bottomIngredients = bottomIngredientsIn;
		this.resultIngredient = resultIngredientIn;
	}

	@Override
	public boolean matches(Container containerIn, Level levelIn) {
		boolean flagFocus = this.focusIngredient.test(containerIn.getItem(0));
		boolean flagTopRow = this.topIngredients[0].test(containerIn.getItem(1)) && this.topIngredients[1].test(containerIn.getItem(2)) && this.topIngredients[2].test(containerIn.getItem(3));
		boolean flagMiddleRow = this.middleIngredients[0].test(containerIn.getItem(4)) && this.middleIngredients[1].test(containerIn.getItem(5));
		boolean flagBottomRow = this.bottomIngredients[0].test(containerIn.getItem(6)) && this.bottomIngredients[1].test(containerIn.getItem(7)) && this.bottomIngredients[2].test(containerIn.getItem(8));
		
		return flagFocus && flagTopRow && flagMiddleRow && flagBottomRow;
	}

	public boolean isFocusIngredient(ItemStack stackIn) {
		return focusIngredient.test(stackIn);
	}
	
	@Override
	public ItemStack assemble(Container containerIn, RegistryAccess accessIn) {
		ItemStack itemstack = this.resultIngredient.copy();
		CompoundTag compoundtag = containerIn.getItem(0).getTag();
		
		if (compoundtag != null) {
			itemstack.setTag(compoundtag.copy());
		}

		return itemstack;
	}

	@Override
	public boolean canCraftInDimensions(int xIn, int yIn) {
		return xIn * yIn >= 2;
	}

	@Override
	public ItemStack getResultItem(RegistryAccess accessIn) {
		return resultIngredient.copy();
	}

	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(ObjectManager.module_upgrade_station);
	}

	@Override
	public ResourceLocation getId() {
		return ident;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ObjectManager.recipe_serializer_upgrade_station;
	}

	@Override
	public RecipeType<?> getType() {
		return ObjectManager.recipe_type_upgrade_station;
	}
	
	@Override
	public boolean isIncomplete() {
		return Stream.of(this.focusIngredient, this.topIngredients[0], this.topIngredients[1], this.topIngredients[2], this.middleIngredients[0], this.middleIngredients[1], this.bottomIngredients[0], this.bottomIngredients[1], this.bottomIngredients[2]).anyMatch((p_151284_) -> {
			return p_151284_.getItems().length == 0;
		});
	}
	
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return NonNullList.of(this.focusIngredient, this.topIngredients[0], this.topIngredients[1], this.topIngredients[2], this.middleIngredients[0], this.middleIngredients[1], this.bottomIngredients[0], this.bottomIngredients[1], this.bottomIngredients[2]);
	}
	
	public List<Ingredient> getIngredientList() {
		return List.of(this.focusIngredient, this.topIngredients[0], this.topIngredients[1], this.topIngredients[2], this.middleIngredients[0], this.middleIngredients[1], this.bottomIngredients[0], this.bottomIngredients[1], this.bottomIngredients[2]);
	}
	
	@Override
	public boolean isSpecial() {
		return true;
	}
	
	public static class Type implements RecipeType<UpgradeStationRecipe> {
		public static final Type INSTANCE = new Type();
		public static final String ID = "upgrading";
		
		private Type() { }
	}

	public static class Serializer implements RecipeSerializer<UpgradeStationRecipe> {
		public static final Serializer INSTANCE = new Serializer();
		public static final ResourceLocation ID = new ResourceLocation(DimensionalPockets.MOD_ID, "upgrading");
				
		@Override
		public UpgradeStationRecipe fromJson(ResourceLocation idIn, JsonObject recipeIn) {
			Ingredient focus = Ingredient.fromJson(GsonHelper.getAsJsonObject(recipeIn, "focus"));

			JsonObject topRowObj = GsonHelper.getAsJsonObject(recipeIn, "topRow");
			Ingredient[] topRow = new Ingredient[] {
				topRowObj.has("left") ? Ingredient.fromJson(GsonHelper.getAsJsonObject(topRowObj, "left")) : Ingredient.EMPTY,
				topRowObj.has("middle") ? Ingredient.fromJson(GsonHelper.getAsJsonObject(topRowObj, "middle")) : Ingredient.EMPTY,
				topRowObj.has("right") ? Ingredient.fromJson(GsonHelper.getAsJsonObject(topRowObj, "right")) : Ingredient.EMPTY
			};

			JsonObject middleRowObj = GsonHelper.getAsJsonObject(recipeIn, "middleRow");
			Ingredient[] middleRow = new Ingredient[] {
				middleRowObj.has("left") ? Ingredient.fromJson(GsonHelper.getAsJsonObject(middleRowObj, "left")) : Ingredient.EMPTY,
				middleRowObj.has("right") ? Ingredient.fromJson(GsonHelper.getAsJsonObject(middleRowObj, "right")) : Ingredient.EMPTY
			};
			
			JsonObject bottomRowObj = GsonHelper.getAsJsonObject(recipeIn, "bottomRow");
			Ingredient[] bottomRow = new Ingredient[] {
				bottomRowObj.has("left") ? Ingredient.fromJson(GsonHelper.getAsJsonObject(bottomRowObj, "left")) : Ingredient.EMPTY,
				bottomRowObj.has("middle") ? Ingredient.fromJson(GsonHelper.getAsJsonObject(bottomRowObj, "middle")) : Ingredient.EMPTY,
				bottomRowObj.has("right") ? Ingredient.fromJson(GsonHelper.getAsJsonObject(bottomRowObj, "right")) : Ingredient.EMPTY
			};
			
			ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(recipeIn, "result"));
			
			return new UpgradeStationRecipe(idIn, focus, topRow, middleRow, bottomRow, result);
		}

		@Override
		public @Nullable UpgradeStationRecipe fromNetwork(ResourceLocation idIn, FriendlyByteBuf extraDataIn) {			
			Ingredient focus = Ingredient.fromNetwork(extraDataIn);
			
			Ingredient[] topRow = new Ingredient[] { null, null, null };
			for (int i = 0; i < 3; i++) {
				topRow[i] = Ingredient.fromNetwork(extraDataIn);
			}

			Ingredient[] middleRow = new Ingredient[] { null, null };
			for (int i = 0; i < 2; i++) {
				middleRow[i] = Ingredient.fromNetwork(extraDataIn);
			}
			
			Ingredient[] bottomRow = new Ingredient[] { null, null, null };
			for (int i = 0; i < 3; i++) {
				bottomRow[i] = Ingredient.fromNetwork(extraDataIn);
			}
			
			ItemStack result = extraDataIn.readItem();
			
			return new UpgradeStationRecipe(idIn, focus, topRow, middleRow, bottomRow, result);
		}

		@Override
		public void toNetwork(FriendlyByteBuf extraDataIn, UpgradeStationRecipe recipeIn) {
			recipeIn.focusIngredient.toNetwork(extraDataIn);
			
			for (int i = 0; i < 3; i ++) {
				recipeIn.topIngredients[i].toNetwork(extraDataIn);
			}

			for (int i = 0; i < 2; i ++) {
				recipeIn.middleIngredients[i].toNetwork(extraDataIn);
			}
			
			for (int i = 0; i < 3; i ++) {
				recipeIn.bottomIngredients[i].toNetwork(extraDataIn);
			}
			
			extraDataIn.writeItem(recipeIn.resultIngredient);
		}
	}
}
