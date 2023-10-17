package com.example.photobuilder.domain

import com.example.photobuilder.data.local.dao.GalleryDao
import com.example.photobuilder.data.local.dao.toPhoto
import com.example.photobuilder.domain.entity.Photo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllPhotosUseCase @Inject constructor(private val dao: GalleryDao) {
    operator fun invoke(): Flow<List<Photo>> {
        return dao.getAllPhotos().map { photos -> photos.map { it.toPhoto() } }
    }
}