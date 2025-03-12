package com.schwarzit.lovenpark.about.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.schwarzit.lovenpark.databinding.CardArticleTopicImageBinding

class ArticleImageAdapter(private var imageList: List<ArticleImage>, var onClick: ArticleImageClickListener) :
    RecyclerView.Adapter<ArticleImageAdapter.ArticleImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ArticleImageViewHolder(
        CardArticleTopicImageBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ArticleImageViewHolder, position: Int) {
        val currentImage = imageList[position]
        holder.bindItem(currentImage)
        holder.itemView.setOnClickListener { onClick.onArticleImageClick(imageList[position].topicImage) }
    }

    override fun getItemCount() = imageList.size

    inner class ArticleImageViewHolder(private val imagesBinding: CardArticleTopicImageBinding) :
        RecyclerView.ViewHolder(imagesBinding.root) {
        fun bindItem(image: ArticleImage) {
            imagesBinding.apply {
                ivTopicPicture.setImageResource(image.topicImage)
            }
            imagesBinding.ivTopicPicture.setOnClickListener {
                onClick.onArticleImageClick(image.topicImage)
            }
        }
    }
}