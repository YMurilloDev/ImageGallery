package com.imagegallery.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.imagegalery.databinding.FragmentImageDetailBinding
import com.imagegallery.viewmodels.ImageDetailState
import com.imagegallery.viewmodels.ImageDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ImageDetailFragment : Fragment() {
    private var _binding: FragmentImageDetailBinding? = null
    private val binding: FragmentImageDetailBinding get() = _binding!!
    private val viewModel: ImageDetailViewModel by viewModels()
    private val args: ImageDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImageDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    invalidate(state)
                }
            }
        }
        viewModel.fetchImageDetail(args.imageId)
    }

    private fun invalidate(state: ImageDetailState) {
        binding.localRoute.text = state.imageDetail?.path
        binding.captureDate.text = state.imageDetail?.date
        binding.captureSize.text = state.imageDetail?.size
    }
}