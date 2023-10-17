package com.example.photobuilder.domain

import android.util.Log
import com.example.photobuilder.data.local.dao.GalleryDao
import com.example.photobuilder.data.local.dao.toPhotoEntity
import javax.inject.Inject

class SavePhotoUseCase @Inject constructor(
    private val dao: GalleryDao
) {

    suspend fun savePhoto(photo: Photo) {
        dao.insert(photo.toPhotoEntity())
        Log.d("TAG", "SAVED $photo")
    }
}
