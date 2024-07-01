package com.example.ingredily.network

import kotlinx.serialization.Serializable

@Serializable
data class DetailedRecipe(
    val id: Int,
    val title: String,
    val summary: String,
    val image: String? = null,
    val aggregateLikes: Int,
    val servings: Int,
    val readyInMinutes: Int,
    val sourceUrl: String,
    val sourceName:String,
    val creditsText:String,
    val extendedIngredients: List<RecipeIngredient>,
    val analyzedInstructions: List<AnalyzedInstruction>
)

@Serializable
data class AnalyzedInstruction(
    val steps: List<InstructionStep>
)

@Serializable
data class InstructionStep(
    val number: Int,
    val step: String
)
