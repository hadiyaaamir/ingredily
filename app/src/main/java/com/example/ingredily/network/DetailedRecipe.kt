package com.example.ingredily.network

import kotlinx.serialization.Serializable

@Serializable
data class DetailedRecipe(
    val id: Int = 0,
    val title: String = "",
    val summary: String = "",
    val image: String? = null,
    val aggregateLikes: Int = 0,
    val servings: Int = 0,
    val readyInMinutes: Int = 0,
    val sourceUrl: String = "",
    val sourceName:String = "",
    val creditsText:String = "",
    val extendedIngredients: List<RecipeIngredient> = listOf(),
    val analyzedInstructions: List<AnalyzedInstruction> = listOf(),
)

@Serializable
data class AnalyzedInstruction(
    val steps: List<InstructionStep> = listOf(),
)

@Serializable
data class InstructionStep(
    val number: Int = 0,
    val step: String = ""
)
