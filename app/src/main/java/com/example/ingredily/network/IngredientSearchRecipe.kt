package com.example.ingredily.network

import kotlinx.serialization.Serializable

@Serializable

data class IngredientSearchRecipe(
    val id: Int = 0,
    val image: String = "",
    val title: String = "",
    val usedIngredientCount: Int = 0,
    val missedIngredientCount: Int = 0,
    val likes: Int = 0,
    val usedIngredients: List<RecipeIngredient> = listOf(),
    val missedIngredients: List<RecipeIngredient> = listOf(),
    val unusedIngredients: List<RecipeIngredient> = listOf(),
)
