package com.schwarzit.lovenpark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.schwarzit.lovenpark.databinding.FragmentTermsAndConditionsBinding

class TermsAndConditionsFragment : Fragment() {
    private var binding: FragmentTermsAndConditionsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTermsAndConditionsBinding.inflate(inflater, container, false)
        return binding?.root
    }

}