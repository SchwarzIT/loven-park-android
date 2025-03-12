package com.schwarzit.lovenpark.about

import com.schwarzit.lovenpark.about.detail.ArticleImage
import java.io.Serializable

data class Article(
    val id: Long,
    val topicImage: Int,
    val title: String,
    val introduction: String,
    val detailsUpperSection: String,
    val detailsLowerSection: String,
    val topicImages: List<ArticleImage>,
) : Serializable