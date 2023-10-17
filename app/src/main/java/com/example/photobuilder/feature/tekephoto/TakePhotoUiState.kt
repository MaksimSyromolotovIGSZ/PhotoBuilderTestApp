package com.example.photobuilder.feature.tekephoto

data class TakePhotoUiState(
    val isPhotoLoaded: Boolean = false,
    val tag: String = "",
    val uri: String = "",
    val createdAt: String = "",
    val existingTags: Set<String> = emptySet()
)
