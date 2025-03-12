package com.schwarzit.lovenpark

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.util.Log
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.schwarzit.lovenpark.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private var binding: FragmentLoginBinding? = null
    private var googleSignInRepo: GoogleSignInRepository? = null
    private val resultLauncher = activityResultLauncher()

    private val viewModelInputData: LoginScreenViewModel by viewModels()
    private var validEmail: Boolean? = null
    private var isPasswordVisible = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        googleSignInRepo = GoogleSignInRepository(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (googleSignInRepo?.hasLastSignInAccount(requireContext()) == true) {
            onGoogleAccountReceived()
        }
    }

    private var focusChangeListener: View.OnFocusChangeListener =
        View.OnFocusChangeListener { _, _ ->
            binding?.apply {
                if (validEmail == false && etLoginEmail.text?.isNotBlank() == true) {
                    layoutLoginEmail.error = getText(R.string.error_message_invalid_email)
                    etLoginEmail.isActivated = true
                } else {
                    layoutLoginEmail.isErrorEnabled = false
                    etLoginEmail.isActivated = false
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setEmailFieldFocusChangeListener()
        checkPasswordFieldIsEmpty()
        setAsteriskPasswordTransformation()
        setPasswordIconVisibilityClickListener()
        setUpLoginButtonState()
        setLoginButtonClickListener()
        observeLoginSuccess()
        binding?.apply {
            btnGoogleSignIn.setOnClickListener {
                signInWithGoogle()
            }
            context?.let {
                if (viewModelInputData.isUserLogged(it)) {
                    navigateToUserProfile()
                }
            }
            btnForgottenPassword.setOnClickListener {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToForgottenPasswordFragment())
            }
        }
    }

    private fun setEmailFieldFocusChangeListener() {
        binding?.etLoginEmail?.onFocusChangeListener = focusChangeListener
    }

    private fun setUpLoginButtonState() {
        addTextChangeListeners()

        binding?.apply {
            nativeLoginButton.isEnabled =
                etLoginPassword.text?.isNotEmpty() == true
                        && viewModelInputData.isEmailValid(
                    etLoginEmail.text.toString()
                )

            viewModelInputData.onInputDataChanged(
                etLoginEmail.text.toString(),
                etLoginPassword.text.toString()
            )

            viewModelInputData.onInputEmailChanged(binding?.etLoginEmail?.text.toString())
            viewModelInputData.emailValidationLiveData.observe(viewLifecycleOwner) { isValidEmail ->
                validEmail = isValidEmail
            }

            viewModelInputData.buttonStateLiveData.observe(viewLifecycleOwner) { validInputs ->
                nativeLoginButton.isEnabled = validInputs
            }
        }
    }

    private fun addTextChangeListeners() {
        binding?.apply {
            val value = object : TextWatcher {
                override fun afterTextChanged(s: Editable) {

                    viewModelInputData.onInputDataChanged(
                        etLoginEmail.text.toString(),
                        etLoginPassword.text.toString()
                    )

                    viewModelInputData.onInputEmailChanged(etLoginEmail.text.toString())

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
            etLoginEmail.addTextChangedListener(value)
            etLoginPassword.addTextChangedListener(value)
        }
    }

    private fun signInWithGoogle() {
        val intent = googleSignInRepo?.getSignInIntent()
        resultLauncher.launch(intent)
    }

    private fun activityResultLauncher() =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                onGoogleAccountReceived()
            }
        }

    //todo - to implement when app design is completed
    private fun onGoogleAccountReceived() {
    }

    private fun setAsteriskPasswordTransformation() {
        val transformedPassword = AsteriskPasswordChange()
        binding?.etLoginPassword?.transformationMethod = transformedPassword

    }

    private fun navigateToUserProfile() =
        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToUserProfileFragment())

    private fun setLoginButtonClickListener() {
        binding?.apply {
            nativeLoginButton.setOnClickListener {
                context?.let {
                    viewModelInputData.loginUser(
                        it,
                        etLoginEmail.text.toString(),
                        etLoginPassword.text.toString()
                    )
                }
            }
        }
    }

    private fun checkPasswordFieldIsEmpty() {
        binding?.apply {
            viewModelInputData.passwordLiveData.observe(viewLifecycleOwner) { isEmpty ->
                if (isEmpty) {
                    layoutLoginPassword.setEndIconDrawable(R.drawable.ic_password_key)

                } else {
                    layoutLoginPassword.setEndIconDrawable(R.drawable.selector_password_end_icon)
                }
            }
        }
    }

    private fun setPasswordIconVisibilityClickListener() {
        binding?.apply {
            layoutLoginPassword.setEndIconOnClickListener {
                if (isPasswordVisible) {
                    isPasswordVisible = false
                    layoutLoginPassword.isSelected = false
                    etLoginPassword.transformationMethod = AsteriskPasswordChange()

                } else {
                    isPasswordVisible = true
                    layoutLoginPassword.isSelected = true
                    etLoginPassword.transformationMethod = HideReturnsTransformationMethod()
                }
            }
        }
    }

    private fun observeLoginSuccess() {
        viewModelInputData.loginSuccessLiveData
            .observe(viewLifecycleOwner) { isUserLogged ->
                if (isUserLogged == true) {
                    navigateToUserProfile()
                }
            }
    }
}