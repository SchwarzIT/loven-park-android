package com.schwarzit.lovenpark.signal

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import com.schwarzit.lovenpark.R
import com.schwarzit.lovenpark.data.utils.UserSharedPrefHelper
import com.schwarzit.lovenpark.databinding.FragmentSignalDescriptionBinding
import com.schwarzit.lovenpark.signal.validation.SignalDescriptionTextInfo
import com.schwarzit.lovenpark.utils.Util.Companion.REQUEST_DATE_AND_TIME
import com.schwarzit.lovenpark.utils.Util.Companion.REQUEST_DESCRIPTION
import com.schwarzit.lovenpark.utils.Util.Companion.SIGNAL_DATE
import com.schwarzit.lovenpark.utils.Util.Companion.SIGNAL_DESCRIPTION
import com.schwarzit.lovenpark.utils.Util.Companion.SIGNAL_TIME
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

class SignalDescriptionFragment : Fragment() {
    private var binding: FragmentSignalDescriptionBinding? = null
    var forwardButtonListener: ((Unit) -> Unit)? = null
    var aYear = context?.let { UserSharedPrefHelper.getSignalYear(it) }

    private val signalRootViewModel: SignalRootViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignalDescriptionBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDatePickerListener()
        setTimePickerListener()
        addTextChangeListeners()
        setReceivedData()
        setTodayDateIfNeeded()
        setCurrentTimeIfNeeded()
    }

    override fun onResume() {
        super.onResume()
        addTextChangeListeners()
    }

    private fun addTextChangeListeners() {
        binding?.apply {
            val value = object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    signalOnInputDataChanged()
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    signalOnInputDataChanged()
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            }
            etSignalDescription.addTextChangedListener(value)
            etSignalDescriptionDate.addTextChangedListener(value)
            etSignalDescriptionTime.addTextChangedListener(value)

        }
    }

    fun signalOnInputDataChanged() {
        binding?.apply {
            onInputDataChanged(
                etSignalDescription.text.toString(),
                etSignalDescriptionDate.text.toString(),
                etSignalDescriptionTime.text.toString()
            )
            passTextToSignalRootFragment(
                etSignalDescription.text.toString(),
                etSignalDescriptionDate.text.toString(),
                etSignalDescriptionTime.text.toString()
            )
        }
    }

    private fun isAllInputValid(text: String, date: String, time: String) =
        SignalDescriptionTextInfo(text, date, time).isAllDataValid()

    private fun onInputDataChanged(text: String, date: String, time: String) {
        binding?.apply {
            if (!SignalDescriptionTextInfo(text, date, time).isDescriptionTextValid()) {
                layoutSignalDescription.error =
                    resources.getString(R.string.error_message_invalid_signal_description)
                signalRootViewModel.setIsSignalDescriptionCorrect(false)
            } else {
                layoutSignalDescription.isErrorEnabled = false
                signalRootViewModel.setIsSignalDescriptionCorrect(isAllInputValid(text, date, time))
            }
        }
    }

    private fun setReceivedData() {
        binding?.apply {
            etSignalDescription.setText(arguments?.getString(SIGNAL_DESCRIPTION))
            etSignalDescriptionDate.setText(arguments?.getString(SIGNAL_DATE))
            etSignalDescriptionTime.setText(arguments?.getString(SIGNAL_TIME))
        }
    }

    private fun passTextToSignalRootFragment(description: String, date: String, time: String) {
        setFragmentResult(REQUEST_DESCRIPTION, bundleOf(DESCRIPTION_KEY to description))
        setFragmentResult(
            REQUEST_DATE_AND_TIME,
            bundleOf(DATE_KEY to date, TIME_KEY to time, YEAR_KEY to aYear)
        )
    }

    private fun setDatePickerListener() {
        binding?.apply {
            etSignalDescriptionDate.setOnClickListener {
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val dateFormat = requireContext().getString(R.string.signal_date_format)
                val datePickerDialog =
                    DatePickerDialog(
                        requireContext(),
                        { _, chosenYear, monthOfYear, dayOfMonth ->
                            setDate(chosenYear, dateFormat, dayOfMonth, monthOfYear + 1)
                        }, year, month, day
                    )
                datePickerDialog.datePicker.maxDate = calendar.timeInMillis
                datePickerDialog.show()
            }
        }
    }

    private fun setTodayDateIfNeeded() {

        if (binding?.etSignalDescriptionDate?.text.isNullOrEmpty()) {
            val today = LocalDate.now()
            setDate(
                today.year,
                requireContext().getString(R.string.signal_date_format),
                today.dayOfMonth,
                today.monthValue
            )
        }
    }

    private fun setCurrentTimeIfNeeded() {
        if (binding?.etSignalDescriptionTime?.text.isNullOrEmpty()) {
            val now = LocalTime.now()
            binding?.etSignalDescriptionTime?.setText(
                String.format(
                    requireContext().getString(R.string.signal_time_format),
                    now.hour,
                    now.minute
                )
            )
            signalOnInputDataChanged()
        }
    }

    private fun setDate(
        year: Int,
        dateFormat: String,
        dayOfMonth: Int,
        monthOfYear: Int
    ) {
        context?.let { UserSharedPrefHelper.removeSignalYear(it) }
        context?.let { UserSharedPrefHelper.saveSignalYear(it, year.toString()) }
        binding?.etSignalDescriptionDate?.setText(
            String.format(
                dateFormat,
                dayOfMonth,
                monthOfYear,
                year
            )
        )
        signalOnInputDataChanged()
    }

    private fun setTimePickerListener() {
        binding?.apply {
            etSignalDescriptionTime.setOnClickListener {
                val currentTime = Calendar.getInstance()
                val hour = currentTime.get(Calendar.HOUR_OF_DAY)
                val minute = currentTime.get(Calendar.MINUTE)
                val timeFormat = requireContext().getString(R.string.signal_time_format)
                val mTimePicker =
                    TimePickerDialog(
                        requireContext(),
                        { view, hourOfDay, minuteOfDay ->
                            etSignalDescriptionTime.setText(
                                String.format(
                                    timeFormat,
                                    hourOfDay,
                                    minuteOfDay
                                )
                            )
                        }, hour, minute, true
                    )
                mTimePicker.show()
            }
        }
    }

    companion object {
        const val DESCRIPTION_KEY = "description"
        const val DATE_KEY = "date"
        const val TIME_KEY = "time"
        const val YEAR_KEY = "year"
    }

}
