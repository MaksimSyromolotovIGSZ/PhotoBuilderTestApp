package com.example.photobuilder.domain

import com.example.photobuilder.data.local.dao.GalleryDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllTagsUseCase @Inject constructor(private val dao: GalleryDao) {

    operator fun invoke(): Flow<List<String>> {
        return dao.getAllTags().map { tags -> tags.map { it.name } }
    }
}