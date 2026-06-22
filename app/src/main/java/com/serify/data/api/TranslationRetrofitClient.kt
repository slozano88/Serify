package com.serify.data.api

import retrofit2.Retrofit as RetrofitBuilder
import retrofit2.converter.gson.GsonConverterFactory

object TranslationRetrofitClient {

    private const val GOOGLE_TRANSLATE_BASE_URL = "https://translate.googleapis.com/"

    val googleTranslateApi: GoogleTranslateApi by lazy {
        RetrofitBuilder.Builder()
            .baseUrl(GOOGLE_TRANSLATE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GoogleTranslateApi::class.java)
    }
}
