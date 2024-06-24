package com.example.ingredily.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ingredily.data.Ingredient
import com.example.ingredily.ui.theme.IngredilyTheme

@Composable
fun IngredientsScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    val ingredientsViewModel: IngredientsViewModel = viewModel(
        factory = IngredientsViewModel.Factory
    )
    val ingredientsUiState by ingredientsViewModel.uiState.collectAsState()

    when (val ingredientsDataState = ingredientsUiState.ingredientsDataState) {
        is IngredientsDataState.Initial -> InitialScreen(modifier = Modifier.fillMaxSize())
        is IngredientsDataState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())

        is IngredientsDataState.Success -> IngredientSuccessScreen(
            isIngredientSelected = { ingredient -> ingredientsUiState.isSelected(ingredient) },
            onSelectionToggled = { ingredient -> ingredientsViewModel.toggleSelection(ingredient) },
            ingredients = ingredientsDataState.ingredients,
            modifier = modifier.padding(top = contentPadding.calculateTopPadding())
        )

        is IngredientsDataState.Error -> ErrorScreen(modifier = Modifier.fillMaxSize())
    }


}

@Composable
fun IngredientSuccessScreen(
    isIngredientSelected: (Ingredient) -> Boolean,
    onSelectionToggled: (Ingredient) -> Unit,
    ingredients: List<Ingredient>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = "What ingredients do you have today?",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.size(28.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(items = ingredients) { ingredient ->
                SelectableIngredientCard(
                    ingredient = ingredient,
                    isChecked = isIngredientSelected(ingredient),
                    onCheckToggled = { onSelectionToggled(ingredient) },
                )
            }
        }
        Spacer(modifier = Modifier.size(28.dp))
        Button(
            onClick = { },
            modifier = Modifier.align(Alignment.CenterHorizontally).fillMaxWidth().height(48.dp),

        ) {
            Text(text = "Get Recipes")
        }
    }
}

@Composable
fun SelectableIngredientCard(
    ingredient: Ingredient,
    isChecked: Boolean,
    onCheckToggled: () -> Unit,
    modifier: Modifier = Modifier
) {


    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onCheckToggled() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { onCheckToggled() },
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = ingredient.name,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun InitialScreen(modifier: Modifier) {
    Text(text = "initial")
}


@Composable
fun LoadingScreen(modifier: Modifier) {
    Text(text = "loading")
}

@Composable
fun ErrorScreen(modifier: Modifier) {
    Text(text = "error")
}


@Preview(showBackground = true)
@Composable
fun IngredientSuccessScreenPreview() {
    IngredilyTheme {
        IngredientSuccessScreen(
            isIngredientSelected = { false },
            onSelectionToggled = {},
            ingredients = listOf<Ingredient>(
                Ingredient(name = "test1"),
                Ingredient(name = "test2"),
                Ingredient(name = "test3"),
                Ingredient(name = "test4"),
            )
        )
    }
}
