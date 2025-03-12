package com.schwarzit.lovenpark.profile.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.schwarzit.lovenpark.FeatureFlags
import com.schwarzit.lovenpark.MainActivity
import com.schwarzit.lovenpark.NetworkLiveData
import com.schwarzit.lovenpark.R
import com.schwarzit.lovenpark.databinding.FragmentReportsBinding
import com.schwarzit.lovenpark.dialogs.NoInternetDialog
import com.schwarzit.lovenpark.profile.signals.view.overview.OthersSignalsFragment
import com.schwarzit.lovenpark.profile.signals.view.overview.UserSignalsFragment
import com.schwarzit.lovenpark.profile.viewmodel.UserProfileViewModel
import com.schwarzit.lovenpark.user.google.GoogleSignInRepository
import com.schwarzit.lovenpark.user.socialLogin.SocialUserSharedPrefs
import com.schwarzit.lovenpark.utils.Util.Companion.DEFAULT_TAB_POSITION
import com.schwarzit.lovenpark.utils.Util.Companion.NETWORK_DIALOG_TAG
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportsFragment : Fragment() {

    private lateinit var binding: FragmentReportsBinding
    private val userProfileViewModel: UserProfileViewModel by viewModels()
    private val checkConnection by lazy { context?.let { NetworkLiveData(it) } }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReportsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkConnection?.observe(viewLifecycleOwner) { isConnected ->

            if (FeatureFlags.FLAG_LOGIN_REGISTRATION.isActive) {
                context?.let {
                    userProfileViewModel.requestUserWhenOffline(it)
                }
            } else {
                if (isConnected) {
                    userProfileViewModel.getUserWhenOnline()
                } else {
                    val dialog = NoInternetDialog()
                    dialog.show(parentFragmentManager, NETWORK_DIALOG_TAG)
                }
            }
            userProfileViewModel.liveDataUser.observe(viewLifecycleOwner) { user ->
                if (user?.firstName.isNullOrEmpty()) {
                    SocialUserSharedPrefs.removeUser(requireContext())
                    GoogleSignInRepository(requireContext()).signOut()
                    navigateToSocialLoginFragment()
                }
            }
        }

        binding.btnCreateSignal.setOnClickListener {
            navigateToSignal()
        }

        binding.tabLayout.setSelectedTabIndicatorColor(resources.getColor(R.color.middle_green_yellow))
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 0) {
                    childFragmentManager.beginTransaction()
                        .replace(R.id.flSignals, UserSignalsFragment()).commit()
                } else {
                    childFragmentManager.beginTransaction()
                        .replace(R.id.flSignals, OthersSignalsFragment())
                        .commit(); }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}

        })
    }

    override fun onResume() {
        super.onResume()
        /*
        Sets the default tab and its corresponding fragment
         */
        (activity as MainActivity).apply {
            findViewById<TextView>(R.id.fragment_title)?.text = getString(R.string.user_profile_reports)
            findViewById<TextView>(R.id.fragment_help_text)?.apply {
                text = getString(R.string.user_profile_reports_description)
                isVisible = true
            }
        }
        binding.tabLayout.selectTab(binding.tabLayout.getTabAt(DEFAULT_TAB_POSITION))
        childFragmentManager.beginTransaction()
            .replace(R.id.flSignals, UserSignalsFragment()).commit()
    }

    private fun navigateToMap() {
        findNavController().navigate(ReportsFragmentDirections.actionReportsFragmentToGoogleMapFragment())
    }

    private fun navigateToSignal() {
        findNavController().navigate(ReportsFragmentDirections.actionReportsFragmentToSignalRootFragment())
    }

    private fun navigateToUserProfileSettings() {
        findNavController().navigate(ReportsFragmentDirections.actionReportsFragmentToUserProfileSettingsFragment())
    }

    private fun navigateToSocialLoginFragment() {
        findNavController().navigate(ReportsFragmentDirections.actionReportsFragmentToSocialLoginFragment())
    }

}