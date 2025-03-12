package com.schwarzit.lovenpark.profile.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.schwarzit.lovenpark.databinding.FragmentDeleteProfileDialogBinding

class DeleteProfileDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentDeleteProfileDialogBinding
    var dialogConfirmButton: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDeleteProfileDialogBinding.inflate(inflater, container, false)
        setBackground()
        setPassButtonClickListener()
        setConfirmButtonClickListener()
        return binding.root
    }

    private fun setConfirmButtonClickListener() {
        binding.buttonConfirm.setOnClickListener {
            dialogConfirmButton?.invoke()
            dismiss()
        }
    }

    private fun setPassButtonClickListener() {
        binding.buttonSkip.setOnClickListener {
            dismiss()
        }
    }

    private fun setBackground() {
        val window = dialog?.window ?: return
        window.setBackgroundDrawableResource(android.R.color.transparent)
    }

}