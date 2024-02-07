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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.imagegalery.R
import com.imagegalery.databinding.FragmentImageFullScreenBinding
import com.imagegallery.viewmodels.ImageFullScreenState
import com.imagegallery.viewmodels.ImageFullScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ImageFullScreenFragment : Fragment() {
    private var _binding: FragmentImageFullScreenBinding? = null
    private val binding: FragmentImageFullScreenBinding get() = _binding!!
    private val viewModel: ImageFullScreenViewModel by viewModels()
    private val args: ImageFullScreenFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImageFullScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.toolbar.inflateMenu(R.menu.menu_details)

        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_details -> {
                    findNavController().navigate(
                        ImageFullScreenFragmentDirections.actionImageFullScreenFragmentToImageDetailFragment(
                            args.imageId
                        )
                    )
                    true
                }
                else -> false
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    invalidate(state)
                }
            }
        }
        viewModel.fetchImageDetail(args.imageId)
    }

    private fun invalidate(state: ImageFullScreenState) {
        Glide.with(this)
            .load(state.imageDetail?.path)
            .into(binding.image)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}