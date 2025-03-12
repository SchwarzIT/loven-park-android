package com.schwarzit.lovenpark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.schwarzit.lovenpark.databinding.FragmentUpdateUserProfileBinding

class UpdateUserProfileFragment : Fragment() {

    private var binding: FragmentUpdateUserProfileBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateUserProfileBinding.inflate(inflater, container, false)
        buttonCancelChangesOnClickListener()
        return binding?.root
    }

    private fun buttonCancelChangesOnClickListener() {
        binding?.cancelChangesButton?.setOnClickListener {
            goBack()
        }
    }

    private fun goBack() =
        findNavController().popBackStack()
}