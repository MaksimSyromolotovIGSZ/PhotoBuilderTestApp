package com.example.photobuilder.feature.tekephoto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photobuilder.domain.GetAllTagsUseCase
import com.example.photobuilder.domain.SavePhotoUseCase
import com.example.photobuilder.domain.SaveTagUseCase
import com.example.photobuilder.domain.entity.Photo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TakePhotoViewModel @Inject constructor(
    private val savePhotoUseCase: SavePhotoUseCase,
    private val saveTagUseCase: SaveTagUseCase,
    private val getAllTagsUseCase: GetAllTagsUseCase,
) : ViewModel() {

    private val _uiEffect = Channel<Unit>(Channel.BUFFERED)
    val uiEffect = _uiEffect.receiveAsFlow()

    private val _uiState = MutableStateFlow(TakePhotoUiState())
    val uiState = _uiState.asStateFlow()

    fun onImageProcessed(
        uri: String,
        createdAt: String,
        tag: String
    ) {
        viewModelScope.launch {

            val setOfTags = getAllTagsUseCase().first().toSet()
            _uiState.value =
                _uiState.value.copy(
                    isPhotoLoaded = true,
                    tag = tag,
                    uri = uri,
                    createdAt = createdAt,
                    existingTags = setOfTags,
                )
        }
    }

    fun savePhoto(tag: String) {
        viewModelScope.launch {
            saveTagUseCase(tag)
            savePhotoUseCase.savePhoto(
                Photo(
                    uri = uiState.value.uri,
                    createdAt = uiState.value.createdAt,
                    tag = tag,
                )
            )
            _uiEffect.send(Unit)
        }
    }
}


