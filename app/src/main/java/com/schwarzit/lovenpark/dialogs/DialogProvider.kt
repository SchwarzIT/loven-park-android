package com.schwarzit.lovenpark.dialogs

import android.content.Context
import com.schwarzit.lovenpark.signal.SignalSubmissionDialog

class DialogProvider(val context: Context) {

    fun getSignalSubmissionDialog(
        title: Int,
        description: Int,
        isCloseVisible: Boolean,
        isOkVisible: Boolean,
        isImageVisible: Boolean = false
    ): SignalSubmissionDialog = SignalSubmissionDialog(
        context.getString(title),
        context.getString(description),
        isCloseVisible = isCloseVisible,
        isOkVisible = isOkVisible,
        isThankYouPopUp = isImageVisible
    )
}