package com.schwarzit.lovenpark.profile.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.textfield.TextInputLayout
import com.schwarzit.lovenpark.FeatureFlags
import com.schwarzit.lovenpark.MainActivity
import com.schwarzit.lovenpark.R
import com.schwarzit.lovenpark.databinding.FragmentUserAccountSettingsBinding
import com.schwarzit.lovenpark.dialogs.BaseDialog
import com.schwarzit.lovenpark.profile.viewmodel.UserProfileSettingsViewModel
import com.schwarzit.lovenpark.utils.Util.Companion.PASSWORD_DIALOG_TAG
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserAccountSettingsFragment : Fragment() {

    private var binding: FragmentUserAccountSettingsBinding? = null

    private val userProfileSettingsViewModel: UserProfileSettingsViewModel by viewModels()

    private var focusChangeListener: View.OnFocusChangeListener =
        View.OnFocusChangeListener { _, _ ->
            binding?.apply {
                if (userProfileSettingsViewModel.isNameValid(etUserName.text.toString()) == false) {
                    userName.error = getText(R.string.error_message_invalid_name)
                } else {
                    val isInputValid =
                        userProfileSettingsViewModel.isNameValid(etUserName.text.toString()) == true
                    userInputValidOrEmpty(userName, isInputValid)
                }
                if (userProfileSettingsViewModel.isPhoneNumberValid(etUserPhone.text.toString()) == false) {
                    userPhoneNumber.error = getText(R.string.error_message_invalid_phone_number)
                } else {
                    val isInputValid =
                        userProfileSettingsViewModel.isPhoneNumberValid(etUserPhone.text.toString()) == true
                    userInputValidOrEmpty(userPhoneNumber, isInputValid)
                }
                saveChanges.isEnabled = userProfileSettingsViewModel.isAllInputValid(
                    etUserName.text.toString(),
                    etUserPhone.text.toString()
                )
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserAccountSettingsBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).apply {
            findViewById<TextView>(R.id.fragment_title)?.text = getString(R.string.profile_settings)
            findViewById<TextView>(R.id.fragment_help_text)?.text = getString(R.string.user_profile_description)
            findViewById<TextView>(R.id.fragment_help_text)?.isVisible = true
        }
        checkFlagStatus()
        setUserInfoFieldsChangeListener()
        addOnTextChangeListeners()
        setSaveChangesButtonState()
        setDialogState()
    }

    private fun setDialogState() {
        userProfileSettingsViewModel.liveDataChangedUser.observe(viewLifecycleOwner) { changedData ->
            if (changedData) {
                showUpdatedUserDialog()
            }
        }
    }

    private fun setSaveChangesButtonState() {
        binding?.apply {
            saveChanges.isEnabled = userProfileSettingsViewModel.isAllInputValid(
                etUserName.text.toString(),
                etUserPhone.text.toString()
            )
            saveChanges.setOnClickListener {
                if (saveChanges.isEnabled) {
                    userProfileSettingsViewModel.requestUpdateUser(
                        etUserEmail.text.toString(),
                        etUserName.text.toString(),
                        etUserPhone.text.toString()
                    )
                }
            }

            userProfileSettingsViewModel.onInputDataChanged(
                etUserName.text.toString(),
                etUserPhone.text.toString()
            )

            userProfileSettingsViewModel.saveChangesButtonStateLiveData.observe(viewLifecycleOwner) { validInputs ->
                saveChanges.isEnabled = validInputs
                        && userProfileSettingsViewModel.isInputDataChanged(
                    etUserName.text.toString(),
                    etUserPhone.text.toString()
                )

            }
        }
    }

    private fun addOnTextChangeListeners() {
        binding?.apply {
            val value = object : TextWatcher {
                override fun beforeTextChanged(
                    p0: CharSequence?,
                    p1: Int,
                    p2: Int,
                    p3: Int
                ) {
                    // empty
                }

                override fun onTextChanged(
                    p0: CharSequence?,
                    p1: Int,
                    p2: Int,
                    p3: Int
                ) {
                    // empty
                }

                override fun afterTextChanged(s: Editable) {
                    userProfileSettingsViewModel.onInputDataChanged(
                        etUserName.text.toString(),
                        etUserPhone.text.toString()
                    )
                }

            }
            etUserName.addTextChangedListener(value)
            etUserPhone.addTextChangedListener(value)
        }
    }

    private fun setUserInfoFieldsChangeListener() {
        binding?.apply {
            etUserName.onFocusChangeListener = focusChangeListener
            etUserPhone.onFocusChangeListener = focusChangeListener
        }
    }

    private fun userInputValidOrEmpty(
        textInputLayout: TextInputLayout,
        isInputValid: Boolean,
    ) {
        textInputLayout.isErrorEnabled = false
        if (isInputValid) {
            textInputLayout.setEndIconDrawable(R.drawable.ic_edit_pen)
        }
    }

    private fun checkFlagStatus() {
        if (FeatureFlags.FLAG_LOGIN_REGISTRATION.isActive) {
            binding?.changePasswordLink?.visibility = View.VISIBLE
            setUserDataRealm()
        } else {
            binding?.changePasswordLink?.visibility = View.GONE
            setUserDataRequest()
        }
    }

    private fun setUserDataRealm() {
        userProfileSettingsViewModel.requestLoggedUserRealm(requireContext())
        userProfileSettingsViewModel.loggedUserRealmLiveData.observe(viewLifecycleOwner) { user ->
            binding?.apply {
                etUserName.setText(user.name.name)
                etUserPhone.setText(user.phoneNumber.phoneNumber)
                etUserEmail.setText(user.email.email)
            }
        }
    }

    private fun setUserDataRequest() {
        userProfileSettingsViewModel.requestLoggedUser()
        userProfileSettingsViewModel.loggedUserLiveData.observe(viewLifecycleOwner) { user ->
            binding?.apply {
                etUserName.setText(user.fullName)
                etUserPhone.setText(user.phoneNumber)
                etUserEmail.setText(user.email)
            }
        }
    }


    private fun showUpdatedUserDialog() {
        val dialog = BaseDialog(
            getString(R.string.user_profile_settings_dialog_title),
            null,
            getString(R.string.user_profile_settings_dialog_description),
            true,
            false
        )
        dialog.show(parentFragmentManager, PASSWORD_DIALOG_TAG)
    }

}