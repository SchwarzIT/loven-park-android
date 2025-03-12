package com.schwarzit.lovenpark

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.schwarzit.lovenpark.databinding.FragmentRegistrationBinding
import com.schwarzit.lovenpark.registration.model.*

class RegistrationFragment : Fragment() {

    private var binding: FragmentRegistrationBinding? = null
    private val viewModelRegistrationInputData: RegisterScreenViewModel by viewModels()

    private var focusChangeListener: View.OnFocusChangeListener =
        View.OnFocusChangeListener { _, _ ->
            binding?.apply {
                if (viewModelRegistrationInputData.isNameValid(etRegistrationName.text.toString()) == false) {
                    layoutRegistrationName.error = getText(R.string.error_message_invalid_name)
                    etRegistrationName.isActivated = true
                } else {
                    val isInputValid =
                        viewModelRegistrationInputData.isNameValid(etRegistrationName.text.toString()) == true
                    registrationInputValidOrEmpty(
                        layoutRegistrationName,
                        etRegistrationName,
                        isInputValid,
                        R.drawable.ic_profile_registration
                    )

                }
                if (viewModelRegistrationInputData.isEmailValid(etRegistrationEmail.text.toString()) == false) {
                    layoutRegistrationEmail.error = getText(R.string.error_message_invalid_email)
                    etRegistrationEmail.isActivated = true
                } else {
                    val isInputValid =
                        viewModelRegistrationInputData.isEmailValid(etRegistrationEmail.text.toString()) == true
                    registrationInputValidOrEmpty(
                        layoutRegistrationEmail,
                        etRegistrationEmail,
                        isInputValid,
                        R.drawable.ic_mail_registration
                    )
                }
                if (viewModelRegistrationInputData.isPhoneNumberValid(etRegistrationPhoneNumber.text.toString()) == false) {
                    layoutRegistrationPhoneNumber.error =
                        getText(R.string.error_message_invalid_phone_number)
                    etRegistrationPhoneNumber.isActivated = true
                } else {
                    val isInputValid =
                        viewModelRegistrationInputData.isPhoneNumberValid(etRegistrationPhoneNumber.text.toString()) == true
                    registrationInputValidOrEmpty(
                        layoutRegistrationPhoneNumber,
                        etRegistrationPhoneNumber,
                        isInputValid,
                        R.drawable.ic_call_registration
                    )
                }
                if (viewModelRegistrationInputData.isPasswordValid(etRegistrationPassword.text.toString()) == false) {
                    layoutRegistrationPassword.error =
                        getText(R.string.error_message_invalid_password)
                    etRegistrationPassword.isActivated = true
                } else {
                    val isInputValid =
                        viewModelRegistrationInputData.isPasswordValid(etRegistrationPassword.text.toString()) == true
                    registrationInputValidOrEmpty(
                        layoutRegistrationPassword,
                        etRegistrationPassword,
                        isInputValid,
                        R.drawable.ic_lock
                    )
                }

                btnConfirmRegistration.isEnabled =
                    viewModelRegistrationInputData.isAllInputValid(
                        etRegistrationName.text.toString(),
                        etRegistrationEmail.text.toString(),
                        etRegistrationPhoneNumber.text.toString(),
                        etRegistrationPassword.text.toString(),
                        checkboxTermsAndConditions.isChecked
                    )
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        setRegistrationFieldsFocusChangeListener()
        asteriskPasswordTransformation()
        setRegistrationButtonState()
        setRegistrationButtonClickListener()
        observeRegisterSuccess()
        return binding?.root
    }

    private fun setRegistrationButtonState() {
        addTextChangeListeners()
        binding?.apply {
            btnConfirmRegistration.isEnabled = viewModelRegistrationInputData.isAllInputValid(
                etRegistrationName.text.toString(),
                etRegistrationEmail.text.toString(),
                etRegistrationPhoneNumber.text.toString(),
                etRegistrationPassword.text.toString(),
                checkboxTermsAndConditions.isChecked
            )

            viewModelRegistrationInputData.onInputDataChanged(
                etRegistrationName.text.toString(),
                etRegistrationEmail.text.toString(),
                etRegistrationPhoneNumber.text.toString(),
                etRegistrationPassword.text.toString(),
                checkboxTermsAndConditions.isChecked
            )

            viewModelRegistrationInputData.registerButtonStateLiveData.observe(viewLifecycleOwner) { validInputs ->
                btnConfirmRegistration.isEnabled = validInputs
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let {
            if (viewModelRegistrationInputData.isUserRegistered(it)) {
                navigateToMaps()
            }
        }
        binding?.checkboxTermsAndConditions?.setOnClickListener {
            setRegistrationButtonState()
        }
        binding?.tvTermsAndConditions?.setOnClickListener {
            findNavController().navigate(RegistrationFragmentDirections.actionRegistrationFragmentToTermsAndConditionsFragment())
        }

    }

    private fun addTextChangeListeners() {
        binding?.apply {
            val value = object : TextWatcher {
                override fun afterTextChanged(s: Editable) {
                    viewModelRegistrationInputData.onInputDataChanged(
                        etRegistrationName.text.toString(),
                        etRegistrationEmail.text.toString(),
                        etRegistrationPhoneNumber.text.toString(),
                        etRegistrationPassword.text.toString(),
                        checkboxTermsAndConditions.isChecked
                    )
                }

                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                }
            }
            etRegistrationName.addTextChangedListener(value)
            etRegistrationEmail.addTextChangedListener(value)
            etRegistrationPhoneNumber.addTextChangedListener(value)
            etRegistrationPassword.addTextChangedListener(value)
        }
    }

    private fun setRegistrationFieldsFocusChangeListener() {
        binding?.apply {
            etRegistrationName.onFocusChangeListener = focusChangeListener
            etRegistrationEmail.onFocusChangeListener = focusChangeListener
            etRegistrationPhoneNumber.onFocusChangeListener = focusChangeListener
            etRegistrationPassword.onFocusChangeListener = focusChangeListener
        }
    }

    private fun setRegistrationButtonClickListener() {
        binding?.btnConfirmRegistration?.setOnClickListener {
            viewModelRegistrationInputData.registerUser(requireContext(), getUserInput())
        }
    }

    private fun navigateToMaps() =
        findNavController().navigate(RegistrationFragmentDirections.actionRegistrationFragmentToGoogleMapFragment())

    private fun getUserInput() = User(
        Name(binding?.etRegistrationName?.text.toString()),
        Email(binding?.etRegistrationEmail?.text.toString()),
        PhoneNumber(binding?.etRegistrationPhoneNumber?.text.toString()),
        Password(binding?.etRegistrationPassword?.text.toString()),
    )

    private fun observeRegisterSuccess() {
        viewModelRegistrationInputData.registerSuccessLiveData
            .observe(viewLifecycleOwner) { isUserRegistered ->
                if (isUserRegistered == true) {
                    navigateToMaps()
                }
            }
    }

    private fun asteriskPasswordTransformation() {
        val transformedPassword = AsteriskPasswordChange()
        binding?.etRegistrationPassword?.transformationMethod = transformedPassword
    }

    private fun registrationInputValidOrEmpty(
        textInputLayout: TextInputLayout,
        textInputEditText: TextInputEditText,
        isInputValid: Boolean, drawable: Int
    ) {
        textInputLayout.isErrorEnabled = false
        textInputEditText.isActivated = false
        if (isInputValid) {
            textInputLayout.setEndIconDrawable(R.drawable.ic_tick_square_green)
            textInputLayout.setEndIconTintList(null)
        } else {
            textInputLayout.setEndIconDrawable(drawable)
        }
    }
}
