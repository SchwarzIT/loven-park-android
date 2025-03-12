package com.schwarzit.lovenpark.signal.validation

import com.schwarzit.lovenpark.utils.Util

class SignalDescriptionTextInfo(
    private val descriptionText: String,
    private val date: String,
    private val time: String
) {

    fun isAllDataValid(): Boolean {
        if (!descriptionText.isNullOrEmpty() && !date.isNullOrEmpty() && !time.isNullOrEmpty()) {
            return isDescriptionTextValid()
        }
        return false
    }

    fun isDescriptionTextValid(): Boolean {
        if (descriptionText.isNotEmpty()) {
            return descriptionText.length in Util.SIGNAL_DESC_TEXT_MIN_L..Util.SIGNAL_DESC_TEXT_MAX_L
        }
        return true
    }
}