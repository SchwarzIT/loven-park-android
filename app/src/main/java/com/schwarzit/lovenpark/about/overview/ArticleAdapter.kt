package com.schwarzit.lovenpark.about.overview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.schwarzit.lovenpark.about.Article
import com.schwarzit.lovenpark.databinding.CardArticlesBinding

class ArticleAdapter(private var articleList: List<Article>, var onClick: ArticleButtonListener) :
    RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ArticleViewHolder(
        CardArticlesBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bindItem(articleList[position])
        holder.itemView.setOnClickListener { onClick.onArticleClick(articleList[position]) }
    }


    override fun getItemCount(): Int {
        return articleList.size
    }

    inner class ArticleViewHolder(private val articlesBinding: CardArticlesBinding) :
        RecyclerView.ViewHolder(articlesBinding.root) {
        fun bindItem(article: Article) {
            articlesBinding.apply {
                ivTopic.setImageResource(article.topicImage)
                tvTitle.text = article.title
                tvIntroduction.text = article.introduction

                btnLearnMore.setOnClickListener {
                    onClick.onArticleClick(article)
                }
            }
        }
    }

}