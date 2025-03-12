package com.schwarzit.lovenpark.profile.signals.view.details

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.schwarzit.lovenpark.MainActivity
import com.schwarzit.lovenpark.NetworkLiveData
import com.schwarzit.lovenpark.R
import com.schwarzit.lovenpark.databinding.FragmentSignalDetailsBinding
import com.schwarzit.lovenpark.mappin.data.remote.PictureModel
import com.schwarzit.lovenpark.profile.signals.data.domainmodel.SignalCategory
import com.schwarzit.lovenpark.profile.signals.data.domainmodel.SignalModel
import com.schwarzit.lovenpark.profile.signals.data.domainmodel.SignalStatus
import com.schwarzit.lovenpark.profile.signals.view.details.signalimage.SignalImageDetailsAdapter
import com.schwarzit.lovenpark.profile.signals.viewmodel.SignalsViewModel
import com.schwarzit.lovenpark.signal.LATITUDE
import com.schwarzit.lovenpark.signal.LONGITUDE
import com.schwarzit.lovenpark.utils.Util.Companion.EMPTY_STRING
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignalDetailsFragment : Fragment() {
    private lateinit var binding: FragmentSignalDetailsBinding
    private val viewModelSignals: SignalsViewModel by activityViewModels()
    private val checkConnection by lazy { context?.let { NetworkLiveData(it) } }

    private val args: SignalDetailsFragmentArgs by navArgs()

    private var signalImageDetailsAdapter: SignalImageDetailsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignalDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkConnection?.observe(viewLifecycleOwner) { isConnected ->
            context?.let {
                if (isConnected) {
                    viewModelSignals.requestSignalWhenOnline(args.signalId)
                } else {
                    viewModelSignals.requestSignalWhenOffline(args.signalId)
                }
            }
        }

        initSignalImageDetailsAdapter()
        viewModelSignals.liveDataSignal.observe(viewLifecycleOwner) {
            it?.let { displaySignalDetails(it) }
            if (it != null) {
                if (it.pictures?.isEmpty() == true) {
                    signalImageDetailsAdapter?.injectImageList(listOf(PictureModel(EMPTY_STRING)))
                } else {
                    it.pictures?.let { pictures ->
                        signalImageDetailsAdapter?.injectImageList(
                            pictures
                        )
                    }

                }
            }
        }

        binding.ivSignalDetailsShare.setOnClickListener {
            //to be discussed
        }

    }

    override fun onResume() {
        super.onResume()
        viewModelSignals.liveDataSignal.observe(viewLifecycleOwner) {
            (activity as MainActivity).apply {
                findViewById<TextView>(R.id.fragment_title)?.text =
                    getString(R.string.signal_number, it?.number ?: 0)
            }
        }
    }

    private fun sendLocationToMapDetails(fragment: Fragment) {
        val bundle = Bundle()

        viewModelSignals.liveDataSignal.observe(viewLifecycleOwner) {
            if (it != null) {
                bundle.putDouble(LATITUDE, it.latitude)
                bundle.putDouble(LONGITUDE, it.longitude)
            }
        }
        fragment.arguments = bundle
    }

    private fun displaySignalDetails(signal: SignalModel) {
        binding.apply {
            tvSignalDetailsStatus.apply {
                if (signal.status == SignalStatus.NEW) {
                    signalStatus.visibility = View.VISIBLE
                    text = getString(R.string.signal_status_processing)

                    setBackgroundColor(resources.getColor(R.color.arylide_yellow))
                    setTextColor(resources.getColor(R.color.olive))
                } else {
                    text = getString(R.string.signal_status_resolved)
                    signalStatus.visibility = View.GONE
                    setBackgroundColor(resources.getColor(R.color.middle_green_yellow))
                    setTextColor(resources.getColor(R.color.fern_green))
                }
            }
            val signalDetails = SpannableStringBuilder()
                .append(
                    getString(
                        R.string.signal_info,
                        getSignalCategoryText(signal.category),
                                signal.createdAt,
                    )
                )
            (activity as MainActivity).findViewById<TextView>(R.id.fragment_help_text)?.text = signalDetails

            tvSignalDetailsDescription.text = signal.description
        }
        displayMap()
    }

    private fun displayMap() {
        val map = SignalDetailsMapFragment()
        childFragmentManager.commit {
            replace(R.id.flSignalDetailsMapContainer, map)
        }
        sendLocationToMapDetails(map)
    }

    private fun initSignalImageDetailsAdapter() {
        signalImageDetailsAdapter = SignalImageDetailsAdapter()
        val recyclerView = binding.rvSignalDetailsFiles
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = signalImageDetailsAdapter
    }

    private fun getSignalCategoryText(category: SignalCategory) =
        when (category) {
            SignalCategory.UNCOLLECTED_WASTE -> getString(R.string.signal_category_uncollected_waste)
            SignalCategory.INJURED_TREE -> getString(R.string.signal_category_injured_tree)
            SignalCategory.ANIMAL_CARCASSES_FOUND -> getString(R.string.signal_category_animal_carcasses_found)
            SignalCategory.DAMAGED_PARK_FURNITURE -> getString(R.string.signal_category_damaged_park_furniture)
            SignalCategory.OTHER -> getString(R.string.signal_category_other)
        }
}