package com.example.ingredily.network

import com.example.ingredily.data.Ingredient
import kotlinx.serialization.Serializable

@Serializable
data class IngredientSearch(
    val results: List<Ingredient>,
    val number: Int,
    val totalResults: Int,
)
