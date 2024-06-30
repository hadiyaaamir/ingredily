package com.example.ingredily.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.ingredily.RecipesApplication
import com.example.ingredily.data.Ingredient
import com.example.ingredily.data.RecipesRepository
import com.example.ingredily.network.DetailedRecipe
import com.example.ingredily.network.IngredientSearchRecipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface SearchedRecipesDataState {
    object Initial : SearchedRecipesDataState
    data class Success(val recipes: List<IngredientSearchRecipe>) : SearchedRecipesDataState
    object Error : SearchedRecipesDataState
    object Loading : SearchedRecipesDataState
}

sealed interface RecipeDetailDataState {
    object Initial : RecipeDetailDataState
    data class Success(val recipe: DetailedRecipe) : RecipeDetailDataState
    object Error : RecipeDetailDataState
    object Loading : RecipeDetailDataState
}

data class RecipesUiState(
    val searchedRecipesDataState: SearchedRecipesDataState = SearchedRecipesDataState.Initial,
    val recipeDetailDataState: RecipeDetailDataState = RecipeDetailDataState.Initial,
    val ingredientsFilter: List<Ingredient> = listOf()
)

class RecipesViewModel(private val recipesRepository: RecipesRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(RecipesUiState())
    val uiState: StateFlow<RecipesUiState>
        get() = _uiState.asStateFlow()

    fun initialiseIngredientsFilter(ingredients: List<Ingredient>) {
        _uiState.update { currentState ->
            currentState.copy(ingredientsFilter = ingredients)
        }
    }

    fun getRecipesByIngredients() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(searchedRecipesDataState = SearchedRecipesDataState.Loading)
            }
            try {
                val listResult = recipesRepository.getRecipesByIngredients(
                    uiState.value.ingredientsFilter
                )
                _uiState.update { currentState ->
                    currentState.copy(
                        searchedRecipesDataState = SearchedRecipesDataState.Success(listResult)
                    )
                }
            } catch (e: IOException) {
                _uiState.update { currentState ->
                    currentState.copy(
                        searchedRecipesDataState = SearchedRecipesDataState.Error
                    )
                }
            }
        }
    }

    fun getRecipeDetails(id: Int) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(recipeDetailDataState = RecipeDetailDataState.Loading)
            }
            try {
                val result = recipesRepository.getRecipeDetail(id)
                _uiState.update { currentState ->
                    currentState.copy(
                        recipeDetailDataState = RecipeDetailDataState.Success(result)
                    )
                }
            } catch (e: IOException) {
                _uiState.update { currentState ->
                    currentState.copy(recipeDetailDataState = RecipeDetailDataState.Error)
                }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (
                        this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                                as RecipesApplication
                        )
                val recipesRepository = application.container.recipesRepository
                RecipesViewModel(recipesRepository)
            }
        }
    }
}