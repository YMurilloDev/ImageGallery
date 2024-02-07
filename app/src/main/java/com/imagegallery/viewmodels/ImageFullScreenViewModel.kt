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


data class ImageFullScreenState(
    val imageDetail: Image? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class ImageFullScreenViewModel @Inject constructor(
    private val repository: ImageRepository
) : ViewModel() {

    private val internalState = MutableStateFlow(ImageFullScreenState())
    val state: StateFlow<ImageFullScreenState> = internalState

    fun fetchImageDetail(id: String) {
        internalState.value = internalState.value.copy(
            isLoading = true
        )
        viewModelScope.launch {
            val image = repository.fetchImageDetail(id)
            internalState.value = internalState.value.copy(
                isLoading = false,
                imageDetail = image
            )
        }
    }
}
