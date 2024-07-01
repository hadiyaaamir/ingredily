package com.example.ingredily

import android.app.Application
import com.example.ingredily.data.AppContainer
import com.example.ingredily.data.DefaultAppContainer

class RecipesApplication: Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}