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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
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
            onSearchSubmit = { text -> viewModel.searchIngredients(text) },
            onSearchCleared = { viewModel.clearIngredientSearch() },
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
    onSearchCleared: () -> Unit,
    onSearchSubmit: (searchText: String) -> Unit,
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

        Spacer(modifier = Modifier.size(16.dp))

        SearchBar(
            onSearchSubmit = onSearchSubmit,
            onClearSearch = onSearchCleared,
            modifier = Modifier.fillMaxWidth()
        )

        if (selectedIngredients.isNotEmpty()) {
            Spacer(modifier = Modifier.size(16.dp))
            ClearAllButton(modifier = Modifier
                .clickable { onClearAllClicked() }
                .align(Alignment.End)
                .padding(bottom = 4.dp, end = 4.dp)
            )
        }

        SelectedIngredientList(ingredients = selectedIngredients)

        Spacer(modifier = Modifier.size(20.dp))

        AllIngredientsList(
            ingredients = sortedIngredients,
            isIngredientSelected = isIngredientSelected,
            onSelectionToggled = onSelectionToggled,
            modifier = Modifier.weight(1f)
        )

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onSearchSubmit: (searchText: String) -> Unit,
    onClearSearch: () -> Unit,
    searchIcon: ImageVector = Icons.Default.Search,
    clearIcon: ImageVector = Icons.Default.Clear,
) {
    var text by remember { mutableStateOf(TextFieldValue()) }

    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            placeholder = { Text(text = "Search Ingredients...") },
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = { if(text.text.isNotEmpty()) onSearchSubmit(text.text) }
            ),
            leadingIcon = {
                Icon(
                    imageVector = searchIcon,
                    contentDescription = "Search",
                )
            },
            trailingIcon = {
                if (text.text.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            text = TextFieldValue("")
                            onClearSearch()
                        }
                    ) {
                        Icon(
                            imageVector = clearIcon,
                            contentDescription = "Clear",
                            tint = Color.Gray
                        )
                    }
                }
            },
            modifier = Modifier.weight(4f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = {  if(text.text.isNotEmpty())  onSearchSubmit(text.text) },
            modifier = Modifier
                .height(52.dp)
                .weight(1f),
            shape =  RoundedCornerShape(16.dp),
        ) {
            Text(text = "Go")
        }
    }
}


@Composable
fun ClearAllButton(
    modifier: Modifier = Modifier
) {
    Text(
        text = "Clear All",
        style = MaterialTheme.typography.labelLarge.copy(
            color = MaterialTheme.colorScheme.primary
        ),
        modifier = modifier
    )
}

@Composable
fun SelectedIngredientList(
    ingredients: List<Ingredient>,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(if (ingredients.size > 5) 2 else 1),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.heightIn(max = 200.dp)
    ) {
        items(items = ingredients, key = { it.id }) { ingredient ->
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

}

@Composable
fun AllIngredientsList(
    ingredients: List<Ingredient>,
    onSelectionToggled: (Ingredient) -> Unit,
    isIngredientSelected: (Ingredient) -> Boolean,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        items(items = ingredients) { ingredient ->
            SelectableIngredientCard(
                ingredient = ingredient,
                isChecked = isIngredientSelected(ingredient),
                onCheckToggled = { onSelectionToggled(ingredient) },
            )
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
        SearchBar(
            onSearchSubmit = {},
            onClearSearch = {},
            modifier = Modifier.fillMaxWidth()
        )
//        IngredientSuccessScreen(
//            isIngredientSelected = { false },
//            onSelectionToggled = {},
//            onNextButtonClicked = {},
//            onClearAllClicked = {},
//            ingredients = listOf(
//                Ingredient(name = "test1", id = 1),
//                Ingredient(name = "test2", id = 2),
//                Ingredient(name = "test3", id = 3),
//                Ingredient(name = "test4", id = 4),
//            ),
//            selectedIngredients = listOf(
//                Ingredient(name = "test3", id = 3),
//                Ingredient(name = "test1", id = 1),
//                Ingredient(name = "test2", id = 2),
//                Ingredient(name = "test3", id = 3),
//                Ingredient(name = "test4", id = 4),
//            ),
//            nextButtonEnabled = true,
//        )
    }
}
