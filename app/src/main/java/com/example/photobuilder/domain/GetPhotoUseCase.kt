package com.example.photobuilder.domain

import com.example.photobuilder.data.local.dao.GalleryDao
import com.example.photobuilder.data.local.dao.toPhoto
import com.example.photobuilder.domain.entity.Photo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetPhotoUseCase @Inject constructor(
    private val dao: GalleryDao
) {

    operator fun invoke(tags: Set<String> = emptySet()): Flow<List<Photo>> =
        dao.getPhotos(tags.toList()).map { photos -> photos.map { it.toPhoto() } }
}
