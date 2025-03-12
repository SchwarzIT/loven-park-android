package com.schwarzit.lovenpark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.schwarzit.lovenpark.databinding.DialogChooseUploadPhotoOptionBinding
import com.schwarzit.lovenpark.utils.Util.Companion.SELECTED_UPLOAD_OPTION
import com.schwarzit.lovenpark.utils.Util.Companion.UPLOAD_OPTION_REQUEST_KEY

class ChooseUploadPhotoOptionFragment : BottomSheetDialogFragment() {

    private var binding: DialogChooseUploadPhotoOptionBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogChooseUploadPhotoOptionBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpOptionsButtons()
    }

    /**
     * Send result with selected button.
     * Default selected upload option will be camera associated with true.
     * Secondary option will be gallery associated with false.
     */
    private fun setUpOptionsButtons() {
        binding?.apply {
            eFabTakePhoto.setOnClickListener {
                setFragmentResult(
                    UPLOAD_OPTION_REQUEST_KEY,
                    bundleOf(SELECTED_UPLOAD_OPTION to true)
                )
                dismiss()
            }
            eFabChooseFromGallery.setOnClickListener {
                setFragmentResult(
                    UPLOAD_OPTION_REQUEST_KEY,
                    bundleOf(SELECTED_UPLOAD_OPTION to false)
                )
                dismiss()
            }
        }
    }
}