package com.serify.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Retrofit {

    // Para centralizar la conexion con la api
    private const val BASE_URL = "https://api.tvmaze.com/"

    val tvMazeApi: TvMazeApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TvMazeApi::class.java)
    }
}