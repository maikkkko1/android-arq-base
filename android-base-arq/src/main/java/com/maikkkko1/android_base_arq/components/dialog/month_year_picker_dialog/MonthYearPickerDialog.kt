package com.maikkkko1.android_base_arq.components.dialog.month_year_picker_dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.maikkkko1.android_base_arq.databinding.DialogMonthYearPickerBinding
import java.util.*

class MonthYearPickerDialog(val date: Date = Date()) : DialogFragment() {
    companion object {
        private const val MAX_YEAR = 2099
    }

    private lateinit var binding: DialogMonthYearPickerBinding

    private var listener: ((result: MonthYearPickerDialogResult) -> Unit)? = null

    fun setListener(listener: ((result: MonthYearPickerDialogResult) -> Unit)?) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogMonthYearPickerBinding.inflate(requireActivity().layoutInflater)
        val cal: Calendar = Calendar.getInstance().apply { time = date }

        binding.run {
            pickerMonth.run {
                minValue = 0
                maxValue = 11
                value = cal.get(Calendar.MONTH)
                displayedValues = arrayOf(
                    "Jan", "Feb", "Mar", "Apr", "May", "June", "July",
                    "Aug", "Sep", "Oct", "Nov", "Dec"
                )
            }

            pickerYear.run {
                val year = cal.get(Calendar.YEAR)
                minValue = year
                maxValue = MAX_YEAR
                value = year
            }

            return AlertDialog.Builder(requireContext())
                .setTitle("Please select a month")
                .setView(binding.root)
                .setPositiveButton("Ok") { _, _ ->
                    listener?.invoke(
                        MonthYearPickerDialogResult(
                            pickerYear.value,
                            pickerMonth.value,
                        )
                    )
                }
                .setNegativeButton("Cancel") { _, _ -> dialog?.cancel() }
                .create()
        }
    }
}

data class MonthYearPickerDialogResult(
    val year: Int,
    val month: Int
)