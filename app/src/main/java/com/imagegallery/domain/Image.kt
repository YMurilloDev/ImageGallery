package com.imagegallery.domain

import java.util.UUID


data class Image(
    val id: String = UUID.randomUUID().toString(),
    val path: String? = null,
    val size: String? = null,
    val date: String? = null,
    val toBeDeleted: Boolean = false
)