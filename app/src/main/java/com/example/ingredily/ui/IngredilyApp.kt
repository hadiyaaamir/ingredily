package com.example.ingredily.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.example.ingredily.ui.screens.RecipesViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ingredily.ui.screens.IngredientsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngredilyApp() {
    Scaffold(
        modifier = Modifier,
    ) { contentPadding ->
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            IngredientsScreen(
                contentPadding = contentPadding
            )
        }
    }

}