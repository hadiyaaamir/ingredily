package com.example.ingredily.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ingredily.data.Ingredient
import com.example.ingredily.ui.theme.IngredilyTheme

@Composable
fun IngredientsScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onNextButtonClicked: (List<Ingredient>) -> Unit,
    viewModel: IngredientsViewModel,
) {

    val uiState by viewModel.uiState.collectAsState()

    when (val ingredientsDataState = uiState.ingredientsDataState) {
        is IngredientsDataState.Initial -> InitialScreen(modifier = Modifier.fillMaxSize())
        is IngredientsDataState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())

        is IngredientsDataState.Success -> IngredientSuccessScreen(
            isIngredientSelected = { ingredient -> uiState.isSelected(ingredient) },
            onSelectionToggled = { ingredient -> viewModel.toggleSelection(ingredient) },
            ingredients = ingredientsDataState.ingredients,
            selectedIngredients = uiState.selectedIngredients,
            onNextButtonClicked = { onNextButtonClicked(uiState.selectedIngredients) },
            onClearAllClicked = { viewModel.clearAllSelection() },
            nextButtonEnabled = uiState.selectedIngredients.isNotEmpty(),
            modifier = modifier.padding(top = contentPadding.calculateTopPadding()),
        )

        is IngredientsDataState.Error -> ErrorScreen(modifier = Modifier.fillMaxSize())
    }
}

@Composable
fun IngredientSuccessScreen(
    isIngredientSelected: (Ingredient) -> Boolean,
    onSelectionToggled: (Ingredient) -> Unit,
    ingredients: List<Ingredient>,
    selectedIngredients: List<Ingredient>,
    onNextButtonClicked: () -> Unit,
    onClearAllClicked: () -> Unit,
    nextButtonEnabled: Boolean,
    modifier: Modifier = Modifier
) {

    val sortedIngredients = ingredients.sortedByDescending { isIngredientSelected(it) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Spacer(modifier = Modifier.size(16.dp))

        Text(
            text = "What ingredients do you have today?",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.SemiBold
            )
        )

        if (selectedIngredients.isNotEmpty()) {
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = "Clear All",
                style = MaterialTheme.typography.labelLarge.copy(
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .clickable { onClearAllClicked() }
                    .align(Alignment.End)
                    .padding(bottom = 4.dp, end = 4.dp)
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(if (selectedIngredients.size > 5) 2 else 1),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.heightIn(max = 200.dp)
        ) {
            items(items = selectedIngredients, key = { it.id }) { ingredient ->
                IconTextRow(
                    icon = Icons.Outlined.Done,
                    iconColor = Color(0xff119c6e),
                    iconSize = 16.dp,
                    iconDescription = "tick icon",
                    text = ingredient.name,
                    textStyle = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.size(20.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(items = sortedIngredients) { ingredient ->
                SelectableIngredientCard(
                    ingredient = ingredient,
                    isChecked = isIngredientSelected(ingredient),
                    onCheckToggled = { onSelectionToggled(ingredient) },
                )
            }
        }
        Spacer(modifier = Modifier.size(28.dp))
        Button(
            onClick = onNextButtonClicked,
            enabled = nextButtonEnabled,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .height(48.dp),
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
            .clickable
            { onCheckToggled() }
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
    Box(modifier = modifier)
}

@Composable
fun LoadingScreen(modifier: Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(32.dp)
        )
    }
}

@Composable
fun ErrorScreen(modifier: Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Something Went Wrong")
    }
}

@Preview(showBackground = true)
@Composable
fun IngredientSuccessScreenPreview() {
    IngredilyTheme {
        IngredientSuccessScreen(
            isIngredientSelected = { false },
            onSelectionToggled = {},
            onNextButtonClicked = {},
            onClearAllClicked = {},
            ingredients = listOf(
                Ingredient(name = "test1", id = 1),
                Ingredient(name = "test2", id = 2),
                Ingredient(name = "test3", id = 3),
                Ingredient(name = "test4", id = 4),
            ),
            selectedIngredients = listOf(
                Ingredient(name = "test3", id = 3),
                Ingredient(name = "test1", id = 1),
                Ingredient(name = "test2", id = 2),
                Ingredient(name = "test3", id = 3),
                Ingredient(name = "test4", id = 4),
            ),
            nextButtonEnabled = true,
        )
    }
}
