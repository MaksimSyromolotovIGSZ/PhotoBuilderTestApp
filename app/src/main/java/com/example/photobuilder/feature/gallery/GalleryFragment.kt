package com.example.photobuilder.feature.gallery

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.photobuilder.R
import com.example.photobuilder.core.extension.observeWithLifecycle
import com.example.photobuilder.databinding.FragmentGalleryBinding
import com.example.photobuilder.feature.gallery.adapter.CuratedPhotosAdapter
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint

private const val SPAN_COUNT = 2

@AndroidEntryPoint
class GalleryFragment : Fragment(R.layout.fragment_gallery) {

    private val binding: FragmentGalleryBinding by viewBinding(FragmentGalleryBinding::bind)
    private val viewModel by viewModels<GalleryViewModel>()
    private val curatedPhotosAdapter by lazy { CuratedPhotosAdapter() }
    private var isFirstEntry: Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            topGalleryHeader.filterButton.setOnClickListener { viewModel.filterPhotosByTags() }
            curatedPhotosRecycler.apply {
                layoutManager = StaggeredGridLayoutManager(SPAN_COUNT, LinearLayoutManager.VERTICAL)
                adapter = curatedPhotosAdapter
            }
            topButtonsPanel.closeTagsFilter.setOnClickListener {
                viewModel.backToGallery()
            }
            viewModel.uiState.observeWithLifecycle(viewLifecycleOwner) { state ->
                cameraButtonPanel.root.isVisible = state.isGalleryAllItemsState
                topGalleryHeader.root.isVisible = state.isGalleryAllItemsState
                topButtonsPanel.topRoot.isVisible = state.isFilterState
                buttonPanel.root.isVisible = state.isSelectionState
                curatedPhotosAdapter.updateCuratedPhotos(state.photos)
            }
            viewModel.tags.observeWithLifecycle(viewLifecycleOwner) { tags ->
                topButtonsPanel.tagChips.removeAllViews()
                tags.toSet().forEach { tag ->
                    topButtonsPanel.tagChips.addView(Chip(requireContext()).also {
                        it.text = tag
                        it.isCheckable = true
                        it.isClickable = true
                        it.isCheckedIconVisible = true
                        it.isEnabled = true
                        it.setOnClickListener {
                            val chip = it as Chip
                            if (chip.isChecked) {
                                viewModel.selectTag(chip.text.toString())
                                chip.setTextAppearance(
                                    requireContext(),
                                    R.style.ChipCheckedText
                                )
                            } else {
                                viewModel.unselectTag(chip.text.toString())
                                chip.setTextAppearance(
                                    requireContext(),
                                    R.style.ChipUncheckedText
                                )
                            }
                        }
                    })
                }
            }
            with(cameraButtonPanel) {
                root.isVisible = true
                takePhotoBtn.isVisible = true
                takePhotoBtn.setOnClickListener {
                    findNavController().navigate(R.id.action_photosFragment_to_TakePhotoFragment)
                }
            }
        }
    }
}
