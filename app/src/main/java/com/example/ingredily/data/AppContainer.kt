package com.example.ingredily.data

import com.example.ingredily.BuildConfig
import com.example.ingredily.network.RecipesApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit


const val API_KEY = BuildConfig.API_KEY

interface AppContainer {
    val recipesRepository: RecipesRepository
}

class DefaultAppContainer : AppContainer {

    private val baseUrl = "https://api.spoonacular.com"

    private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)
    private val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build();

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .client(client)
        .build()


    private val recipesRetrofitService: RecipesApiService by lazy {
        retrofit.create(RecipesApiService::class.java)
    }

    override val recipesRepository: RecipesRepository by lazy {
        RecipesRepositoryImpl(recipesRetrofitService)
    }

}