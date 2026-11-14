package com.yuo.paimeng.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
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

import javax.annotation.Nullable;

public class SynPlatRecipe implements Recipe<Container> {
    private final NonNullList<ItemStack> inputs; //输入
    private final ItemStack catalyzer; //输入
    private final ItemStack result; //输出
    private final ResourceLocation id;

    public SynPlatRecipe(ResourceLocation idIn, NonNullList<ItemStack> inputIn, ItemStack catalyzerIn, ItemStack resultIn){
        this.id = idIn;
        this.inputs = inputIn;
        this.catalyzer = catalyzerIn;
        this.result = resultIn;
    }

    public static class Serializer implements RecipeSerializer<SynPlatRecipe> {

        @Override
        public SynPlatRecipe fromJson(ResourceLocation recipeId, JsonObject json) { //从json中获取信息
            NonNullList<ItemStack> inputs = NonNullList.create();
            for (JsonElement inputJson : GsonHelper.getAsJsonArray(json, "inputs")) {
                ItemStack stack = ShapedRecipe.itemStackFromJson(GsonHelper.convertToJsonObject(inputJson, "input"));
                if (!stack.isEmpty()) inputs.add(stack);
            }
            ItemStack catalyzer = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "catalyzer"));
            ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
            if (inputs.isEmpty() || inputs.size() > 4){
                throw new JsonSyntaxException("Recipe is has Error");
            }
            return new SynPlatRecipe(recipeId, inputs, catalyzer, result);
        }

        @Nullable
        @Override
        public SynPlatRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            int i = buffer.readVarInt();
            NonNullList<ItemStack> list = NonNullList.create();
            for(int j = 0; j < i; ++j) {
                list.add(j, buffer.readItem());
            }
            ItemStack catalyzer = buffer.readItem();
            ItemStack result = buffer.readItem();
            return new SynPlatRecipe(recipeId, list, catalyzer, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, SynPlatRecipe recipe) {
            buffer.writeVarInt(recipe.inputs.size());
            for(ItemStack stack : recipe.inputs) {
                buffer.writeItem(stack);
            }
            buffer.writeItem(recipe.catalyzer);
            buffer.writeItem(recipe.result);
        }
    }

    @Override
    public boolean matches(Container inv, Level worldIn) {
        //合成台：0~2 为原料，3 为催化剂
        int size = RecipeUtils.match(inv.getContainerSize() - 1, inv, inputs);

        boolean flag = false;
        ItemStack stack = inv.getItem(inv.getContainerSize() - 1);
        if (!stack.isEmpty()){
            if (catalyzer.getItem() == stack.getItem() && stack.getCount() >= catalyzer.getCount()){
                flag = true;
            }
        }

        return size == this.inputs.size() && flag;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        for (int i = 0; i < 3; i++){
            if (inputs.size() >= i + 1){
                list.add(Ingredient.of(inputs.get(i)));
            }else list.add(Ingredient.of(ItemStack.EMPTY));
        }
        list.add(Ingredient.of(catalyzer));
        return list;
    }

    public NonNullList<ItemStack> getInputs(){
        NonNullList<ItemStack> list = NonNullList.create();
        for (int i = 0; i < 3; i++){
            if (inputs.size() >= i + 1){
                list.add(inputs.get(i));
            }else list.add(ItemStack.EMPTY);
        }
        list.add(catalyzer);
        return list;
    }

    public ItemStack getCatalyzer(){
        return this.catalyzer;
    }

    /**
     * 1.20.1 标准产出方法
     */
    @Override
    public ItemStack assemble(Container inv, RegistryAccess registryAccess) {
        return this.result.copy();
    }

    /**
     * 配方产物（副本，避免外部修改共享栈）
     */
    public ItemStack getResult() {
        return this.result.copy();
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return PMRecipeSerializers.SYN_PLAT.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeType.SYN_PLAT;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }
}
