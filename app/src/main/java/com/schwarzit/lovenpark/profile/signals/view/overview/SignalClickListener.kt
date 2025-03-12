package com.schwarzit.lovenpark.profile.signals.view.overview

interface SignalClickListener {
    fun onSignalClick(selectedSignalId: String)
    fun onSignalShareClick(selectedSignalId: String)
}