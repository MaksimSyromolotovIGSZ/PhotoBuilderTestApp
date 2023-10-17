package com.example.photobuilder.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.photobuilder.data.local.dao.GalleryDao
import com.example.photobuilder.data.local.entity.PhotoEntity

@Database(
    entities = [
        PhotoEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun galleryDao(): GalleryDao
}
