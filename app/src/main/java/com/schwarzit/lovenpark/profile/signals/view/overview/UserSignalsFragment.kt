package com.schwarzit.lovenpark.profile.signals.view.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.schwarzit.lovenpark.NetworkLiveData
import com.schwarzit.lovenpark.databinding.FragmentUserSignalsBinding
import com.schwarzit.lovenpark.dialogs.NoInternetDialog
import com.schwarzit.lovenpark.profile.signals.viewmodel.SignalsViewModel
import com.schwarzit.lovenpark.profile.view.ReportsFragmentDirections
import com.schwarzit.lovenpark.utils.Util
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserSignalsFragment : Fragment(), SignalClickListener {

    private lateinit var binding: FragmentUserSignalsBinding
    private var signalAdapter: UserProfileSignalAdapter? = null

    private val viewModelSignals: SignalsViewModel by viewModels()
    private val checkConnection by lazy { context?.let { NetworkLiveData(it) } }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserSignalsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.srlUserSignals.setOnRefreshListener {
            checkConnection?.observe(viewLifecycleOwner) { isConnected ->
                context?.let {
                    if (isConnected) {
                        viewModelSignals.requestLatestUserSignals()
                    } else {
                        val dialog = NoInternetDialog()
                        dialog.show(parentFragmentManager, Util.NETWORK_DIALOG_TAG)
                    }
                }
            }
            binding.srlUserSignals.isRefreshing = false
        }

        checkConnection?.observe(viewLifecycleOwner) { isConnected ->
            context?.let {
                if (isConnected) {
                    viewModelSignals.requestUserSignalsWhenOnline()
                } else {
                    viewModelSignals.requestUserSignalsWhenOffline(it)
                }
            }
        }

        viewModelSignals.isLoadSignalsInProgress.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.pbUserSignals.visibility = View.VISIBLE
            } else {
                binding.pbUserSignals.visibility = View.GONE
            }
        }

        viewModelSignals.liveDataUserSignals.observe(viewLifecycleOwner) { signalList ->
            if (signalList.isNullOrEmpty()) {
                binding.tvNoSubmittedSignals.visibility = View.VISIBLE
                binding.animationView.visibility = View.VISIBLE
                binding.animationView.playAnimation()
            } else {
                binding.animationView.visibility = View.GONE
                binding.tvNoSubmittedSignals.visibility = View.GONE
                binding.rvUserSignals.apply {
                    visibility = View.VISIBLE
                    signalAdapter = UserProfileSignalAdapter(this@UserSignalsFragment)
                    signalAdapter?.injectList(signalList)
                    adapter = signalAdapter
                    layoutManager = LinearLayoutManager(activity)
                }
            }
        }
    }

    override fun onSignalClick(selectedSignalId: String) {
        findNavController().navigate(
            ReportsFragmentDirections.actionReportsFragmentToSignalDetailsFragment(
                selectedSignalId
            )
        )
    }

    override fun onSignalShareClick(selectedSignalId: String) {
        //to be discussed
    }

}