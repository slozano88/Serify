package com.serify.di

import com.serify.data.api.AiApi
import com.serify.data.api.GoogleTranslateApi
import com.serify.data.api.TvMazeApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideTvMazeApi(): TvMazeApi {
        return createRetrofit("https://api.tvmaze.com/")
            .create(TvMazeApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAiApi(): AiApi {
        return createRetrofit("https://api.groq.com/openai/v1/")
            .create(AiApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGoogleTranslateApi(): GoogleTranslateApi {
        return createRetrofit("https://translate.googleapis.com/")
            .create(GoogleTranslateApi::class.java)
    }

    private fun createRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
