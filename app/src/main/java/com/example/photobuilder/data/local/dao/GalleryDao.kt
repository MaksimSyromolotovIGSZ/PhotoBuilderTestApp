package com.example.photobuilder.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.photobuilder.data.local.entity.PhotoEntity
import com.example.photobuilder.data.local.entity.TagEntity
import com.example.photobuilder.domain.entity.Photo
import kotlinx.coroutines.flow.Flow

@Dao
interface GalleryDao {

    @Insert
    suspend fun insert(photo: PhotoEntity)

    @Delete
    suspend fun delete(photo: PhotoEntity)

    @Query("SELECT * FROM TagEntity")
    fun getAllTags(): Flow<List<TagEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tag: TagEntity)

    @Query("SELECT * FROM PhotoEntity WHERE tag IN (:tags)")
    fun getPhotos(tags: List<String>): Flow<List<PhotoEntity>>

    @Query("SELECT * FROM PhotoEntity")
    fun getAllPhotos(): Flow<List<PhotoEntity>>
}

fun Photo.toPhotoEntity() = PhotoEntity(
    uri = this.uri,
    createdAt = this.createdAt,
    tag = this.tag
)

fun PhotoEntity.toPhoto() =
    Photo(
        id = this.id,
        uri = this.uri,
        createdAt = this.createdAt,
        tag = this.tag
    )
