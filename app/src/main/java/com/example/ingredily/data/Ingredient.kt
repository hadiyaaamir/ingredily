package com.example.ingredily.data


data class Ingredient (
    val name: String
) {
    override fun toString(): String {
        return name
    }
}

fun List<Ingredient>.toQueryString(): String {
    return this.joinToString(",") { it.name }
}