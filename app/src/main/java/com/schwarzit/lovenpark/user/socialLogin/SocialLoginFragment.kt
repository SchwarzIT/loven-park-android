package com.schwarzit.lovenpark.user.socialLogin

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.VideoView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.schwarzit.lovenpark.NetworkLiveData
import com.schwarzit.lovenpark.R
import com.schwarzit.lovenpark.databinding.FragmentSocialLoginBinding
import com.schwarzit.lovenpark.dialogs.NoInternetDialog
import com.schwarzit.lovenpark.onboarding.OnboardingSharedPrefs
import com.schwarzit.lovenpark.user.google.GoogleSignInRepository
import com.schwarzit.lovenpark.utils.Util.Companion.NETWORK_DIALOG_TAG
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SocialLoginFragment : Fragment() {

    private var binding: FragmentSocialLoginBinding? = null
    private var googleSignInRepo: GoogleSignInRepository? = null
    private val resultLauncher = activityResultLauncher()
    private val viewModel: SocialLoginViewModel by activityViewModels()
    private val checkConnection by lazy { context?.let { NetworkLiveData(it) } }
    private var videoView: VideoView? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        googleSignInRepo = GoogleSignInRepository(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSocialLoginBinding.inflate(inflater, container, false)

        videoView = binding?.videoVIew
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val uri = Uri.parse(
            "android.resource://"
                    + activity?.packageName
                    + "/"
                    + R.raw.login_background_video
        )
        videoView?.setVideoURI(uri)
        videoView?.start()
        videoView?.setOnPreparedListener { mp -> mp.isLooping = true }

        checkConnection?.observe(viewLifecycleOwner) { isConnected ->
            binding?.apply {
                btnGoogleSignIn.setOnClickListener {
                    if (isConnected) {
                        signInWithGoogle()
                    } else {
                        val dialog = NoInternetDialog()
                        dialog.show(parentFragmentManager, NETWORK_DIALOG_TAG)
                    }
                }
            }
        }
    }

    override fun onResume() {
        videoView?.resume()
        super.onResume()
        checkOnboardingWasShowed()

    }

    private fun checkOnboardingWasShowed() {
        context.apply {
            if (this != null
                && !SocialUserSharedPrefs.getLoggedUser(this).isNullOrBlank()
                && !OnboardingSharedPrefs.getOnboardingViewed(this).isNullOrBlank()
            ) {
                navigateToMap()
            }
        }
    }

    override fun onPause() {
        videoView?.suspend()
        super.onPause()
    }

    override fun onDestroy() {
        videoView?.stopPlayback()
        super.onDestroy()
    }

    private fun activityResultLauncher() =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                googleSignInRepo?.getAccount(requireContext())
                onGoogleAccountReceived()
                viewModel.liveDataLoggedUser.observe(viewLifecycleOwner) { isLogged ->
                    context.apply {
                        if (this != null
                            && isLogged
                            && !OnboardingSharedPrefs.getOnboardingViewed(this).isNullOrBlank()
                        ) {
                            navigateToMap()
                        } else {
                            navigateTоOnboarding()
                        }
                    }
                }
            }
        }

    private fun signInWithGoogle() {
        val intent = googleSignInRepo?.getSignInIntent()
        resultLauncher.launch(intent)
    }

    private fun onGoogleAccountReceived() {
        viewModel.googleLogin(requireContext())
    }

    private fun navigateToMap() =
        findNavController().navigate(
            SocialLoginFragmentDirections
                .actionSocialLoginFragmentToGoogleMapFragment()
        )

    private fun navigateTоOnboarding() =
        findNavController().navigate(
            SocialLoginFragmentDirections.actionSocialLoginFragmentToOnboardingFragment()
        )


}