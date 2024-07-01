package com.example.ingredily.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipesApiService {
    @GET("recipes/findByIngredients")
    suspend fun getRecipesByIngredients(
        @Query("apiKey") apiKey: String,
        @Query("ingredients") ingredients: String,
        @Query("number") number: Int,
    ): List<IngredientSearchRecipe>

    @GET("recipes/{id}/information")
    suspend fun getRecipeDetail(
        @Path("id") recipeId: Int,
        @Query("apiKey") apiKey: String,
    ) : DetailedRecipe

    @GET("food/ingredients/search")
    suspend fun searchIngredients(
        @Query("apiKey") apiKey: String,
        @Query("query") query: String,
        @Query("number") number: Int,
    ) : IngredientSearch
}