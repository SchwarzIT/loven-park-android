package com.schwarzit.lovenpark.about.overview

import com.schwarzit.lovenpark.about.Article

interface ArticleButtonListener {
    fun onArticleClick(selectedArticle: Article)
}