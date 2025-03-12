package com.schwarzit.lovenpark.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.view.marginTop
import androidx.fragment.app.DialogFragment
import com.schwarzit.lovenpark.databinding.FragmentBaseDialogBinding
import com.schwarzit.lovenpark.utils.Util.Companion.EMPTY_STRING

open class BaseDialog(
    protected open val title: String,
    protected open val image: Int?,
    protected open val description: String,
    protected open val isCloseVisible: Boolean,
    protected open val isOkVisible: Boolean,
    protected open val isThankYouPopUp: Boolean = false,
    protected open val isNoInternetDialog: Boolean = false
) : DialogFragment() {
    protected lateinit var binding: FragmentBaseDialogBinding

    constructor() : this(EMPTY_STRING, null, EMPTY_STRING, true, false)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBaseDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDialogBackground()
        setDialogButtons()
        setDialogImageDetail()
        if (isThankYouPopUp) {
            setUpThankYouPopUp()
        }
        setDialogText()
        setPopUp()
        if (isNoInternetDialog) {
            setUpNoInternetDialog()
        }
    }

    open fun setUpNoInternetDialog() {
        setDialogText()
    }

    open fun setUpThankYouPopUp() = binding.apply {
        ivThankPopUp.visibility = View.VISIBLE
        ivDialogClose.visibility = View.VISIBLE
        tvDialogTitle.text = title
        tvDialogDescription.text = description
        (tvDialogTitle.layoutParams as ConstraintLayout.LayoutParams).apply {
            topMargin = 8
        }
    }

    open fun setPopUp() {
        binding.apply {
            clButtonContainer.visibility = View.GONE
            tvDialogTitle.text = title
            tvDialogDescription.text = description
        }
    }

    open fun setDialogImageDetail() {
        if (image != null) {
            binding.clDialogImage.isVisible = true
            image?.let { binding.ivImage.setImageResource(it) }
        }

    }

    private fun setDialogBackground() {
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    open fun setDialogText() {
        binding.tvDialogDescription.text = description
        binding.tvDialogTitle.text = title
    }

    private fun setDialogButtons() {
        if (isCloseVisible) {
            binding.ivDialogClose.apply {
                visibility = View.VISIBLE
                setOnClickListener {
                    handleCloseButtonClick()
                }
            }
        } else {
            binding.ivDialogClose.visibility = View.GONE
        }
    }

    protected open fun handleCloseButtonClick() {
        dismiss()
    }
}