import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.humbhi.blackbox.R
import java.util.Calendar

class CustomDatePickerFragment : DialogFragment() {

    private var listener: OnDateSelectedListener? = null
    private val MAX_MONTHS_FROM_CURRENT = 3

    // Define the OnDateSelectedListener interface
    interface OnDateSelectedListener {
        fun onDateSelected(year: Int, month: Int)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateDialog(savedInstanceState: Bundle?): DatePickerDialog {
        val calendar = Calendar.getInstance()
        val picker = object : DatePickerDialog(requireContext(), R.style.CustomDatePickerDialog) {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                // Disable the day picker by hiding it from the view
                datePicker.findViewById<View>(resources.getIdentifier("day", "id", "android"))?.visibility = View.GONE
            }

            override fun onDateChanged(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
                // Do nothing when the date is changed programmatically
            }
        }

        // Set the minimum selectable date to 5 months ago from today
        val minSelectableDate = Calendar.getInstance().apply {
            add(Calendar.MONTH, -MAX_MONTHS_FROM_CURRENT)
        }
        picker.datePicker.minDate = minSelectableDate.timeInMillis

        // Set the maximum selectable date to 5 months from today
        val maxSelectableDate = Calendar.getInstance().apply {
            add(Calendar.MONTH, 0)
        }
        picker.datePicker.maxDate = maxSelectableDate.timeInMillis

        picker.setOnDateSetListener { _, year, month, _ ->
            // Notify the listener with the selected date
            listener?.onDateSelected(year, month + 1)
        }

        return picker
    }

    fun setOnDateSelectedListener(listener: OnDateSelectedListener) {
        this.listener = listener
    }
}
