package com.schwarzit.lovenpark.signal


import android.view.View
import android.view.ViewGroup
import com.schwarzit.lovenpark.databinding.FragmentBaseDialogBinding
import com.schwarzit.lovenpark.dialogs.BaseDialog

class SignalSubmissionDialog(
    override val title: String,
    override val description: String,
    override val isCloseVisible: Boolean,
    override val isOkVisible: Boolean,
    override val isThankYouPopUp: Boolean
) : BaseDialog() {

    var onConfirmCloseListener: (() -> Unit)? = null

    override fun handleCloseButtonClick() {
        super.handleCloseButtonClick()
        onConfirmCloseListener?.invoke()
    }

    override fun setUpThankYouPopUp(): FragmentBaseDialogBinding {
        binding.apply {
            ivDialogClose.setOnClickListener {
                onConfirmCloseListener?.invoke()
                dismiss()
            }
            buttonOk.apply {
                visibility = View.VISIBLE
                setOnClickListener {
                    onConfirmCloseListener?.invoke()
                    dismiss()
                }
            }
        }
        return super.setUpThankYouPopUp()
    }
}