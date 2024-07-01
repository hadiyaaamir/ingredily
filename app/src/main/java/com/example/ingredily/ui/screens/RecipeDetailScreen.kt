package com.example.ingredily.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LocalDining
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ingredily.network.AnalyzedInstruction
import com.example.ingredily.network.DetailedRecipe
import com.example.ingredily.network.IngredientSearchRecipe
import com.example.ingredily.network.InstructionStep
import com.example.ingredily.network.RecipeIngredient
import com.example.ingredily.ui.theme.IngredilyTheme

@Composable
fun RecipeDetailScreen(
    recipe: IngredientSearchRecipe,
    viewModel: RecipesViewModel,
    modifier: Modifier = Modifier
) {

    val uiState by viewModel.uiState.collectAsState()

    when (val recipeDetailDataState = uiState.recipeDetailDataState) {
        is RecipeDetailDataState.Initial -> InitialScreen(modifier = Modifier.fillMaxSize())
        is RecipeDetailDataState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())

        is RecipeDetailDataState.Success -> RecipeDetailSuccessScreen(
            recipe = recipeDetailDataState.recipe,
            searchRecipe = recipe,
            modifier = modifier,
        )

        is RecipeDetailDataState.Error -> ErrorScreen(modifier = Modifier.fillMaxSize())
    }

}

@Composable
fun RecipeDetailSuccessScreen(
    recipe: DetailedRecipe,
    searchRecipe: IngredientSearchRecipe,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val ctx = LocalContext.current

    Column(modifier = modifier.verticalScroll(scrollState)) {

        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(recipe.image)
                .crossfade(true)
                .build(),
            contentDescription = recipe.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .border(
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = recipe.title,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "By ${recipe.sourceName}",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.secondary
                ),
            )
            Spacer(modifier = Modifier.height(16.dp))

            Divider()
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                RecipeIconInfo(
                    icon = Icons.Outlined.LocalDining,
                    iconDescription = "person icon",
                    text = "Servings: ${recipe.servings}",
                )
                RecipeIconInfo(
                    icon = Icons.Outlined.Schedule,
                    iconDescription = "time icon",
                    text = "${recipe.readyInMinutes} min",
                )
                RecipeIconInfo(
                    icon = Icons.Outlined.FavoriteBorder,
                    iconDescription = "like icon",
                    text = "${recipe.aggregateLikes} likes",
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Divider()

            Spacer(modifier = Modifier.height(28.dp))
            Text(
                text = "Ingredients",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            recipe.extendedIngredients.forEach { ingredient ->
                val isMissedIngredient =
                    searchRecipe.missedIngredients.any { it.id == ingredient.id }
                Row(verticalAlignment = Alignment.Top) {
                    if (isMissedIngredient)
                        Icon(
                            imageVector = Icons.Outlined.Clear,
                            contentDescription = "cross icon",
                            tint = Color(0xffe34840),
                            modifier = Modifier
                                .padding(2.dp)
                                .size(16.dp)
                        )
                    else
                        Icon(
                            imageVector = Icons.Outlined.Done,
                            contentDescription = "tick icon",
                            tint = Color(0xff119c6e),
                            modifier = Modifier
                                .padding(2.dp)
                                .size(16.dp)
                        )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = ingredient.original)
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Instructions",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            recipe.analyzedInstructions.forEach { instruction ->
                instruction.steps.forEach { step ->
                    Text(text = "${step.number}. ${step.step}")
                    Spacer(modifier = Modifier.height(12.dp))
                }
                Spacer(modifier = Modifier.height(2.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Divider()
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Credits",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = recipe.creditsText,
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = recipe.sourceUrl,
                style = MaterialTheme.typography.labelMedium.copy(
                    lineHeight = 16.sp,
                    color = MaterialTheme.colorScheme.secondary,
                    textDecoration = TextDecoration.Underline,
                ),
                modifier = Modifier.clickable {
                    launchUrl(recipe.sourceUrl, ctx)
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Divider()
        }
    }
}

fun launchUrl(url: String, ctx: Context) {
    val urlIntent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse(url)
    )
    ctx.startActivity(urlIntent)
}

@Composable
fun RecipeIconInfo(
    icon: ImageVector,
    iconDescription: String,
    text: String,
    modifier: Modifier = Modifier
) {
    IconTextRow(
        icon = icon,
        iconDescription = iconDescription,
        iconSize = 20.dp,
        iconColor = MaterialTheme.colorScheme.outline,
        text = text,
        textStyle = MaterialTheme.typography.bodyMedium,
        modifier = modifier,
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RecipesDetailScreenPreview() {
    val fakeRecipe = IngredientSearchRecipe(
        id = 1,
        title = "Example Recipe",
        image = "https://eatitandlikeit.com/wp-content/uploads/2023/02/IMG-0420-1024x1022-1.jpg",
        likes = 4,
        missedIngredientCount = 2,
        usedIngredientCount = 4,
        missedIngredients = listOf(),
        usedIngredients = listOf(),
        unusedIngredients = listOf(),
    )

    val fakeIngredient = RecipeIngredient(
        id = 1,
        amount = 3.0,
        unit = "cups",
        aisle = "",
        image = "",
        name = "sugar",
        nameClean = "sugar",
        original = "brown or white sugar",
        originalName = "brown or white sugar",
    )
    val fakeIngredient2 = RecipeIngredient(
        id = 1,
        amount = 3.0,
        unit = "",
        aisle = "",
        image = "",
        name = "eggs",
        nameClean = "egg",
        original = "large eggs",
        originalName = "large eggs",
    )

    val fakeDetailRecipe = DetailedRecipe(
        id = 1,
        title = "Yorkshire Pudding",
        image = "https://img.spoonacular.com/recipes/665573-556x370.jpg",
        summary = "You can never have too many side dish recipes, so give Yorkshire Pudding a try.",
        readyInMinutes = 45,
        servings = 12,
        sourceUrl = "https://www.foodista.com/recipe/KPRQCDW7/yorkshire-pudding",
        extendedIngredients = listOf(
            fakeIngredient, fakeIngredient2, fakeIngredient, fakeIngredient, fakeIngredient2
        ),
        analyzedInstructions = listOf(
            AnalyzedInstruction(
                steps = listOf(
                    InstructionStep(
                        1,
                        "Preheat oven to 400-450 F depending on your oven, once you have made these a few times you will work out the best temperature. Pass the flour and salt through a sieve and mix together, in a separate bowl beat the eggs and milk then combine the two."
                    ),
                    InstructionStep(
                        2,
                        "Pour the dripping into your Yorkshire tray, a muffin or cupcake tray would probably work too and place in the oven to get the oil nice and hot. Take the oil out of the oven and spoon a couple of tablespoons into each section of your pan."
                    ),
                    InstructionStep(
                        3,
                        "Place back in the oven and cook for 20 minutes or so. Do not open the oven once added, this is very important, if you don't leave them alone you will get flat doughy puddings."
                    )

                )
            )
        ),
        aggregateLikes = 10,
        creditsText = "Foodista.com – The Cooking Encyclopedia Everyone Can Edit",
        sourceName = "Foodista",
    )

    IngredilyTheme {
        RecipeDetailSuccessScreen(
            recipe = fakeDetailRecipe,
            searchRecipe = fakeRecipe,
        )
    }


}