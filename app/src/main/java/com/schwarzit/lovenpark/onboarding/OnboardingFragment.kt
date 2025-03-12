package com.schwarzit.lovenpark.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.schwarzit.lovenpark.R
import com.schwarzit.lovenpark.databinding.FragmentOnboardingBinding
import com.schwarzit.lovenpark.user.socialLogin.SocialUserSharedPrefs

class OnboardingFragment : Fragment() {
    private lateinit var binding: FragmentOnboardingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBoarding()
    }

    override fun onResume() {
        super.onResume()
        checkOnboardingWasShowed()
    }

    private fun checkOnboardingWasShowed() {
        context.apply {
            if (this != null
                && !SocialUserSharedPrefs.getLoggedUser(this).isNullOrBlank()
                && !OnboardingSharedPrefs.getOnboardingViewed(this).isNullOrBlank()
            ) {
                findNavController().popBackStack()
            }
        }
    }

    private fun goToMaps() = findNavController().navigate(
        OnboardingFragmentDirections
            .actionOnboardingFragmentToGoogleMapFragment()
    )

    private fun onBoarding() {
        var stepCounter = 1
        binding.apply {
            ivTitle.text = getString(R.string.onboarding_title_explore)
            ivFirstStep.setImageResource(R.drawable.onboarding_pager_rectangle)
            onboardingAnimationBackground.setImageResource(R.drawable.onboarding_explore_image)
            onboardingDescription.text = getString(R.string.onboarding_description_explore)


            ivSecondStep.setImageResource(R.drawable.onbording_pager_circle)
            ivThirdStep.setImageResource(R.drawable.onbording_pager_circle)
            lottieAnimationView.setAnimation(R.raw.loven_park_onboarding_explore)
            lottieAnimationView.playAnimation()
            onboardingNext.setOnClickListener {
                stepCounter++
                if (stepCounter == 2) {
                    ivTitle.text = getString(R.string.onboarding_title_enjoy)
                    onboardingDescription.text = getString(R.string.onboarding_description_enjoy)
                    ivSecondStep.setImageResource(R.drawable.onboarding_pager_rectangle)
                    onboardingAnimationBackground.setImageResource(R.drawable.onboarding_enjoy_image)

                    ivFirstStep.setImageResource(R.drawable.onbording_pager_circle)
                    ivThirdStep.setImageResource(R.drawable.onbording_pager_circle)
                    lottieAnimationView.setAnimation(R.raw.loven_park_onboarding_enjoy)
                    lottieAnimationView.playAnimation()
                }

                if (stepCounter == 3) {
                    ivTitle.text = getString(R.string.onboarding_title_help)
                    ivThirdStep.setImageResource(R.drawable.onboarding_pager_rectangle)
                    onboardingAnimationBackground.setImageResource(R.drawable.onboarding_help_image)
                    onboardingDescription.text = getString(R.string.onboarding_description_help)


                    ivFirstStep.setImageResource(R.drawable.onbording_pager_circle)
                    ivSecondStep.setImageResource(R.drawable.onbording_pager_circle)
                    lottieAnimationView.setAnimation(R.raw.loven_park_onboarding_help)
                    lottieAnimationView.playAnimation()
                    tvSkip.visibility = View.INVISIBLE
                }

                if (stepCounter == 4) {
                    context?.let { it1 -> OnboardingSharedPrefs.saveOnboardingViewed(it1, true) }
                    goToMaps()
                }
            }
            binding.tvSkip.setOnClickListener {
                context?.let { it1 -> OnboardingSharedPrefs.saveOnboardingViewed(it1, true) }
                goToMaps()
            }
        }
    }
}