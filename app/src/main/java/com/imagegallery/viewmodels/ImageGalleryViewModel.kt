package com.imagegallery.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imagegallery.domain.ImageRepository
import com.imagegallery.domain.Image
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ImageGalleryState(
    val images: List<Image> = emptyList(),
    val isLoading: Boolean = false,
    val deletionMode: Boolean = false,
    val selectedImageId: String? = null
)

@HiltViewModel
class ImageGalleryViewModel @Inject constructor(
    private val repository: ImageRepository,
) : ViewModel() {

    private val internalState = MutableStateFlow(ImageGalleryState())
    val state: StateFlow<ImageGalleryState> = internalState

    fun addImages(selectedImages: List<Image>) {
        internalState.value = internalState.value.copy(
            isLoading = true
        )
        viewModelScope.launch {
            val newImages = repository.addImages(selectedImages)
            internalState.value = internalState.value.copy(
                images = newImages,
                isLoading = false
            )
        }
    }

    fun fetchImages() {
        internalState.value = internalState.value.copy(
            isLoading = true
        )
        viewModelScope.launch {
            val images = repository.fetchImages()
            internalState.value = internalState.value.copy(
                images = images,
                isLoading = false
            )
        }
    }

    fun deleteImages() {
        viewModelScope.launch {
            val deletionIds = internalState.value.images.filter { it.toBeDeleted }.map { it.id }
            val newImages = repository.deleteImage(deletionIds)
            internalState.value = internalState.value.copy(
                images = newImages
            )
        }
    }

    fun toggleDeletionMode() {
        internalState.value = internalState.value.copy(
            deletionMode = !internalState.value.deletionMode,
        )
    }

    private fun addToDeletionIds(id: String) {
        val images = internalState.value.images.map {
            if (id == it.id) {
                it.copy(toBeDeleted = !it.toBeDeleted)
            } else {
                it
            }
        }
        internalState.value = internalState.value.copy(
            images = images
        )
    }

    fun clearState() {
        internalState.value = ImageGalleryState()
    }

    fun handleClick(id: String) {
        if (state.value.deletionMode) {
            addToDeletionIds(id)
        } else {
            internalState.value = internalState.value.copy(
                selectedImageId = id,
                isLoading = false
            )
        }
    }
}
