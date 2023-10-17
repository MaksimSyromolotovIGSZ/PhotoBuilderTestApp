package com.example.photobuilder.feature.tekephoto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photobuilder.domain.Photo
import com.example.photobuilder.domain.SavePhotoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TakePhotoViewModel @Inject constructor(
    private val savePhotoUseCase: SavePhotoUseCase
) : ViewModel() {

    private val _uiEffect = Channel<Unit>(Channel.BUFFERED)
    val uiEffect = _uiEffect.receiveAsFlow()

    fun onImageSave(
        uri: String,
        createdAt: String,
        tag: String
    ) {
        viewModelScope.launch {
            savePhotoUseCase.savePhoto(
                Photo(
                    uri = uri,
                    createdAt = createdAt,
                    tag = tag
                )
            )
            _uiEffect.send(Unit)
        }
    }
}


