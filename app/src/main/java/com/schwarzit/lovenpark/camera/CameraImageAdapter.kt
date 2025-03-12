package com.schwarzit.lovenpark.camera

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.schwarzit.lovenpark.R
import com.schwarzit.lovenpark.databinding.CardCameraImageBinding

class CameraImageAdapter(private val context: Context) :
    RecyclerView.Adapter<CameraImageViewHolder>() {

    var onDeleteImageListener: ((Int) -> Unit?)? = null
    var cameraImagesList = mutableListOf<Uri>()
    var path: String? = null

    fun setImageList(list: MutableList<Uri>) {
        cameraImagesList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CameraImageViewHolder(
            CardCameraImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: CameraImageViewHolder, position: Int) {
        context.let {

            val bitmap = BitmapProcessor(it).processBitmap(
                cameraImagesList[position],
                it.resources.getDimension(R.dimen.gallery_image_required_size).toInt(),
                true
            ) ?: return

            holder.apply {
                signalCameraImage.setImageBitmap(bitmap)
                xButton.setOnClickListener {
                    onDeleteImageListener?.invoke(position)
                }
            }
        }
    }

    override fun getItemCount() = cameraImagesList.size
}