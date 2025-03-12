package com.schwarzit.lovenpark.camera

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.schwarzit.lovenpark.R
import com.schwarzit.lovenpark.databinding.CardGalleryImageBinding

class GalleryImageAdapter(private val context: Context) :
    RecyclerView.Adapter<GalleryImageViewHolder>() {

    var onDeleteImageListener: ((Int) -> Unit?)? = null
    var signalImagesList = mutableListOf<Uri>()
    var path: String? = null

    fun setImageList(list: MutableList<Uri>) {
        signalImagesList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryImageViewHolder {
        return GalleryImageViewHolder(
            CardGalleryImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: GalleryImageViewHolder, position: Int) {
        context.let {
            val bitmap = BitmapProcessor(it).processBitmap(
                signalImagesList[position],
                it.resources.getDimension(R.dimen.gallery_image_required_size).toInt(), true
            )

            holder.apply {
                signalImage.setImageBitmap(bitmap)
                xButton.setOnClickListener {
                    onDeleteImageListener?.invoke(position)
                }
            }
        }
    }

    override fun getItemCount() = signalImagesList.size
}