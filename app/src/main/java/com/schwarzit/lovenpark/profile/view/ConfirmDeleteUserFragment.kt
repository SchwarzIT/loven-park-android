package com.schwarzit.lovenpark.profile.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.schwarzit.lovenpark.databinding.DialogConfirmDeleteUserBinding


class ConfirmDeleteUserFragment : BottomSheetDialogFragment() {

    private var binding: DialogConfirmDeleteUserBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogConfirmDeleteUserBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            btnConfirmDeleteUser.setOnClickListener {
                //TODO: Delete logged user and navigate to home screen
            }
            btnDeclineDeleteUser.setOnClickListener {
                navigateToUserProfileFragment()
            }
        }
    }

    private fun navigateToUserProfileFragment() =
        findNavController().navigate(ConfirmDeleteUserFragmentDirections.actionConfirmDeleteUserFragmentToUserProfileFragment())
}
