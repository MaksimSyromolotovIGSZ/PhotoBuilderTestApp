package com.example.photobuilder.domain

import com.example.photobuilder.data.local.dao.GalleryDao
import com.example.photobuilder.data.local.dao.toPhoto
import javax.inject.Inject

class GetPhotoUseCase @Inject constructor(
    private val dao: GalleryDao
) {

    suspend fun getPhotos(tags: Set<String>): List<Photo> = dao.getPhotos(tags).map { it.toPhoto() }
}
