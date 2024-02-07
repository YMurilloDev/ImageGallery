package com.imagegallery.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.imagegallery.domain.Image
import java.util.UUID

@Entity(tableName = "ImageDetailEntity")
data class ImageEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val path: String? = null,
    val size: String? = null,
    val date: String? = null
)

fun Image.toImageEntity(): ImageEntity {
    return ImageEntity(
        id = id,
        path = path,
        size = size,
        date = date
    )
}

fun ImageEntity.toImage(): Image {
    return Image(
        id = id,
        path = path,
        size = size,
        date = date
    )
}