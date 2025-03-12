package com.schwarzit.lovenpark.signal

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.schwarzit.lovenpark.R
import com.schwarzit.lovenpark.camera.BitmapProcessor
import com.schwarzit.lovenpark.databinding.SignalOverviewImageBinding

class SignalImageOverviewAdapter(private val context: Context) :
    RecyclerView.Adapter<SignalImageOverviewHolder>() {

    var signalOverviewImagesList = mutableListOf<Uri>()

    fun setImageList(list: MutableList<Uri>) {
        signalOverviewImagesList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SignalImageOverviewHolder {
        return SignalImageOverviewHolder(
            SignalOverviewImageBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: SignalImageOverviewHolder, position: Int) {
        context.let {
            val bitmap = BitmapProcessor(it).processBitmap(
                signalOverviewImagesList[position],
                it.resources.getDimension(R.dimen.gallery_image_required_size).toInt(), true
            )
            holder.signalImage.setImageBitmap(bitmap)
        }
    }

    override fun getItemCount() = signalOverviewImagesList.size
}