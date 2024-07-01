package com.example.ingredily.network

import kotlinx.serialization.Serializable

@Serializable
data class RecipeIngredient(
    val id: Int = 0,
    val amount: Double = 0.0,
    val unit: String = "",
    val aisle: String = "",
    val name: String = "",
    val nameClean: String? = null,
    val original: String = "",
    val originalName: String = "",
    val image: String = "",
)