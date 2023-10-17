package com.example.photobuilder.feature.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photobuilder.domain.GetAllPhotosUseCase
import com.example.photobuilder.domain.GetAllTagsUseCase
import com.example.photobuilder.domain.GetPhotoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val getAllPhotosUseCase: GetAllPhotosUseCase,
    private val getAllTagsUseCase: GetAllTagsUseCase,
    private val getPhotoUseCase: GetPhotoUseCase,
) : ViewModel() {

    private val _tags = MutableStateFlow<List<String>>(emptyList())
    val tags = _tags.asStateFlow()
    private val _uiState = MutableStateFlow(initialState())
    val uiState = _uiState.asStateFlow()

    init {
        getAllPhotos()
        getAllTags()
    }

    private fun getAllTags() {
        viewModelScope.launch {
            getAllTagsUseCase().collect { tags ->
                _tags.value = tags
            }
        }
    }

    fun selectTag(tag: String) {
        _uiState.value.selectedTags.add(tag)
        collectPhotos()
    }

    private fun collectPhotos() {
        viewModelScope.launch {
            if (uiState.value.selectedTags.isEmpty()) {
                getAllPhotosUseCase()
            } else {
                getPhotoUseCase(uiState.value.selectedTags)
            }.collect {
                _uiState.value =
                    uiState.value.copy(selectedTags = uiState.value.selectedTags, photos = it)
            }
        }
    }

    fun unselectTag(tag: String) {
        _uiState.value.selectedTags.remove(tag)
        collectPhotos()
    }

    private fun initialState() = GalleryViewState(
        isGalleryAllItemsState = true,
    )

    private fun getAllPhotos() {
        viewModelScope.launch {
            getAllPhotosUseCase().collect { photos ->
                _uiState.value = uiState.value.copy(photos = photos)
            }
        }
    }

    fun filterPhotosByTags() {
        _uiState.value = uiState.value.changeViewState(isFilterState = true)
    }

    fun enterSelectMode() {
        _uiState.value = uiState.value.changeViewState(isSelectionState = true)
    }

    fun backToGallery() {
        _uiState.value = uiState.value.changeViewState(isGalleryAllItemsState = true)
    }
}