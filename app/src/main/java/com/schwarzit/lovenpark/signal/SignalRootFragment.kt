package com.schwarzit.lovenpark.signal

import android.content.res.Resources
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.core.os.ConfigurationCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.schwarzit.lovenpark.BaseGoogleMapFragment
import com.schwarzit.lovenpark.MainActivity
import com.schwarzit.lovenpark.R
import com.schwarzit.lovenpark.camera.CameraFragment
import com.schwarzit.lovenpark.databinding.FragmentSignalRootBinding
import com.schwarzit.lovenpark.dialogs.CameraPermissionDialogFragment
import com.schwarzit.lovenpark.utils.Util.Companion.REQUEST_DATE_AND_TIME
import com.schwarzit.lovenpark.utils.Util.Companion.REQUEST_DESCRIPTION
import com.schwarzit.lovenpark.utils.Util.Companion.SIGNAL_DATE
import com.schwarzit.lovenpark.utils.Util.Companion.SIGNAL_DESCRIPTION
import com.schwarzit.lovenpark.utils.Util.Companion.SIGNAL_TIME
import com.schwarzit.lovenpark.utils.Util.Companion.SIGNAL_YEAR
import dagger.hilt.android.AndroidEntryPoint
import java.io.File


@AndroidEntryPoint
class SignalRootFragment : Fragment() {

    private val signalRootViewModel: SignalRootViewModel by activityViewModels()
    private var binding: FragmentSignalRootBinding? = null
    var isClickedEditCategoryInOverviewFragment = false
    var isClickedEditLocationInOverviewFragment = false
    var isClickedEditAttachedFilesInOverviewFragment = false
    var isUserInPark = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerForLocationDataFromMapsFragment()
        registerForPhotoDataFromCameraFragment()
        registerSignalDescriptionFragment()
        registerForSignalMapScreenshot()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignalRootBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showSignalCategoryFragment()
        observeSignalCreation()
        if (signalRootViewModel.locationTemp != null) {
            binding?.buttonForward?.isEnabled = true
        }
        (activity as MainActivity).findViewById<TextView>(R.id.fragment_help_text)?.isVisible = true

        binding?.buttonCansel?.setOnClickListener {
            val areYouSurePopUp = AreYouSurePopUp(isCloseVisible = false)
            areYouSurePopUp.isCancelable = false
            areYouSurePopUp.show(parentFragmentManager, SIGNAL_ARE_YOU_SURE_POP_UP_TAG)
            areYouSurePopUp.onPopUpDiscardListener = {
                findNavController().popBackStack()
                signalRootViewModel.clearSignal()
                areYouSurePopUp.dismiss()
            }
            areYouSurePopUp.onPopUpContinueListener = {
                areYouSurePopUp.dismiss()
            }
        }
        onClickForwardButton()
    }

    /*
    Option for cancelling the signal creation should not be available in the Category fragment
     */
    private fun setupMenu(isCategoryFragment: Boolean) {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)
                menu.findItem(R.id.cancel_signal_creation).isVisible = false
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == R.id.cancel_signal_creation) {
                    return true
                }
                return false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }

    private fun onClickForwardButton() {
        binding?.buttonForward?.setOnClickListener {

            if (signalRootViewModel.locationTemp != null) {
                signalRootViewModel.makeLocationEqualToTempLocation()
            }

            if (isClickedEditCategoryInOverviewFragment) {
                showOverviewFragment()
                binding?.buttonForward?.visibility = View.GONE
                isClickedEditCategoryInOverviewFragment = false
            } else {
                showMapsFragment()
            }

            if (isClickedEditLocationInOverviewFragment) {
                showOverviewFragment()
                binding?.buttonForward?.visibility = View.GONE
                isClickedEditLocationInOverviewFragment = false
            } else {
                showPhotoFragment()
            }
        }
    }

    private fun showForwardButtonAndDragPinText() {
        binding?.apply {
            buttonForward.visibility = View.VISIBLE
        }
    }

    private fun displayFragment(fragment: Fragment) {
        childFragmentManager.commit {
            replace(R.id.fl_fragments_container, fragment)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            when (fragment) {
                is SignalCategoryFragment -> {
                    findNavController().popBackStack()
                    signalRootViewModel.clearSignal()
                }

                is BaseGoogleMapFragment -> {
                    showSignalCategoryFragment()
                    signalRootViewModel.makeLocationEqualToTempLocation()
                    binding?.apply {
                        if (signalRootViewModel.signalCategory != null) {
                            buttonForward.apply {
                                isEnabled = true
                                setOnClickListener {
                                    showMapsFragment()
                                    isClickedEditLocationInOverviewFragment = false
                                }
                            }
                        }
                    }
                }

                is CameraFragment -> {
                    showMapsFragment()
                    showForwardButtonAndDragPinText()
                }

                is SignalDescriptionFragment -> {
                    showPhotoFragment()
                }

                is SignalOverviewFragment -> {
                    showDescriptionFragment()
                }

            }
        }
        setupMenu(fragment is SignalCategoryFragment)
    }

    private fun showSignalCategoryFragment() {
        val signalCategoryFragment = SignalCategoryFragment()
        binding?.cvFragmentsContainer?.cardElevation = 0F
        binding?.clInfoContainer?.visibility = View.VISIBLE
        binding?.tvInfoText?.text = getString(R.string.signal_info_select_report_category)
        (activity as MainActivity).apply {
            findViewById<TextView>(R.id.fragment_title)?.text = getString(R.string.choose_category)
            findViewById<TextView>(R.id.fragment_help_text)?.text =
                getString(R.string.signal_creation_steps_text, 1)
        }
        onClickSignalCategory(signalCategoryFragment)
        binding?.apply {
            cvFragmentsContainer.cardElevation = 0F
            clInfoContainer.visibility = View.VISIBLE
            clButtonContainer.visibility = View.VISIBLE
            tvInfoText.text = getString(R.string.signal_info_select_report_category)
            buttonForward.apply {
                visibility = View.VISIBLE
                isEnabled = false
                icon = activity?.let {
                    ContextCompat.getDrawable(
                        it,
                        R.drawable.ic_arrow_narrow_right
                    )
                }
                sendSelectedCategoryToSignalCategoryFragment(signalCategoryFragment)
                displayFragment(signalCategoryFragment)
                setOnClickListener {
                    if (isClickedEditCategoryInOverviewFragment) {
                        showOverviewFragment()
                        isClickedEditCategoryInOverviewFragment = false
                        return@setOnClickListener
                    } else {
                        showMapsFragment()
                    }
                }
            }
        }
    }

    private fun showMapsFragment() {
        val mapsFragment = SignalMapFragment()
        (activity as MainActivity).apply {
            findViewById<TextView>(R.id.fragment_title)?.text = getString(R.string.choose_location)
            findViewById<TextView>(R.id.fragment_help_text)?.text =
                getString(R.string.signal_creation_steps_text, 2)
        }
        binding?.apply {
            buttonForward.apply {
                visibility = View.VISIBLE
                text = context.getString(R.string.forward)
                icon = activity?.let {
                    ContextCompat.getDrawable(
                        it,
                        R.drawable.ic_arrow_narrow_right
                    )
                }
            }
            clButtonContainer.visibility = View.VISIBLE
            cvFragmentsContainer.cardElevation = 0F
            clInfoContainer.visibility = View.VISIBLE
            tvInfoText.text = getString(R.string.signal_info_place_pin)
        }
        hideNestedFragmentContent(mapsFragment)
        binding?.buttonForward?.isEnabled = signalRootViewModel.locationTemp != null && isUserInPark
        displayFragment(mapsFragment)
        binding?.buttonForward?.setOnClickListener {
            // Create a snapshot of the map
            mapsFragment.gMap?.snapshot { snapshot ->
                if (snapshot != null) {
                    signalRootViewModel.screenshot = snapshot
                }
            }
            if (isClickedEditLocationInOverviewFragment) {
                showOverviewFragment()
                isClickedEditLocationInOverviewFragment = false
            } else {
                showPhotoFragment()
            }
        }
    }

    private fun showPhotoFragment() {
        val cameraFragment = CameraFragment()
        binding?.clInfoContainer?.visibility = View.GONE
        (activity as MainActivity).apply {
            findViewById<TextView>(R.id.fragment_title)?.text =
                getString(R.string.fragment_title_add_photos)
            findViewById<TextView>(R.id.fragment_help_text)?.text =
                getString(R.string.signal_creation_steps_text, 3)
        }
        binding?.buttonForward?.isEnabled = false
        signalRootViewModel.isPhotoNumberHigherThanMin.observe(viewLifecycleOwner) {
            if (it != null) {
                binding?.buttonForward?.isEnabled = it
            } else {
                binding?.buttonForward?.isEnabled = false
            }
        }
        binding?.apply {
            buttonForward.apply {
                visibility = View.VISIBLE
                text = context.getString(R.string.forward)
                icon = activity?.let {
                    ContextCompat.getDrawable(
                        it,
                        R.drawable.ic_arrow_narrow_right
                    )
                }
            }
            clInfoContainer.visibility = View.GONE
            clButtonContainer.visibility = View.VISIBLE
        }
        displayFragment(cameraFragment)
        onClickPhotoForwardButton()
        sendPhotoListSizeToCameraFragment(cameraFragment)
    }

    private fun showDescriptionFragment() {
        val signalDescriptionFragment = SignalDescriptionFragment()
        binding?.apply {
            buttonForward.apply {
                visibility = View.VISIBLE
                text = context.getString(R.string.forward)
                icon = activity?.let {
                    ContextCompat.getDrawable(
                        it,
                        R.drawable.ic_arrow_narrow_right
                    )
                }
            }
            clInfoContainer.visibility = View.GONE
            clButtonContainer.visibility = View.VISIBLE
        }
        binding?.clInfoContainer?.visibility = View.GONE
        (activity as MainActivity).apply {
            findViewById<TextView>(R.id.fragment_title)?.text =
                getString(R.string.fragment_title_description)
            findViewById<TextView>(R.id.fragment_help_text)?.text =
                getString(R.string.signal_creation_steps_text, 4)
        }
        displayFragment(signalDescriptionFragment)
        signalRootViewModel.isSignalDescriptionInputCorrect.observe(viewLifecycleOwner) {
            if (it != null) {
                binding?.buttonForward?.isEnabled = it
            }
        }
        binding?.buttonForward?.setOnClickListener {
            showOverviewFragment()
        }
        sendArgsToSignalDescriptionFragment(signalDescriptionFragment)
    }

    private fun showOverviewFragment() {
        val signalOverviewFragment = SignalOverviewFragment()
        binding?.apply {
            buttonForward.apply {
                visibility = View.VISIBLE
                text = context.getString(R.string.confirm)
                icon = activity?.let { ContextCompat.getDrawable(it, R.drawable.ic_check) }
            }
            clInfoContainer.visibility = View.GONE
        }
        displayFragment(signalOverviewFragment)
        sendDataToSignalOverviewFragment(signalOverviewFragment)
        onClickEditButtonsSignalOverview(signalOverviewFragment)
        // showing the screenshot in overviewFragment
        signalRootViewModel.signal = context?.let { signalRootViewModel.createSignal(it) }
        createSignal()
        signalRootViewModel.signal = context?.let {
            signalRootViewModel.createSignal(it)

        }

        val signalName = signalRootViewModel.signal?.createdAt?.split("-", "T", ":")
        val dateTime = requireContext()
            .getString(
                R.string.display_overview_signal_number,
                signalName?.get(0),
                signalName?.get(1),
                signalName?.get(2),
                signalName?.get(3),
                signalName?.get(4),
            )
        (activity as MainActivity).findViewById<TextView>(R.id.fragment_title)?.text = dateTime
        observeSignalCreation()
    }

    private fun createSignal() {
        binding?.buttonForward?.setOnClickListener {
            val areYouSurePopUp = AreYouSurePopUp()
            areYouSurePopUp.descriptionText =
                getString(R.string.signal_confirm)
            areYouSurePopUp.isCancelable = false
            areYouSurePopUp.show(
                parentFragmentManager,
                SIGNAL_ARE_YOU_SURE_POP_UP_TAG
            )
            areYouSurePopUp.onPopUpContinueListener = {
                context?.let { signalRootViewModel.postSignal(getImageOutputDirectory()) }
                areYouSurePopUp.dismiss()
                showOverviewFragment()
            }
            areYouSurePopUp.onPopUpDiscardListener = {
                areYouSurePopUp.dismiss()
            }
        }
    }

    private fun getImageOutputDirectory(): File {
        val mediaDir = requireActivity().externalMediaDirs?.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else requireActivity().filesDir
    }

    private fun observeSignalCreation() {
        signalRootViewModel.isCountDownTimerActive.observe(viewLifecycleOwner) { isMessageVisible ->
            binding?.tvDelayInfo?.isVisible = isMessageVisible == true
        }
        signalRootViewModel.isUploadSignalInProgress.observe(viewLifecycleOwner) { uploadingSignalInProgress ->
            if (uploadingSignalInProgress == true) {
                requireActivity().window?.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )
                binding?.signalRootViewHolder?.alpha = 0.5f
                binding?.pbCreateSignalLoadingBar?.visibility = View.VISIBLE
            } else if (uploadingSignalInProgress == false) {
                requireActivity().window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                binding?.signalRootViewHolder?.alpha = 1f
                binding?.pbCreateSignalLoadingBar?.visibility = View.GONE
                binding?.tvDelayInfo?.visibility = View.GONE
            }

        }

    }

    private fun registerForLocationDataFromMapsFragment() {
        childFragmentManager.setFragmentResultListener(IS_USER_IN_PARK_KEY, this)
        { _, bundle ->
            isUserInPark = bundle.getBoolean(IS_USER_IN_PARK)
            binding?.buttonForward?.isEnabled = isUserInPark
        }

        childFragmentManager.setFragmentResultListener(KEY, this)
        { _, bundle ->
            signalRootViewModel.locationTemp = bundle.getParcelable(LOCATION)
            if (signalRootViewModel.locationTemp != null) {
                binding?.buttonForward?.isEnabled = true
            }
        }
    }

    private fun registerForSignalMapScreenshot() {
        childFragmentManager.setFragmentResultListener(MAP_SIGNAL_SCREENSHOT_KEY, this)
        { _, bundle ->
            val screenshot = bundle.getParcelable<Bitmap?>(MAP_SIGNAL_SCREENSHOT)
            if (screenshot != null) {
                signalRootViewModel.screenshot = screenshot
            }
        }
    }

    private fun registerForPhotoDataFromCameraFragment() {
        childFragmentManager.setFragmentResultListener(REQUEST_PHOTO_URIS, this)
        { _, bundle ->
            val listUris = bundle.getParcelableArrayList<Uri>(PHOTO_URIS)
            if (listUris != null) {
                signalRootViewModel.photoURIs = listUris.toMutableList()
            }
        }
        childFragmentManager.setFragmentResultListener(REQUEST_PHOTO_PERMISSIONS, this)
        { _, bundle ->
            val permissionsDenied = bundle.getBoolean(PHOTO_PERMISSIONS)
            if (permissionsDenied) {
                val dialog = CameraPermissionDialogFragment(
                    getString(R.string.photo_warning_dialog_title),
                    getString(R.string.photo_warning_dialog_description),
                    isCloseVisible = true,
                    isOkVisible = false
                )
                dialog.isCancelable = false
                dialog.show(parentFragmentManager, CAMERA_PERMISSION_DIALOG_TAG)
                signalRootViewModel.clearSignal()
            }
        }
    }

    private fun registerSignalDescriptionFragment() {
        registerForSignalDescriptionText()
        registerForSignalDescriptionForDateAndTime()
    }

    private fun registerForSignalDescriptionText() {
        childFragmentManager.setFragmentResultListener(REQUEST_DESCRIPTION, this)
        { _, bundle ->
            val description = bundle.getString(SIGNAL_DESCRIPTION)
            if (description != null) {
                signalRootViewModel.description = description
            }
        }
    }

    private fun registerForSignalDescriptionForDateAndTime() {
        childFragmentManager.setFragmentResultListener(REQUEST_DATE_AND_TIME, this)
        { _, bundle ->
            val date = bundle.getString(SIGNAL_DATE)
            val time = bundle.getString(SIGNAL_TIME)
            val year = bundle.getString(SIGNAL_YEAR)
            if (date != null) {
                signalRootViewModel.date = date
            }
            if (time != null) {
                signalRootViewModel.time = time
            }
            if (year != null) {
                signalRootViewModel.year = year
            }
        }
    }

    private fun sendSelectedCategoryToSignalCategoryFragment(fragment: Fragment) {
        val bundle = Bundle()
        val category = signalRootViewModel.signalCategory
        bundle.putString(CATEGORY, category)
        fragment.arguments = bundle
    }

    private fun sendPhotoListSizeToCameraFragment(fragment: Fragment) {
        val bundle = Bundle()
        val uris = signalRootViewModel.photoURIs
        val stringList = uris.map { it.toString() }
        bundle.putStringArrayList(URIS, ArrayList(stringList))
        fragment.arguments = bundle
    }

    private fun sendDataToSignalOverviewFragment(fragment: Fragment) {
        signalRootViewModel.makeLocationEqualToTempLocation()
        val bundle = Bundle()
        bundle.putString(CATEGORY, signalRootViewModel.signalCategory)
        val uris = signalRootViewModel.photoURIs
        val stringList = uris.map { it.toString() }
        bundle.putStringArrayList(URIS, ArrayList(stringList))
        signalRootViewModel.location?.latitude?.let { bundle.putDouble(LATITUDE, it) }
        signalRootViewModel.location?.longitude?.let { bundle.putDouble(LONGITUDE, it) }
        val date = signalRootViewModel.date?.split("/")
        val dateTime = requireContext()
            .getString(
                R.string.display_date_time,
                date?.get(0),
                date?.get(1),
                date?.get(2),
            )
        bundle.putString(DATETIME, dateTime)
        bundle.putString(DESCRIPTION, signalRootViewModel.description)
        fragment.arguments = bundle
    }

    private fun sendArgsToSignalDescriptionFragment(fragment: Fragment) {
        val bundle = Bundle()
        signalRootViewModel.description?.let { bundle.putString(SIGNAL_DESCRIPTION, it) }
        signalRootViewModel.date?.let { bundle.putString(SIGNAL_DATE, it) }
        signalRootViewModel.time?.let { bundle.putString(SIGNAL_TIME, it) }
        fragment.arguments = bundle
    }

    private fun hideNestedFragmentContent(fragment: Fragment) {
        val bundle = Bundle()
        signalRootViewModel.locationTemp?.latitude?.toFloat()
            ?.let { bundle.putFloat(LATITUDE, it) }
        signalRootViewModel.locationTemp?.longitude?.toFloat()
            ?.let { bundle.putFloat(LONGITUDE, it) }
        bundle.putBoolean(HIDE_MAP_INFO, true)
        fragment.arguments = bundle
    }

    private fun onClickSignalCategory(signalCategoryFragment: SignalCategoryFragment) {
        signalCategoryFragment.categorySelectedListener = { aCategory: String ->
            binding?.buttonForward?.apply {
                visibility = View.VISIBLE
                isEnabled = true
                setOnClickListener {
                    if (isClickedEditCategoryInOverviewFragment) {
                        signalRootViewModel.signalCategory = aCategory
                        showOverviewFragment()
                        isClickedEditCategoryInOverviewFragment = false
                    } else {
                        signalRootViewModel.signalCategory = aCategory
                        showMapsFragment()
                        showForwardButtonAndDragPinText()
                    }
                    onClickForwardButton()
                }
            }
        }
    }

    private fun onClickPhotoForwardButton() {
        binding?.buttonForward?.setOnClickListener {
            if (isClickedEditAttachedFilesInOverviewFragment) {
                showOverviewFragment()
                isClickedEditAttachedFilesInOverviewFragment = false
            } else {
                showDescriptionFragment()
            }
        }
    }

    private fun onClickEditButtonsSignalOverview(signalOverviewFragment: SignalOverviewFragment) {
        signalOverviewFragment.editCategoryButtonListener = {
            isClickedEditCategoryInOverviewFragment = true
            showSignalCategoryFragment()
            if (signalRootViewModel.signalCategory != null) {
                binding?.buttonForward?.apply {
                    visibility = View.VISIBLE
                    isEnabled = true
                    binding?.buttonForward?.setOnClickListener {
                        showOverviewFragment()
                    }
                }
            }
        }

        signalOverviewFragment.editLocationButtonListener = {
            binding?.buttonForward?.apply {
                visibility = View.VISIBLE
                isEnabled = true
            }
            showMapsFragment()
            isClickedEditLocationInOverviewFragment = true
        }

        signalOverviewFragment.editAttachedFilesButtonListener = {
            showPhotoFragment()
            isClickedEditAttachedFilesInOverviewFragment = true
        }

        signalOverviewFragment.editDescriptionButtonListener = {
            showDescriptionFragment()
        }
    }
}