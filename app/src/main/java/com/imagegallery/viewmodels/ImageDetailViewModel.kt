package com.imagegallery.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imagegallery.domain.Image
import com.imagegallery.domain.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


data class ImageDetailState(
    val imageDetail: Image? = null
)

@HiltViewModel
class ImageDetailViewModel @Inject constructor(
    private val repository: ImageRepository
) : ViewModel() {

    private val internalState = MutableStateFlow(ImageDetailState())
    val state: StateFlow<ImageDetailState> = internalState

    fun fetchImageDetail(id: String) {

        viewModelScope.launch {
            val image = repository.fetchImageDetail(id)
            internalState.value = internalState.value.copy(
                imageDetail = image
            )
        }
    }
}
