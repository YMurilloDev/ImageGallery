package com.imagegallery.views


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.imagegalery.databinding.ItemImageBinding
import com.imagegallery.domain.Image
import com.imagegallery.utils.ImageGalleryDiffUtils


class ImageGalleryAdapter(
    private var images: List<Image> = emptyList(),
    private var deletionMode: Boolean = false,
    private val clickListener: (String) -> Unit,
) : RecyclerView.Adapter<ImageGalleryAdapter.ImageGalleryViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageGalleryViewHolder {
        val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageGalleryViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(
        holder: ImageGalleryViewHolder,
        position: Int
    ) {
        val image = images[position]
        holder.bind(image, deletionMode)
    }


    fun update(newImages: List<Image>) {
        val imagesLists = ImageGalleryDiffUtils(images, newImages)
        val result = DiffUtil.calculateDiff(imagesLists)
        images = newImages
        result.dispatchUpdatesTo(this)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDeletionMode(deletionMode: Boolean) {
        this.deletionMode = deletionMode
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = images.size

    class ImageGalleryViewHolder(
        private val binding: ItemImageBinding,
        private val clickListener: (String) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val image = it.tag as Image
                image.id.let { id -> clickListener.invoke(id) }
            }
        }

        fun bind(image: Image, deletionMode: Boolean) {
            Glide.with(itemView.context)
                .load(image.path)
                .centerCrop()
                .into(binding.image)
            binding.checkbox.isVisible = deletionMode
            binding.checkbox.isChecked = image.toBeDeleted
            binding.root.tag = image
        }
    }
}