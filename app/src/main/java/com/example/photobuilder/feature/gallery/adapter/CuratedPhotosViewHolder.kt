package com.example.photobuilder.feature.gallery.adapter

import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.photobuilder.databinding.CuratedPhotoItemBinding
import com.example.photobuilder.domain.entity.Photo

private const val CACHING_TIMEOUT = 3600000

class CuratedPhotosViewHolder(
    private val binding: CuratedPhotoItemBinding,
    private val itemClick: (Int) -> Unit,
) :
    RecyclerView.ViewHolder(binding.root) {
    fun onBind(curatedPhoto: Photo) {
        binding.shimmerViewContainer.startShimmer()
        val requestListener = object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean = false

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                binding.shimmerViewContainer.apply {
                    stopShimmer()
                    setShimmer(null)
                }
                return false
            }
        }
        Glide.with(itemView.context)
            .load(curatedPhoto.uri)
            .listener(requestListener)
            .into(binding.curatedPhotoItem)

        itemView.setOnClickListener {
            itemView.animate().scaleX(0.9f).scaleY(0.9f).setDuration(50).withEndAction {
                itemView.animate().scaleX(1f).scaleY(1f).setDuration(50).start()
            }.start()
            itemClick(curatedPhoto.id)
        }
    }
}