package com.example.ingredily.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ingredily.network.IngredientSearchRecipe
import com.example.ingredily.ui.theme.IngredilyTheme

@Composable
fun RecipesScreen(
    viewModel: RecipesViewModel,
    modifier: Modifier = Modifier,
    backToHome: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    when (val searchedRecipesDataState = uiState.searchedRecipesDataState) {
        is SearchedRecipesDataState.Initial -> InitialScreen(modifier = Modifier.fillMaxSize())
        is SearchedRecipesDataState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())

        is SearchedRecipesDataState.Success -> RecipesSuccessScreen(
            recipes = searchedRecipesDataState.recipes,
            viewModel = viewModel,
            backToHome = backToHome,
            modifier = modifier,
        )

        is SearchedRecipesDataState.Error -> ErrorScreen(
            onTryAgain = { viewModel.getRecipesByIngredients() },
            modifier = Modifier.fillMaxSize()
        )
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun RecipesSuccessScreen(
    recipes: List<IngredientSearchRecipe>,
    viewModel: RecipesViewModel,
    backToHome: () -> Unit,
    modifier: Modifier = Modifier
) {

    val navigator = rememberListDetailPaneScaffoldNavigator<IngredientSearchRecipe>()

    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }

    Box(modifier = modifier) {
        ListDetailPaneScaffold(
            directive = navigator.scaffoldDirective,
            value = navigator.scaffoldValue,
            listPane = {
                AnimatedPane {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Spacer(modifier = Modifier.size(48.dp))
                        Text(
                            text = "Recipes for You",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            text = "Handpicked recipes using what you have",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.secondary
                            )
                        )
                        Spacer(modifier = Modifier.size(28.dp))
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                            items(items = recipes) { recipe ->
                                RecipeCard(
                                    recipe = recipe,
                                    onClicked = {
                                        viewModel.getRecipeDetails(recipe.id)
                                        navigator.navigateTo(
                                            ListDetailPaneScaffoldRole.Detail,
                                            recipe
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            },
            detailPane = {
                AnimatedPane {
                    navigator.currentDestination?.content?.let {
                        RecipeDetailScreen(
                            recipe = it,
                            viewModel = viewModel,
                            modifier = modifier
                        )
                    }
                }

            },
        )
        CustomBackArrow(
            onBackButtonClicked = {
                if (navigator.canNavigateBack()) {
                    navigator.navigateBack()
                } else {
                    backToHome()
                }
            },
        )

    }
}


@Composable
fun RecipeCard(
    recipe: IngredientSearchRecipe,
    onClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier
        .fillMaxWidth()
        .clickable { onClicked() }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(recipe.image)
                .crossfade(true)
                .build(),
            contentDescription = recipe.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .border(
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    shape = RoundedCornerShape(16.dp)
                )
                .clip(RoundedCornerShape(16.dp))
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            Text(
                recipe.title, style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(3.5f),
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                IconTextRow(
                    icon = Icons.Outlined.FavoriteBorder,
                    iconDescription = "like icon",
                    iconColor = MaterialTheme.colorScheme.outline,
                    iconSize = 16.dp,
                    text = "${recipe.likes} likes",
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.secondary
                    ),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        IconTextRow(
            icon = Icons.Outlined.Done,
            iconDescription = "tick icon",
            iconColor = Color(0xff119c6e),
            text = "You have ${recipe.usedIngredientCount} ingredients"
        )
        IconTextRow(
            icon = Icons.Outlined.Clear,
            iconDescription = "cross icon",
            iconColor = Color(0xffe34840),
            text = "You need ${recipe.missedIngredientCount} ingredients",
        )
    }
}

@Composable
fun IconTextRow(
    icon: ImageVector,
    iconDescription: String,
    iconColor: Color = MaterialTheme.colorScheme.outline,
    text: String,
    modifier: Modifier = Modifier,
    iconSize: Dp = 12.dp,
    textStyle: TextStyle? = null,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = iconDescription,
            modifier = Modifier.size(iconSize),
            tint = iconColor
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text,
            style = textStyle ?: MaterialTheme.typography.bodySmall
                .copy(color = MaterialTheme.colorScheme.secondary)
        )
    }
}

@Composable
fun CustomBackArrow(
    onBackButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onBackButtonClicked,
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .background(MaterialTheme.colorScheme.surface, shape = CircleShape),
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "back navigation button"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RecipesSuccessScreenPreview() {
    val fakeRecipe = IngredientSearchRecipe(
        id = 1,
        title = "Example Recipe Long fxfdx fgfdffff dsdsvf ",
        image = "https://eatitandlikeit.com/wp-content/uploads/2023/02/IMG-0420-1024x1022-1.jpg",
        likes = 4,
        missedIngredientCount = 2,
        usedIngredientCount = 4,
        unusedIngredients = listOf(),
        usedIngredients = listOf(),
        missedIngredients = listOf(),
    )
    IngredilyTheme {
        Column {
            Spacer(modifier = Modifier.size(48.dp))
            Text(
                text = "Recipes for You",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = "Handpicked recipes using what you have",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Normal,
                )
            )
            Spacer(modifier = Modifier.size(28.dp))
        }
//        RecipesSuccessScreen(
//            viewModel = viewModel(),
//            recipes = listOf(
//                fakeRecipe,
//                fakeRecipe,
//                fakeRecipe
//            ),
//            backToHome = {}
//        )
    }
}