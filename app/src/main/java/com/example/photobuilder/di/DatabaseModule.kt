package com.example.photobuilder.di

import android.content.Context
import androidx.room.Room
import com.example.photobuilder.data.local.dao.GalleryDao
import com.example.photobuilder.data.local.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase =
        Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "gallery"
        )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideGalleryDao(appDatabase: AppDatabase): GalleryDao = appDatabase.galleryDao()
}
