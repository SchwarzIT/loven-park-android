package com.schwarzit.lovenpark.signal

import androidx.recyclerview.widget.RecyclerView
import com.schwarzit.lovenpark.databinding.SignalOverviewImageBinding

class SignalImageOverviewHolder(val binding: SignalOverviewImageBinding): RecyclerView.ViewHolder(binding.root) {
    var signalImage = binding.ivSignalOverviewImage
        private set
}