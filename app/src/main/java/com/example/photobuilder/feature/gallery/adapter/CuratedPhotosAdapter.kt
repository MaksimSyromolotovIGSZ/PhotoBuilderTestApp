package com.example.photobuilder.feature.gallery.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.photobuilder.databinding.CuratedPhotoItemBinding
import com.example.photobuilder.domain.entity.Photo

class CuratedPhotosAdapter : RecyclerView.Adapter<CuratedPhotosViewHolder>() {

    var itemClick: ((Int) -> Unit) = {}

    private val curatedPhotos = mutableListOf<Photo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CuratedPhotosViewHolder {
        val binding =
            CuratedPhotoItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return CuratedPhotosViewHolder(binding, itemClick)
    }

    override fun getItemCount() = curatedPhotos.size

    override fun onBindViewHolder(holder: CuratedPhotosViewHolder, position: Int) {
        holder.onBind(curatedPhotos[position])
    }

    fun updateCuratedPhotos(newList: List<Photo>) {
        val diffResult = DiffUtil.calculateDiff(
            GenericDiffCallback(
                oldList = curatedPhotos,
                newList = newList,
                areItemsTheSame = { old, new -> old.id == new.id },
                areContentsTheSame = { old, new -> old == new }
            )
        )
        curatedPhotos.clear()
        curatedPhotos.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

}