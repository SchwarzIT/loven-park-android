package com.schwarzit.lovenpark.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView
import com.schwarzit.lovenpark.databinding.AccountOptionBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AccountOption @Inject constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : MaterialCardView(context, attrs) {

    private val binding by lazy {
        AccountOptionBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun bind(
        icon: Int,
        iconTint: Int? = null,
        text: String,
        textColor: Int? = null,
        onClickListener: OnClickListener
    ) {
        binding.apply {
            iconAndTextButton.icon = ContextCompat.getDrawable(context, icon)
            if (iconTint != null) iconAndTextButton.setIconTintResource(iconTint)
            iconAndTextButton.text = text
            if (textColor != null) iconAndTextButton.setTextColor(context.getColor(textColor))
            this.root.setOnClickListener(onClickListener)
        }
    }


}