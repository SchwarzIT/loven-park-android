package com.schwarzit.lovenpark.signal

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.schwarzit.lovenpark.R
import com.schwarzit.lovenpark.databinding.CardSignalCategoryBinding

class SignalCategoryAdapter(val context: Context) :
    RecyclerView.Adapter<SignalCategoryAdapter.CardSignalCatViewHolder>() {

    var categorySelectedListener: ((String) -> Unit?)? = null
    private var signalList = emptyList<SignalCategory>()
    private var selectedCategory = ""

    fun setCategoryList(categories: List<SignalCategory>) {
        signalList = categories
        notifyDataSetChanged()
    }

    fun setCategory(aSelectedCat: String) {
        selectedCategory = aSelectedCat
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardSignalCatViewHolder {
        return CardSignalCatViewHolder(
            CardSignalCategoryBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CardSignalCatViewHolder, position: Int) {
        val currentSignalCategory = signalList[position]
        holder.apply {
            clearSelected(this)
            imageViewSignalCategory.setImageResource(currentSignalCategory.signalImage)
            textViewSignalCategory.text = currentSignalCategory.signalCategory
            markSelectedCategory(this)
            cardView.setOnClickListener {
                categorySelectedListener?.invoke(signalList[position].signalCategory)
                imageViewSignalCategory.setBackgroundColor(context.resources.getColor(R.color.black_glaze_transparent))
                markSelectedCategory(this)
            }
        }
    }

    private fun markSelectedCategory(holder: CardSignalCatViewHolder) {
        holder.apply {
            if (textViewSignalCategory.text == selectedCategory) {
                cardView.background = context.resources.getDrawable(R.drawable.round_category_selected)
                checkBox.setImageResource(R.drawable.signal_check_box_checked)
            }
        }
    }

    private fun clearSelected(holder: CardSignalCatViewHolder) {
        signalList.forEach { _ ->
            holder.apply {
                cardView.background = context.resources.getDrawable(R.drawable.round_category_unselected)
                checkBox.setImageResource(R.drawable.signal_check_box_unchecked)
            }
        }
    }

    override fun getItemCount() = signalList.size

    inner class CardSignalCatViewHolder(val binding: CardSignalCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var imageViewSignalCategory = binding.ivSignalCategory
            private set
        var textViewSignalCategory = binding.tvSignalCategories
            private set
        var cardView = binding.cvCategories
            private set

        var checkBox = binding.ivSignalCheckBox
            private set
    }
}