package com.example.photobuilder.feature.gallery

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.photobuilder.R
import com.example.photobuilder.databinding.FragmentGalleryBinding

class GalleryFragment : Fragment(R.layout.fragment_gallery) {

    private val binding: FragmentGalleryBinding by viewBinding(FragmentGalleryBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.buttonPanel.takePhotoBtn) {
            isVisible = true
            setOnClickListener {
                findNavController().navigate(R.id.action_photosFragment_to_TakePhotoFragment)
            }
        }
        Glide
            .with(requireContext())
            .load("content://media/external/images/media/1000004587")
            .circleCrop()
            .error(R.drawable.ic_launcher_foreground)
            .into(binding.photoView)
    }

}
