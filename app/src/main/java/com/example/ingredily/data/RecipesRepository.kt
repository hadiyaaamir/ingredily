package com.example.ingredily.data


interface RecipesRepository {
    fun getIngredients(): List<Ingredient>
    suspend fun getRecipes(): List<String>
}

class RecipesRepositoryImpl() : RecipesRepository {

    override fun getIngredients(): List<Ingredient> {
        return listOf<Ingredient>(
            Ingredient(name = "apple"),
            Ingredient("banana"),
            Ingredient("green chilli"),
            Ingredient("cucumber"),
            Ingredient("fish"),
            Ingredient("chicken"),
            Ingredient("beef"),
            Ingredient("meat"),
            Ingredient("egg"),
            Ingredient("onion"),
            Ingredient("capsicum"),
            Ingredient("mushroom"),
            Ingredient("corn"),
            Ingredient("cucumber"),
            Ingredient("flour"),
            Ingredient("yeast"),
            Ingredient("oregano"),
            Ingredient("cinnamon"),
            Ingredient("red chilli flakes"),
            Ingredient("orange"),
            Ingredient("coriander"),
            Ingredient("mint"),
            Ingredient("lemon"),
            Ingredient("cabbage"),
            Ingredient("lettuce")
        )
    }

    override suspend fun getRecipes(): List<String> {
        TODO("Not yet implemented")
    }

}
