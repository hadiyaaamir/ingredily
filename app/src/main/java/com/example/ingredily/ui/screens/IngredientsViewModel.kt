package com.example.ingredily.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.ingredily.RecipesApplication
import com.example.ingredily.data.Ingredient
import com.example.ingredily.data.RecipesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


sealed interface IngredientsDataState {
    object Initial : IngredientsDataState
    data class Success(val ingredients: List<Ingredient>) : IngredientsDataState
    object Error : IngredientsDataState
    object Loading : IngredientsDataState
}

data class IngredientsUiState(
    val ingredientsDataState: IngredientsDataState = IngredientsDataState.Initial,
    val selectedIngredients: List<Ingredient> = listOf(),
    val allIngredients: List<Ingredient> = listOf(),
    val searchText: String = "",
) {
    fun isSelected(ingredient: Ingredient): Boolean {
        return selectedIngredients.contains(ingredient)
    }
}

class IngredientsViewModel(private val recipesRepository: RecipesRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(IngredientsUiState())
    val uiState: StateFlow<IngredientsUiState>
        get() = _uiState.asStateFlow()

    init {
        getIngredients()
    }

    fun getIngredients() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(ingredientsDataState = IngredientsDataState.Loading)
            }
            try {
                val listResult = recipesRepository.getIngredients()
                _uiState.update { currentState ->
                    currentState.copy(
                        ingredientsDataState = IngredientsDataState.Success(listResult),
                        allIngredients = listResult,
                    )
                }
            } catch (e: Exception) {
                _uiState.update { currentState ->
                    currentState.copy(ingredientsDataState = IngredientsDataState.Error)
                }
            }
        }
    }

     fun searchIngredients() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(ingredientsDataState = IngredientsDataState.Loading)
            }
            try {
                val listResult = recipesRepository.searchIngredients(uiState.value.searchText)
                _uiState.update { currentState ->
                    currentState.copy(
                        ingredientsDataState = IngredientsDataState.Success(listResult)
                    )
                }
            } catch (e: Exception) {
                _uiState.update { currentState ->
                    currentState.copy(ingredientsDataState = IngredientsDataState.Error)
                }
            }
        }
    }

     fun clearIngredientSearch() {
        _uiState.update { currentState ->
            currentState.copy(
                searchText = "",
                ingredientsDataState = IngredientsDataState.Success(uiState.value.allIngredients)
            )
        }
    }

    fun updateSearchText(text: String) {
        _uiState.update { currentState ->
            currentState.copy(searchText = text)
        }
    }

    fun toggleSelection(ingredient: Ingredient) {
        _uiState.update { currentState ->
            val updatedList = currentState.selectedIngredients.toMutableList()
            if (!_uiState.value.isSelected(ingredient)) {
                updatedList.add(ingredient)
            } else {
                updatedList.remove(ingredient)
            }
            currentState.copy(selectedIngredients = updatedList)
        }
    }

    fun clearAllSelection() {
        _uiState.update { currentState ->
            currentState.copy(selectedIngredients = listOf())
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
                IngredientsViewModel(recipesRepository)
            }
        }
    }
}