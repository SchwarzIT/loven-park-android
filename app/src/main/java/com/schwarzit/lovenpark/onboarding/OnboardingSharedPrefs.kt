package com.schwarzit.lovenpark.onboarding

import android.content.Context
import com.schwarzit.lovenpark.utils.Util

object OnboardingSharedPrefs {

    fun saveOnboardingViewed(context: Context, isViewed: Boolean) =
        context
            .getSharedPreferences(OnboardingSharedKeys.ONBOARDING_SHARED_PREFS_, Context.MODE_PRIVATE)
            .edit()
            .putString(OnboardingSharedKeys.ONBOARDING_KEY, isViewed.toString())
            .commit()

    fun getOnboardingViewed(context: Context): String? =
        context
            .getSharedPreferences(OnboardingSharedKeys.ONBOARDING_SHARED_PREFS_, Context.MODE_PRIVATE)
            .getString(OnboardingSharedKeys.ONBOARDING_KEY, Util.EMPTY_STRING)
}