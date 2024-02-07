package com.imagegallery.di

import com.imagegallery.data.DefaultImageRepository
import com.imagegallery.data.ImageDao
import com.imagegallery.domain.ImageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ImageModule {

    @Provides
    @Singleton
    fun provideImageRepository(dao: ImageDao): ImageRepository {
        return DefaultImageRepository(dao)
    }
}