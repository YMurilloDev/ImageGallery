package com.imagegallery.views

import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.imagegalery.databinding.FragmentImageGalleryBinding
import com.imagegallery.domain.Image
import com.imagegallery.viewmodels.ImageGalleryState
import com.imagegallery.viewmodels.ImageGalleryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.nio.file.Files
import java.nio.file.attribute.BasicFileAttributes
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@AndroidEntryPoint
class ImageGalleryFragment : Fragment() {
    private var _binding: FragmentImageGalleryBinding? = null
    private val binding: FragmentImageGalleryBinding get() = _binding!!
    private val viewModel: ImageGalleryViewModel by viewModels()
    private val adapter = ImageGalleryAdapter { id ->
        viewModel.handleClick(id)
    }
    private val selectedImagesToAdd: MutableList<Image> = mutableListOf()
    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris: List<Uri>? ->
            if (uris != null) {
                for (uri in uris) {
                    val path = getRealPathFromURI(uri)
                    val imageSize = getImageSize(path)
                    val imageDate = getImageDate(path)
                    val image = Image(path = path, size = imageSize, date = imageDate)
                    selectedImagesToAdd.add(image)
                }
                viewModel.addImages(selectedImagesToAdd)
                selectedImagesToAdd.clear()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImageGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.addImages.setOnClickListener {
            openGallery()
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    invalidate(state)
                }
            }
        }
        binding.recyclerView.layoutManager = GridLayoutManager(this.context, 2)
        binding.recyclerView.adapter = adapter
        viewModel.fetchImages()
    }

    private fun invalidate(state: ImageGalleryState) {
        binding.deleteMode.setOnClickListener {
            viewModel.toggleDeletionMode()
        }
        adapter.setDeletionMode(state.deletionMode)
        binding.arrowBack.setOnClickListener {
            viewModel.toggleDeletionMode()
        }
        binding.arrowBack.isVisible = state.deletionMode
        binding.deleteImages.isVisible = state.deletionMode
        binding.addImages.isVisible = !state.deletionMode
        binding.deleteMode.isVisible = !state.deletionMode
        if (state.deletionMode) {
            binding.deleteImages.setOnClickListener {
                viewModel.deleteImages()
                viewModel.clearState()
            }
        }
        adapter.update(state.images)
        if (state.selectedImageId != null) {
            findNavController().navigate(
                ImageGalleryFragmentDirections.actionImageGalleryFragmentToImageFullSizeFragment(
                    state.selectedImageId
                )
            )
            viewModel.clearState()
        }
        binding.emptyText.isVisible = state.images.isEmpty() && !state.isLoading
    }

    private fun openGallery() {
        galleryLauncher.launch("image/*")
    }

    private fun getRealPathFromURI(contentUri: Uri): String {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = requireContext().contentResolver.query(contentUri, proj, null, null)
        val index = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        val result = cursor?.getString(index ?: 0)
        cursor?.close()
        return result.orEmpty()
    }

    private fun getImageSize(path: String): String {
        val fileSizeInBytes = File(path).length()
        val fileSizeInKB = fileSizeInBytes / 1024
        return if (fileSizeInKB < 1024) {
            "$fileSizeInKB KB"
        } else {
            val fileSizeInMB = fileSizeInKB / 1024.0
            String.format("%.2f MB", fileSizeInMB)
        }
    }

    private fun getImageDate(path: String): String {
        val file = File(path)
        val attributes = Files.readAttributes(file.toPath(), BasicFileAttributes::class.java)
        val creationTime = attributes.creationTime().toMillis()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        return dateFormat.format(Date(creationTime))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerView.adapter = null
        _binding = null
    }
}