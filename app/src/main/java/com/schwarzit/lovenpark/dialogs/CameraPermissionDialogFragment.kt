package com.schwarzit.lovenpark.dialogs

import androidx.navigation.fragment.findNavController

class CameraPermissionDialogFragment(
    title: String,
    description: String,
    isCloseVisible: Boolean,
    isOkVisible: Boolean,
) : BaseDialog(title,null, description, isCloseVisible, isOkVisible) {

    override fun handleCloseButtonClick() {
        super.handleCloseButtonClick()
        findNavController().popBackStack()
    }
}