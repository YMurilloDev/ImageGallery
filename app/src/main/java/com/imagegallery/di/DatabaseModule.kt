package com.imagegallery.di

import android.content.Context
import com.imagegallery.data.AppDataBase
import com.imagegallery.data.ImageDao
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
    fun provideAppDataBase(@ApplicationContext context: Context): AppDataBase {
        return AppDataBase.buildDataBase(context)
    }

    @Provides
    @Singleton
    fun provideImageDao(dataBase: AppDataBase): ImageDao {
        return dataBase.imageDao()
    }
}