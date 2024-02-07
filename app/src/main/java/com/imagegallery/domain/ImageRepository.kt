package com.imagegallery.domain

interface ImageRepository {
    suspend fun addImages(images: List<Image>): List<Image>

    suspend fun deleteImage(ids: List<String?>): List<Image>

    suspend fun fetchImages(): List<Image>

    suspend fun fetchImageDetail(id: String?): Image
}