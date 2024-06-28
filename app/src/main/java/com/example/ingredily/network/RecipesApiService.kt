package com.example.ingredily.network

import retrofit2.http.GET
import retrofit2.http.Query

interface RecipesApiService {
    @GET("recipes/findByIngredients")
    suspend fun getRecipesByIngredients(
        @Query("apiKey") apiKey: String,
        @Query("ingredients") ingredients: String,
    ): List<IngredientSearchRecipe>
}