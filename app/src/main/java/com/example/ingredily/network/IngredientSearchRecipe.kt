package com.example.ingredily.network

import kotlinx.serialization.Serializable

@Serializable
data class IngredientSearchRecipe(
    val id: Int,
    val image: String,
    val title: String,
)
