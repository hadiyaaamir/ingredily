package com.example.ingredily.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.ingredily.RecipesApplication
import com.example.ingredily.data.Ingredient
import com.example.ingredily.data.RecipesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


data class RecipesUiState(
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