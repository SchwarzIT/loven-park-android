package com.schwarzit.lovenpark.about.detail

import androidx.core.view.isVisible
import com.schwarzit.lovenpark.dialogs.BaseDialog

class AboutDetailImageDialog(
    override val image: Int,
    override val isCloseVisible: Boolean,
) : BaseDialog() {

    override fun setDialogImageDetail() {
        binding.clDialogImage.isVisible = true
        binding.ivImage.setImageResource(image)

    }
}