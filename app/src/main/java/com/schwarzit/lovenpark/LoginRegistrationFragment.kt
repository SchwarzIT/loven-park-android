package com.schwarzit.lovenpark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.schwarzit.lovenpark.databinding.FragmentLoginRegistrationBinding

class LoginRegistrationFragment : Fragment() {
    private var binding: FragmentLoginRegistrationBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginRegistrationBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            btnSignInWithEmail.setOnClickListener {
                navigateToLogin()
            }
            btnRegistration.setOnClickListener {
                navigateToRegistration()
            }
            btnCloseTopLeft.setOnClickListener {
                navigateToMaps()
            }
        }
    }

    private fun navigateToLogin() =
        findNavController().navigate(LoginRegistrationFragmentDirections.actionLoginRegistrationFragmentToLoginFragment())

    private fun navigateToRegistration() =
        findNavController().navigate(LoginRegistrationFragmentDirections.actionLoginRegistrationFragmentToRegistrationFragment())

    private fun navigateToMaps() =
        findNavController().navigate(LoginRegistrationFragmentDirections.actionLoginRegistrationFragmentToMapsFragment())

}