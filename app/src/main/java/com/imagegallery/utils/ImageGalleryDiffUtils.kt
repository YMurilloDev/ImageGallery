package com.imagegallery.utils

import androidx.recyclerview.widget.DiffUtil
import com.imagegallery.domain.Image


class ImageGalleryDiffUtils(
    private val oldList: List<Image>,
    private val newList: List<Image>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].path == newList[newItemPosition].path
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
                || oldList[oldItemPosition].toBeDeleted != newList[newItemPosition].toBeDeleted
    }
}