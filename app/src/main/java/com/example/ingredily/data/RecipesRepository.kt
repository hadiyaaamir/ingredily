package com.example.ingredily.data

import android.content.Context
import com.example.ingredily.network.DetailedRecipe
import com.example.ingredily.network.IngredientSearchRecipe
import com.example.ingredily.network.RecipesApiService
import com.opencsv.CSVReader
import java.io.InputStreamReader


interface RecipesRepository {
    fun getIngredients(): List<Ingredient>
    suspend fun getRecipesByIngredients(
        ingredients: List<Ingredient>,
        number: Int = 20,
    ): List<IngredientSearchRecipe>

    suspend fun getRecipeDetail(id: Int): DetailedRecipe
}

class RecipesRepositoryImpl(
    private val recipesApiService: RecipesApiService,
    private val context: Context,
) : RecipesRepository {

    override fun getIngredients(): List<Ingredient> {
        val ingredients = mutableListOf<Ingredient>()

        val inputStream = context.assets.open("top-1k-ingredients.csv")
        val reader = CSVReader(InputStreamReader(inputStream))

        reader.use {
            var line: Array<String>?
            while (reader.readNext().also { line = it } != null) {
                if (line != null && line!!.isNotEmpty()) {
                    val ingredient = line!![0].split(';')
                    ingredients.add(Ingredient(ingredient[0], ingredient[1].toIntOrNull() ?: 0))
                }
            }
        }

        return ingredients
    }

    override suspend fun getRecipesByIngredients(
        ingredients: List<Ingredient>,
        number: Int,
    ): List<IngredientSearchRecipe> {
        return recipesApiService.getRecipesByIngredients(
            API_KEY,
            ingredients.toQueryString(),
            number
        )
    }

    override suspend fun getRecipeDetail(id: Int): DetailedRecipe {
        return recipesApiService.getRecipeDetail(id, API_KEY)
    }
}
