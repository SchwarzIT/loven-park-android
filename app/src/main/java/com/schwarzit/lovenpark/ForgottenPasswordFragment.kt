package com.schwarzit.lovenpark

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.schwarzit.lovenpark.databinding.FragmentForgottenPasswordBinding
import com.schwarzit.lovenpark.dialogs.BaseDialog
import com.schwarzit.lovenpark.utils.Util.Companion.PASSWORD_DIALOG_TAG

class ForgottenPasswordFragment : Fragment() {

    private var binding: FragmentForgottenPasswordBinding? = null
    private val viewModelInputData: LoginScreenViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForgottenPasswordBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpLoginButtonState()
        setLoginButtonClickListener()
    }

    private fun setUpLoginButtonState() {
        addTextChangeListeners()
        binding?.apply {
            btnConfirmForgottenPassword.isEnabled =
                etForgottenPasswordEmail.text?.isNotEmpty() == true && viewModelInputData.isEmailValid(
                    etForgottenPasswordEmail.text.toString()
                )

            viewModelInputData.onInputEmailChanged(
                etForgottenPasswordEmail.text.toString()
            )

            viewModelInputData.emailValidationLiveData.observe(viewLifecycleOwner) { isValidEmail ->
                btnConfirmForgottenPassword.isEnabled = isValidEmail
            }
        }
    }

    private fun addTextChangeListeners() {
        binding?.apply {
            val value = object : TextWatcher {
                override fun afterTextChanged(s: Editable) {
                    viewModelInputData.onInputEmailChanged(
                        etForgottenPasswordEmail.text.toString()
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
            etForgottenPasswordEmail.addTextChangedListener(value)
        }
    }

    private fun navigateToLogin() =
        findNavController().navigate(
            ForgottenPasswordFragmentDirections.actionForgottenPasswordFragmentToLoginFragment()
        )

    private fun setLoginButtonClickListener() {
        binding?.apply {
            btnConfirmForgottenPassword.setOnClickListener {
                if (btnConfirmForgottenPassword.isEnabled) {
                    navigateToLogin()
                    showDialog()
                }
            }
        }
    }

    private fun showDialog() {
        val dialog = BaseDialog(
            getString(R.string.forgotten_password_dialog_title),
            null,
            getString(R.string.forgotten_password_dialog_description),
            true,
            false
        )
        dialog.show(parentFragmentManager, PASSWORD_DIALOG_TAG)
    }
}