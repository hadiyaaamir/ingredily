package com.example.ingredily.network

import kotlinx.serialization.Serializable

@Serializable
data class RecipeIngredient(
    val id: Int,
    val amount: Double,
    val unit: String,
    val aisle: String,
    val name: String,
    val nameClean: String? = null,
    val original: String,
    val originalName: String,
    val image: String,
)