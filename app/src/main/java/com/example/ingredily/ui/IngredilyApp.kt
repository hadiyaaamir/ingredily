package com.example.ingredily.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ingredily.ui.screens.IngredientsScreen
import com.example.ingredily.ui.screens.IngredientsViewModel
import com.example.ingredily.ui.screens.RecipesScreen
import com.example.ingredily.ui.screens.RecipesViewModel

enum class IngredilyScreen {
    Ingredients,
    Recipes
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngredilyApp(
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        modifier = Modifier,
    ) { innerPadding ->

        val ingredientsViewModel: IngredientsViewModel = viewModel(
            factory = IngredientsViewModel.Factory
        )
        val recipesViewModel: RecipesViewModel = viewModel(
            factory = RecipesViewModel.Factory
        )

        NavHost(
            navController = navController,
            startDestination = IngredilyScreen.Ingredients.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(IngredilyScreen.Ingredients.name) {
                IngredientsScreen(
                    viewModel = ingredientsViewModel,
                    onNextButtonClicked = { ingredients ->
                        recipesViewModel.initialiseIngredientsFilter(ingredients)
                        recipesViewModel.getRecipesByIngredients()
                        navController.navigate(IngredilyScreen.Recipes.name)
                    }
                )
            }

            composable(IngredilyScreen.Recipes.name) {
                RecipesScreen(
                    viewModel = recipesViewModel,
                )
            }


//            StartOrderScreen(
//                    quantityOptions = DataSource.quantityOptions,
//                    onNextButtonClicked = {
//                        viewModel.setQuantity(it)
//                        navController.navigate(CupcakeScreen.Flavor.name)
//                    },
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(dimensionResource(R.dimen.padding_medium))
//                )
        }
//        Surface(
//            modifier = Modifier.fillMaxSize()
//        ) {
//            IngredientsScreen(
//                contentPadding = contentPadding
//            )
//        }
    }

}