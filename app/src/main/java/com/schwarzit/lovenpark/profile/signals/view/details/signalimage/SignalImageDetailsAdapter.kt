package com.schwarzit.lovenpark.profile.signals.view.details.signalimage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.schwarzit.lovenpark.databinding.SignalOverviewImageBinding
import com.schwarzit.lovenpark.mappin.data.remote.PictureModel
import com.schwarzit.lovenpark.mappin.data.remote.setGlideRes

class SignalImageDetailsAdapter :
    RecyclerView.Adapter<SignalImageDetailsAdapter.SignalImageDetailsViewHolder>() {

    var imageList = emptyList<PictureModel>()

    fun injectImageList(list: List<PictureModel>) {
        imageList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = SignalImageDetailsViewHolder(
        SignalOverviewImageBinding.inflate(
            LayoutInflater.from(
                parent.context
            ), parent, false
        )
    )


    override fun onBindViewHolder(holder: SignalImageDetailsViewHolder, position: Int) =
        holder.bindItem(imageList[position])

    override fun getItemCount() = imageList.size

    inner class SignalImageDetailsViewHolder(private val binding: SignalOverviewImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(picture: PictureModel) {
            binding.apply {
                ivSignalOverviewImage.setGlideRes(
                    picture.url,
                )
            }

        }

    }
}