package com.example.ingredily.data



interface RecipesRepository {
    fun getIngredients(): List<String>
    suspend fun getRecipes(): List<String>
}

class RecipesRepositoryImpl() : RecipesRepository {

    override fun getIngredients(): List<String> {
        return listOf(
            "apple",
            "banana",
            "chilli",
            "cucumber",
            "fish",
            "chicken",
            "beef",
            "meat",
            "egg",
            "onion",
            "capsicum",
            "mushroom"
        )
    }

    override suspend fun getRecipes(): List<String> {
        TODO("Not yet implemented")
    }

}
