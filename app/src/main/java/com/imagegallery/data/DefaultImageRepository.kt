package com.imagegallery.data

import com.imagegallery.domain.Image
import com.imagegallery.domain.ImageRepository

class DefaultImageRepository(
    private val dao: ImageDao
) : ImageRepository {
    override suspend fun addImages(images: List<Image>): List<Image> {
        val imageEntities = images.map { it.toImageEntity() }
        dao.addImage(imageEntities)
        return dao.getAllImages().map { it.toImage() }
    }

    override suspend fun deleteImage(ids: List<String?>): List<Image> {
        dao.deleteImages(ids = ids)
        return dao.getAllImages().map { it.toImage() }
    }

    override suspend fun fetchImages(): List<Image> {
        return dao.getAllImages().map { it.toImage() }
    }

    override suspend fun fetchImageDetail(id: String?): Image {
        return dao.getImageById(id)?.toImage() ?: Image("")
    }
}