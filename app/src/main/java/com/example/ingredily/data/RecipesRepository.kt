package com.example.ingredily.data

import com.example.ingredily.network.DetailedRecipe
import com.example.ingredily.network.IngredientSearchRecipe
import com.example.ingredily.network.RecipesApiService


interface RecipesRepository {
    fun getIngredients(): List<Ingredient>
    suspend fun getRecipesByIngredients(
        ingredients: List<Ingredient>
    ): List<IngredientSearchRecipe>

    suspend fun getRecipeDetail(id: Int): DetailedRecipe
}

class RecipesRepositoryImpl(
    private val recipesApiService: RecipesApiService
) : RecipesRepository {

    override fun getIngredients(): List<Ingredient> {
        return listOf<Ingredient>(
            Ingredient(name = "apple"),
            Ingredient("banana"),
            Ingredient("green chilli"),
            Ingredient("cucumber"),
            Ingredient("fish"),
            Ingredient("chicken"),
            Ingredient("beef"),
            Ingredient("meat"),
            Ingredient("egg"),
            Ingredient("onion"),
            Ingredient("capsicum"),
            Ingredient("mushroom"),
            Ingredient("corn"),
            Ingredient("flour"),
            Ingredient("yeast"),
            Ingredient("oregano"),
            Ingredient("cinnamon"),
            Ingredient("red chilli flakes"),
            Ingredient("orange"),
            Ingredient("coriander"),
            Ingredient("mint"),
            Ingredient("lemon"),
            Ingredient("cabbage"),
            Ingredient("lettuce")
        )
    }

    override suspend fun getRecipesByIngredients(
        ingredients: List<Ingredient>
    ): List<IngredientSearchRecipe> {
        return recipesApiService.getRecipesByIngredients(API_KEY, ingredients.toQueryString())
    }

    override suspend fun getRecipeDetail(id: Int): DetailedRecipe {
        return recipesApiService.getRecipeDetail(id, API_KEY)
    }


}
