package com.example.ingredily.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val recipesRepository: RecipesRepository
}

class DefaultAppContainer: AppContainer {

//    private val BASE_URL = "https://android-kotlin-fun-mars-server.appspot.com"
//
//    private val retrofit = Retrofit.Builder()
//        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
//        .baseUrl(BASE_URL)
//        .build()

//    val retrofitService : MarsApiService by lazy {
//        retrofit.create(MarsApiService::class.java)
//    }


    override val recipesRepository: RecipesRepository by lazy {
       RecipesRepositoryImpl()
    }

}