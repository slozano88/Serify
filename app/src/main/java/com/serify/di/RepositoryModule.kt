package com.serify.di

import com.serify.data.repository.SeriesRepository
import com.serify.domain.ISeriesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSeriesRepository(
        repository: SeriesRepository
    ): ISeriesRepository
}
