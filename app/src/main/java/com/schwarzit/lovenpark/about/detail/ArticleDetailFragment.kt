package com.schwarzit.lovenpark.about.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.schwarzit.lovenpark.MainActivity
import com.schwarzit.lovenpark.R
import com.schwarzit.lovenpark.about.Article
import com.schwarzit.lovenpark.databinding.FragmentArticleDetailBinding
import com.schwarzit.lovenpark.utils.Util.Companion.ABOUT_IMAGE_DIALOG_TAG
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleDetailFragment : Fragment(), ArticleImageClickListener {
    private lateinit var binding: FragmentArticleDetailBinding
    private val args: ArticleDetailFragmentArgs by navArgs()
    lateinit var selectedArticle: Article

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArticleDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectedArticle = args.article

        binding.rvTopicPhotos.apply {
            adapter = ArticleImageAdapter(selectedArticle.topicImages, this@ArticleDetailFragment)
            layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }

        binding.apply {
            tvTopicDetailsUpperSection.text = selectedArticle.detailsUpperSection
            tvTopicDetailsLowerSection.text = selectedArticle.detailsLowerSection
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).apply {
            findViewById<TextView>(R.id.fragment_title)?.text = selectedArticle.title
            findViewById<TextView>(R.id.fragment_help_text)?.isVisible = false
        }
    }

    override fun onArticleImageClick(selectedArticle: Int) {
        val dialog = AboutDetailImageDialog(selectedArticle, true)
        dialog.show(parentFragmentManager, ABOUT_IMAGE_DIALOG_TAG)
    }
}