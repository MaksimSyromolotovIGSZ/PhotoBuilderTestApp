package com.example.photobuilder.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.photobuilder.data.local.dao.GalleryDao
import com.example.photobuilder.data.local.entity.PhotoEntity
import com.example.photobuilder.data.local.entity.TagEntity

@Database(
    entities = [
        PhotoEntity::class,
        TagEntity::class,
    ],
    version = 2,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun galleryDao(): GalleryDao
}
