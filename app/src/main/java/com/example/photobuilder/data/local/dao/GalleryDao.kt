package com.example.photobuilder.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.photobuilder.data.local.entity.PhotoEntity
import com.example.photobuilder.domain.Photo

@Dao
interface GalleryDao {

    @Insert
    suspend fun insert(photo: PhotoEntity)

    @Delete
    suspend fun delete(photo: PhotoEntity)

    @Query("SELECT * FROM PhotoEntity WHERE tag IN (:tags)")
    suspend fun getPhotos(tags: Set<String>): List<PhotoEntity>
}

fun Photo.toPhotoEntity() = PhotoEntity(
    uri = this.uri,
    createdAt = this.createdAt,
    tag = this.tag
)

fun PhotoEntity.toPhoto() =
    Photo(
        uri = this.uri,
        createdAt = this.createdAt,
        tag = this.tag
    )
