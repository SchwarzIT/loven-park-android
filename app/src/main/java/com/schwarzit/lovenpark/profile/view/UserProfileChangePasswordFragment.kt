package com.schwarzit.lovenpark.profile.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.schwarzit.lovenpark.dialogs.BaseDialog
import com.schwarzit.lovenpark.R
import com.schwarzit.lovenpark.databinding.FragmentUserProfileChangePasswordBinding
import com.schwarzit.lovenpark.profile.viewmodel.UserProfileChangePasswordViewModel
import com.schwarzit.lovenpark.utils.Util

class UserProfileChangePasswordFragment : Fragment() {

    private var binding: FragmentUserProfileChangePasswordBinding? = null
    private var userProfileViewModel = UserProfileChangePasswordViewModel()

    private var focusChangeListener: View.OnFocusChangeListener =
        View.OnFocusChangeListener { _, _ ->
            binding?.apply {
                if (etNewPassword.text.toString().isNotEmpty()
                    && !userProfileViewModel.isNewPasswordValid(etNewPassword.text.toString())
                ) {
                    txtInputLayoutNewPassowrd.error =
                        getText(R.string.error_message_invalid_password)
                } else {
                    txtInputLayoutNewPassowrd.isErrorEnabled = false
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserProfileChangePasswordBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setConfirmButtonClickListener()
        setConfirmButtonState()
        setFieldsFocusChangeListener()
    }

    private fun addTextChangeListeners() {
        binding?.apply {
            val value = object : TextWatcher {
                override fun afterTextChanged(s: Editable) {
                    userProfileViewModel.onInputDataChanged(
                        etOldPassword.text.toString(),
                        etNewPassword.text.toString()
                    )
                }

                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                }
            }
            etNewPassword.addTextChangedListener(value)
            etOldPassword.addTextChangedListener(value)
        }
    }

    private fun setFieldsFocusChangeListener() {
        binding?.apply {
            etOldPassword.onFocusChangeListener = focusChangeListener
            etNewPassword.onFocusChangeListener = focusChangeListener
        }
    }

    private fun setConfirmButtonState() {
        addTextChangeListeners()

        binding?.apply {
            btnConfirm.isEnabled =
                etOldPassword.text?.isNotEmpty() == true || etNewPassword.text?.isNotEmpty() == true

            userProfileViewModel.onInputDataChanged(
                etOldPassword.text.toString(),
                etNewPassword.text.toString()
            )

            userProfileViewModel.buttonStateLiveData.observe(viewLifecycleOwner) { validInputs ->
                btnConfirm.isEnabled = validInputs
            }
        }
    }

    private fun setConfirmButtonClickListener() {
        binding?.btnConfirm?.setOnClickListener {
            findNavController().navigateUp()
            showDialog()
        }
    }

    private fun showDialog() {
        val dialog =
            BaseDialog(
                getString(R.string.user_profile_dialog_changed_password_title),
                null,
                getString(R.string.user_profile_dialog_changed_password_description),
                true,
                false
            )
        dialog.show(parentFragmentManager, Util.PASSWORD_DIALOG_TAG)
    }
}