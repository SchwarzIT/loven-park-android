package com.schwarzit.lovenpark.signal

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.schwarzit.lovenpark.MainActivity
import com.schwarzit.lovenpark.R
import com.schwarzit.lovenpark.databinding.FragmentSignalOverviewBinding

class SignalOverviewFragment : Fragment() {

    private val signalRootViewModel: SignalRootViewModel by activityViewModels()

    private var binding: FragmentSignalOverviewBinding? = null
    private var category: String? = null
    private var listUris = mutableListOf<Uri>()
    private var signalImageOverviewAdapter: SignalImageOverviewAdapter? = null
    private var longitude: Double? = null
    private var latitude: Double? = null
    private var dateTime: String? = null
    private var description: String? = null

    var editCategoryButtonListener: (() -> Unit)? = null
    var editLocationButtonListener: (() -> Unit)? = null
    var editAttachedFilesButtonListener: (() -> Unit)? = null
    var editDescriptionButtonListener: (() -> Unit)? = null
    var confirmSendSignalButtonListener: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignalOverviewBinding.inflate(inflater, container, false)
        initSignalImageOverviewAdapter()
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerForSignalDataFromSignalRootFragment()
        displaySignalOverview()
        setSignalCompleteDialog()
    }
    private fun setSignalCompleteDialog(){
        signalRootViewModel.dialogMutableLiveData.observe(viewLifecycleOwner) { dialog ->
            dialog?.onConfirmCloseListener = {
                findNavController().navigate(SignalRootFragmentDirections.actionSignalRootFragmentToUserProfileFragment())
            }

            dialog?.isCancelable = false
            dialog?.show(parentFragmentManager, SIGNAL_REJECTION_DIALOG_TAG)
            signalRootViewModel.dialogMutableLiveData.postValue(null)
        }
    }

    private fun displaySignalOverview() {
        binding?.apply {
            (activity as MainActivity).findViewById<TextView>(R.id.fragment_help_text)?.text = category
            tvDateTime.text = dateTime
            tvDescriptionText.text = description
        }
        displayMapOverview()
    }

    private fun registerForSignalDataFromSignalRootFragment() {
        val stringUris = arguments?.getStringArrayList(URIS)
        stringUris?.forEach { listUris.add(Uri.parse(it)) }

        signalImageOverviewAdapter?.setImageList(listUris)
        category = arguments?.getString(CATEGORY)
        latitude = arguments?.getDouble(LATITUDE)
        longitude = arguments?.getDouble(LONGITUDE)
        dateTime = arguments?.getString(DATETIME)
        description = arguments?.getString(DESCRIPTION)
    }

    private fun sendLocationToMapOverview(fragment: Fragment) {
        val bundle = Bundle()
        longitude?.let { bundle.putDouble(LONGITUDE, it) }
        latitude?.let { bundle.putDouble(LATITUDE, it) }
        fragment.arguments = bundle
    }

    private fun initSignalImageOverviewAdapter() {
        signalImageOverviewAdapter = SignalImageOverviewAdapter(requireContext())
        val recyclerView = binding?.rvFiles
        recyclerView?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView?.adapter = signalImageOverviewAdapter
    }

    private fun displayMapOverview() {
        val map = SignalOverviewMapFragment()
        childFragmentManager.commit {
            replace(R.id.fl_map_container, map)
        }
        sendLocationToMapOverview(map)
    }

}