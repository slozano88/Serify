package com.serify.di

import com.serify.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object ConfigModule {

    @Provides
    @Named("groqApiKey")
    fun provideGroqApiKey(): String {
        return BuildConfig.GROQ_API_KEY.trim()
    }

    @Provides
    @Named("groqModel")
    fun provideGroqModel(): String {
        return BuildConfig.GROQ_MODEL.trim()
            .ifBlank { "llama-3.1-8b-instant" }
    }
}
