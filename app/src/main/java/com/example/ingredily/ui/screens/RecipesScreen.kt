package com.example.ingredily.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ingredily.network.IngredientSearchRecipe

@Composable
fun RecipesScreen(
    viewModel: RecipesViewModel,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsState()

    when (val searchedRecipesDataState = uiState.searchedRecipesDataState) {
        is SearchedRecipesDataState.Initial -> InitialScreen(modifier = Modifier.fillMaxSize())
        is SearchedRecipesDataState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())

        is SearchedRecipesDataState.Success -> RecipesSuccessScreen(
            recipes = searchedRecipesDataState.recipes,
            modifier = modifier,
        )

        is SearchedRecipesDataState.Error -> ErrorScreen(modifier = Modifier.fillMaxSize())
    }
}

@Composable
fun RecipesSuccessScreen(
    recipes: List<IngredientSearchRecipe>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(items = recipes) {
                recipe ->
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(recipe.image)
                    .crossfade(true)
                    .build(),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
            )
            Text(recipe.title.toString())

        }

    }
}
