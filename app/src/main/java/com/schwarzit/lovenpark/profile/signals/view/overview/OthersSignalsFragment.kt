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
import com.schwarzit.lovenpark.databinding.FragmentOthersSignalsBinding
import com.schwarzit.lovenpark.dialogs.NoInternetDialog
import com.schwarzit.lovenpark.profile.signals.viewmodel.SignalsViewModel
import com.schwarzit.lovenpark.profile.view.ReportsFragmentDirections
import com.schwarzit.lovenpark.utils.Util
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OthersSignalsFragment : Fragment(), SignalClickListener {
    private lateinit var binding: FragmentOthersSignalsBinding
    private var signalAdapter: UserProfileSignalAdapter? = null
    private val viewModelSignals: SignalsViewModel by viewModels()

    private val checkConnection by lazy { context?.let { NetworkLiveData(it) } }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOthersSignalsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.srlOthersSignals.setOnRefreshListener {
            checkConnection?.observe(viewLifecycleOwner) { isConnected ->
                context?.let {
                    if (isConnected) {
                        viewModelSignals.requestLatestOthersSignals()
                    } else {
                        val dialog = NoInternetDialog()
                        dialog.show(parentFragmentManager, Util.NETWORK_DIALOG_TAG)
                    }
                }
            }
            binding.srlOthersSignals.isRefreshing = false
        }

        checkConnection?.observe(viewLifecycleOwner) { isConnected ->
            context?.let {
                if (isConnected) {
                    viewModelSignals.requestOthersSignalsWhenOnline()
                } else {
                    viewModelSignals.requestOthersSignalsWhenOffline(it)
                }
            }
        }

        viewModelSignals.isLoadSignalsInProgress.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.pbOthersSignals.visibility = View.VISIBLE
            } else {
                binding.pbOthersSignals.visibility = View.GONE
            }
        }

        viewModelSignals.liveDataOthersSignals.observe(viewLifecycleOwner) { signalList ->
            if (signalList.isEmpty()) {
                binding.tvNoSubmittedSignals.visibility = View.VISIBLE
            } else {
                binding.rvOthersSignals.apply {
                    visibility = View.VISIBLE
                    signalAdapter = UserProfileSignalAdapter(this@OthersSignalsFragment)
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