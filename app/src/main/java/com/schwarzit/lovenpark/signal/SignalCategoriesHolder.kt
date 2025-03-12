package com.schwarzit.lovenpark.signal

import android.content.Context
import com.schwarzit.lovenpark.R

object SignalCategoriesHolder {

    fun getSignalCategories(context: Context) = listOf(
        SignalCategory(
            context.getString(R.string.signal_category_uncollected_waste),
            R.drawable.ic_signal_can_litter
        ),
        SignalCategory(
            context.getString(R.string.signal_category_injured_tree),
            R.drawable.signal_cat_tree
        ),
        SignalCategory(
            context.getString(R.string.signal_category_damaged_park_furniture),
            R.drawable.signal_cat_furniture
        ),
        SignalCategory(
            context.getString(R.string.signal_category_animal_carcasses_found),
            R.drawable.ic_signal_dead_animal
        ),
        SignalCategory(
            context.getString(R.string.signal_category_other),
            R.drawable.ic_signal_other
        )
    )
}