package com.schwarzit.lovenpark.profile.view

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.schwarzit.lovenpark.FeatureFlags
import com.schwarzit.lovenpark.MainActivity
import com.schwarzit.lovenpark.R
import com.schwarzit.lovenpark.data.utils.UserSharedPrefHelper
import com.schwarzit.lovenpark.databinding.FragmentUserAccountBinding
import com.schwarzit.lovenpark.profile.viewmodel.UserProfileSettingsViewModel
import com.schwarzit.lovenpark.signal.AreYouSurePopUp
import com.schwarzit.lovenpark.signal.SIGNAL_CONFIRM_DIALOG_TAG
import com.schwarzit.lovenpark.user.google.GoogleSignInRepository
import com.schwarzit.lovenpark.user.socialLogin.SocialUserSharedPrefs
import com.schwarzit.lovenpark.utils.Util.Companion.RATE_APP_MARKET_STRING
import com.schwarzit.lovenpark.utils.Util.Companion.RATE_APP_PACKAGE_STRING
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserAccountFragment : Fragment() {

    private var binding: FragmentUserAccountBinding? = null

    private val userProfileSettingsViewModel: UserProfileSettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserAccountBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).apply {
            findViewById<TextView>(R.id.fragment_title)?.text = getString(R.string.profile)
            findViewById<TextView>(R.id.fragment_help_text)?.text =
                getString(R.string.user_profile_description)
            findViewById<TextView>(R.id.fragment_help_text)?.isVisible = true
        }
        setAccountOptions()
        checkFlagStatus()
        setEditButtonClickListener()
    }

    override fun onResume() {
        super.onResume()
        checkFlagStatus()
    }

    private fun setAccountOptions() {
        binding?.apply {
            reports.bind(
                icon = R.drawable.ic_megaphone,
                text = getString(R.string.reports),
                onClickListener = View.OnClickListener {
                    navigateToReports()
                }
            )
            savedFavouritesPins.bind(
                icon = R.drawable.ic_bookmark,
                text = getString(R.string.favourites),
                onClickListener = View.OnClickListener {
                    navigateToBookmarks()
                }
            )
            privacyPolicy.bind(
                icon = R.drawable.ic_legal_document,
                text = getString(R.string.privacy_policy_and_terms),
                onClickListener = View.OnClickListener {
                    navigateToPrivacyPolicyAndTermsAndConditions()
                }
            )
            appRating.bind(
                icon = R.drawable.ic_edit,
                text = getString(R.string.user_profile_setting_app_rating),
                onClickListener = View.OnClickListener { launchRateAppIntentInGoogleStore() }
            )
            logout.bind(
                icon = R.drawable.ic_log_out,
                text = getString(R.string.log_out),
                onClickListener = View.OnClickListener {
                    onLogOutClickListener()
                }
            )
            deleteUserProfile.bind(
                icon = R.drawable.ic_trashcan_red,
                iconTint = R.color.loven_red,
                text = getString(R.string.user_profile_setting_delete_account),
                textColor = R.color.loven_red,
                onClickListener = { showDeleteDialog() }
            )

        }
    }

    private fun onLogOutClickListener() {
        context?.let { context ->
            if (FeatureFlags.FLAG_LOGIN_REGISTRATION.isActive) {
                UserSharedPrefHelper.removeUserData(context)
                navigateToMap()
            } else {
                SocialUserSharedPrefs.removeUser(context)
                GoogleSignInRepository(context).signOut()
            }
            navigateToMap()
        }
    }

    private fun showDeleteDialog() {
        val areYouSurePopUp = AreYouSurePopUp()
        areYouSurePopUp.descriptionText = getString(R.string.user_profile_settings_delete_dialog_)
        areYouSurePopUp.onPopUpContinueListener = {
            userProfileSettingsViewModel.deleteProfile()
            userProfileSettingsViewModel.liveDataDeletedUser.observe(viewLifecycleOwner) { isDeleted ->
                if (isDeleted) {
                    SocialUserSharedPrefs.removeUser(requireContext())
                    GoogleSignInRepository(requireContext()).signOut()
                    navigateToMap()
                }
            }
            areYouSurePopUp.dismiss()
        }
        areYouSurePopUp.onPopUpDiscardListener = {
            areYouSurePopUp.dismiss()
        }
        areYouSurePopUp.show(parentFragmentManager, SIGNAL_CONFIRM_DIALOG_TAG)
    }

    private fun setEditButtonClickListener() {
        binding?.apply {
            editButton.setOnClickListener {
                navigateToUserAccountSettings()
            }
        }
    }

    private fun checkFlagStatus() {
        if (FeatureFlags.FLAG_LOGIN_REGISTRATION.isActive) {
            setUserDataRealm()
        } else {
            setUserDataRequest()
        }
    }

    private fun setUserDataRealm() {
        userProfileSettingsViewModel.requestLoggedUserRealm(requireContext())
        userProfileSettingsViewModel.loggedUserRealmLiveData.observe(viewLifecycleOwner) { user ->
            binding?.apply {
                userName.text = user.name.name
            }
        }
    }

    private fun setUserDataRequest() {
        userProfileSettingsViewModel.requestLoggedUser()
        userProfileSettingsViewModel.loggedUserLiveData.observe(viewLifecycleOwner) { user ->
            binding?.apply {
                userName.text = user.fullName
            }
        }
    }

    private fun navigateToBookmarks() {
        findNavController().navigate(UserAccountFragmentDirections.actionUserAccountFragmentToBookmarksFragment())
    }

    private fun navigateToUserAccountSettings() {
        findNavController().navigate(UserAccountFragmentDirections.actionUserAccountFragmentToUserAccountSettingsFragment())
    }

    private fun navigateToPrivacyPolicyAndTermsAndConditions() {
        findNavController().navigate(UserAccountFragmentDirections.actionUserAccountFragmentToPrivacyAndPolicyFragment())
    }

    private fun navigateToMap() {
        findNavController().navigate(UserAccountFragmentDirections.actionUserAccountFragmentToGoogleMapFragment())
    }

    private fun navigateToReports() {
        findNavController().navigate(UserAccountFragmentDirections.actionUserAccountFragmentToReportsFragment())
    }

    private fun launchRateAppIntentInGoogleStore() {
        val uri: Uri = Uri.parse(RATE_APP_MARKET_STRING + activity?.packageName)
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        goToMarket.addFlags(
            Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        )
        context?.let {
            try {
                ContextCompat.startActivity(it, goToMarket, null)
            } catch (e: ActivityNotFoundException) {
                ContextCompat.startActivity(
                    it,
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(RATE_APP_PACKAGE_STRING + activity?.packageName)
                    ), null
                )
            }
        }
    }
}