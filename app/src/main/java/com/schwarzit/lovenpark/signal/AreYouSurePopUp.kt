package com.schwarzit.lovenpark.signal

import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import com.schwarzit.lovenpark.R
import com.schwarzit.lovenpark.dialogs.BaseDialog

class AreYouSurePopUp(
    override val isCloseVisible: Boolean = true
): BaseDialog() {
    var onPopUpDiscardListener: (() -> Unit)? = null
    var onPopUpContinueListener: (() -> Unit)? = null
    var titleText : String? = null
    var descriptionText : String? = null

    override fun setPopUp() {
        super.setPopUp()
        binding.apply {
            clButtonContainer.visibility = View.VISIBLE
            tvDialogTitle.text =
                if (titleText == null)
                    context?.getString(R.string.signal_confirm_dialog_title)
                else
                    titleText
            tvDialogDescription.text =
                if (descriptionText == null)
                    context?.getString(R.string.signal_confirm_dialog_description)
                else
                    descriptionText

            buttonConfirm.apply {
                text = context?.getString(R.string.dialog_continue)
                icon = context?.let { AppCompatResources.getDrawable(it, R.drawable.ic_check) }
                setOnClickListener {
                    onPopUpContinueListener?.invoke()
                }
            }


            buttonSkip.apply {
                text = context?.getString(R.string.dialog_discard)
                icon = context?.let { AppCompatResources.getDrawable(it, R.drawable.ic_trash) }
                setOnClickListener {
                    onPopUpDiscardListener?.invoke()
                }
            }

            if (isCloseVisible.not()) {
                ivDialogClose.visibility = View.GONE
            }
        }
    }
}