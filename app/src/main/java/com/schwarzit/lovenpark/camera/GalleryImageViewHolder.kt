package com.schwarzit.lovenpark.camera

import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.schwarzit.lovenpark.databinding.CardGalleryImageBinding

class GalleryImageViewHolder(val binding: CardGalleryImageBinding) :
    RecyclerView.ViewHolder(binding.root) {
    var signalImage = binding.ivSignalImageGallery
        private set
    var xButton: MaterialButton = binding.btnRemoveItem
        private set

    init {
        signalImage.clipToOutline = true
    }
}