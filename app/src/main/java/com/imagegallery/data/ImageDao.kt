package com.imagegallery.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface ImageDao {

    @Upsert
    suspend fun addImage(images: List<ImageEntity>)

    @Query("SELECT * FROM ImageDetailEntity")
    suspend fun getAllImages(): List<ImageEntity>

    @Query("SELECT * FROM ImageDetailEntity WHERE id = :id")
    suspend fun getImageById(id: String?): ImageEntity?

    @Query("DELETE FROM ImageDetailEntity WHERE id IN (:ids)")
    suspend fun deleteImages(ids: List<String?>)
}
