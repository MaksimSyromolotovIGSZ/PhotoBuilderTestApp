package com.example.photobuilder.domain

import com.example.photobuilder.data.local.dao.GalleryDao
import com.example.photobuilder.data.local.entity.TagEntity
import javax.inject.Inject

class SaveTagUseCase @Inject constructor(private val dao: GalleryDao) {

    suspend operator fun invoke(tag: String) {
        dao.insert(TagEntity(name = tag))
    }
}