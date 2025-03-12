package com.schwarzit.lovenpark.about.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.schwarzit.lovenpark.MainActivity
import com.schwarzit.lovenpark.R
import com.schwarzit.lovenpark.about.Article
import com.schwarzit.lovenpark.about.detail.ArticleImage
import com.schwarzit.lovenpark.databinding.FragmentAboutSectionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutSectionFragment : Fragment(), ArticleButtonListener {
    private lateinit var binding: FragmentAboutSectionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAboutSectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val articles = showArticles()
        (activity as MainActivity).apply {
            findViewById<TextView>(R.id.fragment_title)?.text = getString(R.string.about_park)
            findViewById<TextView>(R.id.fragment_help_text)?.text = getString(R.string.about_learn_more_invitation)
            findViewById<TextView>(R.id.fragment_help_text)?.isVisible = true
        }
        binding.rvArticles.apply {
            adapter = ArticleAdapter(articles, this@AboutSectionFragment)
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun showArticles() = listOf(
        Article(
            1,
            R.drawable.article_topic_history,
            getString(R.string.article_history_title),
            getString(R.string.article_history_introduction),
            getString(R.string.article_history_description_upper_section),
            getString(R.string.article_history_description_lower_section),
            listOf(
                ArticleImage(
                    1,
                    R.drawable.about_loven_park_history1
                ),
                ArticleImage(
                    2,
                    R.drawable.about_loven_park_history2
                )
            )
        ),
        Article(
            2,
            R.drawable.article_topic_preservation,
            getString(R.string.article_preservation_title),
            getString(R.string.article_preservation_introduction),
            getString(R.string.article_preservation_description_upper_section),
            getString(R.string.article_preservation_description_lower_section),
            listOf(
                ArticleImage(
                    5,
                    R.drawable.about_cleaning_park_1
                ),
                ArticleImage(
                    6,
                    R.drawable.about_cleaning_park_2
                ),
                ArticleImage(
                    7,
                    R.drawable.about_cleaning_park_3
                ),
                ArticleImage(
                    8,
                    R.drawable.about_cleaning_park_4
                ),
                ArticleImage(
                    9,
                    R.drawable.about_cleaning_park_5
                )
            )
        ),
        Article(
            3,
            R.drawable.article_topic_wildlife,
            getString(R.string.article_wildlife_title),
            getString(R.string.article_wildlife_introduction),
            getString(R.string.article_wildlife_description_upper_section),
            getString(R.string.article_wildlife_description_lower_section),
            listOf(
                ArticleImage(
                    3,
                    R.drawable.about_fauna
                ),
                ArticleImage(
                    4,
                    R.drawable.about_flora
                )
            )
        ),

    )

    override fun onArticleClick(selectedArticle: Article) {
        findNavController().navigate(
            AboutSectionFragmentDirections.actionAboutSectionFragmentToArticleDetailFragment(
                selectedArticle
            )
        )
    }

}