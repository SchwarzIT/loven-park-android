package com.schwarzit.lovenpark.camera

import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.schwarzit.lovenpark.databinding.CardCameraImageBinding

class CameraImageViewHolder(val binding: CardCameraImageBinding) :
    RecyclerView.ViewHolder(binding.root) {
    var signalCameraImage = binding.ivCameraImage
        private set
    var xButton: Button = binding.btnCameraRemoveImage
        private set

    init {
        signalCameraImage.clipToOutline = true
    }
}