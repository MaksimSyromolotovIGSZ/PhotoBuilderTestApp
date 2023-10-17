package com.example.photobuilder.feature.gallery

import com.example.photobuilder.domain.entity.Photo

data class GalleryViewState(
    val photos: List<Photo> = emptyList(),
    val tags: List<String> = emptyList(),
    val isGalleryAllItemsState: Boolean = false,
    val isSelectionState: Boolean = false,
    val isFilterState: Boolean = false,
    val selectedTags: MutableSet<String> = mutableSetOf(),
) {

    fun changeViewState(
        isGalleryAllItemsState: Boolean = false,
        isSelectionState: Boolean = false,
        isFilterState: Boolean = false,
    ): GalleryViewState {
        return this.copy(
            isGalleryAllItemsState = isGalleryAllItemsState,
            isSelectionState = isSelectionState,
            isFilterState = isFilterState
        )
    }
}