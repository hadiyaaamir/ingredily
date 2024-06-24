package com.example.ingredily.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.ingredily.RecipesApplication
import com.example.ingredily.data.RecipesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.IOException


sealed interface IngredientsDataState {
    object Initial : IngredientsDataState
    data class Success(val ingredients: List<String>) : IngredientsDataState
    object Error : IngredientsDataState
    object Loading : IngredientsDataState
}

data class IngredientsUiState(
    val ingredientsDataState: IngredientsDataState = IngredientsDataState.Initial,
)

class IngredientsViewModel(private val recipesRepository: RecipesRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(IngredientsUiState())
    val uiState: StateFlow<IngredientsUiState>
        get() = _uiState.asStateFlow()

    init {
        getIngredients()
    }

    fun getIngredients() {
        IngredientsDataState.Loading
        try {
            val listResult = recipesRepository.getIngredients()
            updateIngredientsDataState(IngredientsDataState.Success(listResult))
        } catch (e: IOException) {
            updateIngredientsDataState(IngredientsDataState.Error)
        }
    }

    private fun updateIngredientsDataState(state: IngredientsDataState) {
        _uiState.update { currentState ->
            currentState.copy(ingredientsDataState = state)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as RecipesApplication)
                val recipesRepository = application.container.recipesRepository
                IngredientsViewModel(recipesRepository)
            }
        }
    }
}