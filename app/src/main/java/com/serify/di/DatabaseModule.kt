package com.serify.di

import android.content.Context
import androidx.room.Room
import com.serify.data.local.SerifyDao
import com.serify.data.local.SerifyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): SerifyDatabase {
        return Room.databaseBuilder(
            context,
            SerifyDatabase::class.java,
            "serify_local.db"
        ).build()
    }

    @Provides
    fun provideSerifyDao(database: SerifyDatabase): SerifyDao {
        return database.serifyDao()
    }
}
