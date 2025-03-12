package com.schwarzit.lovenpark.dialogs

import com.schwarzit.lovenpark.R

class NoInternetDialog(
    override val isNoInternetDialog: Boolean = true
) : BaseDialog() {

    override fun setUpNoInternetDialog() {
        super.setUpNoInternetDialog()
        binding.tvDialogDescription.text = getString(R.string.network_error_description)
        binding.tvDialogTitle.text = getString(R.string.no_internet_connection)
    }
}